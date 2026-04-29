<template>
  <div class="codeyingBox">
    <x-ai-chat></x-ai-chat>
    <!-- 顶部导航区 -->
    <header class="w2-header">
    <div class="w2-header-container">
      <!-- 左侧系统名称 -->
      <div class="w2-header-left">
        <h1 class="w2-logo">
        <i class="fas fa-store"></i>
        <span>{{ appConfig.name }}</span>
        </h1>
      </div>
      
      <!-- 中部导航链接 -->
      <div class="w2-header-center">
        <a href="javascript:void(0)" class="w2-nav-link" @click="goto('/welcome')">
        <i class="fas fa-home"></i>
        <span>首页</span>
        </a>
        <a href="javascript:void(0)" class="w2-nav-link" @click="goto('/webu/intangibleCultural/list-web')">
        <i class="fas fa-blog"></i>
        <span>非遗文化科普</span>
        </a>
        <a href="javascript:void(0)" class="w2-nav-link" @click="goto('/webu/news/list-web')">
        <i class="fas fa-blog"></i>
        <span>非遗资讯</span>
        </a>
        <a href="javascript:void(0)" class="w2-nav-link" @click="goto('/webu/culActivi/list')">
        <i class="fas fa-newspaper"></i>
        <span>非遗传承活动</span>
        </a>
        <a href="javascript:void(0)" class="w2-nav-link" @click="goto('/webu/cultureType/list')">
        <i class="fas fa-newspaper"></i>
        <span>非遗文化类型</span>
        </a>
      </div>
      
      <!-- 右侧功能区 -->
      <div class="w2-header-right">
        <!-- 后台用户 -->
        <template v-if="userInfo && userInfo.isWuser===false">
          <a href="javascript:void(0)" @click="goto('/hello')" class="w2-user-avatar">
          <img v-if="userInfo.avatar" :src="`/api/file/downloadById?fileid=${userInfo.avatar}`">
          <i v-else class="fas fa-user"></i>
          <span class="w2-user-name">{{userInfo.name?userInfo.name:userInfo.username}}</span>
          </a>
          <a href="javascript:void(0)" @click="loginOut" class="w2-logout-btn">
          <i class="fas fa-sign-out-alt"></i>
          <span>退出</span>
          </a>
        </template>
        
        <!-- 前台用户 -->
        <template v-else-if="userInfo && userInfo.isWuser">
          <a href="javascript:void(0)" @click="goto('/webu/personal')" class="w2-user-avatar">
          <img v-if="userInfo.avatar" :src="`/api/file/downloadById?fileid=${userInfo.avatar}`">
          <i v-else class="fas fa-user"></i>
          <span class="w2-user-name">{{userInfo.name?userInfo.name:userInfo.username}}</span>
          </a>
          <a href="javascript:void(0)" @click="loginOut" class="w2-logout-btn">
          <i class="fas fa-sign-out-alt"></i>
          <span>退出登录</span>
          </a>
        </template>
        
        <!-- 未登录 -->
        <template v-else>
          <a href="javascript:void(0)" @click="goto('/login')" class="w2-login-btn">
          <i class="fas fa-sign-in-alt"></i>
          <span>登录</span>
          </a>
          <a href="javascript:void(0)" @click="goto('/login')" class="w2-register-btn">
          <i class="fas fa-user-plus"></i>
          <span>注册</span>
          </a>
        </template>
      </div>
    </div>
    </header>
    
    <slot></slot>
    
    <!-- 页脚区域 -->
    <footer class="w2-footer">
    <div class="w2-container">
      <div class="w2-footer-content">
        <div class="w2-footer-column">
          <h3 class="w2-footer-title">关于我们</h3>
          <p>Java是最好的语言！致力用Java开发做出更优质的网站系统，为用户提供更好的服务体验。</p>
        </div>
        <div class="w2-footer-column">
          <h3 class="w2-footer-title">联系我们</h3>
          <p><i class="fas fa-phone-alt"></i> 客服热线：400-123-4567</p>
          <p><i class="fas fa-envelope"></i> 邮箱：service@nongchanpin.com</p>
          <p><i class="fas fa-map-marker-alt"></i> 地址：中国·北京·朝阳区农产品大厦</p>
        </div>
        <div class="w2-footer-column">
          <h3 class="w2-footer-title">关注我们</h3>
          <div class="w2-social-icons">
            <a href="#" class="w2-social-icon"><i class="fab fa-weixin"></i></a>
            <a href="#" class="w2-social-icon"><i class="fab fa-weibo"></i></a>
            <a href="#" class="w2-social-icon"><i class="fab fa-tiktok"></i></a>
          </div>
        </div>
      </div>
      <div class="w2-footer-bottom">
        <p>© 2026 Powered By Java 版权所有 | 京ICP备12345678号</p>
      </div>
    </div>
    </footer>
  </div>
</template>

<script setup name="x-web">
  const router = useRouter();
  import {GlobalConfig} from 'core';
  import {onMounted, reactive} from "vue";

  const {user, msg} = useStore();
  const {appConfig} = GlobalConfig()

  //登录用户信息
  const userInfo = ref({})
  onMounted(async () => {
    console.log("Web layout onMounted")
    userInfo.value = await user.getUserInfo()
    Cache.setUser(userInfo.value)
  })

  //设置/获取 导航栏高亮
  const activeNav = () => {
    return ''
  }

  //goto
  const goto = (url) => {
    console.log('goto:' + url)
    router.push(url)
  }
  //登出
  const loginOut = () => {
    const op = Msg.confirm('是否退出登录？')
    op.then(async () => {
      let result = await user.loginOut()
      userInfo.value = false
      Cache.setUser(null);//必须设置为空，否则有缓存用户
    })
  }
  //取消收藏
  const cancelStar = async (id, entityName, callback, op = "收藏") => {
    let {success} = await Http.post('/web/star', {action: 'cancelStar', id, entityName, op})
    if (success) {
      Msg.success("已取消~")
      if (callback) callback()
    } else {
      Msg.error("失败~")
    }
  }
  //收藏
  const star = async (id, entityName, callback, op = "收藏") => {
    let {success, message} = await Http.post('/web/star', {action: 'star', id, entityName, op})
    if (success) {
      Msg.success(message)
      if (callback) callback()
    } else {
      Msg.error(message)
    }
  }

  defineExpose({
    goto, activeNav, cancelStar, star
  })
</script>

<style lang="less">
  //防止页面不滚动
  .codeyingBox {
    height: 100%;
    overflow: scroll;
  }

  .codeyingBox::-webkit-scrollbar {
    display: none;
  }

  @import '../../assets/css/web1Style.css';
</style>



