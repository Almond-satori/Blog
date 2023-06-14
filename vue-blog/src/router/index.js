import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '../views/Login.vue'
import Blogs from '../views/Blogs.vue'
import BlogDetail from '../views/BlogDetail.vue'
import BlogEdit from '../views/BlogEdit.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'index',
    redirect: {name: "Blogs"}
  },
  {
    path: '/blogs',
    name: 'Blogs',
    component: Blogs
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  // :blogId参数传入
  // 路由配置BlogDetail先于BlogEdit，为了匹配
  {
    path: '/Blog/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/Blog/:blogId/edit',
    name: 'BlogEdit',
    component: BlogEdit
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
