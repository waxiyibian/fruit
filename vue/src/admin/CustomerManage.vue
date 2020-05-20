<template>
  <div class="fit-parent">
    <div class="account-manage-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>用户管理</span>
          </h2>
        </div>
        <div class="accounts-div">
          <div class="tools-div">
            <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
              <el-form-item label="查找用户:">
                <el-input
                  size="small"
                  class="search-input"
                  placeholder="请输入关键字"
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
                  @click="dialog.addAccountVisible=true"
                  icon="el-icon-plus"
                >添加用户</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="account-table">
            <el-tabs type="border-card" v-model="activeTab" @tab-click="updateInfo">
              <el-tab-pane label="用户列表" name="menuList">
                <i class="el-icon-refresh refresh-table" @click="getTableInfo()"></i>
                <div class="table-div jquery-scrollbar-element scrollbar-macosx">
                  <div class="inline-block">
                    <el-table :data="tableInfo.tableData" border style="width: 100%">
                      <el-table-column label="用户 ID" prop="id"></el-table-column>
                      <el-table-column label="用户名" prop="username"></el-table-column>
                      <el-table-column label="昵称" prop="nickname"></el-table-column>
                      <el-table-column label="手机号码" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
                      <el-table-column label="操作" width="100">
                        <template slot-scope="scope">
                          <el-button
                            type="primary"
                            icon="el-icon-edit"
                            circle
                            title="编辑"
                            @click="showEditAccount(scope.$index)"
                            size="small"
                          ></el-button>
                          <el-button
                            type="danger"
                            icon="el-icon-delete"
                            circle
                            title="删除用户"
                            @click="deleteAccount(scope.$index)"
                            size="small"
                          ></el-button>
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
                    <el-table :data="searchInfo.tableData" style="width: 100%" border>
                      <el-table-column label="用户 ID" prop="id"></el-table-column>
                      <el-table-column label="用户名" prop="username"></el-table-column>
                      <el-table-column label="昵称" prop="nickname"></el-table-column>
                      <el-table-column label="手机号码" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
                      <el-table-column label="更新时间" prop="updateTime"></el-table-column>
                      <el-table-column label="操作" width="100">
                        <template slot-scope="scope">
                          <el-button
                            type="primary"
                            icon="el-icon-edit"
                            circle
                            title="编辑"
                            @click="showEditAccount(scope.$index)"
                            size="small"
                          ></el-button>
                          <el-button
                            type="danger"
                            icon="el-icon-delete"
                            circle
                            title="删除用户"
                            @click="deleteAccount(scope.$index)"
                            size="small"
                          ></el-button>
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
        <el-dialog title="添加用户" :visible.sync="dialog.addAccountVisible" :modal-append-to-body="false">
          <el-form :model="dialog.newAccount" :rules="dialog.rules" ref="addAccountForm">
            <el-form-item label="用户名" :label-width="'120px'" prop="username">
              <el-input v-model="dialog.newAccount.username" placeholder="请输入昵称" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称" :label-width="'120px'" prop="nickname">
              <el-input v-model="dialog.newAccount.nickname" placeholder="请输入昵称" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号码" :label-width="'120px'" prop="phone">
              <el-input v-model="dialog.newAccount.phone" placeholder="请输入手机号码" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="'120px'" prop="password">
              <el-input v-model="dialog.newAccount.password" show-password placeholder="请输入密码" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialog.addAccountVisible = false">取 消</el-button>
            <el-button type="primary" @click="addAccount()">确 定</el-button>
          </div>
        </el-dialog>
        <el-dialog title="编辑用户" :visible.sync="dialog.editAccountVisible" :modal-append-to-body="false">
          <el-form :model="dialog.account" :rules="dialog.rules" ref="editAccountForm">
            <el-form-item label="用户名" :label-width="'120px'" prop="username">
              <el-input v-model="dialog.account.username" placeholder="请输入昵称" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="昵称" :label-width="'120px'" prop="nickname">
              <el-input v-model="dialog.account.nickname" placeholder="请输入昵称" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="手机号码" :label-width="'120px'" prop="phone">
              <el-input v-model="dialog.account.phone" placeholder="请输入手机号码" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="'120px'">
              <el-input v-model="dialog.account.password" show-password placeholder="请输入密码，为空则不更改" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialog.editAccountVisible = false">取 消</el-button>
            <el-button type="primary" @click="editAccount()">确 定</el-button>
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
    let data = {
      dialog: {
        addAccountVisible: false,
        editAccountVisible: false,
        newAccount: {
          username: "",
          nickname: "",
          phone: "",
          password: ""
        },
        account: {
          id: "",
          nickname: "",
          username: "",
          phone: "",
          password: ""
        },
        rules: {
          username: [
            {
              validator: notNull,
              trigger: "blur"
            }
          ],
          nickname: [
            {
              validator: notNull,
              trigger: "blur"
            }
          ],
          password: [
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
          ]
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
      let p;
      if (this.activeTab == "menuList") {
        p = this.getTableInfo();
      } else {
        p = this.getSearchInfo(true);
      }
      p.then(()=>{this.updateScrollbar([],true)});
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
      return axios
        .get("/admin/user/list", {
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
      return this.getTableInfo(true);
    },
    //
    showAddAccount(){
      this.dialog.addAccountVisible = true;
    },
    showEditAccount(index) {
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      this.dialog.account = {...table.tableData[index]};
      this.dialog.editAccountVisible = true;

    },
    addAccount(){
      this.dialog.addAccountVisible = false;
      this.editAccount(true);
    },
    editAccount(isAdd=false){
      if(!isAdd) this.dialog.editAccountVisible = false;
      let act = isAdd ? this.dialog.newAccount : this.dialog.account;
      let action = isAdd ? '添加' : '编辑';
      let ref = isAdd ? this.$refs["addAccountForm"] : this.$refs["editAccountForm"];
      let account = {...act};
      delete account.updateTime;
      delete account.createTime;
      delete account.roles;
      if(!account.password) delete account.password;
      ref.validate(valid => {
        if(valid){
          axios.post('/admin/user/save',account).then(res=>{
            if (res && res.code == 1) {
              this.$message({ message: action+"用户成功", type: "success" });
              if(isAdd){
                ref.resetFields();
              }
              this.updateInfo();
            } else {
              this.$alert(res.tips, action+"用户失败", {
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
    deleteAccount(index) {
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let id = table.tableData[index].id;
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/admin/user/delete',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除用户成功',type: 'success'});
            this.updateInfo();
          }else{
            this.$alert(res.tips,"删除用户失败",{confirmButtonText: '确定'})
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
