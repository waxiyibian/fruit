<template>
<div class="fit-parent">
  <div class="product-manage-div jquery-scrollbar-element scrollbar-macosx">
    <div class="padding24">
    <div class="title-div">
      <h2 class="title-text">
        <span>推荐菜单管理</span>
      </h2>
    </div>
    <div class="orders-div">
      <div class="tools-div">
        <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
          <el-form-item>
            <el-input
              size="small"
              class="search-input"
              placeholder="请输入关键字"
              prefix-icon="el-icon-search"
              @keydown.enter.native="getSearchInfo()"
              v-model="keyword"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button
              size="small"
              plain
              type="primary"
              @click="getSearchInfo()"
              icon="el-icon-search"
            >查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button size="small" plain @click="dialog.addCategoryVisible=true" icon="el-icon-plus">添加分类</el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              size="small"
              plain
              type="success"
              @click="showAddProduct"
              icon="el-icon-plus"
            >添加商品</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="order-table">
        <el-tabs type="border-card" v-model="activeTab" @tab-click="updateInfo">
          <el-tab-pane label="菜单列表" name="menuList">
            <div class="category-tags">
              <el-tag
                class="category-tag"
                v-for="(value,index) in tableInfo.categories" @click.native="showEditCategory($event,index)"
                :key="value.id"
                @click="selectCategory"
                @close="deleteCategory(index)"
                size="small"
                closable
              >{{value.name}}<i class="el-icon-edit edit-category"></i></el-tag>
            </div>
            <div class="table-div jquery-scrollbar-element scrollbar-macosx">
              <i class="el-icon-refresh refresh-table" @click="getTableInfo()"></i>
              <div class="inline-block">
                <el-table :data="tableInfo.tableData" style="width: 100%" @expand-change="updateScrollbar">
                  <el-table-column type="expand">
                    <template slot-scope="props">
                      <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item
                          v-for="(v,k) in tableInfo.stringMap"
                          :key="v.id"
                          :label="tableInfo.stringMap[k]"
                        >
                          <span>{{ props.row[k] }}</span>
                        </el-form-item>
                        <el-form-item label="商品图片">
                          <el-avatar shape="square" :size="100" fit="cover" :src="props.row['productIcon']"></el-avatar>
                        </el-form-item>
                      </el-form>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品 ID" prop="productId"></el-table-column>
                  <el-table-column label="商品名称" prop="productName"></el-table-column>
                  <el-table-column label="价格" prop="productPrice"></el-table-column>
                  <el-table-column label="库存" prop="productStock"></el-table-column>
                  <el-table-column label="操作" width="80">
                    <template slot-scope="scope">
                      <el-button
                        type="danger"
                        icon="el-icon-delete"
                        circle
                        title="删除"
                        @click="deleteProduct(scope.$index)"
                        size="small"
                      ></el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
            <div class="pager-div">
              <el-pagination
                @size-change="changePageSize"
                @current-change="changePage"
                :current-page="tableInfo.page_num"
                :page-sizes="[6, 8, 10, 15, 20, 30]"
                :page-size="tableInfo.page_size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="tableInfo.total_num"
                :pager-count="5"
                size="small"
              ></el-pagination>
            </div>
          </el-tab-pane>
          <!-- <el-tab-pane label="菜单预览" name="menuPreview">菜单预览</el-tab-pane> -->
          <el-tab-pane label="搜索结果" name="searchResult">
            <i class="el-icon-refresh refresh-table" @click="getSearchInfo(true)"></i>
            <div class="table-div jquery-scrollbar-element scrollbar-macosx">
              <div class="inline-block">
                <el-table :data="searchInfo.tableData" style="width: 100%" @expand-change="updateScrollbar">
                  <el-table-column type="expand">
                    <template slot-scope="props">
                      <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item
                          v-for="(v,k) in tableInfo.stringMap"
                          :key="v.id"
                          :label="tableInfo.stringMap[k]"
                        >
                          <span>{{ props.row[k] }}</span>
                        </el-form-item>
                        <el-form-item label="商品图片">
                          <el-avatar shape="square" :size="100" fit="cover" :src="props.row['productIcon']"></el-avatar>
                        </el-form-item>
                      </el-form>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品 ID" prop="productId"></el-table-column>
                  <el-table-column label="商品名称" prop="productName"></el-table-column>
                  <el-table-column label="价格" prop="productPrice"></el-table-column>
                  <el-table-column label="库存" prop="productStock"></el-table-column>
                  <el-table-column label="操作" width="80">
                    <template slot-scope="scope">
                      <el-button
                        type="danger"
                        icon="el-icon-delete"
                        circle
                        title="删除"
                        @click="deleteProduct(scope.$index)"
                        size="small"
                      ></el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
            <div class="pager-div">
              <el-pagination
                @size-change="changeSearchPageSize"
                @current-change="changeSearchPage"
                :current-page="searchInfo.page_num"
                :page-sizes="[6, 8, 10, 15, 20, 30]"
                :page-size="searchInfo.page_size"
                layout="total, sizes, prev, pager, next, jumper"
                :total="searchInfo.total_num"
                :pager-count="5"
                size="small"
              ></el-pagination>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <el-dialog title="添加分类" :visible.sync="dialog.addCategoryVisible" :modal-append-to-body='false'>
      <el-form :model="dialog.newCategory">
        <el-form-item label="分类名称" :label-width="'120px'">
          <el-input v-model="dialog.newCategory.categoryName" autocomplete="off"
          @keydown.enter.native="dialogFormVisible = false;addCategory()"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.addCategoryVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialog.addCategoryVisible = false;addCategory()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑分类" :visible.sync="dialog.editCategoryVisible" :modal-append-to-body='false'>
      <el-form :model="dialog.editCategory">
        <el-form-item label="分类名称" :label-width="'120px'" >
          <el-input v-model="dialog.editCategory.oldName" disabled></el-input>
        </el-form-item>
        <el-form-item label="编辑名称" :label-width="'120px'" >
          <el-input v-model="dialog.editCategory.categoryName" @keydown.enter.native="editCategoryVisible = false;editCategory()"
          placeholder="请输入名称" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.editCategoryVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialog.editCategoryVisible = false;editCategory()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="添加商品" :visible.sync="dialog.addProductVisible" :modal-append-to-body='false' :append-to-body="true" @close="dialog.addProductVisible = false">
      <el-form :model="dialog.newProduct" status-icon :rules="dialog.rules">
        <el-form-item label="推荐商品" :label-width="'120px'" >
          <el-select v-model="dialog.newProduct.productId" filterable placeholder="请选择">
            <el-option
              v-for="item in productList"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="分类" :label-width="'120px'" >
          <el-select v-model="dialog.newProduct.typeId" size="small" placeholder="请选择">
            <el-option v-for="item in tableInfo.categories" :key="item.id"
            :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.addProductVisible = false">取 消</el-button>
        <el-button type="primary" @click="addProduct">确 定</el-button>
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
    return {
      productList: [],
      dialog: {
        addCategoryVisible: false,
        editCategoryVisible: false,
        addProductVisible: false,
        editCategoryIndex: -1,
        newCategory: {
          categoryName: ''
        },
        editCategory: {
          categoryName: '',
          oldName: ''
        },
        newProduct: {
          id: '',
          typeId: '',
          productId: ''
        },
        rules: {
        },
        editProduct: {
          id: '',
          typeId: '',
          productId: ''
        }
      },
      activeTab: "menuList",
      categoryTypeMap: {},
      searchInfo: {
        keyword: "",
        page_num: 1,
        page_size: 8,
        total_num: 1,
        tableData: []
      },
      keyword: "",
      tableInfo: {
        page_num: 1,
        page_size: 8,
        total_num: 1,
        categories: [
        ],
        stringMap: {
          productId: "商品ID",
          productName: "商品名称",
          categoryName: "分类",
          productDescription: "描述",
          productPrice: "价格",
          productStock: "库存",
          //productIcon: "图片",
          updateTime: "更新时间",
          createTime: "创建时间"
        },
        tableData: [
        ]
      }
    };
  },
  mounted() {
    this.$('.jquery-scrollbar-element').scrollbar();
    this.getTableInfo();
    this.getProductList();
  },
  methods: {
    getProductList(){
      axios.get('/admin/product/findAll').then(res=>{
        if(res&&res.code==1){
          if(res.data instanceof Array){
            res.data.forEach(v=>{
              v.label = `ID:${v.id} 名称:${v.productName} 价格:${v.price}`;
              v.value = v.id;
            })
            this.productList = res.data;
          }
        }
      })
    },
    updateScrollbar(row,rows){
      this.$nextTick(()=>{
        if(rows.length==0||row===true){
          this.$('.table-div').updateScrollbars(true)
        }
      });
    },
    changeSearchPage(page_num){
      this.searchInfo.page_num=page_num;
      this.getSearchInfo(true);
    },
    changeSearchPageSize(page_size){
      this.searchInfo.page_size=page_size;
      this.getSearchInfo(true);
    },
    changePage(page_num){
      this.tableInfo.page_num=page_num;
      this.getTableInfo();
    },
    changePageSize(page_size){
      this.tableInfo.page_size=page_size;
      this.getTableInfo();
    },
    updateInfo(){
      //console.log(this.activeTab);
      let p;
      if(this.activeTab=='menuList'){
        p = this.getTableInfo();
      }else{
        p = this.getSearchInfo(true);
      }
      p.then(()=>{this.updateScrollbar([],true)});
    },
    getTableInfo(isSearch=false) {
      let tb = !isSearch ? this.tableInfo : this.searchInfo,param={};
      if(isSearch){
        param = {
          keyword: this.searchInfo.keyword
        }
      }
      //获取商品列表信息
      return axios
        .get("/admin/commend/list", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
            //手动设置分类
            let cts = {};
            res.data.categories.map(v => {
              cts[v.id] = v.name;
            });
            this.categoryTypeMap = cts;
            let props = ['productName','productDescription']
            res.data.tableData.forEach(v => {
              v.categoryName = cts[v.typeId];
              v.productId = v.product.id;
              v.productIcon = v.product.imgUrl;
              v.productPrice = v.product.price;
              v.productStock = v.product.stock;
              props.forEach(vl=>{v[vl]=v.product[vl];});
            });
            console.log(cts);
            if(isSearch){
              this.searchInfo = Object.assign(this.searchInfo,res.data);
            }else{
              this.tableInfo = Object.assign(this.tableInfo, res.data);
            }
          }
        });
    },
    getSearchInfo(cacheKeyword=false) {
      if(!cacheKeyword){
        this.searchInfo.keyword = this.keyword;
        this.searchInfo.productStatus = this.productStatus;
      }
      this.activeTab = 'searchResult';
      return this.getTableInfo(true);
    },
    showAddProduct(){
      this.dialog.addProductVisible=true;
      this.dialog.newProduct.productStatus = 0;
      try{
        this.dialog.newProduct.typeId = this.tableInfo.categories[0].id;
      }catch(e){
        //console.warn(e);
      }
    },
    addProduct() {
      this.editProduct(true);
    },
    //
    editProduct(isAdd=false) {
      //判断是在搜索页还是列表页
      // let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      // let index = this.dialog.editProductIndex,param;
      let param;
      let action = isAdd? '添加' : '编辑';
      if(isAdd){
        param = this.dialog.newProduct;
        this.dialog.addProductVisible = false
      }else{
        param = this.dialog.editProduct;
        this.dialog.editProductVisible = false
      }
      if(!param.productId){
        this.$alert("请选择要推荐的商品","未提交",{confirmButtonText: '确定'})
        return;
      }
      if(!param.typeId){
        this.$alert("请选择推荐分类","未提交",{confirmButtonText: '确定'})
        return;
      }
      axios.post('/admin/commend/save',param).then((res)=>{
        if(res&&res.code==1){
          this.$message({message: action+'推荐商品成功',type: 'success'});
          if(isAdd){
            //清空输入框
            for(let k in this.dialog.newProduct){
              this.dialog.newProduct[k]='';
            }
          }
          this.updateInfo();
        }else{
          this.$alert(res.tips,action+"推荐商品失败",{confirmButtonText: '确定'})
        }
      });
    },
    //
    deleteProduct(index) {
      this.$confirm('此操作将永久删除该推荐商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
        let id = table.tableData[index].id;
        axios.post('/admin/commend/delete',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除推荐商品成功',type: 'success'});
            this.updateInfo();
            this.updateScrollbar(true,[]);
          }else{
            this.$alert(res.tips,"删除推荐商品失败",{confirmButtonText: '确定'})
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    },
    showEditCategory(e,index){
      if(e.target.classList.contains('edit-category')){
        this.dialog.editCategoryIndex = index;
        this.dialog.editCategoryVisible=true;
        this.dialog.editCategory.categoryName = this.dialog.editCategory.oldName = this.tableInfo.categories[index].name;
      }
    },
    addCategory(){
      this.editCategory(true);
      this.dialog.newCategory.categoryName = '';
    },
    editCategory(isAdd=false) {
      this.dialog.editCategoryVisible = false;
      this.dialog.addCategoryVisible = false;
      let index = this.dialog.editCategoryIndex,id='';
      let categoryName = this.dialog.editCategory.categoryName;
      let action = isAdd? '添加' : '编辑';
      if(!isAdd){
        id = this.tableInfo.categories[index].id;
      }else{
        categoryName = this.dialog.newCategory.categoryName;
      }
      axios.post('/admin/commend/saveCate',{
        id,
        name: categoryName
      }).then((res)=>{
        if(res&&res.code==1){
          this.$message({message: action+'推荐分类成功',type: 'success'});
          this.updateInfo();
        }else{
          this.$alert(res.tips,action+"推荐分类失败",{confirmButtonText: '确定'})
        }
      });
    },
    selectCategory() {},
    deleteCategory(index) {
      this.$confirm('此操作将永久删除该推荐分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let id = this.tableInfo.categories[index].id;
        axios.post('/admin/commend/deleteCate',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除推荐分类成功',type: 'success'});
            this.updateInfo();
          }else{
            this.$alert(res.tips,"删除推荐分类失败",{confirmButtonText: '确定'})
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
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
.edit-category{
  cursor: pointer;
  border-radius: 50%;
  margin-left: 3px;
  padding: 2px;
  transform: scale(.8);
  &:hover{
    background: #409EFF;
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

.upload-image{
  .el-upload{
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #409EFF;
    }
  }
}
/*copy end*/
.product-manage-div {
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
  .orders-div {
    .tools-div {
      .search-input {
        width: 250px;
      }
    }
    .order-table {
      .inline-block {
        display: inline-block;
        min-width: 100%;
      }
      .category-tags {
        .category-tag {
          margin-right: 15px;
          padding: 0 12px;
        }
      }
    }
  }
}
</style>
