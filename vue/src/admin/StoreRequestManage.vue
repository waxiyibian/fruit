<template>
  <div class="fit-parent">
    <div class="store-manage-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>入驻申请</span>
          </h2>
        </div>
        <div class="stores-div">
          <div class="tools-div">
            <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
              <el-form-item>
              <el-select v-model="status" placeholder="请选择" size="small" style="width:100px">
                <el-option
                  v-for="item in statusList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
              <el-form-item label="查找:">
                <el-input
                  size="small"
                  class="search-input"
                  placeholder="请输入ID/用户ID/店名/描述/地址/手机号"
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
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column label="ID" prop="id"></el-table-column>
                      <el-table-column label="用户ID" prop="userId"></el-table-column>
                      <el-table-column label="申请人" prop="name"></el-table-column>
                      <el-table-column label="联系方式" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
                      <el-table-column label="操作" width="240">
                        <template slot-scope="scope">
                          <el-button
                            type="warning"
                            icon="el-icon-check"
                            title="确认已阅"
                            v-if="scope.row.status==0"
                            @click="confirmRead(scope.row.id)"
                            size="small"
                          >已阅</el-button>
                          <el-button
                            type="danger"
                            icon="el-icon-delete"
                            title="删除"
                            @click="deleteRequest(scope.$index)"
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
                          </el-form>
                        </template>
                      </el-table-column>
                      <el-table-column label="ID" prop="id"></el-table-column>
                      <el-table-column label="用户ID" prop="userId"></el-table-column>
                      <el-table-column label="申请人" prop="name"></el-table-column>
                      <el-table-column label="联系方式" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
                      <el-table-column label="操作" width="240">
                        <template slot-scope="scope">
                          <el-button
                            type="warning"
                            icon="el-icon-check"
                            title="确认已阅"
                            v-if="scope.row.status==0"
                            @click="confirmRead(scope.row.id)"
                            size="small"
                          >已阅</el-button>
                          <el-button
                            type="danger"
                            icon="el-icon-delete"
                            title="删除"
                            @click="deleteRequest(scope.$index)"
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
        
      </div>
    </div>
  </div>
</template>

<script>
import axios from "../common/http.js";
export default {
  data() {
    
    let data = {
      statusList: [
        { label: "全部", value: -1 },
        { label: "新申请", value: 0 },
        { label: "已阅", value: 1 }
      ],
      activeTab: "menuList",
      searchInfo: {
        keyword: "",
        status: -1,
        page_num: 1,
        page_size: 8,
        total_num: 1,
        tableData: []
      },
      status: -1,
      keyword: "",
      tableInfo: {
        page_num: 1,
        page_size: 8,
        total_num: 1,
        stringMap: {
          id: "ID",
          userId: "用户ID",
          name: "申请人",
          storeName: "店铺名称",
          phone: "联系方式",
          address: "店铺地址",
          description: "店铺描述",
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
          keyword: this.searchInfo.keyword,
          status: this.searchInfo.status
        };
      }
      //获取列表信息
      axios
        .get("/admin/requestList", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
            if (isSearch) {
              this.searchInfo = {...this.searchInfo, ...res.data};
            } else {
              this.tableInfo = {...this.tableInfo, ...res.data};
            }
          }
        });
    },
    getSearchInfo(cacheKeyword = false) {
      if (!cacheKeyword) {
        this.searchInfo.keyword = this.keyword;
        this.searchInfo.status = this.status;
      }
      this.activeTab = "searchResult";
      this.getTableInfo(true);
    },
    confirmRead(id){
      this.$confirm(`将ID为${id}的订单确认已阅, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/admin/readRequest',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '确认已阅成功',type: 'success'});
            this.updateInfo();
          }else{
            this.$alert(res.tips,"确认已阅失败",{confirmButtonText: '确定'})
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    },
    //
    deleteRequest(index) {
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let id = table.tableData[index].id;
      this.$confirm('此操作将永久删除该申请, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/admin/deleteRequest',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除申请成功',type: 'success'});
            this.updateInfo();
          }else{
            this.$alert(res.tips,"删除申请失败",{confirmButtonText: '确定'})
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
