import axios from 'axios';
import QS from 'qs';
import router from '../router/index.js'
import {Message} from 'element-ui'
import ERROR from '../common/errorCode.js'
import $ from 'jquery'

//请求超时
axios.defaults.timeout = 20000;
//设置post请求头 (设置post请求格式，以及编码)
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8';
//设置基础URL地址，往后的请求都是带上该地址
//axios.defaults.baseURL = "/";

//添加响应拦截器
axios.interceptors.response.use(function (response) {
    let path = router.currentRoute.path;
    if(response.status == 200){
        let res = response.data;
        if(res.code == ERROR.UNAUTHORIZED){//认证失败
            if(!path.endsWith("/login")){//如果不在任何一组登录页面
                //则跳转到对应的登录页面,如 /admin/login, /store/login /delivery/login
                let arr = ['admin','store','delivery'];
                let root = path.split("/").filter(v=>v).shift();
                //console.log(root);
                if(arr.indexOf(root)<0){
                    //console.log($(window));
                    $(window).trigger("show-login");
                }else{
                    router.push({
                        path: '/'+[root,'login'].filter(v=>v).join("/"),
                        query: {
                            from: path
                        }
                    })
                }
            }
        }else if(res.code == ERROR.PERMISSION_DENIED){//权限不足
            Message({
                message: '权限不足，请检查登录账号或注销重新登录',
                type: 'warning'
            })
        }
    }
    return response;
  }, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
  });

// 封装get请求
const get = (url, params,config={}) => {
	return new Promise((resolve, reject) => {
		axios.get(url, {
            params: params,
            ...config
		}).then(res => {
			//成功回调,将返回值返回给调用者
			resolve(res.data);
		}).catch(err => {
			reject(err.data);
		})
	});
}

// 封装post请求
const post = (url, params,config={}) => {
    if(typeof params !== 'string') params = QS.stringify(params, { arrayFormat: 'repeat' });
	return new Promise((resolve, reject) => {
		axios.post(url, params,config).then(res => {
			//成功回调,将返回值返回给调用者
			resolve(res.data);
		}).catch(err => {
			reject(err.data);
		})
	});
}
export {
    axios
}
export default {
	get,
    post
}
