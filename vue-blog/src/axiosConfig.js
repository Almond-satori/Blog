import axios from "axios";
import ElementUI from "element-ui";
import store from "./store";
import router from "./router";

// axios 会自动添加这个URL
axios.defaults.baseURL = "http://47.94.59.162:8080/api"

axios.interceptors.request.use(config => {
    // console.log("前置拦截");
    return config;
})

axios.interceptors.response.use(
    response => {
        // console.log("response 捕获到信息");
        let res = response.data;

        if (res.code == 200) {
            return response;
        } else {
            // 弹窗提醒错误
            ElementUI.Message.error(res.msg, { duration: 2 * 1000 })

            // 拦截信息 注意所有的axios请求都要加catch捕获
            return Promise.reject(response.data.msg)
        }
    },
    error => {
        console.log("error捕获到信息:" + error);

        // 使用我们自定义的错误信息
        if (error.response.data) {
            error.message = error.response.data.msg;
        }

        // 需要登录
        if (error.response.status === 401) {
            store.commit('REMOVE_INFO')
            router.push('/login');
            error.message = '请登录'
        }

        // 权限错误
        if (error.response.status === 403) {
            error.message = '没有访问权限'
        }

        if (error.response.status === 400) {
            error.message = '发生错误,请重试'
        }



        // 弹窗提醒错误
        ElementUI.Message({
            message: error.message,
            showClose: true,
            type: 'error',
            duration: 2 * 1000 // 2s 消失
        });

        return Promise.reject(error)
    }
)