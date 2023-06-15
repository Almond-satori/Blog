import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

// Vue只有一个app页面，在app页面中刷新各种组件，
// 为了各个组件都能获取到某些变量，将这些变量存储在store中
export default new Vuex.Store({
  state: {
    token: '',
    // 一开始就去sessionStorage中查找userInfo作为初始值(相当于get方法)
    // 这里的存储是string，取数据的时候要将其转换为JSON
    userInfo: JSON.parse(sessionStorage.getItem("userInfo"))
  },
  getters: {
    getUser: state => {
      return state.userInfo;
    }
  },
  mutations: {
    // mutations 对数据的操作 
    // state 当前的状态，状态来源于store中的数据
    SET_TOKEN: (state, token) => {
      state.token = token
      localStorage.setItem("token", token)
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo
      // userInfo是对象类型，需要转换为string
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
    },
    REMOVE_INFO: (state) => {
      localStorage.setItem("token", '')
      // userInfo是对象类型，需要转换为string
      // 将会话存储的信息删除
      
      sessionStorage.setItem("userInfo", JSON.stringify(''))

      state.userInfo = {}
    }
  },
  actions: {
  },
  modules: {
  }
})
