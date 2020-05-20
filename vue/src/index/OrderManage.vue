<template>
  <div class="fit-parent">
    <div class="order-manage-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>订单管理</span>
          </h2>
        </div>
        <div class="orders-div">
          <div class="tools-div">
            <el-form
              :inline="true"
              :model="searchInfo"
              class="demo-form-inline"
            >
              <el-form-item>
                <el-select
                  v-model="orderStatus"
                  placeholder="请选择"
                  size="small"
                  style="width:100px"
                >
                  <el-option
                    v-for="item in statusList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input
                  size="small"
                  class="search-input"
                  placeholder="请输入用户昵称/联系方式/订单编号"
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
                  >查询</el-button
                >
              </el-form-item>
            </el-form>
          </div>
          <div class="order-table">
            <el-tabs
              type="border-card"
              v-model="activeTab"
              @tab-click="updateInfo"
            >
              <el-tab-pane label="订单列表" name="menuList">
                <i
                  class="el-icon-refresh refresh-table"
                  @click="updateInfo()"
                ></i>
                <div
                  class="table-div jquery-scrollbar-element scrollbar-macosx"
                >
                  <div class="inline-block">
                    <el-table
                      :data="tableInfo.tableData"
                      border
                      style="width: 100%"
                      @expand-change="updateScrollbar"
                    >
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-form
                            label-position="left"
                            inline
                            class="demo-table-expand"
                          >
                            <el-form-item
                              v-for="(v, k) in tableInfo.stringMap"
                              :key="v.id"
                              :label="tableInfo.stringMap[k]"
                            >
                              <span>{{ props.row[k] }}</span>
                            </el-form-item>
                            <el-form-item :label="'订单内容'">
                              <div
                                v-for="v in props.row.orderDetailList"
                                :key="v.detailId"
                              >
                                {{ v.productName }} &yen;{{ v.productPrice }} x
                                {{ v.productQuantity }}
                              </div>
                            </el-form-item>
                            <el-form-item :label="'评论内容'">
                              <div>
                                <el-button v-if="!commentMap[props.row.orderId]" @click="findComment(props.row.orderId)">查看评价</el-button>
                                <div v-else>
                                  <span v-if="commentMap[props.row.orderId].empty">暂无评论</span>
                                  <span v-else>{{commentMap[props.row.orderId].content}}</span>
                                </div>
                              </div>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column
                        width="80"
                        label="订单 ID"
                        prop="orderId"
                      ></el-table-column>
                      <el-table-column
                        label="用户名称"
                        prop="buyerName"
                      ></el-table-column>
                      <el-table-column
                        label="订单金额"
                        prop="orderAmount"
                      ></el-table-column>
                      <el-table-column
                        label="联系方式"
                        prop="buyerPhone"
                      ></el-table-column>
                      <el-table-column
                        label="订单状态"
                        prop="orderStatusStr"
                      ></el-table-column>
                      <el-table-column label="操作" width="320">
                        <template slot-scope="scope">
                          <el-button
                            type="warning"
                            icon="el-icon-coin"
                            v-if="
                              scope.row.orderStatus == actionMap['new'].status
                            "
                            @click="changeStatus(scope.$index, 'pay')"
                            size="small"
                            >支付</el-button
                          >
                          <el-button
                            type="success"
                            icon="el-icon-check"
                            v-if="
                              actionMap['over'].status > scope.row.orderStatus
                            "
                            @click="changeStatus(scope.$index, 'over')"
                            size="small"
                            >确认收货</el-button
                          >
                          <el-button
                            type="warning"
                            icon="el-icon-coin"
                            v-if="
                              scope.row.orderStatus == actionMap['over'].status
                            "
                            @click="showComment(scope.$index)"
                            size="small"
                            >评价</el-button
                          >
                          <el-button
                            type="danger"
                            icon="el-icon-close"
                            v-if="
                              actionMap['over'].status > scope.row.orderStatus
                            "
                            @click="changeStatus(scope.$index, 'cancel')"
                            size="small"
                            >取消订单</el-button
                          >
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
                <i
                  class="el-icon-refresh refresh-table"
                  @click="updateInfo(true)"
                ></i>
                <div
                  class="table-div jquery-scrollbar-element scrollbar-macosx"
                >
                  <div class="inline-block">
                    <el-table
                      :data="searchInfo.tableData"
                      style="width: 100%"
                      border
                      @expand-change="updateScrollbar"
                    >
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-form
                            label-position="left"
                            inline
                            class="demo-table-expand"
                          >
                            <el-form-item
                              v-for="(v, k) in tableInfo.stringMap"
                              :key="v.id"
                              :label="tableInfo.stringMap[k]"
                            >
                              <span>{{ props.row[k] }}</span>
                            </el-form-item>
                            <el-form-item :label="'订单内容'">
                              <div
                                v-for="v in props.row.orderDetailList"
                                :key="v.detailId"
                              >
                                {{ v.productName }} &yen;{{ v.productPrice }} x
                                {{ v.productQuantity }}
                              </div>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column
                        width="80"
                        label="订单 ID"
                        prop="orderId"
                      ></el-table-column>
                      <el-table-column
                        label="用户名称"
                        prop="buyerName"
                      ></el-table-column>
                      <el-table-column
                        label="订单金额"
                        prop="orderAmount"
                      ></el-table-column>
                      <el-table-column
                        label="联系方式"
                        prop="buyerPhone"
                      ></el-table-column>
                      <el-table-column
                        label="订单状态"
                        prop="orderStatusStr"
                      ></el-table-column>
                      <el-table-column label="操作" width="320">
                        <template slot-scope="scope">
                          <el-button
                            type="warning"
                            icon="el-icon-coin"
                            v-if="
                              scope.row.orderStatus == actionMap['new'].status
                            "
                            @click="changeStatus(scope.$index, 'pay')"
                            size="small"
                            >支付</el-button
                          >
                          <el-button
                            type="success"
                            icon="el-icon-check"
                            v-if="
                              actionMap['over'].status > scope.row.orderStatus
                            "
                            @click="changeStatus(scope.$index, 'over')"
                            size="small"
                            >确认收货</el-button
                          >
                          <el-button
                            type="warning"
                            icon="el-icon-coin"
                            v-if="
                              scope.row.orderStatus == actionMap['over'].status
                            "
                            @click="showComment(scope.$index)"
                            size="small"
                            >评价</el-button
                          >
                          <el-button
                            type="danger"
                            icon="el-icon-close"
                            v-if="
                              actionMap['over'].status > scope.row.orderStatus
                            "
                            @click="changeStatus(scope.$index, 'cancel')"
                            size="small"
                            >取消订单</el-button
                          >
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
          <el-dialog
            title="评价订单"
            :visible.sync="comentVisible"
            :modal-append-to-body="false"
          >
            <el-form :model="commentOrder">
              <el-form-item label="订单编号:" :label-width="'120px'"
                ><div>{{ commentOrder.orderId }}</div></el-form-item
              >
              <el-form-item label="订单内容:" :label-width="'120px'">
                <div>
                  <span style="border-right: 1px solid #ccc;padding-right:10px;margin-right:10px;"
                    v-for="v in commentOrder.orderDetailList"
                    :key="v.detailId"
                  >
                    {{ v.productName }} &yen;{{ v.productPrice }} x
                    {{ v.productQuantity }}
                  </span>
                </div>
              </el-form-item>
              <el-form-item label="评价内容" :label-width="'120px'">
                <el-input
                  v-model="commentOrder.content"
                  autocomplete="off"
                  @keydown.enter.native="
                    comentVisible = false;
                    addComment();
                  "
                ></el-input>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button @click="comentVisible = false">取 消</el-button>
              <el-button
                type="primary"
                @click="
                  comentVisible = false;
                  addComment();
                "
                >确 定</el-button
              >
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "../common/http.js";
export default {
  data() {
    let data = {
      commentMap: {},
      comentVisible: false,
      commentOrder: {
        orderId: "",
        orderDetailList: [],
        content: "",
      },
      activeTab: "menuList",
      statusList: [
        { label: "全部", value: -1 },
        { label: "新订单未支付", value: 0 },
        { label: "已支付/接单", value: 1 }, //value 1 is not use
        { label: "配送中", value: 3 },
        { label: "已完结", value: 4 },
        { label: "已评价", value: 5 },
        { label: "已取消", value: 6 },
      ],
      actionMap: {
        //存储状态码
        new: {
          status: 0,
        },
        pay: {
          url: "/user/order/confirmPay",
          tips: "确认支付",
          status: 1,
        },
        accept: {
          url: "/user/order/confirmPay", // /user/order/accept
          tips: "确认接单",
          status: 1,
        },
        delivery: {
          status: 3,
        },
        over: {
          url: "/user/order/finish",
          tips: "确认完结",
          status: 4,
        },
        comment: {
          status: 5,
        },
        cancel: {
          url: "/user/order/cancel",
          tips: "订单取消",
          status: 6,
        },
      },
      statusMap: {},
      searchInfo: {
        keyword: "",
        orderStatus: -1,
        page_num: 1,
        page_size: 8,
        total_num: 1,
        tableData: [],
      },
      orderStatus: -1,
      keyword: "",
      tableInfo: {
        page_num: 1,
        page_size: 8,
        total_num: 1,
        stringMap: {
          orderId: "订单ID",
          buyerName: "用户名称",
          buyerAddress: "地址",
          buyerPhone: "联系方式",
          //productIcon: "图片",
          updateTime: "更新时间",
          createTime: "创建时间",
        },
        tableData: [
          /*{orderId ...}*/
        ],
      },
    };
    data.statusList.forEach((v) => {
      data.statusMap[v.value] = v.label;
    });
    return data;
  },
  mounted() {
    this.$(".jquery-scrollbar-element").scrollbar();
    this.getTableInfo();
  },
  methods: {
    findComment(orderId){
      let action="获取评论";
      axios.post('/user/comment/find',{orderId}).then(res=>{
        if(res&&res.code==1){
          let data;
          if(res.data) data=res.data;
          else data = {empty:true}
          this.commentMap = {...this.commentMap,[orderId]:data};
          console.log(this.commentMap)
        }else{
          this.$alert(res.tips, action + "失败", {
              confirmButtonText: "确定",
            });
        }
      })
    },
    showComment(index) {
      this.commentOrder = { ...this.tableInfo.tableData[index], content: "" };
      this.comentVisible = true;
    },
    addComment() {
      let orderId = this.commentOrder.orderId;
      let content = this.commentOrder.content;
      if(!(orderId+'')||!content){
        let message = content ? '请重新选择要评价的订单' : '请输入评价内容'
        this.$confirm(message, "未提交", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
        this.updateInfo();
      }else{
        let action = '评价订单'
        axios.post('/user/comment/save',{orderId,content}).then(res=>{
          if (res && res.code == 1) {
            this.$message({ message: action + "成功", type: "success" });
          } else {
            this.$alert(res.tips, action + "失败", {
              confirmButtonText: "确定",
            });
          }
        });
      }
    },
    updateScrollbar(row, rows) {
      this.$nextTick(() => {
        if (rows.length == 0 || row === true) {
          this.$(".table-div").updateScrollbars(true);
        }
      });
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
      //清空评论
      this.commentMap = {};
      console.log(this.commentMap);
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
          keyword: this.searchInfo.keyword,
          status: this.searchInfo.orderStatus,
        };
      }
      //获取商品列表信息
      axios
        .get("/user/order/list", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param,
        })
        .then((res) => {
          if (res && res.code == 1 && res.data) {
            res.data.tableData.forEach((v) => {
              v.orderStatusStr = this.statusMap[v.orderStatus];
            });
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
        this.searchInfo.orderStatus = this.orderStatus;
      }
      this.activeTab = "searchResult";
      this.getTableInfo(true);
    },
    //
    changeStatus(index, act) {
      let map = this.actionMap[act];
      let url = map.url;
      let action = map.tips;
      //判断是在搜索页还是列表页
      let table =
        this.activeTab == "menuList" ? this.tableInfo : this.searchInfo;
      let orderId = table.tableData[index].orderId;
      this.$confirm(`将${orderId}号订单${action}, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          axios.get(url, { orderId }).then((res) => {
            if (res && res.code == 1) {
              this.$message({ message: action + "成功", type: "success" });
              let index = -1;
              table.tableData.some((v, i) => {
                if (v.orderId == orderId) {
                  index = i;
                  return true;
                }
              });
              let status = map.status;
              if (index >= 0) {
                table.tableData[index].orderStatus = status;
                table.tableData[index].orderStatusStr = this.statusMap[status];
              }
            } else {
              this.$alert(res.tips, action + "失败", {
                confirmButtonText: "确定",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消",
          });
        });
    },
  },
};
</script>

<style scoped lang="scss">
.fit-parent {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.padding24 {
  padding: 24px !important;
}
.refresh-table {
  position: absolute;
  right: 0;
  top: 0;
  padding: 5px;
  background: #fff;
  border-radius: 50px;
  box-sizing: 0 0 10px rbga(0, 0, 0, 0.3);
  z-index: 11;
  color: #409eff;
  cursor: pointer;

  &:hover {
    background: #409eff;
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
.order-manage-div {
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
  .orders-div {
    .tools-div {
      .search-input {
        width: 250px;
      }
    }
    .order-table {
      .inline-block {
        display: inline-block;
        min-width: 100%;
      }
    }
  }
}
</style>
