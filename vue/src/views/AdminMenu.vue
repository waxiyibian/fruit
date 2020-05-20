<template>
  <div class="home-div">
    <div class="header-div">
      <span class="username" @click="editProfile" v-show="userInfo.username">
        <i class="el-icon-user-solid"></i>
        <span class="username-span">{{userInfo.username}}</span>
      </span>
      <span>
        <router-link to="/" class="alink">返回首页</router-link>
      </span>
      <span class="logout-button" @click="logout">
        <i class="el-icon-switch-button"></i>退出
      </span>
    </div>
    <el-menu unique-opened router
      :default-active="$route.path"
      class="left-menu"
      @open="collapseLeft(true)" @close="collapseLeft(false)"
      :collapse="leftMenu.isCollapse"
    >
      <component class="menu-item" v-for="(value) in leftMenu.navList" :key="value.title+value.url"
      :index="value.url"
      :is="(value.children&&value.children.length>0)?'el-submenu':'el-menu-item'">
        <template slot="title">
          <i :class="[value.icon]"></i>
          <span>{{value.title}}</span>
        </template>
        <template v-if="value.children&&value.children.length>0">
          <el-menu-item class="menu-item" v-for="(v,i) in value.children" :key="v.url+i" :index="v.url">
            <i :class="[v.icon]"></i>
            <span slot="title">{{v.title}}</span>
          </el-menu-item>
        </template>
      </component>
    </el-menu>
    <div class="right-part" :class="{'collapse':leftMenu.isCollapse}">
      <div class="article-div">
        <router-view @update-user="getUserInfo"></router-view>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
  .alink{
    color: #333;
    font-size: 12px;
    text-decoration: none;
  }
  .home-div{
    position: fixed;
    width: 100%;
    height: 100%;
    left: 0;
    top: 0;

    .header-div{
      position: fixed;
      height: 30px;
      width: 100%;
      box-sizing: border-box;
      padding: 0 20px;
      border-bottom: 1px solid #f5f5f5;
      right: 0;
      top: 0;
      z-index: 10;
      display: flex;
      justify-content: flex-end;
      align-items: center;

      .logout-button{
        display: inline-block;
        height: 100%;
        cursor: pointer;
        font-size: 12px;
        line-height: 30px;
        padding: 0 20px;

        &:hover{
          background: #eee;
        }
      }
      .username{
        font-size: 12px;
        margin: 0 12px;
        cursor: pointer;
        &:hover{
          text-decoration: underline;
        }
      }
    }
    .left-menu{
      position: absolute;
      height: calc(100% - 30px);
      width: 200px;
      left: 0;
      bottom: 0;
      border-right: 1px solid #f5f5f5;
      box-sizing: border-box;
      z-index: 5;

      .menu-item{
        border-bottom: 1px solid #f5f5f5;
      }
    }
    .right-part{
      position: absolute;
      width: calc(100% - 200px);
      height: calc(100% - 30px);
      right: 0;
      bottom: 0;
      box-sizing: border-box;
      .article-div{
        position: relative;
        width: 100%;
        height: 100%;
        border: 1px solid #fff;
        box-sizing: border-box;
      }
    }
  }
</style>
<script>
import axios from '../common/http.js';
window.axios=axios;

export default {
  data(){
    return {
      userInfo:{
        username: '',
      },
      leftMenu:{
        isCollapse: false,
        navList: [{
          icon: 'el-icon-suitcase',
          title: '商家管理',
          url: '/admin/menu/StoreManage',
        },{
          icon: 'el-icon-setting',
          title: '用户管理',
          url: '/admin/menu/CustomerManage'
        },{
          icon: 'el-icon-truck',
          title: '配送员管理',
          url: '/admin/menu/DeliverManage'
        },{
          icon: 'el-icon-price-tag',
          title: '推荐管理',
          url: '/admin/menu/CommendManage'
        },{
          icon: 'el-icon-s-promotion',
          title: '入驻申请',
          url: '/admin/menu/StoreRequestManage'
        }, {
          icon: 'el-icon-folder-opened',
          title: '备份还原',
          url: '/admin/menu/BackupRestore'
        },{
          icon: 'el-icon-user',
          title: '个人资料',
          url: '/admin/menu/PersonProfile'
        }]
      }
    }
  },
  mounted(){
    window.$ = this.$;
    this.getUserInfo();
    this
    //
  },
  methods: {
    getUserInfo(id=null){
      if(id==null||id==this.userInfo.id){
        //获取用户信息
        axios.get("/open/loginStatus").then((res)=>{
          if(res&&res.code==1){
            this.userInfo = res.data;
          }
        });
      }
    },
    logout(){
      axios.get('/logout').then(()=>{
        this.$router.push("/admin/login");
      })
    },
    collapseLeft(){

    },
    editProfile(){
      this.$router.push("/admin/menu/PersonProfile")
    }
  }
};
</script>
