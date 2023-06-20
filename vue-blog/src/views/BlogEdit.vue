<template>
  <div>
    <Header></Header>
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="100px"
      class="demo-ruleForm"
    >
      <el-form-item label="标题" prop="title">
        <el-input v-model="ruleForm.title"></el-input>
      </el-form-item>

      <el-form-item label="摘要" prop="description">
        <el-input v-model="ruleForm.description"></el-input>
      </el-form-item>

      <el-form-item label="内容" prop="content">
        <mavon-editor v-model="ruleForm.content" @imgAdd="imgAdd" @imgDel="imgDel"></mavon-editor>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('ruleForm')"
          >立即创建</el-button
        >
        <el-button @click="resetForm('ruleForm')">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
  
<script>
import Header from "@/components/Header.vue";

export default {
  name: "BlogEdit.vue",
  components: { Header },
  data() {
    return {
      ruleForm: {
        id: "",
        userId: "",
        title: "",
        description: "",
        content: "",
      },
      rules: {
        title: [
          { required: true, message: "请输入标题", trigger: "blur" },
          {
            min: 3,
            max: 50,
            message: "长度在 3 到 50 个字符",
            trigger: "blur",
          },
        ],
        description: [
          { required: true, message: "请输入描述", trigger: "change" },
          {
            min: 3,
            max: 50,
            message: "长度在 3 到 50 个字符",
            trigger: "blur",
          },
        ],
        content: [{ required: true, message: "请填写内容", trigger: "blur" }],
      },
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 如果是创建博客
          if(this.ruleForm.id === ''){
            this.$axios
              .post("/blog", this.ruleForm, {
                headers: {
                  Authorization: localStorage.getItem("token"),
                },
              })
              .then((res) => {
                this.$alert("操作成功", "提示", {
                  confirmButtonText: "确定",
                  callback: (action) => {
                    this.$router.push("/blogs");
                  },
                });
              })
              .catch((r) => {
  
              });
          }else {
            // 如果是修改博客
            this.$axios
              .put("/blog", this.ruleForm, {
                headers: {
                  Authorization: localStorage.getItem("token"),
                },
              })
              .then((res) => {
                this.$alert("操作成功", "提示", {
                  confirmButtonText: "确定",
                  callback: (action) => {
                    this.$router.push("/blogs");
                  },
                });
              })
              .catch((r) => {
  
              });
          }
          
        } else {
          console.log("");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    imgAdd(){

    },
    imgDel(){
      
    }
  },
  created() {
    this.ruleForm.userId  = this.$store.getters.getUser.id
    
    if(this.$route.params.blogId != null){
      // 如果编辑的是已经存在的页面，回显页面的信息
      this.ruleForm.id = this.$route.params.blogId;
      
      this.$axios.get('/blog/' + this.ruleForm.id).then((res) => {
        // this.ruleForm.id = res.data.data.id
        this.ruleForm.title = res.data.data.title
        this.ruleForm.description = res.data.data.description
        this.ruleForm.content = res.data.data.content
      })
    }
  }
};
</script>

<style scoped>
</style>