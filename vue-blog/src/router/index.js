import Vue from 'vue'
import VueRouter from 'vue-router'

import Login from '../views/Login.vue'
import Blogs from '../views/Blogs.vue'
import BlogDetail from '../views/BlogDetail.vue'
import BlogEdit from '../views/BlogEdit.vue'
import Register from '../views/Register.vue'

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
  {
    path: '/blog/edit',
    name: 'Blogadd',
    component: BlogEdit
  },
  // :blogId参数传入
  // 路由配置BlogDetail先于BlogEdit，为了匹配
  {
    path: '/blog/:blogId',
    name: 'BlogDetail',
    component: BlogDetail
  },
  {
    path: '/blog/:blogId/edit',
    name: 'BlogEdit',
    component: BlogEdit
  },
  {
    path: '/user/register',
    name: 'userRegister',
    component: Register
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
