(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-a928cb80"],{"03e8":function(t,e,a){"use strict";var i=a("3b6e"),o=a.n(i);o.a},"3b6e":function(t,e,a){},"63c7":function(t,e,a){"use strict";a.r(e);var i=function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"fit-parent"},[a("div",{staticClass:"account-manage-div jquery-scrollbar-element scrollbar-macosx"},[a("div",{staticClass:"padding24"},[t._m(0),a("div",{staticClass:"accounts-div"},[a("div",{staticClass:"account-table"},[a("el-tabs",{attrs:{type:"border-card"},on:{"tab-click":t.updateInfo},model:{value:t.activeTab,callback:function(e){t.activeTab=e},expression:"activeTab"}},[a("el-tab-pane",{attrs:{label:"配送员列表",name:"menuList"}},[a("i",{staticClass:"el-icon-refresh refresh-table",on:{click:function(e){return t.getTableInfo()}}}),a("div",{staticClass:"table-div jquery-scrollbar-element scrollbar-macosx"},[a("div",{staticClass:"inline-block"},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.tableInfo.tableData,border:""}},[a("el-table-column",{attrs:{label:"配送员 ID",prop:"id"}}),a("el-table-column",{attrs:{label:"用户名",prop:"username"}}),a("el-table-column",{attrs:{label:"昵称",prop:"nickname"}}),a("el-table-column",{attrs:{label:"手机号码",prop:"phone"}}),a("el-table-column",{attrs:{label:"创建时间",prop:"createTime"}}),a("el-table-column",{attrs:{label:"操作",width:"100"},scopedSlots:t._u([{key:"default",fn:function(e){return[a("el-button",{attrs:{type:"primary",icon:"el-icon-edit",circle:"",title:"编辑",size:"small"},on:{click:function(a){return t.showEditAccount(e.$index)}}})]}}])})],1)],1)])])],1)],1)]),a("el-dialog",{attrs:{title:"编辑配送",visible:t.dialog.editAccountVisible,"modal-append-to-body":!1},on:{"update:visible":function(e){return t.$set(t.dialog,"editAccountVisible",e)}}},[a("el-form",{ref:"editAccountForm",attrs:{model:t.dialog.account,rules:t.dialog.rules}},[a("el-form-item",{attrs:{label:"配送员","label-width":"140px"}},[a("div",[t._v("ID"+t._s(t.dialog.account.id)+",昵称"+t._s(t.dialog.account.nickname||t.dialog.account.username)+",手机号码"+t._s(t.dialog.account.phone))])]),a("el-form-item",{attrs:{label:"为指定的店铺配送","label-width":"140px"}},[a("el-select",{staticStyle:{width:"300px"},attrs:{multiple:"",filterable:"",placeholder:"请选择"},model:{value:t.dialog.account.storeIds,callback:function(e){t.$set(t.dialog.account,"storeIds",e)},expression:"dialog.account.storeIds"}},t._l(t.storeList,(function(t){return a("el-option",{key:t.value,attrs:{label:t.label,value:t.value}})})),1)],1)],1),a("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{on:{click:function(e){t.dialog.editAccountVisible=!1}}},[t._v("取 消")]),a("el-button",{attrs:{type:"primary"},on:{click:function(e){return t.editAccount()}}},[t._v("确 定")])],1)],1)],1)])])},o=[function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticClass:"title-div"},[a("h2",{staticClass:"title-text"},[a("span",[t._v("配送商铺")])])])}],l=(a("b4fb"),a("fe59"),a("2eeb"),a("08d5"),a("08ba"),a("80a0")),n=a("1f75"),s={data:function(){var t={storeList:[],dialog:{editAccountVisible:!1,account:{userId:"",storeIds:[]},rules:{}},activeTab:"menuList",tableInfo:{page_num:1,page_size:8,total_num:1,tableData:[]}};return t},mounted:function(){this.$(".jquery-scrollbar-element").scrollbar(),this.getTableInfo(),this.getStoreList()},methods:{getStoreList:function(){var t=this;n["a"].get("/open/store/findAll").then((function(e){e&&1==e.code&&e.data instanceof Array&&(e.data.forEach((function(t){t.label="ID:".concat(t.id," 店名:").concat(t.storeName," 电话:").concat(t.phone),t.value=t.id})),t.storeList=e.data)}))},updateInfo:function(){"menuList"==this.activeTab&&this.getTableInfo()},getTableInfo:function(){var t=this,e=arguments.length>0&&void 0!==arguments[0]&&arguments[0];e||this.tableInfo;n["a"].get("/delivery/get").then((function(e){if(e&&1==e.code&&e.data){if(e.data&&e.data.tableData instanceof Array){var a={};e.data.tableData.forEach((function(t){if(a[t.user.id]){a[t.user.id].storeIds.push(t.store.id);var e=a[t.user.id].ct;t.createTime>e&&(a[t.user.id].ct=t.createTime)}else a[t.user.id]=Object(l["a"])(Object(l["a"])({},t.user),{},{storeIds:[t.store.id],ct:t.createTime})}));var i=[];for(var o in a)i.push(a[o]);i=i.sort((function(t,e){t.ct,e.ct})),e.data.tableData=i}t.tableInfo=Object.assign(t.tableInfo,e.data)}}))},showEditAccount:function(t){var e="menuList"==this.activeTab?this.tableInfo:this.searchInfo,a=e.tableData[t];this.dialog.account=Object(l["a"])(Object(l["a"])({},a),{},{userId:a.id}),this.dialog.editAccountVisible=!0},editAccount:function(){var t=this,e=arguments.length>0&&void 0!==arguments[0]&&arguments[0];this.dialog.editAccountVisible=!1;var a=e?"":this.dialog.account,i=e?"添加":"编辑",o=e?"":this.$refs["editAccountForm"],l=a.storeIds||[];if(!l||l.length<=0)this.$alert("请选择配送至少一个商铺","未提交",{confirmButtonText:"确定"});else if(a.userId){var s=JSON.stringify(l.map((function(t){return{storeId:t,userId:a.userId}})));console.log(s,l,a),o.validate((function(a){if(!a)return t.$alert("信息填写有误","未提交",{confirmButtonText:"确定"}),!1;n["a"].post("/delivery/save",s,{headers:{"Content-Type":"application/json;charset=utf-8"}}).then((function(a){a&&1==a.code?(t.$message({message:i+"配送成功",type:"success"}),e&&o.resetFields(),t.updateInfo()):t.$alert(a.tips,i+"配送失败",{confirmButtonText:"确定"})}))}))}else this.$alert("您还不是配送员","未提交",{confirmButtonText:"确定"})}}},c=s,r=(a("03e8"),a("4023")),u=Object(r["a"])(c,i,o,!1,null,"89fe6eb2",null);e["default"]=u.exports}}]);
//# sourceMappingURL=chunk-a928cb80.75218f11.js.map