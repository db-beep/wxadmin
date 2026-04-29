<template>
  <x-web ref="webLayout">
  <div class="main-content">
    <div class="w2-container">
      <div class="w2-main-content">
        <!-- 页面标题 -->
        <div class="w2-content-title">
          <h2>非遗资讯列表</h2>
          <div>共 {{ respData.listData?.length }} 条非遗资讯</div>
        </div>
        
        <!-- 搜索筛选区域 -->
        <div class="w2-search-box-container">
          <div class="w2-search-title">
            <span>筛选条件 <i class="fas fa-search"></i></span>
          </div>
          
          <form class="w2-search-form" method="get" id="tagSearchForm">
          <!-- 标签筛选 -->
          
          <!-- 表单字段组 -->
          <div class="w2-form-fields">
            <div class="w2-form-group">
              <label >资讯标题</label>
              <input maxlength="25" type="text" v-model="form.showtitle" placeholder="请输入资讯标题" >
            </div>
            <div class="w2-form-group">
              <label class="">发布时间</label>
              <input autocomplete="off" type="date" name="publishtimeL" v-model="form.publishtimeL" placeholder="从此开始" />
            </div>
            <div class="w2-form-group">
              <label class="">~</label>
              <input autocomplete="off" type="date" name="publishtimeR" v-model="form.publishtimeR" placeholder="到此结束" />
            </div>
            
            <!-- 查询按钮 -->
            <button type="button" @click="loadData" class="w2-form-btn">
            <i class="fas fa-search"></i> 查询
            </button>
          </div>
          </form>
        </div>
        
        <!-- 内容列表区域 -->
        <div class="w2-content-list">
          <div class="w2-content-item" v-for="i in respData.listData">
            <a @click="goto(`/webu/news/detail?id=${i.id}`)">
            <img :src="filePath + i.showpic" alt="非遗资讯">
            <span v-if="i.smallTip" class="w2-small-tip">{{ i.smallTip }}</span>
            <p class="w2-item-title" :title="i.showtitle">{{ i.showtitle }}</p>
            <p class="w2-item-desc">{{ i.showdesc }}</p>
            <p class="w2-item-stats">
            <i class="fas fa-heart" style="color: red;"></i>点赞 {{ i.praiseCount }}
            <i class="fas fa-star" style="color: gold;"></i>收藏 {{ i.starCount }}
            </p>
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
  </x-web>
</template>
<script setup>
  import xWeb from './_WebLayout'
  const router = useRouter();
  import {GlobalConfig} from 'core';
  import {onMounted} from "vue";
  const {user, msg} = useStore();
  const {appConfig} = GlobalConfig()
  const webLayout = ref(null)
  const userInfo = ref({})
  const form = ref({})
  //页面数据，包括查询下拉框
  const respData = ref({});

  onMounted(async () => {
    //查询条件
    let query = router.currentRoute.value.query
    console.log(query)
    if(query.showtitle){
      console.log(query.showtitle)
      form.value.showtitle = query.showtitle
    }

    userInfo.value = Cache.getUser()
    await loadData()
    webLayout.value.activeNav("news");
  })

  //加载数据
  const loadData = async () => {
    Msg.loading("正在加载...")
    let {data} = await Http.get("/webu/news/list-web", form.value);
    respData.value = data;
    Msg.loading(false)
  }

  const goto = (url)=>{
    webLayout.value.goto(url)
  }

</script>

<style scoped lang="less">
@import '../../assets/css/web1Style.css';
:deep(.el-select__placeholder){
  z-index: 1000!important;
  left: 5px!important;
}
</style>

