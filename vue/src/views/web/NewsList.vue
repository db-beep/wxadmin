<template>
  <x-web ref="webLayout">
  <div class="main-content">
    <div class="wlist-main-content">
      <div class="wlist-m-body">
        <div class="wlist-content-title">
          <h2>非遗资讯列表</h2>
          <div class="wlist-count">共 {{pager.total}} 条非遗资讯</div>
        </div>
        <!-- 搜索 -->
        <div class="wlist-search-filter-section">
          <span class="wlist-search-title">筛选条件</span>
          <div class="wlist-search-form">
            <input id="pageIndex" type="hidden" name="pageIndex" value="1">
            
            <div class="wlist-form-group">
              <label >资讯标题</label>
              <input maxlength="25" type="text" v-model="form.showtitle" placeholder="请输入资讯标题" >
            </div>
            <div class="wlist-form-group">
              <label class="">发布时间</label>
              <input autocomplete="off" type="date" name="publishtimeL" v-model="form.publishtimeL" placeholder="从此开始" />
            </div>
            <div class="wlist-form-group">
              <label class="">~</label>
              <input autocomplete="off" type="date" name="publishtimeR" v-model="form.publishtimeR" placeholder="到此结束" />
            </div>
            
            <button type="button" @click="loadThisPage" class="wlist-form-btn wlist-form-btn-wlist-primary"><i class="fas fa-search"></i> 查询</button>
            <a class="wlist-form-btn wlist-form-btn-wlist-success" v-if="user && ( user.role.toLowerCase() == 'admin'   )" @click="onAdd"><i class="fas fa-plus-square"></i> 发布资讯</a>
          </div>
        </div>
        
        <p v-if="pager.statisticInfo" class="wlist-statistics-info">{{ pager.statisticInfo }}</p>
        
        <!-- 数据表格 -->
        <div class="wlist-content-table-container">
          <table class="wlist-content-table">
          <thead>
          <tr>
          <th>#</th>
          <th>资讯首图</th>
          <th>资讯标题</th>
          <th>资讯描述</th>
          <th>发布时间</th>
          <th>展示</th>
          <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(i,index) in pager.records">
          <th scope="row">{{index + 1}}</th>
          <!--<td><input class="t-check" type="checkbox" value="${ i.id}"></td>-->
          <td>
          <el-tooltip class="box-item" effect="dark" :content="i.showpic" placement="top-start">
          <x-file-view :disabled="true" :list="i.showpic"></x-file-view>
          </el-tooltip>
          </td>
          <td>{{ i.showtitle}}</td>
          <td>{{ i.showdesc}}</td>
          <td>{{ i.publishtime}}</td>
          <td>
          <el-tooltip class="box-item" effect="dark" :content="i.vv" placement="top-start">
          <x-file-view :disabled="true" :list="i.vv"></x-file-view>
          </el-tooltip>
          </td>
          
          <td class="wlist-action-buttons">
          <a @click="onDetail(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-success"><i class="fas fa-info-circle"></i> 查看</a>
          <a v-if="user &&( user.role.toLowerCase() == 'admin'  )" @click="onEdit(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-primary"><i class="fas fa-edit"></i> 修改</a>
          <a v-if="user &&(  user.role.toLowerCase() == 'admin'  )" @click="onDelete(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-danger"><i class="far fa-trash-alt"></i> 删除</a>
          
          </td>
          </tr>
          </tbody>
          </table>
        </div>
        
        <!-- 分页 -->
        <div class="wlist-pagination">
          <el-pagination
          v-model:current-page="pager.current"
          v-model:page-size="pager.size"
          background
          :page-sizes="[5, 10, 15, 20,50]"
          layout="total,sizes, prev, pager, next, jumper"
          :total="pager.total"
          @size-change="loadThisPage"
          @current-change="loadThisPage"
          />
        </div>
      </div>
    </div>
  </div>
  </x-web>
</template>
<script setup>
  import xWeb from './_WebLayout'
  import {Timer,ChatDotSquare,Delete,Edit,InfoFilled} from '@element-plus/icons-vue'
  const router = useRouter();
  import {GlobalConfig} from 'core';
  import {onMounted} from "vue";
  const {appConfig} = GlobalConfig()
  const webLayout = ref(null)
  const user = ref(false)
  const form = ref({userid:''})
  const pager = ref({current: 1, size: 20,records:[]})

    
  onMounted(async () => {
    user.value = Cache.getUser()
        await loadThisPage()
    webLayout.value.activeNav("news");
  })

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
  //加载数据
  const loadThisPage = async () => {
    Msg.loading("正在加载...")
    let params = Object.assign(form.value, {
      current: pager.value.current,
      size: pager.value.size
    });
    let {data,message} = await Http.get("/webu/news/list", params);
    if (data) {
      pager.value = data
    }else {
      Msg.error(message);
    }
    Msg.loading(false)
  }

  import DetailPage from "../news/Detail"
  //详情页
  const onDetail = async (id) => {
    if(!user.value){
      Msg.error("请先登录哦！")
      return
    }
    const op = Dialog.open(DetailPage, '详情').setCancelText('').setConfirmText('')
    op.mounted(c => {
      c.render(id)
    })
  }
  import EditPage from "../news/Edit"
  //编辑页
  const onEdit = async (id) => {
    const op = Dialog.open(EditPage, `修改`).setConfirmText('保存')
    op.mounted(c => {
      c.render(id, loadThisPage)
    })
    op.confirm(async (c) => {
      c.submit()
    })
  }
  //列表页更新数据通用方法
  const ups = async (id, msg, field, value) => {
    const op = Msg.confirm(msg)
    op.then(async () => {
      let upsForm = {id: id, [field]: value}
      let {success, message} = await Http.post(`/webu/news/save`, upsForm);
      if (!success) {
        Msg.error(message);
      }else{
        Msg.success(message);
        await loadThisPage()
      }
    })
  }
  import AddPage from "../news/Add"
  //新增页
  const onAdd = async () => {
    const op = Dialog.open(AddPage, `发布资讯`).setConfirmText('确认')
    op.mounted(c => {
      c.render(loadThisPage)
    })
    op.confirm(async (c) => {
      c.submit()
    })
  }
  //删除
  const onDelete = async (id) => {
    const op = Msg.confirm('确定删除?')
    op.then(async () => {
      let {success, message} = await Http.post(`/webu/news/delete?id=` + id);
      if (!success) {
        Msg.error(message);
      } else {
        Msg.success(message);
        await loadThisPage()
      }
    })
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
.form-btn{
  margin-right: 3px;
}
</style>

