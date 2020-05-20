import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'jquery.scrollbar/jquery.scrollbar.css'
import './assets/css/yui-reset.css'

//引入jquery
import $ from 'jquery'
//执行脚本，脚本会往jquery中注册插件
import 'jquery.scrollbar'
//为jquery.scroll兼容vue中style标签的scoped属性
let sb = $.prototype.scrollbar;
$.prototype.scrollbar = function(){
  sb.apply(this,arguments);
  //在执行原scrollbar()方法后，会生成一个当前节点的父节点，并进行类名同步，此处为了兼容进行dataset同步。
  this.each((i,v)=>{
    let $self = $(v);
    let ats = [...v.attributes];

    for(let k of ats){
      if(/^data-v-/.test(k.nodeName)){
        $self.parent().attr(k.nodeName,"");
        break;
      }
    }
  });
}

Vue.use(ElementUI);
Vue.config.productionTip = false
//将jquery链接到Vue的原型中
Vue.prototype.$ = Vue.prototype.jQuery = $;

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
