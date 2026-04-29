<!--网站的一种类型，默认图文-->
<template>
  <x-web ref="webLayout" :doSearch="doHandle">
  <!--轮播内容区-->
  <section class="w2-carousel-section">
    <div class="w2-carousel-container">
      <!--轮播图-->
      <div class="w2-carousel">
        <div class="w2-carousel-slides">
          <div class="w2-carousel-slide active">
            <img :src="`${publicPath}img/b1.jpg`" alt="轮播图片1">
          </div>
          <div class="w2-carousel-slide">
            <img :src="`${publicPath}img/b2.jpg`" alt="轮播图片2">
          </div>
          <div class="w2-carousel-slide">
            <img :src="`${publicPath}img/b3.jpg`" alt="轮播图片3">
          </div>
        </div>
        
        <!--轮播控制按钮-->
        <button class="w2-carousel-prev" @click="prevSlide"><i class="fas fa-chevron-left"></i></button>
        <button class="w2-carousel-next" @click="nextSlide"><i class="fas fa-chevron-right"></i></button>
        
        <!--轮播指示器-->
        <div class="w2-carousel-indicators">
          <button class="w2-indicator active" @click="showSlide(0)"></button>
          <button class="w2-indicator" @click="showSlide(1)"></button>
          <button class="w2-indicator" @click="showSlide(2)"></button>
        </div>
      </div>
      
      <!--公告轮播条-->
      <div class="w2-notice-carousel">
        <div class="w2-notice-label">
          <i class="fas fa-bullhorn"></i>
          <span>公告：</span>
        </div>
        <div class="w2-notice-content">
          <div class="w2-notice-item" v-for="(notice, index) in webData.notices" :key="index">
            {{ notice.content }}
          </div>
        </div>
      </div>
    </div>
  </section>
  
  <!--农产品资讯板块-->
  <section class="w2-news-section">
    <div class="w2-container">
      <div class="w2-section-header">
        <h2 class="w2-section-title">非遗文化科普</h2>
        <div class="w2-search-box">
          <input type="text" v-model="intangibleCulturalSearchText" placeholder="搜索非遗文化科普..." class="w2-search-input">
          <button class="w2-search-btn" @click="goto(`/webu/intangibleCultural/list-web?showtitle=${ intangibleCulturalSearchText }`)" title="搜索">
          <i class="fas fa-search"></i>
          </button>
        </div>
      </div>
      
      <!--资讯列表-->
      <div class="w2-news-list">
        <!--资讯项-->
        <div class="w2-news-item" v-for="item in webData.intangibleCulturalHotList" :key="item.id">
          <a @click="goto(`/webu/intangibleCultural/detail?id=${item.id}`)" class="w2-news-link">
          <div class="w2-news-image">
            <img :src="filePath + item.showpic" alt="非遗文化科普">
          </div>
          <div class="w2-news-content">
            <h3 class="w2-news-title">{{ item.showtitle }}</h3>
            <p class="w2-news-description">{{ item.showdesc }}</p>
            <div class="w2-news-tags">
              <span class="w2-news-tag" v-for="label in item.labelsLabels" :key="label.id">{{ label.name }}</span>
            </div>
            <div class="w2-news-stats">
              <span class="w2-news-stat">热度<i class="fas fa-fire"></i>{{ item.starCount}}</span>
            </div>
          </div>
          </a>
        </div>
      </div>
    </div>
  </section>
  <section class="w2-news-section">
    <div class="w2-container">
      <div class="w2-section-header">
        <h2 class="w2-section-title">非遗资讯</h2>
        <div class="w2-search-box">
          <input type="text" v-model="newsSearchText" placeholder="搜索非遗资讯..." class="w2-search-input">
          <button class="w2-search-btn" @click="goto(`/webu/news/list-web?showtitle=${ newsSearchText }`)" title="搜索">
          <i class="fas fa-search"></i>
          </button>
        </div>
      </div>
      
      <!--资讯列表-->
      <div class="w2-news-list">
        <!--资讯项-->
        <div class="w2-news-item" v-for="item in webData.newsHotList" :key="item.id">
          <a @click="goto(`/webu/news/detail?id=${item.id}`)" class="w2-news-link">
          <div class="w2-news-image">
            <img :src="filePath + item.showpic" alt="非遗资讯">
          </div>
          <div class="w2-news-content">
            <h3 class="w2-news-title">{{ item.showtitle }}</h3>
            <p class="w2-news-description">{{ item.showdesc }}</p>
            <div class="w2-news-tags">
            </div>
            <div class="w2-news-stats">
              <span class="w2-news-stat">热度<i class="fas fa-fire"></i>{{ item.starCount}}</span>
            </div>
          </div>
          </a>
        </div>
      </div>
    </div>
  </section>
  
  <!--数据统计-->
  <section class="w2-news-section" v-show="statisticData && statisticData.length > 0">
    <div class="w2-container">
      <div class="w2-section-header">
        <h2 class="w2-section-title">数据统计</h2>
      </div>
      <div style="margin-top: 20px;">
        <div id="statisticNum" style="display: flex;flex-wrap: wrap;">
          <div v-for="(item, index) in statisticData" :key="'num-'+index" style="width: 280px; margin: 10px; padding: 10px; background-color: var(--theme-color);color: white; text-align: center">
            <h2>{{ item.name }}</h2>
            <p style="font-size: 22px; font-weight: 600;">{{ item.value }}</p>
          </div>
        </div>
        <div id="echarts" style="display: flex;flex-wrap: wrap;">
          <div v-for="(item, index) in echartsList" :key="'chart-'+index" style="width: 580px; box-sizing: border-box">
            <h2 style="margin: 0">{{ item.name }}</h2>
            <div :ref="el => setChartRef(el, index)" style="height: 300px"></div>
          </div>
        </div>
      </div>
    </div>
  </section>
  
  <!--快捷入口区-->
  <section class="w2-shortcuts-section">
    <div class="w2-container">
      <div class="w2-shortcuts-list">
        <a @click="goto(`/webu/intangibleCultural/list-web`)" class="w2-shortcut-item">
        <i class="fas fa-newspaper w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">非遗文化科普</span>
        </a>
        <a @click="goto(`/webu/news/list-web`)" class="w2-shortcut-item">
        <i class="fas fa-newspaper w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">非遗资讯</span>
        </a>
        <a @click="goto(`/webu/culActivi/list`)" class="w2-shortcut-item">
        <i class="fas fa-list w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">非遗传承活动列表</span>
        </a>
        <a @click="goto(`/webu/baom/list`)" class="w2-shortcut-item">
        <i class="fas fa-info-circle w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">非遗活动报名信息</span>
        </a>
        <a @click="goto(`/webu/cultureType/list`)" class="w2-shortcut-item">
        <i class="fas fa-list w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">非遗文化类型列表</span>
        </a>
        <a @click="goto(`/webu/fankui/list`)" class="w2-shortcut-item">
        <i class="fas fa-info-circle w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">反馈与建议信息</span>
        </a>
        <a @click="goto(`/webu/notice/list`)" class="w2-shortcut-item">
        <i class="fas fa-info-circle w2-shortcut-icon"></i>
        <span class="w2-shortcut-text">公告信息</span>
        </a>
      </div>
    </div>
  </section>
  
  </x-web>
</template>

<script setup>
  import xWeb from './_WebLayout'
  import {Helper} from "core";
  let conf = codeying
  const router = useRouter();
  import {GlobalConfig} from 'core';
  import {onMounted,reactive,ref,onBeforeUnmount,nextTick} from "vue";
  const webLayout = ref(null)
  const {appConfig} = GlobalConfig()
  let user
  import * as echarts from 'echarts';

    const intangibleCulturalSearchText = ref("")
    const newsSearchText = ref("")

  // 轮播图相关
  const currentSlide = ref(0);
  const currentNotice = ref(0);
  let slideInterval = null;
  let noticeInterval = null;

  onMounted(() => {
    console.log("Web onMounted")
    user = Cache.getUser()
    if (!user || !user.id) {
      user = false
    }
    doHandle()
    getEcharts()
    window.addEventListener('resize', handleResize);

    // 启动轮播
    startSlideInterval();
    startNoticeInterval();
  })
  const webData = ref({
    notices:[],
  })

  // 处理数据
  const doHandle = async function () {
    Msg.loading("正在前往...")
    let {data} = await Http.get("/welcome?n=4");
    webData.value = data;
    Msg.loading(false)
  }

  const goto = (url)=>{
    webLayout.value.goto(url)
  }

  // 轮播图功能
  const showSlide = (index) => {
    currentSlide.value = index;
    const slides = document.querySelectorAll('.w2-carousel-slide');
    const indicators = document.querySelectorAll('.w2-indicator');

    slides.forEach(slide => slide.classList.remove('active'));
    indicators.forEach(indicator => indicator.classList.remove('active'));

    if (slides[index]) slides[index].classList.add('active');
    if (indicators[index]) indicators[index].classList.add('active');
  }

  const prevSlide = () => {
    let newIndex = currentSlide.value - 1;
    if (newIndex < 0) newIndex = 2; // 假设3张轮播图
    showSlide(newIndex);
  }

  const nextSlide = () => {
    let newIndex = currentSlide.value + 1;
    if (newIndex >= 3) newIndex = 0; // 假设3张轮播图
    showSlide(newIndex);
  }

  const startSlideInterval = () => {
    slideInterval = setInterval(() => {
      nextSlide();
    }, 3000);
  }

  // 公告轮播
  const startNoticeInterval = () => {
    noticeInterval = setInterval(() => {
      const noticeItems = document.querySelectorAll('.w2-notice-item');
      const noticeContent = document.querySelector('.w2-notice-content');

      if (noticeItems.length > 0) {
        currentNotice.value = (currentNotice.value + 1) % noticeItems.length;
        if (noticeContent) {
          noticeContent.style.transform = `translateY(-${ currentNotice.value * 30}px)`;
        }
      }
    }, 3000);
  }

  //echarts
  onBeforeUnmount(() => {
    // 清理
    if (slideInterval) clearInterval(slideInterval);
    if (noticeInterval) clearInterval(noticeInterval);
    chartInstances.value.forEach(chart => chart?.dispose());
    window.removeEventListener('resize', handleResize);
  })

  const echartsList = ref([]);
  const statisticData = ref([]);
  const chartInstances = ref([]);
  const chartDoms = ref([]);
  // 设置图表容器引用
  const setChartRef = (el, index) => {
    if (el) chartDoms.value[index] = el;
  };

  // 获取数据
  const getEcharts = async () => {
    try {
      const data = await Http.post("/hello");
      if (!data.success) {
        console.log(data.message);
        return;
      }

      // 处理统计数据
      statisticData.value = data.data.filter(e => e.type === 'n').map(e => ({
        name: e.name,
        value: e.value
      }));

      // 处理图表数据
      echartsList.value = data.data
          .filter(e => ['line', 'bar', 'pie'].includes(e.type))
          .map(e => {
            const item = { name: e.name };

            if (e.type === 'line' || e.type === 'bar') {
              item.option = {
                xAxis: {
                  data: e.names,
                  axisLabel: { rotate: 45, interval: 0 }
                },
                yAxis: {},
                series: [{
                  type: e.type,
                  data: e.values,
                  label: { show: true, position: 'top', formatter: '{c}' }
                }]
              };
            }
            else if (e.type === 'pie') {
              item.option = {
                series: [{
                  type: 'pie',
                  data: e.values.map((value, idx) => ({
                    name: e.names[idx],
                    value: value
                  })),
                  label: { show: true, formatter: '{b}: {c} ({d}%)' },
                  itemStyle: {
                    color: (params) => {
                      const colors = [
                        '#C1232B', '#e3ee0d', '#1ad02a', '#132ee0',
                        '#12d7d0', '#4015c2', '#9c16b9', '#d37408',
                        '#203b12', '#9eece5', '#d397d7', '#561812'
                      ];
                      return colors[params.dataIndex % colors.length];
                    }
                  }
                }]
              };
            }

            return item;
          });

      // 确保DOM更新后渲染图表
      await nextTick();
      renderCharts();

    } catch (error) {
      console.error('获取图表数据失败:', error);
    }
  };

  // 渲染图表
  const renderCharts = () => {
    // 清理旧实例
    chartInstances.value.forEach(instance => instance?.dispose());
    chartInstances.value = [];

    echartsList.value.forEach((item, index) => {
      if (chartDoms.value[index]) {
        const chart = echarts.init(chartDoms.value[index]);
        chart.setOption(item.option);
        chartInstances.value[index] = chart;
      }
    });
  };

  // 窗口缩放处理
  const handleResize = () => {
    chartInstances.value.forEach(chart => chart?.resize());
  };
</script>

<style lang="less">
@import '../../assets/css/web1Style.css';
</style>

