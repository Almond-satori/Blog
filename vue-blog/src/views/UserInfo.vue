<template>
  <div>
    <h4>上传头像</h4>
    <!-- <p>{{ user.userId }}</p> -->
    <MyUploadAvatar></MyUploadAvatar>
  </div>
</template> 

<script>
import MyUploadAvatar from '@/components/MyUploadAvatar.vue';

export default {
  components: { MyUploadAvatar },
  data() {
    return {
      user: {
        userId: "?",
      },
    };
  },
  methods: {},
  created() {
    if (this.$store.getters.getUser.username) {
      this.user.userId = this.$store.getters.getUser.id;
    } else {
      this.$router.push("/login");
    }
  },
};
</script>