<template>
  <div class="outer-div">
    <el-dropdown class="cbutton">
      <span class="person-center-button">其他登录</span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item
          ><router-link to="/admin/login" class="alink"
            >管理员登录</router-link
          ></el-dropdown-item
        >
        <el-dropdown-item
          ><router-link to="/" class="alink"
            >返回首页</router-link
          ></el-dropdown-item
        >
        <el-dropdown-item
          ><router-link to="/delivery/login" class="alink"
            >配送员登录</router-link
          ></el-dropdown-item
        >
      </el-dropdown-menu>
    </el-dropdown>
    <div class="login-div">
      <h2 class="title-text">商家管理</h2>
      <el-form
        :model="loginForm"
        status-icon
        :rules="rules"
        ref="loginForm"
        label-width="100px"
        class="login-form"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="注册时的手机号码或邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input
            type="password"
            @keydown.enter.native="loginAction('loginForm')"
            v-model="loginForm.password"
            autocomplete="off"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loginAction('loginForm')"
            >登录</el-button
          >
          <el-button @click="resetForm('loginForm')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.alink {
  color: #333;
  text-decoration: none;
}
.outer-div {
  overflow: hidden;
  position: relative;

  .cbutton {
    position: fixed;
    top: 10px;
    right: 50%;
  }
}
.login-div {
  width: 500px;
  margin: 0 auto;
  margin-top: 150px;
  padding: 20px 30px;
  border: 1px solid #f5f5f5;
  border-radius: 10px;
  .title-text {
    font-size: 30px;
    text-align: center;
    color: #333;
    line-height: 2;
    margin-bottom: 30px;
  }
  .login-form {
  }
}
</style>
<script>
import axios from "../common/http.js";
export default {
  data() {
    const checkUsername = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("请输入手机号码或邮箱"));
      } else {
        callback();
      }
    };
    var validatePassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        callback();
      }
    };
    return {
      toURL: this.$route.query.from || "/store/menu",
      loginForm: {
        username: "",
        password: "",
      },
      rules: {
        username: [{ validator: checkUsername, trigger: "blur" }],
        password: [{ validator: validatePassword, trigger: "blur" }],
      },
    };
  },
  created() {
    //获取用户信息
    let role_name = "store";
    axios.get("/open/loginStatus").then((res) => {
      if (res && res.code == 1) {
        let user = res.data;
        if (user.roles instanceof Array) {
          if (user.roles.map((v) => v.name).indexOf(role_name)>=0) {
            this.$router.push(this.toURL); //跳转到默认页面
          }
        }
      }
    });
  },
  methods: {
    loginAction(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post("/open/storeLogin", this.loginForm).then((res) => {
            if (res && res.code == 1) {
              this.$message({
                message: "登录成功",
                type: "success",
                showClose: true,
                onClose: () => {
                  this.$router.push(this.toURL); //跳转到默认页面
                },
              });
            } else if (res.tips) {
              this.$alert(res.tips, "登录失败", {
                confirmButtonText: "确定",
              });
            }
          });
        } else {
          this.$alert("信息填写有误，请检查用户名和密码", "未提交", {
            confirmButtonText: "确定",
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>
