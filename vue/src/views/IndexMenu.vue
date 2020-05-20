<template>
  <div>
    <div class="fix-top-bar">
      <div class="center-box">
        <div class="right-part-list">
          <span class="user-info" v-if="userInfo.username">
            <el-dropdown class="cbutton">
              <span class="person-center-button">我的</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-divider content-position="center">{{
                    userInfo.nickname
                  }}</el-divider>
                </el-dropdown-item>
                <el-dropdown-item icon="el-icon-check"
                  ><router-link
                    to="/personCenter"
                    style="color:#666;text-decoration:none;"
                    >个人中心</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  icon="el-icon-circle-check"
                  @click.native="logout"
                  >退出</el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </span>
          <span class="cbutton login-button" v-else @click="showLogin"
            >登录</span
          >
          <span class="cbutton"
            ><router-link style="text-decoration:none;color:#333;" to="/"
              >首页</router-link
            ></span
          >
          <span class="cbutton">
            <el-dropdown class="cbutton">
              <span class="person-center-button">后台登录</span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  ><router-link to="/store/login" class="alink"
                    >商家登录</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><router-link to="/admin/login" class="alink"
                    >管理员登录</router-link
                  ></el-dropdown-item
                >
                <el-dropdown-item
                  ><router-link to="/delivery/login" class="alink"
                    >配送员登录</router-link
                  ></el-dropdown-item
                >
              </el-dropdown-menu>
            </el-dropdown>
          </span>
          <span
            class="cbutton store-apply-button"
            @click="dialog.storeRequestVisible = true"
            >商家入驻</span
          >
          <el-badge :value="cartSum" class="item">
            <span
              class="cbutton shopping-cart-button"
              @click="shoppingCartVisible = true"
              >购物车</span
            >
          </el-badge>
        </div>
      </div>
    </div>
    <div class="top-bar-offset"></div>
    <router-view @cart-change="cartChangeAction" :cart="cartList" />
    <el-dialog
      width="400px"
      :visible.sync="dialog.loginDialogVisible"
      :modal-append-to-body="false"
    >
      <el-tabs v-model="loginDialogTab" type="card">
        <el-tab-pane label="登录" name="login">
          <el-form :model="dialog.user" :rules="dialog.rules" ref="loginForm">
            <el-form-item prop="username">
              <el-input
                prefix-icon="el-icon-user"
                v-model="dialog.user.username"
                placeholder="请输入注册的手机号码或邮箱"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                prefix-icon="el-icon-key"
                v-model="dialog.user.password"
                show-password
                placeholder="请输入密码"
                autocomplete="off"
                @keydown.enter.native="loginAction"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                @click="loginAction"
                class="login-action-button"
                >立即登录</el-button
              >
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="dialog.reg" :rules="dialog.rules" ref="regForm">
            <el-form-item prop="username" label="邮箱" label-width="80px">
              <el-input
                prefix-icon="el-icon-user"
                v-model="dialog.reg.username"
                placeholder="请输入邮箱"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item prop="nickname" label="昵称" label-width="80px">
              <el-input
                prefix-icon="el-icon-user"
                v-model="dialog.reg.nickname"
                placeholder="请输入昵称"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item prop="code" label="验证码" label-width="80px">
              <el-input
                v-model="dialog.reg.code"
                placeholder="请输入验证码"
                autocomplete="off"
              ></el-input>
              <el-button
                plain
                :disabled="dialog.cooling > 0"
                class="get-code-button"
              >
                <span v-if="dialog.cooling <= 0" @click="getCode"
                  >获取验证码</span
                >
                <span v-else>{{ dialog.cooling }}s后重试</span>
              </el-button>
            </el-form-item>
            <el-form-item prop="password" label="密码" label-width="80px">
              <el-input
                prefix-icon="el-icon-key"
                v-model="dialog.reg.password"
                show-password
                placeholder="请输入密码"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item prop="phone" label="手机" label-width="80px">
              <el-input
                v-model="dialog.reg.phone"
                placeholder="请输入手机号码"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                @click="registerAction"
                class="login-action-button"
                >立即注册</el-button
              >
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
    <!-- 商家入驻 -->
    <el-dialog
      title="申请商家入驻"
      :visible.sync="dialog.storeRequestVisible"
      :modal-append-to-body="false"
    >
      <el-form
        :model="dialog.newStore"
        :rules="dialog.requestRules"
        ref="storeRequestForm"
      >
        <el-form-item label="您的姓名" :label-width="'120px'" prop="name">
          <el-input
            v-model="dialog.newStore.name"
            placeholder="请输入您的姓名"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="店铺名称" :label-width="'120px'" prop="storeName">
          <el-input
            v-model="dialog.newStore.storeName"
            placeholder="请输入店铺名称"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="店铺地址" :label-width="'120px'" prop="address">
          <el-input
            v-model="dialog.newStore.address"
            placeholder="请输入店铺地址"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="联系方式" :label-width="'120px'" prop="phone">
          <el-input
            v-model="dialog.newStore.phone"
            placeholder="请输入联系方式"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="店铺描述"
          :label-width="'120px'"
          prop="description"
        >
          <el-input
            v-model="dialog.newStore.description"
            placeholder="请输入店铺描述"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog.storeRequestVisible = false">取 消</el-button>
        <el-button type="primary" @click="storeRequest()">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 购物车抽屉 -->
    <el-drawer
      title="购物车!"
      :visible.sync="shoppingCartVisible"
      direction="rtl"
      size="50%" class="shopping-car"
    >
      <el-card v-if="!storeCart || JSON.stringify(storeCart) == '{}'">
        <div>购物车是空的，去逛逛吧~</div>
      </el-card>
      <el-card
        class="box-card"
        v-for="(cart, storeId) in storeCart"
        :key="storeId"
      >
        <div slot="header" class="cart-store">
          <!-- <el-radio style="margin-right: 10px"></el-radio> -->
          <el-image
            fit="fit"
            v-if="storeMap[storeId]"
            :src="storeMap[storeId].logo"
            style="width:30px;height:30px"
          ></el-image>
          <el-image fit="fit" v-else></el-image>
          <span class="store-name" v-if="storeMap[storeId]">{{
            storeMap[storeId].storeName
          }}</span>
        </div>
        <div
          class="product-info-item"
          v-for="productItem in cart"
          :key="productItem.productId"
        >
          <template v-if="productMap[productItem.productId]">
            <span class="left-image" @click.stop>
              <el-image
                fit="fill"
                class="limg"
                :src="productMap[productItem.productId].imgUrl"
                :preview-src-list="[productMap[productItem.productId].imgUrl]"
              ></el-image>
            </span>
            <span class="right-introduce">
              <div class="store-name">
                {{ productMap[productItem.productId].productName }}
              </div>
              <div class="store-meta">
                <span class="min-cost smw"
                  >库存{{ productMap[productItem.productId].stock }}</span
                >
                <span class="post-fee smw red-word"
                  >&yen;{{ productMap[productItem.productId].price }}</span
                >
              </div>
              <div
                class="store-intro"
                :title="productMap[productItem.productId].description"
              >
                {{ productMap[productItem.productId].description }}
              </div>
              <div class="tools-div">
                <el-button
                  type="warning"
                  @click="setCart(storeId, productItem.productId, -1)"
                  icon="el-icon-minus"
                  size="mini"
                  circle
                ></el-button>
                <span class="buy-num" style="margin: 0 10px">{{
                  productItem.count
                }}</span>
                <el-button
                  type="primary"
                  @click="
                    setCart(
                      storeId,
                      productItem.productId,
                      +1,
                      productMap[productItem.productId].stock
                    )
                  "
                  icon="el-icon-plus"
                  size="mini"
                  circle
                ></el-button>
              </div>
            </span>
          </template>
        </div>
        <div>
          共{{ OneStoreCount[storeId].totalNum }}件商品,总计&yen;{{
            OneStoreCount[storeId].totalAmount
          }}
        </div>
      </el-card>
      <div
        class="confirm-button"
        v-if="storeCart && JSON.stringify(storeCart) != '{}'"
      >
        <el-button
          type="primary"
          class="confirm-btn"
          @click="addressDrawerVisible = true"
          >立即结算</el-button
        >
        <el-button type="warning" class="cancel-btn" @click="clearCart"
          >清空</el-button
        >
      </div>
      <address-drawer
        :visible="addressDrawerVisible"
        @select="submitOrder"
        @change="addressDrawerChange"
      ></address-drawer>
    </el-drawer>
    <div class="footer-div">
      <div class="text-center">&copy;Copyright 2020</div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.shopping-car ::v-deep .el-drawer__body{
  overflow: auto;
}
.alink {
  color: #333;
  text-decoration: none;
}
.cart-store {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  cursor: pointer;
}
.confirm-button {
  padding-top: 10px;
  display: flex;
  justify-content: center;
}
.confirm-btn {
  display: block;
}
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
      .red-word {
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
  margin: 0 auto;
}
.border-div {
  box-sizing: border-box;
  border: 1px solid #f5f5f5;
}
/*设置一个和fix-top-bar一样高的元素*/
.top-bar-offset {
  height: 35px;
}
.fix-top-bar {
  position: fixed;
  height: 35px;
  width: 100%;
  z-index: 1000;
  background: #f5f5f5;
  .center-box {
    height: 100%;
  }
  .right-part-list {
    float: right;
    display: inline-flex;
    height: 100%;
    justify-content: flex-end;
    align-items: center;
  }
  .user-info {
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .cbutton {
    padding: 5px 10px;
    color: #6c6c6c;
    margin-left: 5px;
    font-size: 14px;
    cursor: pointer;

    &:hover {
      text-decoration: underline;
    }
  }
}
.get-code-button {
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
  position: absolute;
  right: 0;
  top: 0;
}
.login-action-button {
  position: relative;
  width: 100%;
}
.footer-div {
  background: #f5f5f5;
  .text-center {
    text-align: center;
    line-height: 2;
    color: #444;
    font-size: 14px;
  }
}
</style>
<script>
import axios from "../common/http.js";
import $ from "jquery";
import AddressDrawer from "../index/AdressDrawer.vue";
export default {
  components: {
    AddressDrawer,
  },
  data() {
    let requestRules = {};
    ["name", "storeName", "phone", "address", "description"].forEach((v) => {
      requestRules[v] = [{
        validator: (rule, value, callback) => {
          if (!value) {
            callback(new Error("不能为空"));
          } else {
            callback();
          }
        },
      }];
    });
    return {
      shoppingCartVisible: false,
      addressDrawerVisible: false,
      userInfo: {
        username: "",
        nickname: "",
      },
      loginDialogTab: "login",
      timer: null,
      dialog: {
        cooling: 0,
        storeRequestVisible: false,
        loginDialogVisible: false,
        requestRules,
        user: {
          username: "",
          password: "",
        },
        newStore: {
          name: "",
          storeName: "",
          address: "",
          phone: "",
          description: "",
        },
        reg: {
          username: "",
          password: "",
          code: "",
          nickname: "",
          phone: "",
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
            },
          ],
          username: [
            {
              validator: (rule, value, callback) => {
                if (!value) {
                  callback(new Error("请输入邮箱"));
                } else {
                  callback();
                }
              },
              trigger: "blur",
            },
          ],
          phone: [
            {
              validator: (rule, value, callback) => {
                if (!value) {
                  callback(new Error("请输入手机号码"));
                } else {
                  callback();
                }
              },
              trigger: "blur",
            },
          ],
          password: [
            {
              validator: (rule, value, callback) => {
                if (!value) {
                  callback(new Error("请输入密码"));
                } else {
                  callback();
                }
              },
              trigger: "blur",
            },
          ],
        },
      },
      cartData: [],
      storeCart: {},
      productMap: {},
      storeAccountMap: {},
      storeMap: {},
      cartSum: "",
    };
  },
  watch: {
    storeCart: {
      handler: function(val) {
        let sum = 0;
        let sc = val;
        for (let k in sc) {
          for (let i = 0; i < sc[k].length; i++) {
            sum -= -sc[k][i].count;
          }
        }
        if (sum <= 0 || isNaN(sum)) sum = "";
        this.cartSum = sum;
      },
      deep: true,
    },
  },
  computed: {
    cartList() {
      let storeId = this.$route.query.storeId;
      return storeId ? this.storeCart[storeId] : [];
    },
    OneStoreCount() {
      let obj = {};
      let car = this.storeCart;
      let map = this.productMap;
      for (let si in car) {
        let totalNum = 0,
          totalAmount = 0;
        car[si].forEach((value) => {
          totalNum += value.count;
        });
        car[si].forEach((value) => {
          if (map[value.productId])
            totalAmount =
              map[value.productId].price * value.count + totalAmount;
        });
        obj[si] = {
          totalNum,
          totalAmount,
        };
      }
      // eslint-disable-next-line vue/no-side-effects-in-computed-properties
      this.storeAccountMap = obj;
      return obj;
    },
  },
  beforeRouteLeave(to, from, next) {
    $(window).off("show-login");
    next();
  },
  mounted() {
    let showLogin = () => {
      $(window).on("show-login", () => {
        this.dialog.loginDialogVisible = true;
        this.loginDialogTab = "login";
      });
    };
    this.getUserInfo()
      .then(showLogin)
      .catch(showLogin);
    this.storeCart =
      JSON.parse(window.localStorage.getItem("cart") || null) || {};
    for (let k in this.storeCart) this.cartChangeAction(k, this.storeCart[k]);
  },
  methods: {
    storeRequest() {
      this.dialog.storeRequestVisible = false;
      let act = this.dialog.newStore;
      let action = "申请商家入驻信息提交";
      let ref = this.$refs["storeRequestForm"];
      let store = { ...act };
      ref.validate((valid) => {
        if (valid) {
          axios.post("/user/storeRequest", store).then((res) => {
            if (res && res.code == 1) {
              this.$message({ message: action + "成功", type: "success" });
              ref.resetFields();
            } else {
              this.$alert(res.tips, action + "失败", {
                confirmButtonText: "确定",
              });
            }
          });
        } else {
          this.$alert("信息填写有误", "未提交", {
            confirmButtonText: "确定",
          });
          return false;
        }
      });
    },
    addressDrawerChange(visible) {
      this.addressDrawerVisible = visible;
    },
    submitOrder(addr) {
      if (!addr || !addr.name || !addr.address || !addr.phone) {
        this.$alert("未选择收货地址", "未提交", { confirmButtonText: "确定" });
        return;
      }
      let car = this.storeCart;
      let map = this.productMap;
      let user = this.userInfo;
      let orders = [];
      for (let storeId in car) {
        let orderDetailList = [];
        car[storeId].forEach((v) => {
          let id = v.productId;
          orderDetailList.push({
            productId: id,
            productName: map[id].productName,
            productPrice: map[id].price,
            productQuantity: v.count,
            productIcon: map[id].imgUrl,
          });
        });
        let order = {
          storeId,
          buyerName: addr.name,
          buyerPhone: addr.phone,
          buyerId: user.id,
          buyerAddress: addr.address,
          orderAmount: this.storeAccountMap[storeId].totalAmount,
          orderStatus: 0,
          orderDetailList,
        };
        orders.push(order);
      }
      axios
        .post("/user/submitOrder", JSON.stringify(orders), {
          headers: { "Content-Type": "application/json;charset=utf-8" },
        })
        .then((res) => {
          if (res && res.code == 1) {
            this.$message({ type: "success", message: "提交订单成功" });
            this.shoppingCartVisible = false;
            this.addressDrawerVisible = false;
            this.clearCart();
          } else {
            this.$alert(res.tips || "错误", "提交订单失败", {
              confirmButtonText: "确定",
            });
          }
        });
    },
    getCode() {
      let email = this.dialog.reg.username;
      // eslint-disable-next-line no-useless-escape
      let reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
      if (!reg.test(email)) {
        this.$message({
          type: "warning",
          message: "请输入正确的邮箱地址",
        });
        return;
      }
      // 倒计时
      this.dialog.cooling = 60;
      clearInterval(this.timer);
      this.timer = setInterval(() => {
        this.dialog.cooling--;
        if (this.dialog.cooling <= 0) clearInterval(this.timer);
      }, 1000);
      axios
        .get("/open/getCode", {
          email,
        })
        .then((res) => {
          // eslint-disable-next-line eqeqeq
          if (res && res.code == 1) {
            // 成功
            this.$message({
              type: "success",
              message: "发送邮箱验证码成功，请注意查收",
            });
          } else {
            // 清空倒计时
            this.dialog.cooling = 0;
            this.$message({
              type: "warning",
              message: "获取验证码失败，请重试",
            });
          }
        });
    },
    logout() {
      axios.get("/logout").then(() => {
        this.$router.go(0);
      });
    },
    clearCart() {
      this.storeCart = {};
      window.localStorage.removeItem("cart");
    },
    //购物车商品数量变化
    setCart(storeId, productId, count = 0, stock = false) {
      let validCount = (count) => {
        if (stock) count = count > stock ? stock : count;
        count = count < 0 ? 0 : count;
        return count;
      };
      let cart = this.storeCart[storeId],
        oldCount = -1,
        newCount = -2;
      var i;
      for (i = 0; i < cart.length; i++) {
        let ct = cart[i];
        if (ct.productId == productId) {
          oldCount = ct.count;
          newCount = ct.count = validCount(ct.count - -count);
          break;
        }
      }
      if (i >= cart.length) {
        //cart中
        cart.push({
          productId,
          count: validCount(count),
        });
      }
      if (newCount == 0) {
        cart.splice(i, 1);
      }
      if (oldCount != newCount) this.cartChangeAction(storeId, cart);
    },
    //购物车内容发生变化
    cartChangeAction(storeId, cart) {
      //cart = [{productId,count},...];
      this.$set(this.storeCart, storeId, cart);
      if (!cart || cart.length <= 0) {
        delete this.storeCart[storeId];
      }
      window.localStorage.setItem("cart", JSON.stringify(this.storeCart));
      //console.log(this.storeCart);
      //let arr=[];
      //获取店铺详细信息和商品详细信息
      if (!this.storeMap[storeId]) {
        axios.post("/open/index/getStoreInfo", { storeId }).then((res) => {
          if (res && res.code == 1 && res.data) {
            this.storeMap[storeId] = res.data;
          }
        });
      }
      axios
        .post("/open/index/getProductInfo", {
          products: cart
            .map((v) => v.productId)
            .filter((v) => {
              return !this.productMap[v];
            }),
        })
        .then((res) => {
          if (res && res.code == 1) {
            this.productMap = { ...this.productMap, ...res.data };
          }
        });
    },
    showLogin() {
      this.dialog.loginDialogVisible = true;
    },
    getUserInfo(id = null) {
      if (id == null || id == this.userInfo.id) {
        //获取用户信息
        return axios.get("/open/loginStatus").then((res) => {
          if (res && res.code == 1) {
            this.userInfo = res.data;
          }
        });
      }
      return Promise.reject();
    },
    loginAction() {
      let form = this.$refs["loginForm"];
      form.validate((valid) => {
        if (valid) {
          axios.post("/login", this.dialog.user).then((res) => {
            if (res && res.code == 1) {
              this.dialog.loginDialogVisible = false;
              this.$message({
                message: "登录成功",
                type: "success",
                showClose: true,
                onClose: () => {
                  //获取用户信息
                  this.getUserInfo();
                },
              });
            } else {
              this.$alert(res.tips, "登录失败", {
                confirmButtonText: "确定",
              });
            }
          });
        } else {
          this.$alert("信息填写有误", "未提交", {
            confirmButtonText: "确定",
          });
          return false;
        }
      });
    },
    registerAction() {
      let form = this.$refs["regForm"];
      form.validate((valid) => {
        if (valid) {
          axios.post("/open/register", this.dialog.reg).then((res) => {
            if (res && res.code == 1) {
              this.dialog.loginDialogVisible = false;
              this.$message({
                message: "注册成功",
                type: "success",
                showClose: true,
              });
              this.dialog.reg = {
                code: "",
                username: "",
                password: "",
                nickname: "",
              };
            } else if (res.tips) {
              this.$alert(res.tips, "注册失败", {
                confirmButtonText: "确定",
              });
            }
          });
        } else {
          this.$alert("信息填写有误", "未提交", {
            confirmButtonText: "确定",
          });
          return false;
        }
      });
    },
  },
};
</script>
