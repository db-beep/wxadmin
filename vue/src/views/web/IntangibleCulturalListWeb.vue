<template>
  <x-web ref="webLayout">
  <div class="main-content">
    <div class="w2-container">
      <div class="w2-main-content">
        <!-- 页面标题 -->
        <div class="w2-content-title">
          <h2>非遗文化科普列表</h2>
          <div>共 {{ respData.listData?.length }} 条非遗文化科普</div>
        </div>
        
        <!-- 搜索筛选区域 -->
        <div class="w2-search-box-container">
          <div class="w2-search-title">
            <span>筛选条件 <i class="fas fa-search"></i></span>
          </div>
          
          <form class="w2-search-form" method="get" id="tagSearchForm">
          <!-- 标签筛选 -->
          <div class="w2-tag-filters">
            <input type="radio" name="labels" id="all" value="" class="w2-radio-tag" checked>
            <label for="all">全部</label>
            <template v-for="i in respData.labelsFrnList">
              <input v-model="form.labels" type="radio" name="labels" :id="i.id" :value="i.id" class="w2-radio-tag">
              <label :for="i.id">{{ i.name }}</label>
            </template>
          </div>
          
          <!-- 表单字段组 -->
          <div class="w2-form-fields">
            <div class="w2-form-group">
              <label >非遗文化标题</label>
              <input maxlength="25" type="text" v-model="form.showtitle" placeholder="请输入非遗文化标题" >
            </div>
            <div class="w2-form-group">
              <label for="fabjg">发布机构：</label>
              <select id="fabjg" v-model="form.fabjg">
              <option value="" selected>全部</option>
              <option v-for="i in respData.fabjgFrnList" :value="i.id">{{ i.name }}</option>
              </select>
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
            <a @click="goto(`/webu/intangibleCultural/detail?id=${i.id}`)">
            <img :src="filePath + i.showpic" alt="非遗文化科普">
            <span v-if="i.smallTip" class="w2-small-tip">{{ i.smallTip }}</span>
            <p class="w2-item-title" :title="i.showtitle">{{ i.showtitle }}</p>
            <p class="w2-item-desc">{{ i.showdesc }}</p>
            <div class="w2-item-tags">
              <a v-for="label in i.labelsLabels" href="#"><span class="w2-label-xs">{{ label.name }}</span></a>
            </div>
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
    webLayout.value.activeNav("intangibleCultural");
  })

  //加载数据
  const loadData = async () => {
    Msg.loading("正在加载...")
    let {data} = await Http.get("/webu/intangibleCultural/list-web", form.value);
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

