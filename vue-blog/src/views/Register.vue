<template>
  <div>
    <el-container>
      <el-header>
        <img class="mlog" src="@/static/logo.png" />
      </el-header>

      <el-card class="box-card">
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

          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="ruleForm.password"></el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="ruleForm.email"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">
              完成注册
            </el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Login.vue",
  data() {
    return {
      ruleForm: {
        username: "",
        password: "",
        email: "",
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
        password: [
          { required: true, message: "请输入密码", trigger: "change" },
        ],
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
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
            .post("/user/register", this.ruleForm)
            .then((res) => {
              this.$message({
                showClose: true,
                message: "注册成功,请登录",
                type: "success",
              });
              _this.$router.push("/login");
            })
            .catch((error) => {});
        } else {
          this.$message({
            showClose: true,
            message: "输入不合法，请重新输入",
            type: "error",
          });
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    guestLogin() {
      this.$router.push({ name: "index" });
    },
  },
};
</script>

<style scoped>
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #fffefe;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
.mlog {
  height: 60px;
  width: 80px;
}

.demo-ruleForm {
  max-width: 500px;
  margin: 0 auto;
}
</style>