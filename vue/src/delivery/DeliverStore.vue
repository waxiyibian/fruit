<template>
  <div class="fit-parent">
    <div class="account-manage-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>配送商铺</span>
          </h2>
        </div>
        <div class="accounts-div">
          <div class="account-table">
            <el-tabs type="border-card" v-model="activeTab" @tab-click="updateInfo">
              <el-tab-pane label="配送员列表" name="menuList">
                <i class="el-icon-refresh refresh-table" @click="getTableInfo()"></i>
                <div class="table-div jquery-scrollbar-element scrollbar-macosx">
                  <div class="inline-block">
                    <el-table :data="tableInfo.tableData" border style="width: 100%">
                      <el-table-column label="配送员 ID" prop="id"></el-table-column>
                      <el-table-column label="用户名" prop="username"></el-table-column>
                      <el-table-column label="昵称" prop="nickname"></el-table-column>
                      <el-table-column label="手机号码" prop="phone"></el-table-column>
                      <el-table-column label="创建时间" prop="createTime"></el-table-column>
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
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
        <el-dialog title="编辑配送" :visible.sync="dialog.editAccountVisible" :modal-append-to-body="false">
          <el-form :model="dialog.account" :rules="dialog.rules" ref="editAccountForm">
            <el-form-item label="配送员" :label-width="'140px'">
              <div>ID{{dialog.account.id}},昵称{{dialog.account.nickname||dialog.account.username}},手机号码{{dialog.account.phone}}</div>
            </el-form-item>
            <el-form-item label="为指定的店铺配送" :label-width="'140px'">
              <el-select v-model="dialog.account.storeIds" style="width:300px;" multiple filterable placeholder="请选择">
                <el-option
                  v-for="item in storeList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
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
    let data = {
      storeList: [],
      dialog: {
        editAccountVisible: false,
        account: {
          userId: "",
          storeIds: []
        },
        rules: {
          
        }
      },
      activeTab: "menuList",
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
    this.getStoreList();
  },
  methods: {
    getStoreList(){
      axios.get('/open/store/findAll').then(res=>{
        if(res&&res.code==1){
          if(res.data instanceof Array){
            res.data.forEach(v=>{
              v.label = `ID:${v.id} 店名:${v.storeName} 电话:${v.phone}`;
              v.value = v.id;
            })
            this.storeList = res.data;
          }
        }
      })
    },
    updateInfo() {
      //console.log(this.activeTab);
      if (this.activeTab == "menuList") {
        this.getTableInfo();
      }
    },
    getTableInfo(isSearch = false) {
      let tb = !isSearch ? this.tableInfo : '',
        param = {};
      //获取列表信息
      axios
        .get("/delivery/get")
        .then(res => {
          if (res && res.code == 1 && res.data) {
            if(res.data&&res.data.tableData instanceof Array){
              let map = {};
              res.data.tableData.forEach(v=>{
                if(map[v.user.id]){
                  map[v.user.id].storeIds.push(v.store.id);
                  let ct = map[v.user.id].ct;
                  if(v.createTime > ct) map[v.user.id].ct = v.createTime;
                }else{
                  map[v.user.id] = {...v.user,storeIds:[v.store.id],ct:v.createTime}
                }
              });
              let arr = [];
              for(let k in map) arr.push(map[k]);
              arr = arr.sort((a,b)=>{
                a.ct < b.ct;
              });
              res.data.tableData = arr;
            }
            this.tableInfo = Object.assign(this.tableInfo, res.data);
          }
        });
    },
    showEditAccount(index) {
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let data = table.tableData[index];
      this.dialog.account = {...data,userId: data.id};
      this.dialog.editAccountVisible = true;

    },
    editAccount(isAdd=false){
      this.dialog.editAccountVisible = false;
      let act = isAdd ? '': this.dialog.account;
      let action = isAdd ? '添加' : '编辑';
      let ref = isAdd ? '' : this.$refs["editAccountForm"];
      let arr = act.storeIds || [];
      if(!arr||arr.length <=0){
        this.$alert("请选择配送至少一个商铺", "未提交", {
          confirmButtonText: "确定"
        });
        return;
      }
      if(!act.userId){
        this.$alert("您还不是配送员", "未提交", {
          confirmButtonText: "确定"
        });
        return;
      }
      let param = JSON.stringify(arr.map(v=>({storeId:v,userId:act.userId})) );
      console.log(param,arr,act);
      ref.validate(valid => {
        if(valid){
          axios.post('/delivery/save',param,
          {headers:{'Content-Type':'application/json;charset=utf-8'}}).then(res=>{
            if (res && res.code == 1) {
              this.$message({ message: action+"配送成功", type: "success" });
              if(isAdd){
                ref.resetFields();
              }
              this.updateInfo();
            } else {
              this.$alert(res.tips, action+"配送失败", {
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
