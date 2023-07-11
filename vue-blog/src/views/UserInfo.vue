<template>
  <div>
    <h4>修改用户信息</h4>
    <el-main>
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="username">
          <el-input v-model="ruleForm.username"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">
            修改信息
          </el-button>

        </el-form-item>
      </el-form>
    </el-main>
    
    <h4>上传头像</h4>
    <MyUploadAvatar></MyUploadAvatar>
  </div>
</template> 

<script>
import MyUploadAvatar from "@/components/MyUploadAvatar.vue";

export default {
  name: "UserInfo.vue",
  components: { MyUploadAvatar },
  data() {
    return {
      user: {
        userId: "?",
      },
      ruleForm: {
        id: "?",
        username: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          {
            min: 3,
            max: 15,
            message: "长度在 3 到 15 个字符",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this; // 获取全局this对象
          this.$axios
            .put("/user", this.ruleForm)
            .then((res) => {

              _this.$store.commit("SET_USERINFO", res.data.data);

              _this.$router.push("/blogs");
            })
            .catch((error) => {
              console.log(error);
            });
        } else {
          this.$message({
            showClose: true,
            message: "输入不合法，请重新输入",
            type: "error",
          });
          return false;
        }
      });
    },
  },
  created() {
    if (this.$store.getters.getUser.username) {
      this.user.userId = this.$store.getters.getUser.id;
      this.ruleForm.id = this.user.userId;
      this.ruleForm.username = this.$store.getters.getUser.username;
      
    } else {
      this.$router.push("/login");
    }
  },
};
</script>