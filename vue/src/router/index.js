import Vue from 'vue'
import VueRouter from 'vue-router'
//index
import IndexMenu from '../views/IndexMenu.vue'
import Index from '../index/Index.vue'
import Store from '../index/Store.vue'
//container
import Container from '../views/Container.vue'
//store
import StoreLogin from '../views/StoreLogin.vue'
import StoreMenu from '../views/StoreMenu.vue'
import StoreProductManage from '../store/ProductManage.vue'
import StoreOrderManage from '../store/OrderManage.vue'
import StoreProfileManage from '../store/StoreProfile.vue'
import StoreCommendManage from '../store/CommendManage.vue'
import StorePersonProfile from '../store/PersonProfile.vue'
//admin
import AdminLogin from '../views/AdminLogin.vue'
import Admin from '../views/AdminMenu.vue'
import PersonProfile from '../admin/PersonProfile.vue'
import StoreManage from '../admin/StoreManage.vue'
import CustomerManage from '../admin/CustomerManage.vue'
import DeliverManage from '../admin/DeliverManage.vue'
import CommendManage from '../admin/CommendManage.vue'
import BackupRestore from '../admin/BackupRestore.vue'


Vue.use(VueRouter)

const routes = [{
	path: '/store',
	component: Container,
	children: [{
		path: 'login',
		component: StoreLogin
	}, {
		path: 'menu',
		component: StoreMenu,
		children: [{
			path: 'ProductManage',
			component: StoreProductManage
		}, {
			path: 'StoreProfile',
			component: StoreProfileManage
		}, {
			path: 'OrderManage',
			component: StoreOrderManage
		}, {
			path: 'CommendManage',
			component: StoreCommendManage
		}, {
			path: 'PersonProfile',
			component: StorePersonProfile
		}, {
			path: '',
			redirect: 'ProductManage'
		}]
	}, {
		path: '',
		redirect: 'login'
	}]
},{
	path: '/delivery',
	component: Container,
	children: [{
		path: 'login',
		component: ()=>import("../views/DeliveryLogin.vue")
	},{
		path: 'menu',
		component: ()=>import("../views/DeliveryMenu.vue"),
		children: [{
			path: 'OrderDelivery',
			component: ()=>import('../delivery/OrderDelivery.vue')
		},{
			path: 'DeliverStore',
			component: ()=>import('../delivery/DeliverStore.vue')
		},{
			path: 'PersonProfile',
			component: ()=>import('../delivery/PersonProfile.vue')
		},{
			path: '',
			redirect: 'OrderDelivery'
		}]
	}]
}, {
	path: '/admin',
	component: Container,
	children: [{
		path: 'login',
		component: AdminLogin
	}, {
		path: 'menu',
		component: Admin,
		children: [ {
			path: 'StoreManage',//商家管理
			component: StoreManage
		}, {
			path: 'CustomerManage',//客户管理
			component: CustomerManage
		}, {
			path: 'DeliverManage',//配送员管理
			component: DeliverManage
		}, {
			path: 'StoreRequestManage',//商家入驻请求管理
			component: ()=>import('../admin/StoreRequestManage.vue')
		}, {
			path: 'BackupRestore',//备份还原
			component: BackupRestore
		}, {
			path: 'CommendManage',//推荐管理
			component: CommendManage
		},  {
			path: 'PersonProfile',//个人资料
			component: PersonProfile
		}, {
			path: '',
			redirect: 'StoreManage'
		}]
	}, {
		path: '',
		redirect: 'login'
	}]
},
{
	path: '/',
	// route level code-splitting
	// this generates a separate chunk (about.[hash].js) for this route
	// which is lazy-loaded when the route is visited.
	component: IndexMenu,
	children: [{
		path: 'storeDetail',
		component: Store
	}, {
		path: '',
		component: Index
	},{
		path: 'personCenter',
		component: ()=>{return import('../index/PersonCenter.vue')},
		children: [{
			path: 'myOrder',
			component: ()=>{return import('../index/OrderManage.vue')}
		},{
			path: 'myComment',
			component: ()=>{return import('../index/CommendManage.vue')}
		},,{
			path: 'profile',
			component: ()=>{return import('../index/PersonProfile.vue')}
		},{
			path: '',
			redirect: 'myOrder'
		}]
	}]
},
{
	path: '*',
	redirect: '/'
}
]

const router = new VueRouter({
	routes
})

export default router
