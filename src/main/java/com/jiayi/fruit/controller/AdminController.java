package com.jiayi.fruit.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.jiayi.fruit.config.DatabaseProperties;
import com.jiayi.fruit.config.FileProperties;
import com.jiayi.fruit.entity.StoreRequest;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.env.StoreRequestStatus;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.exception.RecordNotFoundException;
import com.jiayi.fruit.exception.UnknownException;
import com.jiayi.fruit.repository.StoreRequestRepository;
import com.jiayi.fruit.util.CommonUtil;
import com.jiayi.fruit.util.DateUtils;
import com.jiayi.fruit.util.RuntimeUtils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {
  @Autowired
  private Gson gson;
  @Autowired
  private FileProperties prop;
  @Autowired
  private DatabaseProperties db;
  @Autowired
  private StoreRequestRepository storeRequestRepository;

  @RequestMapping("/backup")
  public ResponseEntity<byte[]> backupData(HttpServletRequest request, @RequestHeader("User-Agent") String userAgent)
      throws IOException, ExecutionException {
    // 1执行备份命令
    // 1.1获取当前时间，格式如2020020202,yyyyMMddhhmmss
    String date = DateUtils.getSimpleFormateDate();
    // 1.2生成文件名
    String filename = "backup-" + date + ".sql";
    // 1.3拼接出命令
    String cmd = db.getMysqlDumpCommand();
    log.debug(cmd);
    RuntimeUtils.RuntimeResolve resolve = RuntimeUtils.execute(cmd);
    String input = resolve.getDatum().toString();
    // 执行写入文件并返回
    // 构建File
    File file = new File(prop.getUploadFolder(), filename);
    FileUtil.writeAsString(file, input);
    // ok表示http请求中状态码200
    ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
    // 内容长度
    builder.contentLength(file.length());
    // application/octet-stream 二进制数据流（最常见的文件下载）
    builder.contentType(MediaType.APPLICATION_OCTET_STREAM);
    // 根据浏览器类型，决定处理方式
    if (userAgent.indexOf("MSIE") > 0) {
      builder.header("Content-Disposition", "attachment; filename=" + filename);
    } else {
      builder.header("Content-Disposition", "attacher; filename*=UTF-8''" + filename);
    }
    byte[] bytes = "".getBytes();
    if (input != null) {
      bytes = input.toString().getBytes();
    }
    return builder.body(bytes);
  }

  @RequestMapping("/uploadAndrestore")
  public String uploadAndrestore(@RequestParam("file") MultipartFile file) throws IOException, ExecutionException {
    // 1执行还原命令
    // 1.1获取当前时间，格式如2020020202,yyyyMMddhhmmss
    String date = DateUtils.getSimpleFormateDate();
    // 1.2生成文件名
    String filename = "restore-" + date + ".sql";
    // 构建File
    File rf = new File(prop.getUploadFolder(), filename);
    // 1.3文件备份
    file.transferTo(rf);
    // 1.4拼接出命令
    String create = db.getMysqlAdminCommand();
    String restore = db.getMysqlCommend();
    log.debug(create);
    log.debug(restore);
    // 封装后的exec变成了同步代码，命令执行完成前会阻塞当前进程
    RuntimeUtils.RuntimeResolve createResolve = RuntimeUtils.execute(create);
    RuntimeUtils.RuntimeResolve restoreResolve = RuntimeUtils.execute(restore, FileUtil.readAsString(rf));
    // 返回一个JSON信息
    ResponseFormat rep = Result.success();
    // 返回控制台的正确输出
    rep.setMessage(createResolve.getDatum().toString() + restoreResolve.getDatum().toString());
    return gson.toJson(rep);
  }

  @RequestMapping("/requestList")
  public String requestList(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
      @RequestParam(value = "page_size", defaultValue = "10") Integer size,
      @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
      @RequestParam(value = "status", required = false, defaultValue = "-1") Integer status) {
    Sort sort = Sort.by(Sort.Order.desc("updateTime"));
    PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
    Page<StoreRequest> lists = storeRequestRepository.findAll((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<Predicate>();
      Predicate p = null;
      // equal 示例
      if (keyword != null && !StringUtils.isEmpty(keyword)) {
        p = cb.like(root.get("userId").as(String.class), "%" + keyword + "%");
        p = cb.or(p, cb.like(root.get("id").as(String.class), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("name"), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("storeName"), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("address"), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("description"), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("phone"), "%" + keyword + "%"));
      }
      if (status != null && status >= 0) {
        if (p == null)
          p = cb.equal(root.get("status"), status);
        else
          p = cb.and(p, cb.equal(root.get("status"), status));
      }
      if (p != null) {
        predicates.add(p);
      }
      return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }, request);
    List<StoreRequest> list = lists.getContent();
    ResponseFormat rep = Result.success();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("total_num", lists.getTotalElements());
    map.put("page_num", page);
    map.put("page_size", size);
    map.put("tableData", list);
    rep.setData(map);
    return gson.toJson(rep);
  }

  private void setStatus(Long id,Integer status){
    StoreRequest sr = storeRequestRepository.findById(id).orElse(null);
    if(sr==null){
        throw new RecordNotFoundException();
    }
    sr.setStatus(status);
    storeRequestRepository.save(sr);
  }

  @RequestMapping("/readRequest")
  public String confirmPay(Long id) {
    setStatus(id, StoreRequestStatus.READED);
    ResponseFormat rep = Result.success();
    return gson.toJson(rep);
  }

  @RequestMapping("/deleteRequest")
  public String deleteRequest(Long id){
    StoreRequest sr = storeRequestRepository.findById(id).orElse(null);
    if(sr==null){
      throw new IllegalParameterException(){{setMessage("申请信息ID"+id+"不存在");}};
    }
    storeRequestRepository.deleteById(id);
    return gson.toJson(Result.success());
  }
}