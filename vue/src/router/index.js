import { createRouter, createWebHistory } from 'vue-router';
import Index from '../views/Index.vue';
import Login from '@/views/Login'
const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
 // {
 //   path: '/',
 //   name: 'Web',
 //   component: Index
 // },
 //  router前台页面
  {
    path: '/',
    name: 'Web',
    component: () => import('../views/web/_WebRouterView.vue'),//网站首页router-view
    children: [
      {
        path: '/welcome',
        name: 'Welcome',
        component: () => import('../views/web/Index.vue') //首页
      },{
        path: '/webu/personal',
        name: 'personal',
        component: () => import('../views/web/Personal.vue') //收藏页
      },
            {
        path: '/webu/admin/list',
        name: 'webuAdminList',
        component: () => import('../views/web/AdminList.vue')//管理员  详情页
      },
            {
        path: '/webu/institution/list',
        name: 'webuInstitutionList',
        component: () => import('../views/web/InstitutionList.vue')//非遗机构  详情页
      },
            {
        path: '/webu/user/list',
        name: 'webuUserList',
        component: () => import('../views/web/UserList.vue')//用户  详情页
      },
              {
          path: '/webu/intangibleCultural/detail',
          name: 'webuIntangibleCulturalDetail',
          component: () => import('../views/web/IntangibleCulturalDetail.vue')//非遗文化科普详情页
        },{
          path: '/webu/intangibleCultural/list-web',
          name: 'webuIntangibleCulturalListWeb',
          component: () => import('../views/web/IntangibleCulturalListWeb.vue')//非遗文化科普搜索页页
        },
            {
        path: '/webu/intangibleCultural/list',
        name: 'webuIntangibleCulturalList',
        component: () => import('../views/web/IntangibleCulturalList.vue')//非遗文化科普  详情页
      },
              {
          path: '/webu/news/detail',
          name: 'webuNewsDetail',
          component: () => import('../views/web/NewsDetail.vue')//非遗资讯详情页
        },{
          path: '/webu/news/list-web',
          name: 'webuNewsListWeb',
          component: () => import('../views/web/NewsListWeb.vue')//非遗资讯搜索页页
        },
            {
        path: '/webu/news/list',
        name: 'webuNewsList',
        component: () => import('../views/web/NewsList.vue')//非遗资讯  详情页
      },
            {
        path: '/webu/culActivi/list',
        name: 'webuCulActiviList',
        component: () => import('../views/web/CulActiviList.vue')//非遗传承活动  详情页
      },
            {
        path: '/webu/baom/list',
        name: 'webuBaomList',
        component: () => import('../views/web/BaomList.vue')//非遗活动报名  详情页
      },
            {
        path: '/webu/cultureType/list',
        name: 'webuCultureTypeList',
        component: () => import('../views/web/CultureTypeList.vue')//非遗文化类型  详情页
      },
            {
        path: '/webu/fankui/list',
        name: 'webuFankuiList',
        component: () => import('../views/web/FankuiList.vue')//反馈与建议  详情页
      },
            {
        path: '/webu/notice/list',
        name: 'webuNoticeList',
        component: () => import('../views/web/NoticeList.vue')//公告  详情页
      },
            {
        path: '/webu/userComment/list',
        name: 'webuUserCommentList',
        component: () => import('../views/web/UserCommentList.vue')//评论  详情页
      },
    ]
  },

  //router后台页面
  {
    path: '/hello',
    name: 'Index',
    component: Index,
    children: [
        {
          meta: {history: true, title: "管理员"},
          path: '/hello/codeying/admin',
          name: 'admin',
          component: () => import('../views/admin/Index.vue')
        },
        {
          meta: {history: true, title: "非遗机构"},
          path: '/hello/codeying/institution',
          name: 'institution',
          component: () => import('../views/institution/Index.vue')
        },
        {
          meta: {history: true, title: "用户"},
          path: '/hello/codeying/user',
          name: 'user',
          component: () => import('../views/user/Index.vue')
        },
        {
          meta: {history: true, title: "非遗文化科普"},
          path: '/hello/codeying/intangibleCultural',
          name: 'intangibleCultural',
          component: () => import('../views/intangibleCultural/Index.vue')
        },
        {
          meta: {history: true, title: "非遗资讯"},
          path: '/hello/codeying/news',
          name: 'news',
          component: () => import('../views/news/Index.vue')
        },
        {
          meta: {history: true, title: "非遗传承活动"},
          path: '/hello/codeying/culActivi',
          name: 'culActivi',
          component: () => import('../views/culActivi/Index.vue')
        },
        {
          meta: {history: true, title: "非遗活动报名"},
          path: '/hello/codeying/baom',
          name: 'baom',
          component: () => import('../views/baom/Index.vue')
        },
        {
          meta: {history: true, title: "非遗文化类型"},
          path: '/hello/codeying/cultureType',
          name: 'cultureType',
          component: () => import('../views/cultureType/Index.vue')
        },
        {
          meta: {history: true, title: "反馈与建议"},
          path: '/hello/codeying/fankui',
          name: 'fankui',
          component: () => import('../views/fankui/Index.vue')
        },
        {
          meta: {history: true, title: "公告"},
          path: '/hello/codeying/notice',
          name: 'notice',
          component: () => import('../views/notice/Index.vue')
        },
        {
          meta: {history: true, title: "评论"},
          path: '/hello/codeying/userComment',
          name: 'userComment',
          component: () => import('../views/userComment/Index.vue')
        },
    ]
  }
]
//创建路由
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})
//路由跳转
router.beforeEach((to, from, next) => {
  if(to.meta.history === true) {
    Cache.dispatch("menu", 'addHistory', to);
  }
  next()
})

export default router

