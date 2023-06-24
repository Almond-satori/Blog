<template>
  <div class="m-content">
    <h3>欢迎！</h3>
    <div class="block">
      <el-avatar :size="50" :src="user.avatar" ></el-avatar>
    </div>
    <div>{{ user.username }}</div>
    <div>
      <span v-show="user.haslogin" >
        <span ><el-link type="primary" href="/user/userInfo">用户信息</el-link></span>
        <el-divider v-show="user.haslogin" direction="vertical"></el-divider>
      </span>
      <span><el-link type="primary" href="/blogs">博客主页</el-link></span>
      <el-divider direction="vertical"></el-divider>
      <span><el-link type="success" @click="edit">发布博客</el-link></span>
      <el-divider direction="vertical"></el-divider>
      <span v-show="user.haslogin"><el-link type="danger" @click="logout">退出账号</el-link></span>
      <span v-show="!user.haslogin"><el-link type="info" href="/login">登录</el-link></span>
    </div>
  </div>
</template>

<script>

export default {
  name: "Header",
  data() {
    return {
      user: {
        username: "请先登录",
        avatar:
          "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        haslogin: false
      },
    };
  },
  methods: {
    logout() {
      const _this = this;
      _this.$axios
        .get("/user/logout", {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          _this.$store.commit("REMOVE_INFO");
          _this.$router.push("/login");
        })
        .catch((res) => {});
    },
    edit(){
      if(this.user.haslogin){
        console.log("已经登录");
        this.$router.push({ name: 'Blogadd' })
      }else{
        this.$message({
          showClose: true,
          message: '请先登录',
          type: 'warning'
        });
      }
    },
  },
  // 在页面创建的时候自动调用created
  created() {
    // 将全局的用户信息赋值到当前信息中
    if (this.$store.getters.getUser.username) {
      this.user.username = this.$store.getters.getUser.username;
     
      // 拼接资源地址
      var id = this.$store.getters.getUser.id.toString();
      var server = 'http://47.94.59.162:8080/user/';
      
      this.user.avatar = server + id + "/avatar";
      this.user.haslogin = true
    }
  },
};
</script>

<style>
.m-content {
  max-width: 960px;
  margin: 0 auto;
  text-align-last: center;
}
</style>



