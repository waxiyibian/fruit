<template>
  <div class="fit-parent">
    <div class="person-profile-div jquery-scrollbar-element scrollbar-macosx">
    <div class="padding24">
      <div class="title-div">
        <h2 class="title-text">
          <span>个人资料</span>
        </h2>
      </div>
      <div class="tab-div">
        <el-tabs :tab-position="'left'" style="height: 350px;">
          <el-tab-pane label="用户信息">
            <el-form :label-position="'right'" label-width="80px" :model="userInfo">
              <el-form-item label="账号 ID:" class="closer-form-item">
                <span class="field-span">{{userInfo.id}}</span>
              </el-form-item>
              <el-form-item label="昵称:" class="closer-form-item">
                <span class="field-span">{{userInfo.nickname}}</span>
              </el-form-item>
              <el-form-item label="用户名:" class="closer-form-item">
                <span class="field-span">{{userInfo.username}}</span>
              </el-form-item>
              <el-form-item label="手机号码:" class="closer-form-item">
                <span class="field-span">{{userInfo.phone}}</span>
              </el-form-item>
              <el-form-item label="创建时间:" class="closer-form-item">
                <span class="field-span">{{userInfo.createTime}}</span>
              </el-form-item>
              <el-form-item label="更新时间:" class="closer-form-item">
                <span class="field-span">{{userInfo.updateTime}}</span>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="修改资料">
            <el-form
              :label-position="'right'"
              label-width="80px"
              :model="user"
              :rules="rules"
              ref="userForm"
            >
              <el-form-item class="first-line" label="昵称" prop="nickname">
                <el-input v-model="user.nickname" placeholder="请输入用户昵称"></el-input>
              </el-form-item>
              <el-form-item label="密码" prop="password">
                <el-input v-model="user.password" show-password placeholder="请输入密码，为空则不更改"></el-input>
              </el-form-item>
              <el-form-item label="用户名">
                <el-input v-model="user.username"  placeholder="请输入邮箱作为用户名"></el-input>
              </el-form-item>
              <el-form-item label="手机号码">
                <el-input v-model="user.phone"  placeholder="请输入11位手机号码"></el-input>
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
      userInfo: {
        createTime: "",
        username: "",
        phone: '',
        id: "",
        updateTime: "",
        nickname: ""
      },
      user: {
        nickname: "",
        password: "",
        username: "",
        phone: '',
      },
      rules: {
        nickname: [
          {
            validator: (rule, value, callback) => {
              if (!value) {
                callback(new Error("请输入昵称"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ],
        username: [
          {
            validator: (rule, value, callback) => {
              if (!/\d{11}/.test(value)) {
                callback(new Error("请输入11位手机号码"));
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
    //console.log(this.$.prototype.scrollbar);
    this.getUserInfo();
  },
  methods: {
    getUserInfo(){
      //获取用户信息
      axios.get("/open/loginStatus").then(res => {
        if (res && res.code == 1) {
          this.userInfo = res.data;
          this.user.nickname = res.data.nickname;
          this.user.username = res.data.username;
          this.user.phone = res.data.phone;
        }
      });
    },
    resetForm(){
      this.$refs['userForm'].resetFields();
    },
    editProfile() {
      let user = JSON.parse(JSON.stringify(this.user));
      if(user.password==null) delete user.password;
      this.$refs["userForm"].validate(valid => {
        if (valid) {
          axios.post("/user/save", {
            id: this.userInfo.id,
            ...user
          })
          .then(res => {
            if (res && res.code == 1) {
              this.$message({ message: "修改资料成功", type: "success" });
              this.resetForm();
              this.getUserInfo();
              this.$emit('update-user');
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
.fit-parent{
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.padding24{
  padding: 24px !important;
}
.person-profile-div {
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
