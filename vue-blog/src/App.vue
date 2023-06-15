<template>
  <div id="app" class="global">
    <router-view/>
  </div>
</template>

<script>

export default {
  name: 'app',
  components: {
  }
}
</script>

<style>
  .global{
    max-width: 960px;
    margin: 0 auto;
  }
</style>
