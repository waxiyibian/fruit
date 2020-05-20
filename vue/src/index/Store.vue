<template>
  <div>
    <div class="center-box">
      <div class="top-box">
        <div class="back-box">
          <!-- 加上v-for以便在store.logo异步返回值之后再初始化组件 -->
          <el-image v-for="(store) in storeInfo" :key="store.id"
            fit="fit"
            :src="store.logo"
            :preview-src-list="[store.logo]"
            class="store-image"
          ></el-image>
        </div>
        <div class="intro-div">
          <div class="title-line">{{store.storeName}}</div>
          <div class="store-address">地址：{{store.address}}</div>
          <div class="store-meta">
            <span class="min-cost smw">&yen;{{store.minCost}}起送</span>
            <span class="post-fee smw">配送费&yen;{{store.postFee}}</span>
          </div>
          <div class="store-intro" :title="store.description">简介：{{store.description}}</div>
        </div>
      </div>
      <div class="article-box">
        <el-tabs tab-position="left" v-model="currentCategory">
          <el-tab-pane v-for="(products,index) in productList" :key="products.id" :label="products.name">
            <div
              class="product-info-item"
              v-for="(product,ind) in products.list"
              :key="product.id"
            >
              <span class="left-image" @click.stop>
                <el-image
                  fit="fill"
                  class="limg"
                  :src="product.imgUrl"
                  :preview-src-list="[product.imgUrl]"
                ></el-image>
              </span>
              <span class="right-introduce">
                <div class="store-name">{{product.productName}}</div>
                <div class="store-meta">
                  <span class="min-cost smw">库存{{product.stock}} </span>
                  <span class="post-fee smw red-word">&yen;{{product.price}}</span>
                </div>
                <div class="store-intro" :title="product.description">{{product.description}}</div>
                <div class="tools-div">
                  <el-button type="warning" @click="setCart(product.id,-1)" icon="el-icon-minus" size="mini" circle></el-button>
                  <span class="buy-num" style="margin: 0 10px">{{cartCounts[index][ind]}}</span>
                  <el-button type="primary" @click="setCart(product.id,+1,product.stock)" icon="el-icon-plus" size="mini" circle></el-button>
                </div>
              </span>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.product-info-item {
  width: 100%;
  box-sizing: border-box;
  padding: 10px 20px;
  display: flex;
  justify-content: stretch;
  align-items: flex-start;
  padding-bottom: 20px;
  margin-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  .left-image {
    flex-grow: 0;
    flex-shrink: 0;
    width: 120px;
    height: 120px;
    margin-right: 20px;
    cursor: pointer;

    .limg {
      width: 100%;
      height: 100%;
    }
  }
  .right-introduce {
    flex-grow: 1;
    flex-shrink: 1;
    .store-name {
      color: #333;
      font-weight: bold;
      font-size: 18px;
      line-height: 2;
    }
    .store-address {
      color: #999;
      font-size: 12px;
      line-height: 2;
    }
    .store-meta {
      font-size: 12px;
      color: #999;
      line-height: 2;
      .red-word{
        color: orangered;
        font-size: 14px;
      }
      .smw {
      }
      .min-cost {
        margin-right: 15px;
      }
      .post-fee {
      }
    }
    .store-intro {
      font-size: 12px;
      color: #999;
      line-height: 1.5;
      width: 100%;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}
.center-box {
  width: 1024px;
  position: relative;
  margin: 0 auto;
}
.top-box {
  padding-bottom: 10px;
  border-bottom: 1px solid #f5f5f5;
  .back-box {
    background: rgb(236, 245, 255);
    height: 150px;
    position: relative;
    background-clip: content-box;
    padding-bottom: 40px;
    .store-image {
      width: 150px;
      height: 150px;
      border-radius: 3px;
      margin: 0 auto;
      position: relative;
      bottom: -30px;
      display: block;
    }
  }
  .intro-div {
      text-align: center;
    .title-line {
      color: #333;
      font-weight: bold;
      font-size: 18px;
      line-height: 2;
    }
    .store-address {
      color: #999;
      font-size: 12px;
      line-height: 2;
    }
    .store-meta {
      font-size: 12px;
      color: #999;
      line-height: 2;
      .smw {
      }
      .min-cost {
        margin-right: 15px;
      }
      .post-fee {
      }
    }
    .store-intro {
      font-size: 12px;
      color: #999;
      line-height: 1.5;
      width: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}
</style>
<script>
import axios from '../common/http.js';
export default {
  props: {
    cart: {
      type: Array,
      default: ()=>[]
    }
  },
  data() {
    return {
      currentCategory: "",
      storeId: '',
      productList: [],
      store: {
        id: '',
        logo: '',
        storeName: '',
        address: '',
        minCost: '',
        postFee: '',
        description: ''
      },
      /* cart: [
        {
          productId: '',
          count: ''
        }
      ] */
    };
  },
  mounted(){
    this.storeId = this.$route.query.storeId;
    this.getStoreInfo();
    this.getProductList();
  },
  computed:{
    storeInfo(){
      return this.store.id?[this.store]:[];
    },
    cartCounts(){
      //arr的预期结果是二维数组，如[[0,0],[0,1]]。那个1代表第二个分类中的第二个商品在购物车中的数量为1
      let map = {};
      this.cart.forEach(v => {
        map[v.productId] = v.count;
      });
      let arr = this.productList.map((products)=>{
        //这一层的预期结果是返回一个数组如[0,0]，即单个分类所有商品对应的购物车中数量，购物车中不存在商品则对应为0
        return products.list.map(proudct=>{
          //这里判断当前商品在购车中数量，不存在则返回0
          return map[proudct.id] || 0;
        })
      });
      return arr;
    }
  },
  methods: {
    setCart(productId,count=0,stock = false){
      let validCount = count =>{
        if(stock) count = count > stock ? stock : count;
        count = count < 0 ? 0 : count;
        return count;
      };
      let cart = this.cart,oldCount = -1,newCount=-2;
      var i;
      for(i=0;i<cart.length;i++){
        let ct = cart[i];
        if(ct.productId== productId){
          oldCount = ct.count;
          newCount = ct.count = validCount(ct.count - -count);
          break;
        }
      }
      if(i>=cart.length){//cart中
        cart.push({
          productId,
          count: validCount(count)
        });
      }
      if(newCount==0){
        cart.splice(i,1);
      }
      if(oldCount!=newCount) this.$emit("cart-change",this.storeId,cart);
    },
    getStoreInfo(){
      return axios.post("/open/index/getStoreInfo",{
        storeId: this.storeId
      }).then(res=>{
        if(res&&res.code==1&&res.data){
          this.store = res.data;
        }
      })
    },
    getProductList(){
      return axios.post('/open/index/getProductList',{
        storeId: this.storeId
      }).then(res=>{
        if (res && res.code == 1 && res.data) {
            this.productList = res.data;
          }
      });
    }
  }
};
</script>