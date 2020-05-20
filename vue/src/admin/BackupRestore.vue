<template>
  <div class="fit-parent">
    <div class="person-profile-div jquery-scrollbar-element scrollbar-macosx">
      <div class="padding24">
        <div class="title-div">
          <h2 class="title-text">
            <span>备份还原</span>
          </h2>
        </div>
        <div class="tab-div">
          <el-tabs type="border-card" v-model="activeTab">
            <el-tab-pane label="备份" name="backup">
              <el-button icon="el-icon-download" type="primary" @click="downloadAction">导出备份</el-button>
              <a href="/admin/backup" ref="backup" target="_blank" class="unvisible"></a>
            </el-tab-pane>
            <el-tab-pane label="还原" name="restore">
              <el-form :label-position="'right'" ref="userForm">
                <el-form-item class="first-line" label="选择SQL文件">
                  <el-upload
                    class="upload-demo"
                    :auto-upload="false"
                    ref="upload"
                    :action="uploadURL"
                    :on-success="uploadSuccess"
                    :file-list="fileList"
                  >
                    <el-button icon="el-icon-upload" size="small" type="primary">点击上传</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传sql文件</div>
                  </el-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="warning" size="small" @click="restoreAction">确认还原</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
//import axios from "../common/http.js";
export default {
  data() {
    return {
      activeTab: "backup",
      uploadURL: "/admin/uploadAndrestore",
      fileList: []
    };
  },
  mounted() {
    this.$(".jquery-scrollbar-element").scrollbar();
    //console.log(this.$.prototype.scrollbar);
  },
  methods: {
    uploadSuccess(res) {
      let action = '执行恢复';
      this.fileList=[];
      if (res && res.code == 1) {
        this.$message({ message: action+"成功", type: "success" });
      } else {
        this.$alert(res.tips, action+"失败", { confirmButtonText: "确定" });
      }
    },
    restoreAction() {
      this.$refs.upload.submit();
    },
    downloadAction() {
      this.$refs.backup.click();
    }
  }
};
</script>

<style scoped lang="scss">
.unvisible {
  opacity: 0;
  position: fixed;
  top: -999px;
  z-index: -1;
}
.closer-form-item {
  margin: 0;
}
.first-line {
  margin-top: 24px;
}
.fit-parent {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
}
.padding24 {
  padding: 24px !important;
}
.person-profile-div {
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  .tab-div {
    border: 1px solid #f5f5f5;
    border-radius: 5px;
  }
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
}
</style>
