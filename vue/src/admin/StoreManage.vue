<template>
  <div class="fit-parent">
    <div class="store-manage-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>商家管理</span>
          </h2>
        </div>
        <div class="stores-div">
          <div class="tools-div">
            <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
              <el-form-item label="查找商家:">
                <el-input
                  size="small"
                  class="search-input"
                  placeholder="请输入ID/店主ID/店名/描述/地址/手机号"
                  prefix-icon="el-icon-search"
                  @keydown.enter.native="getSearchInfo()"
                  v-model="keyword"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button
                  size="small"
                  plain
                  type="primary"
                  @click="getSearchInfo()"
                  icon="el-icon-search"
                >查询</el-button>
              </el-form-item>
              <el-form-item>
                <el-button
                  size="small"
                  plain
                  @click="dialog.addStoreVisble=true"
                  icon="el-icon-plus"
                >添加商家</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="account-table">
            <el-tabs type="border-card" v-model="activeTab" @tab-click="updateInfo">
              <el-tab-pane label="商家列表" name="menuList">
                <i class="el-icon-refresh refresh-table" @click="getTableInfo()"></i>
                <div class="table-div jquery-scrollbar-element scrollbar-macosx">
                  <div class="inline-block">
                    <el-table :data="tableInfo.tableData" border style="width: 100%" @expand-change="updateScrollbar">
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item
                              v-for="(v,k) in tableInfo.stringMap"
                              :key="v.id"
                              :label="tableInfo.stringMap[k]"
                            >
                              <span>{{ props.row[k] }}</span>
                            </el-form-item>
                            <el-form-item label="店铺图标">
                              <el-avatar shape="square" :size="100" fit="cover" :src="props.row['logo']"></el-avatar>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column label="商家 ID" prop="id"></el-table-column>
                      <el-table-column label="店名" prop="storeName"></el-table-column>
                      <el-table-column label="店主" prop="ownerName"></el-table-column>
                      <el-table-column label="联系方式" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
                      <el-table-column label="操作" width="300">
                        <template slot-scope="scope">
                          <el-button
                            type="warning"
                            icon="el-icon-setting"
                            title="切换为该商家登录状态并跳转到店铺管理页面"
                            @click="chroot(scope.row.id)"
                            size="small"
                          >打开</el-button>
                          <el-button
                            type="primary"
                            icon="el-icon-edit"
                            title="编辑商家信息"
                            @click="showEditStore(scope.$index)"
                            size="small"
                          >编辑</el-button>
                          <el-button
                            type="danger"
                            icon="el-icon-delete"
                            title="删除商家"
                            @click="deleteStore(scope.$index)"
                            size="small"
                          >删除</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
                <div class="pager-div">
                  <el-pagination
                    @size-change="changePageSize"
                    @current-change="changePage"
                    :current-page="tableInfo.page_num"
                    :page-sizes="[6, 8, 10, 15, 20, 30]"
                    :page-size="tableInfo.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="tableInfo.total_num"
                    :pager-count="5"
                    size="small"
                  ></el-pagination>
                </div>
              </el-tab-pane>
              <!-- <el-tab-pane label="菜单预览" name="menuPreview">菜单预览</el-tab-pane> -->
              <el-tab-pane label="搜索结果" name="searchResult">
                <i class="el-icon-refresh refresh-table" @click="getSearchInfo(true)"></i>
                <div class="table-div jquery-scrollbar-element scrollbar-macosx">
                  <div class="inline-block">
                    <el-table :data="searchInfo.tableData" style="width: 100%" border @expand-change="updateScrollbar">
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item
                              v-for="(v,k) in tableInfo.stringMap"
                              :key="v.id"
                              :label="tableInfo.stringMap[k]"
                            >
                              <span>{{ props.row[k] }}</span>
                            </el-form-item>
                            <el-form-item label="店铺图标">
                              <el-avatar shape="square" :size="100" fit="cover" :src="props.row['logo']"></el-avatar>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column label="商家 ID" prop="id"></el-table-column>
                      <el-table-column label="店名" prop="storeName"></el-table-column>
                      <el-table-column label="店主" prop="ownerName"></el-table-column>
                      <el-table-column label="联系方式" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
                      <el-table-column label="操作" width="300">
                        <template slot-scope="scope">
                          <el-button
                            type="warning"
                            icon="el-icon-setting"
                            title="切换为该商家登录状态并跳转到店铺管理页面"
                            @click="chroot(scope.row.id)"
                            size="small"
                          >打开</el-button>
                          <el-button
                            type="primary"
                            icon="el-icon-edit"
                            title="编辑商家信息"
                            @click="showEditStore(scope.$index)"
                            size="small"
                          >编辑</el-button>
                          <el-button
                            type="danger"
                            icon="el-icon-delete"
                            title="删除商家"
                            @click="deleteStore(scope.$index)"
                            size="small"
                          >删除</el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
                <div class="pager-div">
                  <el-pagination
                    @size-change="changeSearchPageSize"
                    @current-change="changeSearchPage"
                    :current-page="searchInfo.page_num"
                    :page-sizes="[6, 8, 10, 15, 20, 30]"
                    :page-size="searchInfo.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="searchInfo.total_num"
                    :pager-count="5"
                    size="small"
                  ></el-pagination>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
        <el-dialog title="添加商家" :visible.sync="dialog.addStoreVisble" :modal-append-to-body="false">
          <el-form :model="dialog.newStore" :rules="dialog.rules" ref="addStoreForm">
            <el-form-item label="店主" :label-width="'120px'">
              <el-select v-model="dialog.newStore.ownerId" filterable placeholder="请选择">
                <el-option
                  v-for="item in userList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="店铺名称" :label-width="'120px'" prop="storeName">
              <el-input v-model="dialog.newStore.storeName" placeholder="请输入店铺名称" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺地址" :label-width="'120px'" prop="address">
              <el-input v-model="dialog.newStore.address" placeholder="请输入店铺地址" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="联系方式" :label-width="'120px'" prop="phone">
              <el-input v-model="dialog.newStore.phone" placeholder="请输入联系方式" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺描述" :label-width="'120px'" prop="description">
              <el-input v-model="dialog.newStore.description" placeholder="请输入店铺描述" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="起送金额" :label-width="'120px'" prop="minCost">
              <el-input v-model="dialog.newStore.minCost" placeholder="请输入起送金额>=0" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="配送费用" :label-width="'120px'" prop="postFee">
              <el-input v-model="dialog.newStore.postFee" placeholder="请输入配送费用>=0" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺图标" :label-width="'120px'">
              <el-upload class="upload-image" :on-success="uploadIconSuccess" :action="uploadUrl" :limit="1"
              name="file" list-type="picture-card" :before-upload="beforeIconUpload" 
              :on-exceed="uploadExcced">
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialog.addStoreVisble = false">取 消</el-button>
            <el-button type="primary" @click="addStore()">确 定</el-button>
          </div>
        </el-dialog>
        <el-dialog title="编辑商家" :visible.sync="dialog.editStoreVisible" :modal-append-to-body="false">
          <el-form :model="dialog.editStore" :rules="dialog.rules" ref="editStoreForm">
            <el-form-item label="店主" :label-width="'120px'">
              <el-select v-model="dialog.editStore.ownerId" filterable placeholder="请选择">
                <el-option
                  v-for="item in userList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="店铺名称" :label-width="'120px'" prop="storeName">
              <el-input v-model="dialog.editStore.storeName" placeholder="请输入店铺名称" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺地址" :label-width="'120px'" prop="address">
              <el-input v-model="dialog.editStore.address" placeholder="请输入店铺地址" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="联系方式" :label-width="'120px'" prop="phone">
              <el-input v-model="dialog.editStore.phone" placeholder="请输入联系方式" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺描述" :label-width="'120px'" prop="description">
              <el-input v-model="dialog.editStore.description" placeholder="请输入店铺描述" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="起送金额" :label-width="'120px'" prop="minCost">
              <el-input v-model="dialog.editStore.minCost" placeholder="请输入起送金额>=0" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="配送费用" :label-width="'120px'" prop="postFee">
              <el-input v-model="dialog.editStore.postFee" placeholder="请输入配送费用>=0" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="店铺图标" :label-width="'120px'">
              <el-upload class="upload-image"  :on-success="editIconSuccess" :action="uploadUrl" :limit="1"
          name="file" list-type="picture-card" :before-upload="beforeIconUpload" :on-exceed="uploadExcced"
          :file-list="dialog.editStore.fileList">
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialog.editStoreVisible = false">取 消</el-button>
            <el-button type="primary" @click="editStore()">确 定</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "../common/http.js";
export default {
  data() {
    let notNull = (rule, value, callback) => {
      if (!value) {
        callback(new Error("不能为空"));
      } else {
        callback();
      }
    };
    let isNumber = (rule, value, callback) => {
      if (!(value+'')||value<0) {
        callback(new Error("请输入大于等于0的数字"));
      } else {
        callback();
      }
    }
    let data = {
      host: window.location.protocol +"//"+window.location.host,
      uploadUrl: '/store/uploadIcon',
      userList: [],
      dialog: {
        addStoreVisble: false,
        editStoreVisible: false,
        newStore: {
          ownerId: "",
          storeName: "",
          address: "",
          phone: "",
          description: "",
          minCost: "",
          postFee: "",
          logo: ""
        },
        editStore: {
          ownerId: "",
          storeName: "",
          address: "",
          phone: "",
          description: "",
          minCost: "",
          postFee: "",
          logo: ""
        },
        rules: {
          ownerName: [
            {
              validator: notNull,
              trigger: "change"
            }
          ],
          storeName: [
            {
              validator: notNull,
              trigger: "blur"
            }
          ],
          address: [
            {
              validator: notNull,
              trigger: "blur"
            }
          ],
          phone: [
            {
              validator: notNull,
              trigger: "blur"
            }
          ],
          description: [
            {
              validator: notNull,
              trigger: "blur"
            }
          ],
          minCost: [
            {
              validator: isNumber,
              trigger: "blur"
            }
          ],
          postFee: [
            {
              validator: isNumber,
              trigger: "blur"
            }
          ],
        }
      },
      activeTab: "menuList",
      searchInfo: {
        keyword: "",
        page_num: 1,
        page_size: 8,
        total_num: 1,
        tableData: []
      },
      keyword: "",
      tableInfo: {
        page_num: 1,
        page_size: 8,
        total_num: 1,
        stringMap: {
          id: "店铺ID",
          ownerId: "店主ID",
          storeName: "店铺名称",
          phone: "联系方式",
          address: "店铺地址",
          description: "店铺描述",
          minCost: "起送金额",
          postFee: "配送费用",
          //productIcon: "图片",
          updateTime: "更新时间",
          createTime: "创建时间"
        },
        tableData: [
          /*{sellerId ...}*/
        ]
      }
    };
    return data;
  },
  mounted() {
    this.$(".jquery-scrollbar-element").scrollbar();
    this.getTableInfo();
    this.getUserList();
  },
  methods: {

    updateScrollbar(row,rows){
      this.$nextTick(()=>{
        if(rows.length==0||row===true){
          this.$('.table-div').updateScrollbars(true)
        }
      });
    },
    chroot(id){
      axios.get("/admin/store/chroot",{id}).then(res=>{
        if(res&&res.code==1){
          this.$router.push('/store/menu');
        }
      })
    },
    getUserList(){
      axios.get('/admin/user/findAll').then(res=>{
        if(res&&res.code==1){
          if(res.data instanceof Array){
            res.data.forEach(v=>{
              let name= v.nickname || v.username;
              v.label = `ID:${v.id} 姓名:${name} 电话:${v.phone}`;
              v.value = v.id;
            })
            this.userList = res.data;
          }
        }
      })
    },
    uploadExcced(){
      this.$message({message:'仅支持单个图片上传，点击删除原图片后重新选择',type: 'warning'});
    },
    beforeIconUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    uploadIconSuccess(res){
      //this.newProduct.imgUrl = 
      if(res&&res.code==1){
        this.dialog.newStore.logo = res.data;
      }
    },
    editIconSuccess(res){
      if(res&&res.code==1){
        this.dialog.editStore.logo = res.data;
      }
    },
    changeSearchPage(page_num) {
      this.searchInfo.page_num = page_num;
      this.getSearchInfo(true);
    },
    changeSearchPageSize(page_size) {
      this.searchInfo.page_size = page_size;
      this.getSearchInfo(true);
    },
    changePage(page_num) {
      this.tableInfo.page_num = page_num;
      this.getTableInfo();
    },
    changePageSize(page_size) {
      this.tableInfo.page_size = page_size;
      this.getTableInfo();
    },
    updateInfo() {
      //console.log(this.activeTab);
      if (this.activeTab == "menuList") {
        this.getTableInfo();
      } else {
        this.getSearchInfo(true);
      }
    },
    getTableInfo(isSearch = false) {
      let tb = !isSearch ? this.tableInfo : this.searchInfo,
        param = {};
      if (isSearch) {
        param = {
          keyword: this.searchInfo.keyword
        };
      }
      //获取列表信息
      axios
        .get("/admin/store/list", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
            if(res.data&&res.data.tableData instanceof Array){
              res.data.tableData.forEach( v=>{
                let user = res.data.userMap[v.ownerId];
                v.ownerName = user.nickname || user.username;
              })
            }
            if (isSearch) {
              this.searchInfo = Object.assign(this.searchInfo, res.data);
            } else {
              this.tableInfo = Object.assign(this.tableInfo, res.data);
            }
          }
        });
    },
    getSearchInfo(cacheKeyword = false) {
      if (!cacheKeyword) {
        this.searchInfo.keyword = this.keyword;
      }
      this.activeTab = "searchResult";
      this.getTableInfo(true);
    },
    //
    showaddStore(){
      this.dialog.addStoreVisble = true;
    },
    showEditStore(index) {
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let data = table.tableData[index];
      this.dialog.editStore = {...data,fileList:[{name: data.logo.split('/').pop(),url: data.logo}]};
      this.dialog.editStoreVisible = true;
      console.log(this.dialog.editStore)
    },
    addStore(){
      this.dialog.addStoreVisble = false;
      this.editStore(true);
    },
    editStore(isAdd=false){
      if(!isAdd) this.dialog.editStoreVisible = false;
      let act = isAdd ? this.dialog.newStore : this.dialog.editStore;
      let action = isAdd ? '添加' : '编辑';
      let ref = isAdd ? this.$refs["addStoreForm"] : this.$refs["editStoreForm"];
      let store = {...act};
      delete store.updateTime;
      delete store.createTime;
      ref.validate(valid => {
        if(valid){
          axios.post('/admin/store/save',store).then(res=>{
            if (res && res.code == 1) {
              this.$message({ message: action+"商家成功", type: "success" });
              if(isAdd){
                ref.resetFields();
              }
              this.updateInfo();
            } else {
              this.$alert(res.tips, action+"商家失败", {
                confirmButtonText: "确定"
              });
            }
          });
        } else {
          this.$alert("信息填写有误", "未提交", {
            confirmButtonText: "确定"
          });
          return false;
        }
      });
    },
    deleteStore(index) {
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let id = table.tableData[index].id;
      this.$confirm('此操作将永久删除该商家, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/admin/store/delete',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除商家成功',type: 'success'});
            this.updateInfo();
          }else{
            this.$alert(res.tips,"删除商家失败",{confirmButtonText: '确定'})
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    }
  }
};
</script>

<style scoped lang="scss">
.fit-parent{
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.padding24{
  padding: 24px !important;
}
.refresh-table{
  position: absolute;
  right: 0;
  top: 0;
  padding: 5px;
  background: #fff;
  border-radius: 50px;
  box-sizing: 0 0 10px rbga(0,0,0,0.3);
  z-index: 11;
  color: #409EFF;
  cursor: pointer;

  &:hover{
    background: #409EFF;
    color: #fff;
  }
}
.edit-category {
  cursor: pointer;
  border-radius: 50%;
  margin-left: 3px;
  padding: 2px;
  transform: scale(0.8);
  &:hover {
    background: #409eff;
    color: #fff;
  }
}
/*copy from element ui*/
.demo-table-expand {
  font-size: 0;
  label {
    width: 90px;
    color: #99a9bf;
  }
  .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
}

/*copy end*/
.store-manage-div {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  .title-div {
    padding-bottom: 8px;
    border-bottom: 1px solid #f5f5f5;
    margin-bottom: 24px;
    .title-text {
      color: #333;
      font-size: 18px;
      font-weight: bold;
      line-height: 2;
      span {
        border-bottom: 2px solid #409eff;
      }
    }
  }
  .stores-div {
    .tools-div {
      .search-input {
        width: 250px;
      }
    }
    .account-table {
      .inline-block {
        display: inline-block;
        min-width: 100%;
      }
    }
  }
}
</style>
