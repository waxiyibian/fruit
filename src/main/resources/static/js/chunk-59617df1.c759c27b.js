(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-59617df1"],{"53d1":function(e,t,a){"use strict";var r=a("e5b0"),l=a.n(r);l.a},e5b0:function(e,t,a){},f793:function(e,t,a){"use strict";a.r(t);var r=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"fit-parent"},[a("div",{staticClass:"order-manage-div jquery-scrollbar-element scrollbar-macosx"},[a("div",{staticClass:"padding24"},[e._m(0),a("div",{staticClass:"orders-div"},[a("div",{staticClass:"tools-div"},[a("el-form",{staticClass:"demo-form-inline",attrs:{inline:!0,model:e.searchInfo}},[a("el-form-item",[a("el-select",{staticStyle:{width:"100px"},attrs:{placeholder:"请选择",size:"small"},model:{value:e.orderStatus,callback:function(t){e.orderStatus=t},expression:"orderStatus"}},e._l(e.statusList,(function(e){return a("el-option",{key:e.value,attrs:{label:e.label,value:e.value}})})),1)],1),a("el-form-item",[a("el-input",{staticClass:"search-input",attrs:{size:"small",placeholder:"请输入用户昵称/联系方式/订单编号","prefix-icon":"el-icon-search"},nativeOn:{keydown:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.getSearchInfo()}},model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}})],1),a("el-form-item",[a("el-button",{attrs:{size:"small",plain:"",type:"primary",icon:"el-icon-search"},on:{click:function(t){return e.getSearchInfo()}}},[e._v("查询")])],1)],1)],1),a("div",{staticClass:"order-table"},[a("el-tabs",{attrs:{type:"border-card"},on:{"tab-click":e.updateInfo},model:{value:e.activeTab,callback:function(t){e.activeTab=t},expression:"activeTab"}},[a("el-tab-pane",{attrs:{label:"订单列表",name:"menuList"}},[a("i",{staticClass:"el-icon-refresh refresh-table",on:{click:function(t){return e.getTableInfo()}}}),a("div",{staticClass:"table-div jquery-scrollbar-element scrollbar-macosx"},[a("div",{staticClass:"inline-block"},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.tableInfo.tableData,border:""},on:{"expand-change":e.updateScrollbar}},[a("el-table-column",{attrs:{type:"expand"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-form",{staticClass:"demo-table-expand",attrs:{"label-position":"left",inline:""}},[e._l(e.tableInfo.stringMap,(function(r,l){return a("el-form-item",{key:r.id,attrs:{label:e.tableInfo.stringMap[l]}},[a("span",[e._v(e._s(t.row[l]))])])})),a("el-form-item",{attrs:{label:"订单内容"}},e._l(t.row.orderDetailList,(function(t){return a("div",{key:t.detailId},[e._v(" "+e._s(t.productName)+" ¥"+e._s(t.productPrice)+" x "+e._s(t.productQuantity)+" ")])})),0)],2)]}}])}),a("el-table-column",{attrs:{label:"订单 ID",prop:"orderId"}}),a("el-table-column",{attrs:{label:"用户名称",prop:"buyerName"}}),a("el-table-column",{attrs:{label:"订单金额",prop:"orderAmount"}}),a("el-table-column",{attrs:{label:"联系方式",prop:"buyerPhone"}}),a("el-table-column",{attrs:{label:"订单状态",prop:"orderStatusStr"}}),a("el-table-column",{attrs:{label:"操作",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"success",icon:"el-icon-check",circle:"",title:"确定配送",disabled:t.row.orderStatus>=e.actionMap["delivery"].status,size:"small"},on:{click:function(a){return e.changeStatus(t.$index,"delivery")}}})]}}])})],1)],1)]),a("div",{staticClass:"pager-div"},[a("el-pagination",{attrs:{"current-page":e.tableInfo.page_num,"page-sizes":[6,8,10,15,20,30],"page-size":e.tableInfo.page_size,layout:"total, sizes, prev, pager, next, jumper",total:e.tableInfo.total_num,"pager-count":5,size:"small"},on:{"size-change":e.changePageSize,"current-change":e.changePage}})],1)]),a("el-tab-pane",{attrs:{label:"搜索结果",name:"searchResult"}},[a("i",{staticClass:"el-icon-refresh refresh-table",on:{click:function(t){return e.getSearchInfo(!0)}}}),a("div",{staticClass:"table-div jquery-scrollbar-element scrollbar-macosx"},[a("div",{staticClass:"inline-block"},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:e.searchInfo.tableData,border:""},on:{"expand-change":e.updateScrollbar}},[a("el-table-column",{attrs:{type:"expand"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-form",{staticClass:"demo-table-expand",attrs:{"label-position":"left",inline:""}},[e._l(e.tableInfo.stringMap,(function(r,l){return a("el-form-item",{key:r.id,attrs:{label:e.tableInfo.stringMap[l]}},[a("span",[e._v(e._s(t.row[l]))])])})),a("el-form-item",{attrs:{label:"订单内容"}},e._l(t.row.orderDetailList,(function(t){return a("div",{key:t.detailId},[e._v(" "+e._s(t.productName)+" ¥"+e._s(t.productPrice)+" x "+e._s(t.productQuantity)+" ")])})),0)],2)]}}])}),a("el-table-column",{attrs:{label:"订单 ID",prop:"orderId"}}),a("el-table-column",{attrs:{label:"用户名称",prop:"buyerName"}}),a("el-table-column",{attrs:{label:"订单金额",prop:"orderAmount"}}),a("el-table-column",{attrs:{label:"联系方式",prop:"buyerPhone"}}),a("el-table-column",{attrs:{label:"订单状态",prop:"orderStatusStr"}}),a("el-table-column",{attrs:{label:"操作",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[a("el-button",{attrs:{type:"success",icon:"el-icon-check",circle:"",title:"确定配送",disabled:t.row.orderStatus>=e.actionMap["delivery"].status,size:"small"},on:{click:function(a){return e.changeStatus(t.$index,"delivery")}}})]}}])})],1)],1)]),a("div",{staticClass:"pager-div"},[a("el-pagination",{attrs:{"current-page":e.searchInfo.page_num,"page-sizes":[6,8,10,15,20,30],"page-size":e.searchInfo.page_size,layout:"total, sizes, prev, pager, next, jumper",total:e.searchInfo.total_num,"pager-count":5,size:"small"},on:{"size-change":e.changeSearchPageSize,"current-change":e.changeSearchPage}})],1)])],1)],1)])])])])},l=[function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"title-div"},[a("h2",{staticClass:"title-text"},[a("span",[e._v("订单配送")])])])}],s=(a("b4fb"),a("fe59"),a("d497"),a("08ba"),a("80a0")),n=a("1f75"),o={data:function(){var e={activeTab:"menuList",statusList:[{label:"全部",value:-1},{label:"新订单未支付",value:0},{label:"已支付/接单",value:1},{label:"配送中",value:3},{label:"已完结",value:4},{label:"已评价",value:5},{label:"已取消",value:6}],actionMap:{new:{status:0},pay:{url:"/store/order/confirmPay",tips:"确认支付",status:1},accept:{url:"/store/order/confirmPay",tips:"确认接单",status:1},delivery:{status:3,url:"/delivery/order/confirmDelivery",tips:"确认配送"},over:{url:"/store/order/finish",tips:"确认完结",status:4},cancel:{url:"/store/order/cancel",tips:"订单取消",status:6}},statusMap:{},searchInfo:{keyword:"",orderStatus:-1,page_num:1,page_size:8,total_num:1,tableData:[]},orderStatus:-1,keyword:"",tableInfo:{page_num:1,page_size:8,total_num:1,stringMap:{orderId:"订单ID",buyerName:"用户名称",buyerAddress:"地址",buyerPhone:"联系方式",updateTime:"更新时间",createTime:"创建时间"},tableData:[]}};return e.statusList.forEach((function(t){e.statusMap[t.value]=t.label})),e},mounted:function(){this.$(".jquery-scrollbar-element").scrollbar(),this.getTableInfo()},methods:{updateScrollbar:function(e,t){var a=this;this.$nextTick((function(){0!=t.length&&!0!==e||a.$(".table-div").updateScrollbars(!0)}))},changeSearchPage:function(e){this.searchInfo.page_num=e,this.getSearchInfo(!0)},changeSearchPageSize:function(e){this.searchInfo.page_size=e,this.getSearchInfo(!0)},changePage:function(e){this.tableInfo.page_num=e,this.getTableInfo()},changePageSize:function(e){this.tableInfo.page_size=e,this.getTableInfo()},updateInfo:function(){"menuList"==this.activeTab?this.getTableInfo():this.getSearchInfo(!0)},getTableInfo:function(){var e=this,t=arguments.length>0&&void 0!==arguments[0]&&arguments[0],a=t?this.searchInfo:this.tableInfo,r={};t&&(r={keyword:this.searchInfo.keyword,status:this.searchInfo.orderStatus}),n["a"].get("/delivery/order/list",Object(s["a"])({page_num:a.page_num,page_size:a.page_size},r)).then((function(a){a&&1==a.code&&a.data&&(a.data.tableData.forEach((function(t){t.orderStatusStr=e.statusMap[t.orderStatus]})),t?e.searchInfo=Object.assign(e.searchInfo,a.data):e.tableInfo=Object.assign(e.tableInfo,a.data))}))},getSearchInfo:function(){var e=arguments.length>0&&void 0!==arguments[0]&&arguments[0];e||(this.searchInfo.keyword=this.keyword,this.searchInfo.orderStatus=this.orderStatus),this.activeTab="searchResult",this.getTableInfo(!0)},changeStatus:function(e,t){var a=this,r=this.actionMap[t],l=r.url,s=r.tips,o="menuList"==this.activeTab?this.tableInfo:this.searchInfo,i=o.tableData[e].orderId;this.$confirm("将".concat(i,"号订单").concat(s,", 是否继续?"),"提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){n["a"].get(l,{orderId:i}).then((function(e){if(e&&1==e.code){a.$message({message:s+"成功",type:"success"});var t=-1;o.tableData.some((function(e,a){if(e.orderId==i)return t=a,!0}));var l=r.status;t>=0&&(o.tableData[t].orderStatus=l,o.tableData[t].orderStatusStr=a.statusMap[l])}else a.$alert(e.tips,s+"失败",{confirmButtonText:"确定"})}))}))["catch"]((function(){a.$message({type:"info",message:"已取消"})}))}}},i=o,c=(a("53d1"),a("4023")),u=Object(c["a"])(i,r,l,!1,null,"7e1354bc",null);t["default"]=u.exports}}]);
//# sourceMappingURL=chunk-59617df1.c759c27b.js.map