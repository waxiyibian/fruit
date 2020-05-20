<template>
<div class="fit-parent">
  <div class="product-manage-div jquery-scrollbar-element scrollbar-macosx">
    <div class="padding24">
    <div class="title-div">
      <h2 class="title-text">
        <span>商品管理</span>
      </h2>
    </div>
    <div class="orders-div">
      <div class="tools-div">
        <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
          <el-form-item>
            <el-select v-model="status" placeholder="请选择" size="small" style="width:100px">
              <el-option
                v-for="item in statusList"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-input
              size="small"
              class="search-input"
              placeholder="请输入商品名称/编号/描述"
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
                :key="value.categoryId"
                @click="selectCategory"
                @close="deleteCategory(index)"
                size="small"
                closable
              >{{value.categoryName}}<i class="el-icon-edit edit-category"></i></el-tag>
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
                          <el-avatar shape="square" :size="100" fit="cover" :src="props.row['imgUrl']"></el-avatar>
                        </el-form-item>
                      </el-form>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品 ID" prop="id"></el-table-column>
                  <el-table-column label="商品名称" prop="productName"></el-table-column>
                  <el-table-column label="价格" prop="price"></el-table-column>
                  <el-table-column label="库存" prop="stock"></el-table-column>
                  <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                      <el-button
                        type="primary"
                        icon="el-icon-edit"
                        circle
                        title="编辑"
                        @click="showEditProduct(scope.$index)"
                        size="small"
                      ></el-button>
                      <el-button
                        type="success"
                        icon="el-icon-upload2"
                        circle
                        title="上架"
                        v-if="scope.row.status"
                        @click="changeStatus(scope.$index,true)"
                        size="small"
                      ></el-button>
                      <el-button
                        type="warning"
                        icon="el-icon-download"
                        circle
                        title="下架"
                        v-else
                        @click="changeStatus(scope.$index,false)"
                        size="small"
                      ></el-button>
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
                          <el-avatar shape="square" :size="100" fit="cover" :src="props.row['imgUrl']"></el-avatar>
                        </el-form-item>
                      </el-form>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品 ID" prop="id"></el-table-column>
                  <el-table-column label="商品名称" prop="productName"></el-table-column>
                  <el-table-column label="价格" prop="price"></el-table-column>
                  <el-table-column label="库存" prop="stock"></el-table-column>
                  <el-table-column label="操作" width="150">
                    <template slot-scope="scope">
                      <el-button
                        type="primary"
                        icon="el-icon-edit"
                        circle
                        title="编辑"
                        @click="showEditProduct(scope.$index)"
                        size="small"
                      ></el-button>
                      <el-button
                        type="success"
                        icon="el-icon-upload2"
                        circle
                        title="上架"
                        v-if="scope.row.status"
                        @click="changeStatus(scope.$index,true)"
                        size="small"
                      ></el-button>
                      <el-button
                        type="warning"
                        icon="el-icon-download"
                        circle
                        title="下架"
                        v-else
                        @click="changeStatus(scope.$index,false)"
                        size="small"
                      ></el-button>
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
    <el-dialog title="添加商品" :visible.sync="dialog.addProductVisible" :modal-append-to-body='false'>
      <el-form :model="dialog.newProduct" status-icon :rules="dialog.rules">
        <el-form-item label="商品名称" :label-width="'120px'" prop="productName">
          <el-input v-model="dialog.newProduct.productName" size="small" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" :label-width="'120px'" prop="description">
          <el-input v-model="dialog.newProduct.description" size="small" placeholder="请输入描述" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分类" :label-width="'120px'">
          <el-select v-model="dialog.newProduct.categoryId" size="small" placeholder="请选择">
          <el-option v-for="item in tableInfo.categories" :key="item.id"
          :label="item.categoryName" :value="item.categoryId">
          </el-option>
        </el-select>
        </el-form-item>
        <el-form-item label="价格" :label-width="'120px'" prop="price">
          <el-input v-model="dialog.newProduct.price" size="small" placeholder="请输入价格" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="库存" :label-width="'120px'" prop="stock">
          <el-input v-model="dialog.newProduct.stock" size="small" placeholder="请输入库存" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图片" :label-width="'120px'" prop="imgUrl">
          <el-upload class="upload-image" :on-success="uploadIconSuccess" :action="uploadUrl" :limit="1"
          name="file" list-type="picture-card" :before-upload="beforeIconUpload" 
          :on-exceed="uploadExcced">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.addProductVisible = false">取 消</el-button>
        <el-button type="primary" @click="addProduct">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑商品" :visible.sync="dialog.editProductVisible" :modal-append-to-body='false'>
      <el-form :model="dialog.editProduct" status-icon :rules="dialog.rules">
        <el-form-item label="商品名称" :label-width="'120px'" prop="">
          <el-input v-model="dialog.editProduct.productName" size="small" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" :label-width="'120px'" >
          <el-input v-model="dialog.editProduct.description" size="small" placeholder="请输入描述" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="分类" :label-width="'120px'" >
          <el-select v-model="dialog.editProduct.categoryId" size="small" placeholder="请选择">
          <el-option v-for="item in tableInfo.categories" :key="item.id"
          :label="item.categoryName" :value="item.categoryId">
          </el-option>
        </el-select>
        </el-form-item>
        <el-form-item label="价格" :label-width="'120px'" >
          <el-input v-model="dialog.editProduct.price" size="small" placeholder="请输入价格" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="库存" :label-width="'120px'" >
          <el-input v-model="dialog.editProduct.stock" size="small" placeholder="请输入库存" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图片" :label-width="'120px'" prop="imgUrl">
          <el-upload class="upload-image"  :on-success="editIconSuccess" :action="uploadUrl" :limit="1"
          name="file" list-type="picture-card" :before-upload="beforeIconUpload" :on-exceed="uploadExcced"
          :file-list="dialog.editProduct.fileList">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.editProductVisible = false">取 消</el-button>
        <el-button type="primary" @click="editProduct()">确 定</el-button>
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
    //let host = window.location.protocol +"//"+window.location.host;
    return {
      host: window.location.protocol +"//"+window.location.host,
      uploadUrl: '/store/uploadIcon',
      dialog: {
        addCategoryVisible: false,
        addProductVisible: false,
        editCategoryVisible: false,
        editProductVisible: false,
        editProductIndex: -1,
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
          productName: "",
          price: "",
          stock: '',
          imgUrl: "",
          status: 0,
          description: "",
          categoryId: "",
        },
        rules: {
          productName: [{ validator: (rule,value,callback)=>{
            if(!value){callback(new Error("请输入商品名称"));}else{callback();}
          }, trigger: "blur" }],
          price: [{ validator: (rule,value,callback)=>{
            if(value===''){callback(new Error("请输入价格"));}
            else if(isNaN(parseFloat(value))){callback(new Error("价格输入有误，非法字符"));}
            else{callback();}
          }, trigger: "blur" }],
          stock: [{ validator: (rule,value,callback)=>{
            if(value===''){callback(new Error("请输入库存"));}
            else if(isNaN(parseInt(value))){callback(new Error("库存输入有误，非法字符"));}
            else{callback();}
          }, trigger: "blur" }],
          imgUrl: [{ validator: (rule,value,callback)=>{
            if(!value){callback(new Error("请选择商品图片"));}else{callback();}
          }, trigger: "blur" }],
          description: [{ validator: (rule,value,callback)=>{
            if(value===''){callback(new Error("请输入商品描述"));}else{callback();}
          }, trigger: "blur" }],
        },
        editProduct: {
          id: '',
          productName: "",
          price: "",
          stock: '',
          imgUrl: "",
          status: 0,
          description: "",
          categoryId: ""
        }
      },
      activeTab: "menuList",
      statusOptions: [
        { label: "正常", value: 0 },
        { label: "已下架", value: 1 }
      ],
      statusList: [
        { label: "全部", value: -1 },
        { label: "正常", value: 0 },
        { label: "已下架", value: 1 }
      ],
      categoryIdMap: {},
      searchInfo: {
        keyword: "",
        status: -1,
        page_num: 1,
        page_size: 8,
        total_num: 1,
        tableData: []
      },
      status: -1,
      keyword: "",
      tableInfo: {
        page_num: 1,
        page_size: 8,
        total_num: 1,
        categories: [
          /* {
            categoryId: 1,
            categoryName: "主食",
            categoryId: 1,
            updateTime: '',
            createTime: ''
          } */
        ],
        stringMap: {
          id: "商品ID",
          productName: "商品名称",
          categoryName: "分类",
          description: "描述",
          price: "价格",
          stock: "库存",
          //imgUrl: "图片",
          updateTime: "更新时间",
          createTime: "创建时间"
        },
        tableData: [
          /* {
            id: "12987122",
            productName: "好滋好味鸡蛋仔",
            price: "15.00",
            stock: 1000,
            imgUrl: "",
            status: 0,
            description: "荷兰优质淡奶，奶香浓而不腻",
            categoryName: "江浙小吃、小吃零食",
            categoryId: 1,
            createTime: "",
            updateTime: ""
          } */
        ]
      }
    };
  },
  mounted() {
    this.$('.jquery-scrollbar-element').scrollbar();
    this.getTableInfo();
  },
  methods: {
    updateScrollbar(row,rows){
      this.$nextTick(()=>{
        if(rows.length==0||row===true){
          this.$('.table-div').updateScrollbars(true)
        }
      });
    },
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
      if(this.activeTab=='menuList'){
        return this.getTableInfo()
      }else{
        return this.getSearchInfo(true);
      }
    },
    getTableInfo(isSearch=false) {
      let tb = !isSearch ? this.tableInfo : this.searchInfo,param={};
      if(isSearch){
        param = {
          keyword: this.searchInfo.keyword,
          status: this.searchInfo.status
        }
      }
      //获取商品列表信息
      return axios
        .get("/store/productList", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
            //手动设置分类
            let cts = {};
            res.data.categories.map((v,i) => {
              res.data.categories[i].categoryName = v.name;
              res.data.categories[i].categoryId = v.id;
              cts[v.id] = v.name;
            });
            this.categoryIdMap = cts;
            res.data.tableData.forEach(v => {
              v.categoryName = cts[v.categoryId];
            });
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
        this.searchInfo.status = this.status;
      }
      this.activeTab = 'searchResult';
      return this.getTableInfo(true);
    },
    showAddProduct(){
      this.dialog.addProductVisible=true;
      this.dialog.newProduct.status = 0;
      try{
        this.dialog.newProduct.categoryId = this.tableInfo.categories[0].categoryId;
      }catch(e){
        //console.warn(e);
      }
    },
    showEditProduct(index){
      //判断是在搜索页还是列表页
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      this.dialog.editProductIndex=index;
      this.dialog.editProductVisible=true;
      let pd = this.dialog.editProduct = JSON.parse(JSON.stringify(table.tableData[index]));
      if(pd.imgUrl)
        this.$set(this.dialog.editProduct,"fileList",[{name: pd.imgUrl.split('/').pop(),url: pd.imgUrl}])
    },
    addProduct() {
      this.editProduct(true);
    },
    //
    editProduct(isAdd=false) {
      //判断是在搜索页还是列表页
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let index = this.dialog.editProductIndex,param;
      let action = isAdd? '添加' : '编辑';
      if(isAdd){
        param = this.dialog.newProduct;
        this.dialog.addProductVisible = false
      }else{
        param = this.dialog.editProduct;
        this.dialog.editProductVisible = false
      }
      axios.post('/store/saveProduct',param).then((res)=>{
        if(res&&res.code==1){
          this.$message({message: action+'商品成功',type: 'success'});
          if(isAdd){
            this.updateInfo();
            //清空输入框
            for(let k in this.dialog.newProduct){
              this.dialog.newProduct[k]='';
            }
          }else{
            table.tableData[index] = res.data;
          }
        }else{
          this.$alert(res.tips,action+"商品失败",{confirmButtonText: '确定'})
        }
      });
    },
    //
    deleteProduct(index) {
      this.$confirm('此操作将永久删除该商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
        let id = table.tableData[index].id;
        axios.post('/store/deleteProduct',{id}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除商品成功',type: 'success'});
            this.updateInfo().then(()=>{
              this.updateScrollbar(true,[]);
            });
          }else{
            this.$alert(res.tips,"删除商品失败",{confirmButtonText: '确定'})
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
        this.dialog.editCategory.categoryName = this.dialog.editCategory.oldName = this.tableInfo.categories[index].categoryName;
      }
    },
    //
    changeStatus(index,flag) {
      let url = flag ? '/store/onSaleProduct' : '/store/offSaleProduct';
      let action = flag ? '上架' : '下架';
      //判断是在搜索页还是列表页
      let table = this.activeTab == 'menuList' ? this.tableInfo : this.searchInfo;
      let id = table.tableData[index].id;
      axios.get(url,{id}).then((res)=>{
        if(res&&res.code==1){
          this.$message({message: action+'商品成功',type: 'success'});
          let index = -1;
          table.tableData.some((v,i)=>{
            if(v.id==id){
              index =i;
              return true;
            }
          });
          let status = flag ? 0 : 1;
          if(index>=0){
            table.tableData[index].status=status;
          }
        }else{
          this.$alert(res.tips,action+"商品失败",{confirmButtonText: '确定'});
        }
      })
    },
    addCategory(){
      this.editCategory(true);
      this.dialog.newCategory.categoryName = '';
    },
    editCategory(isAdd=false) {
      this.dialog.editCategoryVisible = false;
      this.dialog.addCategoryVisible = false;
      let index = this.dialog.editCategoryIndex,categoryId='';
      let categoryName = this.dialog.editCategory.categoryName;
      let action = isAdd? '添加' : '编辑';
      if(!isAdd){
        categoryId = this.tableInfo.categories[index].categoryId;
      }else{
        categoryName = this.dialog.newCategory.categoryName;
      }
      axios.post('/store/saveCategory',{
        categoryId,
        categoryName
      }).then((res)=>{
        if(res&&res.code==1){
          this.$message({message: action+'分类成功',type: 'success'});
          if(!isAdd){
            this.tableInfo.categories[index].categoryName = categoryName;
          }else{
            res.data.categoryName = res.data.name;
            res.data.categoryId = res.data.id;
            this.tableInfo.categories.push(res.data);
          }
        }else{
          this.$alert(res.tips,action+"分类失败",{confirmButtonText: '确定'})
        }
      });
    },
    selectCategory() {},
    deleteCategory(index) {
      this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let categoryId = this.tableInfo.categories[index].categoryId;
        axios.post('/store/deleteCategory',{categoryId}).then((res)=>{
          if(res&&res.code==1){
            this.$message({message: '删除分类成功',type: 'success'});
            //删除成功则删除原数组中的元素，为防止数组翻页数据改变，此处再次循环查找，删除对应id的元素。
            let index = -1;
            this.tableInfo.categories.some((v,i)=>{
              if(v.categoryId==categoryId){
                index = i;
                return true;
              }
            });
            if(index>=0) this.tableInfo.categories.splice(index,1);
          }else{
            this.$alert(res.tips,"删除分类失败",{confirmButtonText: '确定'})
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });          
      });
    },
    uploadIconSuccess(res){
      //this.newProduct.imgUrl = 
      if(res&&res.code==1){
        this.dialog.newProduct.imgUrl = res.data;
      }
    },
    editIconSuccess(res){
      if(res&&res.code==1){
        this.dialog.editProduct.imgUrl = res.data;
      }
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
