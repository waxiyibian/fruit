<template>
  <div class="fit-parent">
    <div class="store-profile-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>店铺资料</span>
          </h2>
        </div>
        <div class="tab-div">
          <el-tabs type="card" v-model="activeTab" @tab-click="tabChange">
            <el-tab-pane label="店铺信息" name="info">
              <el-form :label-position="'right'" label-width="80px" :model="storeInfo">
                <el-form-item label="店铺 ID:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.id}}</span>
                </el-form-item>
                <el-form-item label="店名:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.storeName}}</span>
                </el-form-item>
                <el-form-item label="联系方式:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.phone}}</span>
                </el-form-item>
                <el-form-item label="起送金额:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.minCost}}</span>
                </el-form-item>
                <el-form-item label="配送费:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.postFee}}</span>
                </el-form-item>
                <el-form-item label="地址:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.address}}</span>
                </el-form-item>
                <el-form-item label="LOGO:" class="closer-form-item">
                  <el-image v-if="storeInfo.id"
                    fit="fill"
                    class="limg"
                    :src="storeInfo.logo" style="width:200px;height:200px;"
                    :preview-src-list="[storeInfo.logo]"
                  ></el-image>
                </el-form-item>
                <el-form-item label="创建时间:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.createTime}}</span>
                </el-form-item>
                <el-form-item label="更新时间:" class="closer-form-item">
                  <span class="field-span">{{storeInfo.updateTime}}</span>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="修改资料" name="edit">
              <el-form
                :label-position="'right'"
                label-width="80px"
                :model="store"
                :rules="rules"
                ref="storeForm"
              >
                <el-form-item class="first-line" label="店名" prop="storeName">
                  <el-input v-model="store.storeName" placeholder="请输入店铺名称"></el-input>
                </el-form-item>
                <el-form-item label="联系方式" prop="phone">
                  <el-input v-model="store.phone" placeholder="请输入联系方式"></el-input>
                </el-form-item>
                <el-form-item label="地址" prop="address">
                  <el-input v-model="store.address" placeholder="请输入店铺地址"></el-input>
                </el-form-item>
                <el-form-item label="起送/元" prop="minCost">
                  <el-input v-model="store.minCost" placeholder="请输入起送金额"></el-input>
                </el-form-item>
                <el-form-item label="配送费/元" prop="postFee">
                  <el-input v-model="store.postFee" placeholder="请输入配送费"></el-input>
                </el-form-item>
                <el-form-item lanel="LOGO" prop="logo">
                  <el-upload
                    class="upload-image"
                    :on-success="editIconSuccess"
                    :action="uploadUrl"
                    :limit="1"
                    name="file"
                    list-type="picture-card"
                    :before-upload="beforeIconUpload"
                    :on-exceed="uploadExcced"
                    :file-list="store.fileList"
                  >
                    <i class="el-icon-plus"></i>
                  </el-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="editProfile">提交</el-button>
                  <el-button @click="resetForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "../common/http.js";
export default {
  data() {
    return {
      activeTab: 'info',
      uploadUrl: '/store/uploadIcon',
      storeInfo: {
        id: "",
        storeName: "",
        phone: "",
        address: "",
        updateTime: "",
        username: ""
      },
      store: {
        storeName: "",
        phone: "",
        address: "",
        logo: "",
        minCost: 0,
        postFee: 0
      },
      rules: {
        storeName: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("请输入店铺名称"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        address: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("请输入店铺地址"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        phone: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("请输入手机号码"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ]
      }
    };
  },
  mounted() {
    this.$(".jquery-scrollbar-element").scrollbar();
    this.getStoreInfo();
  },
  methods: {
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
    editIconSuccess(res){
      if(res&&res.code==1){
        this.store.logo = res.data;
      }
    },
    tabChange(){
      
    },
    getStoreInfo() {
      //获取店铺信息
      axios.get("/store/info").then(res => {
        if (res && res.code == 1) {
          this.storeInfo = res.data;
          this.store = { ...res.data };
          let logo = this.store.logo;
          if(logo)
            this.store["fileList"] =[{name: logo.split('/').pop(),url: logo}];
        }
      });
    },
    resetForm() {
      this.$refs["storeForm"].resetFields();
    },
    editProfile() {
      let store = { ...this.store };
      delete store.updateTime;
      delete store.createTime;
      this.$refs["storeForm"].validate(valid => {
        if (valid) {
          axios.post("/store/save", store).then(res => {
            if (res && res.code == 1) {
              this.$message({ message: "修改资料成功", type: "success" });
              this.resetForm();
              this.getStoreInfo();
            } else {
              this.$alert(res.tips, "修改资料失败", {
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
.closer-form-item {
  margin: 0;
}
.first-line {
  margin-top: 24px;
}
.fit-parent {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.padding24 {
  padding: 24px !important;
}
.store-profile-div {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  .tab-div {
    border: 1px solid #f5f5f5;
    border-radius: 5px;
  }
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
}
</style>
