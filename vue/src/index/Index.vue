<template>
  <div>
    <div class="center-box article-box">
      <div class="tools-div">
        <span class="search-span">
          <el-form :inline="true" :model="searchInfo" class="demo-form-inline">
            <el-form-item>
              <el-input
                class="search-input"
                placeholder="请输入关键字"
                prefix-icon="el-icon-search"
                @keydown.enter.native="getSearchInfo()"
                v-model="searchInfo.keyword"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button plain type="primary" @click="getSearchInfo()" icon="el-icon-search">搜索</el-button>
            </el-form-item>
          </el-form>
        </span>
      </div>
      <el-row :gutter="20">
        <el-col :span="16">
          <div class="grid-content store-list-box">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>店铺列表</span>
              </div>
              <div
                class="store-info-item"
                v-for="(store,index) in tableInfo.tableData"
                :data-index="index"
                @click="goStore(store)"
                :key="store.id"
              >
                <span class="left-image" @click.stop>
                  <el-image
                    fit="fill"
                    class="limg"
                    :src="store.logo"
                    :preview-src-list="[store.logo]"
                  ></el-image>
                </span>
                <span class="right-introduce">
                  <div class="store-name">{{store.storeName}}</div>
                  <div class="store-address">{{store.address}}</div>
                  <div class="store-meta">
                    <span class="min-cost smw">&yen;{{store.minCost}}起送</span>
                    <span class="post-fee smw">配送费&yen;{{store.postFee}}</span>
                  </div>
                  <div class="store-intro">{{store.description}}</div>
                </span>
              </div>
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
            </el-card>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="grid-content recommend-box">
            <el-card class="box-card" :body-style="{padding: '10px'}">
              <div slot="header" class="clearfix">
                <span>推荐好物</span>
              </div>
              <el-card class="recommend-card" :body-style="{padding: '10px'}" shadow="never"  v-for="(recommend) in recommends" :key="recommend.id">
                <div class="recommend-title">{{recommend.name}}</div>
                <div class="recommend-list">
                  <div class="jquery-scrollbar-element">
                    <div class="recomment-item" v-for="(reco) in recommend.list" :key="reco.id">
                      <div class="ri-title">{{reco.product.productName}}</div>
                      <div class="ri-desc" :title="reco.product.description">{{reco.product.description}}</div>
                      <div class="ri-img">
                        <el-image class="eimg" fit="fill" :src="reco.product.imgUrl" :preview-src-list="[reco.product.imgUrl]"></el-image>
                      </div>
                    </div>
                  </div>
                </div>
              </el-card>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.center-box {
  width: 1024px;
  margin: 0 auto;
}
.article-box {
  .tools-div {
    padding: 10px 0;
    margin: 10px 0;
    display: flex;
    justify-content: center;
    align-items: center;

    .search-input {
      width: 500px;
    }
  }
  .store-list-box {
    .store-info-item {
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
        }
      }
    }
  }
  .recommend-box {
    .recommend-card {
      .recommend-title {
        color: #333;
        font-weight: bold;
        font-size: 18px;
        line-height: 2;
        border-bottom: 1px solid #f5f5f5;
        margin-bottom: 10px;
      }
      .recommend-list {
        .recomment-item {
          display: inline-block;
          width: 90px;
          height: 142px;
          box-sizing: border-box;
          padding: 5px;
          margin-right: 6px;
          border-radius: 3px;
          background: #fcfcfc;
          cursor: pointer;
          .ri-title {
            color: orangered;
            font-size: 14px;
            line-height: 1.5;
            width: 100%;
            overflow: hidden;
            text-overflow: ellipsis;
          }
          .ri-desc {
            color: #444;
            font-size: 13px;
            line-height: 1.5;
            width: 100%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
          .ri-img {
            margin-top: 5px;
            width: 80px;
            height: 80px;
            .eimg {
              width: 100%;
              height: 100%;
            }
          }
        }
      }
    }
  }
}
</style>
<script>
import axios from "../common/http.js";
export default {
  data() {
    return {
      tableInfo: {
        page_num: 1,
        page_size: 8,
        total_num: 1,
        tableData: []
      },
      recommends: [],
      searchInfo: {
          keyword: ''
      }
    };
  },
  mounted() {
    this.getTableInfo();
    this.getRecommends();
  },
  methods: {
    getRecommends(){
      return axios
        .post("/open/index/getRecommend", {
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
            this.recommends = res.data;
          }
        });
    },
    goStore(store) {
      this.$router.push({
        path: '/storeDetail',
        query: {
          storeId: store.id
        }
      })
    },
    changePage(page_num) {
      this.tableInfo.page_num = page_num;
      this.getTableInfo();
    },
    changePageSize(page_size) {
      this.tableInfo.page_size = page_size;
      this.getTableInfo();
    },
    getTableInfo() {
      this.getStoreInfo();
    },
    getStoreInfo() {
      let tb = this.tableInfo;
      let param={};
      let keyword = this.searchInfo.keyword;
      if(keyword) param = {keyword};
      return axios
        .post("/open/index/storeList", {
          page_num: tb.page_num,
          page_size: tb.page_size,
          ...param
        })
        .then(res => {
          if (res && res.code == 1 && res.data) {
            this.tableInfo = res.data;
          }
        });
    },
    getSearchInfo() {
      this.getTableInfo();
    }
  }
};
</script>