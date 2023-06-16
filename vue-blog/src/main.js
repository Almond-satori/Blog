import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './plugins/element.js'
import axios from 'axios'

import '@/axiosConfig'
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'

import githubMarkdownCss from 'github-markdown-css/github-markdown.css'

Vue.use(githubMarkdownCss)
Vue.use(mavonEditor)
Vue.config.productionTip = false
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
