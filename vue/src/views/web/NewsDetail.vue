<!--web的模板-->
<template>
  <x-web ref="webLayout">
  <div class="main-content">
    <div class="m-body main-detail">
      <!-- 详情页头部 -->
      <div class="detail-head">
        <div class="detail-head-box1">
          <img :src="filePath + detail.showpic" alt="产品图片">
        </div>
        <div class="detail-head-box2">
          <h1>{{ detail.showtitle }}</h1>
          <p class="detail-head-box2-desc">{{ detail.showdesc }}</p>
          <p class="detail-head-box2-tip">发布时间：{{ detail.publishtime }}</p>
          <div>
          </div>
          <p class="detail-head-box2-tip"><i class="fas fa-star" style="color: black;"></i> 收藏 <span id="starCount">{{ starCount }}</span></p>
          <p class="detail-head-box2-tip"><i class="fas fa-thumbs-up"></i> 点赞 <span id="praiseCount">{{ praiseCount }}</span></p>
          
          <div>
            <button v-show="startBtnShow" type="button" @click="star(detail.id)"><i class="far fa-star"></i> 收 藏</button>
            <button v-show="!startBtnShow" type="button" @click="cancelStar(detail.id)"><i class="fas fa-star" style="color: gold;"></i> 取消收藏</button>
            <button v-show="praiseBtnShow" type="button" @click="star(detail.id, '点赞')"><i class="far fa-thumbs-up"></i> 点赞</button>
            <button v-show="!praiseBtnShow" type="button" @click="cancelStar(detail.id, '点赞')"><i class="fas fa-thumbs-up" style="color:red"></i> 撤回点赞</button>
            <!--<button type="button" v-show="userInfo && userInfo.role === 'user'" class="buy-now-btn" @click="ddd(`${ detail.id}`)"><i class="fas fa-shopping-cart"></i> 动作</button>-->
          </div>
        </div>
      </div>
      <!-- 详情页主体 -->
      <div class="detail-body">
        <div class="detail-body1">
          <p>热 门 推 荐</p>
          <!-- 热门推荐项 -->
          <div class="detail-hot" v-for="i in recommends">
            <a @click="loadData(i.id)">
            <img :src="filePath + i.showpic">
            <p :title="i.showtitle">{{ i.showtitle }}</p>
            <p>{{ i.showdesc }}</p>
            <p>热度<i class="fas fa-fire"></i> {{ i.starCount }}</p>
            </a>
          </div>
        </div>
        <div class="detail-body2">
          <h2>内容详情</h2>
          <!-- 产品详情字段 -->
          <h3>资讯详情</h3>
          <p>{{ detail.showdetail}}</p>
          
          <h3>发布时间</h3>
          <p>{{detail.publishtime}}</p>
          <h3>展示</h3>
          <template v-if="detail.vv">
            <img v-if="match(detail.vv,['.jpg','.JPG','.png','.gif','.bmp','.tiff'])"
            style="width:100%" :src="filePath + detail.vv"/>
            <video v-if="match(detail.vv,['.mp4','.avi','.wmv','.WMV','.flv'])"
            :src="filePath + detail.vv" autoplay="autoplay" muted="muted" loop="loop" controls></video>
            <audio v-if="match(detail.vv,['.mp3','.wma','.WMA'])"
            :src="filePath + detail.vv" controls="controls"></audio>
            <a v-if="!matchMap[detail.vv]" style="color:#208b6b" :href="filePath + detail.vv" target="_blank">
            下载{{detail.vv}}
            </a>
          </template>
          <x-comment ref="commentRef"></x-comment>
          
        </div>
      </div>
    </div>
  </div>
  </x-web>
</template>

<script setup>
  import xWeb from './_WebLayout'
  let commentRef = ref(null)
  const router = useRouter();
  import {GlobalConfig} from 'core';
  import {onMounted} from "vue";
  //const {user, msg} = useStore();
  const {appConfig} = GlobalConfig()
  const webLayout = ref(null)
  const userInfo = ref(false)
  let entityName = `news`//实体类型
  let detail = ref({})//数据
  let starCount = ref(0)//收藏个数
  let praiseCount = ref(0)//点赞个数
  let recommends = ref([])//热门
  let startBtnShow = ref(true)
  let praiseBtnShow = ref(true)
  onMounted(async () => {
    userInfo.value = Cache.getUser()
    let query = router.currentRoute.value.query
    await loadData(query.id)
    webLayout.value.activeNav(entityName);
  })

                                                                                                                                                          
  //跳转页面
  // import AddPage from "../xxxx/Add"
  // //新增页
  // const ddd = async (id) => {
  //   const op = Dialog.open(AddPage, `我要`).setConfirmText('确认')
  //   op.mounted(c => {
  //     c.render(null,{stuId: id})
  //   })
  //   op.confirm(async (c) => {
  //     c.submit()
  //   })
  // }

  const loadData = async (id) => {
    Msg.loading("正在前往...")
    let {data} = await Http.get("/webu/news/detail?id=" + id);
    detail.value = data.entity;
    starCount.value = data.starCount;
    praiseCount.value = data.praiseCount;
    recommends.value = data.recommends;
    if (userInfo.value && userInfo.value.id) {
      await hasStar(id)
    }
    Msg.loading(false)
    commentRef.value.init(id, entityName)
  }
  //收藏
  const star = async (id, op = '收藏') => {
    webLayout.value.star(id, entityName, () => {
      if (op === '收藏') {
        startBtnShow.value = false;
        starCount.value = starCount.value + 1
      } else {
        praiseBtnShow.value = false;
        praiseCount.value = praiseCount.value + 1
      }
    }, op)
  }
  //取消收藏
  const cancelStar = async (id, op = '收藏') => {
    webLayout.value.cancelStar(id, entityName, () => {
      if (op === '收藏') {
        startBtnShow.value = true;
        starCount.value = starCount.value - 1
      } else {
        praiseBtnShow.value = true;
        praiseCount.value = praiseCount.value - 1
      }
    }, op)
  }
  const hasStar = async function (id) {
    {
      let {data, success, message} = await Http.post('/web/star', {action: 'hasStar', id, entityName, op: '收藏'})
      if (data) {//已经收藏了
        startBtnShow.value = false;
      } else {//没有收藏
        startBtnShow.value = true;
      }
    }
    {
      let {data, success, message} = await Http.post('/web/star', {action: 'hasStar', id, entityName, op: '点赞'})
      if (data) {//已经收藏了
        praiseBtnShow.value = false;
      } else {//没有收藏
        praiseBtnShow.value = true;
      }
    }
  }
  const matchMap = {}
  const match = (name, list) => {
    for (let i = 0; i < list.length; i++) {
      if (name.endsWith(list[i])) {
        console.log(name, list[i])
        matchMap[name] = true
        return true;
      }
    }
    return false
  }


</script>

<style lang="less">
  @import '../../assets/css/web1Style.css';
</style>


