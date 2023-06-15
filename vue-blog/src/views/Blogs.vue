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
          <el-card>
            <h4>
              <router-link
                :to="{ name: 'BlogDetail', params: { blogId: blog.id } }"
              >
                {{ blog.title }}
              </router-link>
            </h4>
            <p>{{ blog.content }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>

    <el-pagination 
      background layout="prev, pager, next" class="mpagination"
      :current-page=currentPage 
      :page-size=pageSize
      @current-change=getPage
      :total="total">
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
      blogs: {},
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
          console.log(res.data);
          // this.blogs = res.data.records 不能这样写
          // res.data获取响应的数据,相当于获取到全部的json数据
          // res.data.data获取的是json数据中的data字段
          _this.blogs = res.data.data.records;
          _this.currentPage = res.data.data.current;
          _this.total = res.data.data.total;
          _this.pageSize = res.data.data.size;
        })
        .catch((e) => {});
    },
  },
  created() {
    this.getPage(this.currentPage);
  },
};
</script>

<style scoped>
.mpagination{
  max-width: 960px;
  margin: 0 auto;
  text-align: center;
}
</style>