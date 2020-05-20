<template>
  <el-drawer
    title="选择收货地址"
    :append-to-body="true"
    :visible.sync="isVisible"
    size="75%"
  >
    <div class="out-box">
      <div class="mb10">
        <el-button type="primary" size="small" @click="showAddAddress"
          >新增地址</el-button
        >
      </div>
      <el-table :data="tableData" style="width: 100%" border>
        <el-table-column label="记录 ID" prop="id"></el-table-column>
        <el-table-column label="收货人" prop="name"></el-table-column>
        <el-table-column label="收货地址" prop="address"></el-table-column>
        <el-table-column label="联系方式" prop="phone"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="创建时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作" width="220">
          <template slot-scope="scope">
            <el-button
              type="success"
              @click="selectAddress(scope.$index)"
              size="small"
              >选用</el-button
            >
            <el-button
              type="primary"
              @click="showEditAddress(scope.$index)"
              size="small"
              >编辑</el-button
            >
            <el-button
              type="danger"
              @click="deleteAddress(scope.$index)"
              size="small"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
      title="添加地址"
      :visible.sync="dialog.addAddressVisible"
      :append-to-body="true"
      :modal-append-to-body="false"
    >
      <el-form :model="dialog.newAddress" ref="addAddressForm"  :rules="rules">
        <el-form-item label="收货人" :label-width="'120px'" prop="name">
          <el-input
            v-model="dialog.newAddress.name"
            autocomplete="off"
            placeholder="请输入收货人"
          ></el-input>
        </el-form-item>
        <el-form-item label="收货地址" :label-width="'120px'" prop="address">
          <el-input
            v-model="dialog.newAddress.address"
            autocomplete="off"
            placeholder="请输入收货地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系方式" :label-width="'120px'" prop="phone">
          <el-input
            v-model="dialog.newAddress.phone"
            autocomplete="off"
            placeholder="请输入联系方式"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.addAddressVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="
            dialog.addAddressVisible = false;
            addAddress();
          "
          >确 定</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      title="编辑地址"
      :visible.sync="dialog.editAddressVisible"
      :append-to-body="true"
      :modal-append-to-body="false"
    >
      <el-form :model="dialog.editAddress" ref="editAddressForm" :rules="rules">
        <el-form-item label="收货人" :label-width="'120px'" prop="name">
          <el-input
            v-model="dialog.editAddress.name"
            autocomplete="off"
            placeholder="请输入收货人"
          ></el-input>
        </el-form-item>
        <el-form-item label="收货地址" :label-width="'120px'" prop="address">
          <el-input
            v-model="dialog.editAddress.address"
            autocomplete="off"
            placeholder="请输入收货地址"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系方式" :label-width="'120px'" prop="phone">
          <el-input
            v-model="dialog.editAddress.phone"
            autocomplete="off"
            placeholder="请输入联系方式"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.editAddressVisible = false">取 消</el-button>
        <el-button
          type="primary"
          @click="
            dialog.editAddressVisible = false;
            editAddress();
          "
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </el-drawer>
</template>
<style lang="scss" scoped>
.out-box {
  padding: 10px 24px;
}
.mb10 {
  margin-bottom: 10px;
}
</style>
<script>
import axios from '../common/http.js'
export default {
  props: ["visible"],
  watch: {
    visible(value) {
      this.isVisible = value;
      if(value) this.getTableData();
    },
    isVisible(value) {
      this.$emit("change", value);
    },
  },
  data() {
    let map = {
      name: "收货人不能为空",
      address: "收货地址不能为空",
      phone: "联系方式不能为空",
    };
    let rules = {};
    for (let k in map) {
      rules[k] = [
        {
          validator: (rule, value, callback) => {
            if (!value) {
              callback(new Error(map[k]));
            } else {
              callback();
            }
          },
          trigger: "blur",
        },
      ];
    }
    return {
      isVisible: this.visible,
      rules,
      dialog: {
        addAddressVisible: false,
        editAddressVisible: false,
        newAddress: {
          name: "",
          address: "",
          phone: "",
        },
        editAddress: {
          name: "",
          address: "",
          phone: "",
        },
      },
      tableData: [],
    };
  },
  mounted(){
    this.getTableData();
  },
  methods: {
    getTableData(){
      return axios.get('/user/address/list').then(res => {
        if(res&&res.code==1){
          this.tableData = res.data;
        }
      })
    },
    showAddAddress() {
      this.dialog.addAddressVisible = true;
    },
    showEditAddress(index) {
      this.dialog.editAddress = this.tableData[index];
      this.dialog.editAddressVisible = true;
    },
    selectAddress(index) {
      this.$emit("select",this.tableData[index]);
    },
    deleteAddress(index) {
      let id = this.tableData[index].id;
      this.$confirm('此操作将永久删除该地址, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('/user/address/delete',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除地址成功',type: 'success'});
            this.getTableData();
          }else{
            this.$alert(res.tips,"删除地址失败",{confirmButtonText: '确定'})
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    },
    addAddress(edit = false) {
      let addr = edit ? this.dialog.editAddress : this.dialog.newAddress;
      let form = edit ? this.$refs['editAddressForm'] : this.$refs['addAddressForm'];
      let action = edit ? '编辑' : '添加';
      form.validate(valid=>{
        if(valid){
          axios.post('/user/address/save',addr).then(res => {
            if(res&&res.code==1){
              this.getTableData();
              this.addAddressVisible = false;
              this.editAddressVisible = false;
              this.$message({type:'success',message:action+'地址成功'});
            }else{
              this.$alert(res.tips,action+"地址失败",{confirmButtonText: '确定'})
            }
          })
        }else{
          this.$alert('信息填写有误',"未提交",{confirmButtonText: '确定'})
        }
      })
    },
    editAddress() {
      this.addAddress(true);
    },
  },
};
</script>
