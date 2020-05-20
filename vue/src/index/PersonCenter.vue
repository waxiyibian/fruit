<template>
  <div class="home-div">
    <el-menu unique-opened router
      :default-active="$route.path"
      class="left-menu"
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
    display: flex;
    justify-content: stretch;
    .left-menu{
      min-height: 500px;
      .menu-item{
        border-bottom
        : 1px solid #f5f5f5;
      }
    }
    .right-part{
      flex-grow: 1;
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
        nickname: ''
      },
      leftMenu:{
        isCollapse: false,
        navList: [{
          icon: 'el-icon-document',
          title: '我的订单',
          url: '/personCenter/myOrder'
        },{
          icon: 'el-icon-s-order',
          title: '我的评价',
          url: '/personCenter/myComment'
        },{
          icon: 'el-icon-user',
          title: '个人资料',
          url: '/personCenter/profile'
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
      this.$emit("update-user",id);
    },
  }
};
</script>
