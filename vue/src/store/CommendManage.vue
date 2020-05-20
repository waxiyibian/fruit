<template>
  <div class="fit-parent">
    <div class="account-manage-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>评价管理</span>
          </h2>
        </div>
        <div class="accounts-div">
          <div class="tools-div">
            <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
              <el-form-item label="查找评论:">
                <el-input
                  size="small"
                  class="search-input"
                  placeholder="请输入内容/评论ID/用户ID"
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
            </el-form>
          </div>
          <div class="account-table">
            <el-tabs type="border-card" v-model="activeTab" @tab-click="updateInfo">
              <el-tab-pane label="评论列表" name="menuList">
                <i class="el-icon-refresh refresh-table" @click="getTableInfo()"></i>
                <div class="table-div jquery-scrollbar-element scrollbar-macosx">
                  <div class="inline-block">
                    <el-table :data="tableInfo.tableData" border @expand-change="updateScrollbar" style="width: 100%">
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item :label="'评论内容'">
                              <div>
                                {{props.row.content}}
                              </div>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column label="评论 ID" prop="id"></el-table-column>
                      <el-table-column label="用户 ID" prop="userId"></el-table-column>
                      <el-table-column label="订单 ID" prop="orderId"></el-table-column>
                      <el-table-column label="昵称" prop="nickname"></el-table-column>
                      <el-table-column label="评论内容">
                        <template slot-scope="scope">
                          <div>{{scope.row.content | cutString}}</div>
                        </template>
                      </el-table-column>
                      <el-table-column label="手机号码" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
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
                    <el-table :data="searchInfo.tableData" style="width: 100%" border>
                      <el-table-column type="expand">
                        <template slot-scope="props">
                          <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item :label="'评论内容'">
                              <div>
                                {{props.row.content}}
                              </div>
                            </el-form-item>
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column label="评论 ID" prop="id"></el-table-column>
                      <el-table-column label="用户 ID" prop="userId"></el-table-column>
                      <el-table-column label="订单 ID" prop="orderId"></el-table-column>
                      <el-table-column label="昵称" prop="nickname"></el-table-column>
                      <el-table-column label="评论内容">
                        <template slot-scope="scope">
                          <div>{{scope.row.content | cutString}}</div>
                        </template>
                      </el-table-column>
                      <el-table-column label="手机号码" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
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
      </div>
    </div>
  </div>
</template>

<script>
import axios from "../common/http.js";
export default {
  filters: {
    cutString(value,len=10){
      if(!value) return '';
      if(value.length>len){
        value = value.substring(0,len) +'...';
      }
      return value;
    }
  },
  data() {
    let data = {
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
  },
  methods: {
    updateScrollbar(row,rows){
      this.$nextTick(()=>{
        if(rows.length==0||row===true){
          this.$('.table-div').updateScrollbars(true)
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
        .get("/store/comment/list", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
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
.account-manage-div {
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
  .accounts-div {
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
