<template>
  <div>
    <Header></Header>
    <div>
      <h2>{{ blog.title }}</h2>

        <el-link type="primary" v-show="showEdit">
            <router-link :to="{name:'BlogEdit',params:{blogId:blog.id}}">编辑</router-link>
        </el-link>
      <el-divider></el-divider>

      <el-card class="box-card">  
        <div class="markdown-body" v-html="blog.content"></div>
      </el-card>
    </div>
  </div>
</template>
  
<script>
import { Header } from "element-ui";

export default {
  name: "BlogDetail.vue",
  components: { Header },
  data() {
    return {
      blog: {
        id: '',
        userId: null,
        title: '',
        description: '',
        content: ""
      },
      showEdit: false
    };
  },
  created() {
    this.$axios.get("/blog/" + this.$route.params.blogId).then((res) => {
      this.blog.id = res.data.data.id;
      this.blog.userId = res.data.data.userId;
      this.blog.title = res.data.data.title;
      this.blog.description = res.data.data.description;

      // 使用markdown语法渲染content
      var markdown = require("markdown-it")
      var md = new markdown()
      this.blog.content = md.render(res.data.data.content);
      // 是否展示编辑按钮
      var userInfo = this.$store.getters.getUser;
      if(userInfo != null && this.blog.userId === userInfo.id){
        this.showEdit = true
      }
    });
  },
};
</script>

<style scoped>

.markdown-body {
    box-sizing: border-box;
    min-width: 200px;
    max-width: 980px;
    margin: 0 auto;
    padding: 30px 15px;
}
</style>