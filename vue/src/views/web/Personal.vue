<!--web的模板-->
<template>
  <x-web ref="webLayout">
  <main class="w2-main-content">
    <div class="w2-container">
      <!--页面标题-->
      <div class="w2-content-title">
        <h2>个人中心</h2>
        <div>管理您的个人信息</div>
      </div>
      
      <!--个人信息区域-->
      <div class="w2-profile-section">
        <div class="w2-section-header">
          <h3 class="w2-section-title">个人信息</h3>
        </div>
        
        <div class="w2-user-profile">
          <div class="w2-user-info-left">
            <div class="w2-avatar-container">
              <img v-if="!userInfo.avatar" :src="`${publicPath}img/defaultAvatar.jpg`" alt="用户头像" class="w2-user-avatar-large">
              <template v-else>
                <img :src="filePath+userInfo.avatar" alt="用户头像" class="w2-user-avatar-large">
                <div class="w2-avatar-overlay" @click="modUserInfo">更换头像</div>
              </template>
            </div>
          </div>
          <div class="w2-user-info-right" v-if="userInfo.role === 'user'">
            
            <div class="w2-info-row">
              <label>用户名：</label>
              <span class="w2-info-value">{{ userInfo.username }}</span>
            </div>
            <div class="w2-info-row">
              <label>姓名：</label>
              <span class="w2-info-value">{{ userInfo.name }}</span>
            </div>
            <div class="w2-info-row">
              <label>性别：</label>
              <span class="w2-info-value">{{ userInfo.gender }}</span>
            </div>
            <div class="w2-info-row">
              <label>兴趣标签：</label>
              <template v-for="label in userInfo.labelsLabels" :key="label.id">
                <span class="w2-news-tag">{{ label.name }}</span>
              </template>
            </div>
            <div class="w2-info-row">
              <label>年龄：</label>
              <span class="w2-info-value">{{ userInfo.age }}</span>
            </div>
            <div class="w2-info-row">
              <label>电话：</label>
              <span class="w2-info-value">{{ userInfo.tele }}</span>
            </div>
            <div class="w2-info-row w2-full-width">
              <a class="w2-form-btn" @click="modUserInfo"><i class="fas fa-user-edit"></i> 修改个人信息</a>
            </div>
          </div>
        </div>
      </div>
      
      <!--用户功能区域-->
      <div class="w2-functions-section" v-if="userInfo.role === 'user'">
        <div class="w2-section-header">
          <h3 class="w2-section-title">用户功能</h3>
        </div>
        
        <div class="w2-function-grid">
          <div class="w2-function-item">
            <a @click="goto(`/webu/baom/list`)" class="w2-function-link">
            <div class="w2-function-icon"><i class="fas fa-file-invoice"></i></div>
            <div class="w2-function-text">非遗活动报名</div>
            </a>
          </div>
          <div class="w2-function-item">
            <a @click="goto(`/webu/fankui/list`)" class="w2-function-link">
            <div class="w2-function-icon"><i class="fas fa-file-invoice"></i></div>
            <div class="w2-function-text">反馈与建议</div>
            </a>
          </div>
          
          
        </div>
      </div>
      
      <!--收藏区域-->
      <div class="w2-collection-section">
        <div class="w2-section-header">
          <h3 class="w2-section-title">我的收藏</h3>
          <a href="#" class="w2-section-more">全部收藏</a>
        </div>
        
        <div class="w2-collection-list">
          <div class="w2-collection-item" v-for="item in stars" :key="item.id" @click="goto(`/webu/${item.itemtype}/detail?id=${item.itemid}`)">
            <div class="w2-collection-image">
              <img :src="filePath+item.showpic" alt="收藏封面">
            </div>
            <div class="w2-collection-details">
              <h4 class="w2-collection-title">{{ item.showtitle }}</h4>
              <p class="w2-collection-desc">{{ item.showdesc }}</p>
              <div class="w2-collection-time">
                <i class="far fa-clock"></i>
                收藏时间：{{ item.createtime }}
              </div>
              <div class="w2-collection-price" v-if="item.price">¥{{ item.price }}</div>
            </div>
          </div>
        </div>
      </div>
      
      <!--快捷入口-->
      <div class="w2-shortcuts-section">
        <div class="w2-section-header">
          <h3 class="w2-section-title">快捷入口</h3>
        </div>
        
        <div class="w2-shortcuts-list">
          <div class="w2-share-item">
            <a @click="goto(`/webu/intangibleCultural/list-web`)" class="w2-share-card">
            <div class="w2-share-text">非遗文化科普</div>
            </a>
          </div>
          <div class="w2-share-item">
            <a @click="goto(`/webu/news/list-web`)" class="w2-share-card">
            <div class="w2-share-text">非遗资讯</div>
            </a>
          </div>
          <div class="w2-share-item">
            <a @click="goto(`/webu/culActivi/list`)" class="w2-share-card">
            <div class="w2-share-text">非遗传承活动列表</div>
            </a>
          </div>
          <div class="w2-share-item">
            <a @click="goto(`/webu/baom/list`)" class="w2-share-card">
            <div class="w2-share-text">非遗活动报名信息</div>
            </a>
          </div>
          <div class="w2-share-item">
            <a @click="goto(`/webu/cultureType/list`)" class="w2-share-card">
            <div class="w2-share-text">非遗文化类型列表</div>
            </a>
          </div>
          <div class="w2-share-item">
            <a @click="goto(`/webu/fankui/list`)" class="w2-share-card">
            <div class="w2-share-text">反馈与建议信息</div>
            </a>
          </div>
          <div class="w2-share-item">
            <a @click="goto(`/webu/notice/list`)" class="w2-share-card">
            <div class="w2-share-text">公告信息</div>
            </a>
          </div>
        </div>
      </div>
    </div>
  </main>
  </x-web>
</template>

<script setup>
  import xWeb from './_WebLayout'
  const router = useRouter();
  import {GlobalConfig} from 'core';
  import {onMounted, ref} from "vue";
  const {appConfig} = GlobalConfig();
  const webLayout = ref(null)
  const userInfo = ref({
    role: '',
    id: '',
    username: '',
  })
  let stars = ref([])//收藏数据
  onMounted(async () => {
    let {data} = await Http.get("/webu/personal");
    userInfo.value = data.user
    stars.value = data.stars;
  })

  const goto = (url)=>{
    webLayout.value?.goto(url)
  }
                            import userEditPage from "../user/Edit";
                                                        //修改个人信息
  const modUserInfo = ()=>{
    let op;
                              if(userInfo.value.role == 'user'){
          op = Dialog.open(userEditPage, `修改个人信息-用户`).setConfirmText('确认')
        }
                                                          op.mounted(c => {
      c.render(userInfo.value.id, ()=>{location.reload()})
    })
    op.confirm(async (c) => {
      c.submit()
    })
  }
    </script>

<style lang="less">
  @import '../../assets/css/web1Style.css';
</style>

