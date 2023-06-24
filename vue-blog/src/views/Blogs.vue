<template>
  <div>
    <Header></Header>
    <div class="block">
      <el-timeline>
        <el-timeline-item
          v-bind:timestamp="blog.created"
          placement="top"
          v-for="blog in blogs"
          :key="blog"
          :label="blog"
        >
          <el-card class="blog-card">
            <h2>
              <router-link
                :to="{ name: 'BlogDetail', params: { blogId: blog.id } }"
              >
                {{ blog.title }}
              </router-link>
            </h2>
            <el-divider></el-divider>

            <div class="content-box">
              <p class="mycontent">{{ blog.content }}</p> 
            </div>
            
            <!-- <div class="markdown-body" v-html="blog.content" ></div> -->
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>

    <el-pagination
      background
      layout="prev, pager, next"
      class="mpagination"
      :current-page="currentPage"
      :page-size="pageSize"
      @current-change="getPage"
      :total="total"
    >
    </el-pagination>
  </div>
</template>
  
<script>
import Header from "@/components/Header.vue";

export default {
  name: "Blogs.vue",
  components: { Header },
  data() {
    return {
      blogs: [
        {
          id: "",
          userId: null,
          title: "",
          description: "",
          content: "",
          static: null,
        },
      ],
      currentPage: 1,
      total: 0,
      pageSize: 5,
    };
  },
  methods: {
    getPage(currentPage) {
      const _this = this;
      this.$axios
        .get("/blog/page/" + currentPage)
        .then((res) => {
          // this.blogs = res.data.records 不能这样写
          // res.data获取响应的数据,相当于获取到全部的json数据
          // res.data.data获取的是json数据中的data字段
          _this.currentPage = res.data.data.current;
          _this.total = res.data.data.total;
          _this.pageSize = res.data.data.size;

          // 使用markdown语法渲染content
          let markdown = require("markdown-it");
          let md = new markdown();
          let elements = res.data.data.records;
          var arr = [];
          for (let i = 0; i < res.data.data.records.length; ++i) {
            // 将 Markdown 转换为 HTML 以提取纯文本摘要
            var html = md.render(elements[i].content);
            const div = document.createElement("div");
            div.innerHTML = html;
            const summary = div.textContent.trim();

            arr.push({
              id: elements[i].id,
              userId: elements[i].userId,
              title: elements[i].title,
              description: elements[i].description,
              content: summary,
              created: elements[i].created,
              static: elements[i].status,
            });
          }
          _this.blogs = arr;
        })
        .catch((e) => {});
    },
  },
  created() {
    this.getPage(this.currentPage);
  },
  filters: {
    truncate(text) {
      const maxLength = 150; // 限制博客内容的长度
      if (text.length <= maxLength) {
        return text;
      } else {
        return text.slice(0, maxLength) + "..."; // 截断并添加省略号
      }
    },
  },
};
</script>

<style scoped>
.mpagination {
  max-width: 960px;
  margin: 0 auto;
  text-align: center;
}

/* 根据需要调整卡片的高度 */
.blog-card {
  max-height: 200px;
  max-width: 960px;
  padding: 5px 5px 5px 5px;
  overflow: hidden;
  text-overflow: ellipsis;
}
.mycontent{
 margin: 0;
 padding: 0;
}
.content-box{
  padding: 10px;
  height: 3em;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3; /* 显示三行，超过的部分将被省略 */
  overflow: hidden;
}
</style>