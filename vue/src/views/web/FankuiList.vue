<template>
  <x-web ref="webLayout">
  <div class="main-content">
    <div class="wlist-main-content">
      <div class="wlist-m-body">
        <div class="wlist-content-title">
          <h2>反馈与建议列表</h2>
          <div class="wlist-count">共 {{pager.total}} 条反馈与建议</div>
        </div>
        <!-- 搜索 -->
        <div class="wlist-search-filter-section">
          <span class="wlist-search-title">筛选条件</span>
          <div class="wlist-search-form">
            <input id="pageIndex" type="hidden" name="pageIndex" value="1">
            
            <div class="wlist-form-group" v-if="!(user && user.role == 'user')">
              <label for="userid">反馈人：</label>
              <select id="userid" v-model="form.userid">
              <option value="" selected>全部</option>
              <option v-for="i in useridFrnList" :value="i.id">{{ i.name }}</option>
              </select>
            </div>
            <div class="wlist-form-group" v-if="!(user && user.role == 'institution')">
              <label for="inid">机构：</label>
              <select id="inid" v-model="form.inid">
              <option value="" selected>全部</option>
              <option v-for="i in inidFrnList" :value="i.id">{{ i.name }}</option>
              </select>
            </div>
            <div class="wlist-form-group">
              <label >反馈内容</label>
              <input maxlength="255" type="text" v-model="form.title" placeholder="请输入反馈内容" >
            </div>
            <div class="wlist-form-group">
              <label for="state">回复状态：</label>
              <select id="state" v-model="form.state">
              <option value="" selected>全部</option>
              <option value="待回复">待回复</option>
              <option value="已回复">已回复</option>
              </select>
            </div>
            
            
            <button type="button" @click="loadThisPage" class="wlist-form-btn wlist-form-btn-wlist-primary"><i class="fas fa-search"></i> 查询</button>
            <a class="wlist-form-btn wlist-form-btn-wlist-success" v-if="user && ( user.role.toLowerCase() == 'user'   )" @click="onAdd"><i class="fas fa-plus-square"></i> 我要反馈</a>
          </div>
        </div>
        
        <p v-if="pager.statisticInfo" class="wlist-statistics-info">{{ pager.statisticInfo }}</p>
        
        <!-- 数据表格 -->
        <div class="wlist-content-table-container">
          <table class="wlist-content-table">
          <thead>
          <tr>
          <th>#</th>
          <th>反馈人</th>
          <th>机构</th>
          <th>反馈内容</th>
          <th>反馈时间</th>
          <th>回复状态</th>
          <th>回复内容</th>
          <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(i,index) in pager.records">
          <th scope="row">{{index + 1}}</th>
          <!--<td><input class="t-check" type="checkbox" value="${ i.id}"></td>-->
          <td>
          <el-button link v-if="i.useridFrn" type="primary" text bg @click="useridDetail(i.userid)">{{ i.useridFrn?.name }}</el-button>
          </td>
          <td>
          <el-button link v-if="i.inidFrn" type="primary" text bg @click="inidDetail(i.inid)">{{ i.inidFrn?.name }}</el-button>
          </td>
          <td>{{ i.title}}</td>
          <td>{{ i.createtime}}</td>
          <td>{{ i.state}}</td>
          <td>{{ i.content}}</td>
          
          <td class="wlist-action-buttons">
          <a @click="onDetail(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-success"><i class="fas fa-info-circle"></i> 查看</a>
          <a v-if="user &&( user.role.toLowerCase() == 'institution'  )" @click="onEdit(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-primary"><i class="fas fa-edit"></i> 回复</a>
          <a v-if="user &&(  user.role.toLowerCase() == ''  )" @click="onDelete(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-danger"><i class="far fa-trash-alt"></i> 删除反馈</a>
          
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

        const useridFrnList = ref([]);
        const inidFrnList = ref([]);
    
  onMounted(async () => {
    user.value = Cache.getUser()
          let useridFrnResp = await Http.get("/webu/user/list/select");
        useridFrnList.value = useridFrnResp.data
          let inidFrnResp = await Http.get("/webu/institution/list/select");
        inidFrnList.value = inidFrnResp.data
        await loadThisPage()
    webLayout.value.activeNav("fankui");
  })

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
  //加载数据
  const loadThisPage = async () => {
    Msg.loading("正在加载...")
    let params = Object.assign(form.value, {
      current: pager.value.current,
      size: pager.value.size
    });
    let {data,message} = await Http.get("/webu/fankui/list", params);
    if (data) {
      pager.value = data
    }else {
      Msg.error(message);
    }
    Msg.loading(false)
  }

  import DetailPage from "../fankui/Detail"
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
  import EditPage from "../fankui/Edit"
  //编辑页
  const onEdit = async (id) => {
    const op = Dialog.open(EditPage, `回复`).setConfirmText('保存')
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
      let {success, message} = await Http.post(`/webu/fankui/save`, upsForm);
      if (!success) {
        Msg.error(message);
      }else{
        Msg.success(message);
        await loadThisPage()
      }
    })
  }
  import AddPage from "../fankui/Add"
  //新增页
  const onAdd = async () => {
    const op = Dialog.open(AddPage, `我要反馈`).setConfirmText('确认')
    op.mounted(c => {
      c.render(loadThisPage)
    })
    op.confirm(async (c) => {
      c.submit()
    })
  }
  //删除
  const onDelete = async (id) => {
    const op = Msg.confirm('确定删除反馈?')
    op.then(async () => {
      let {success, message} = await Http.post(`/webu/fankui/delete?id=` + id);
      if (!success) {
        Msg.error(message);
      } else {
        Msg.success(message);
        await loadThisPage()
      }
    })
  }

    
    
                       //反馈人详情页
      import useridDetailPage from "../user/Detail";
      const useridDetail = async (id)=> {
        if(!user.value){
          Msg.error("请先登录！")
          return
        }
        const op = Dialog.open(useridDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
          c.render(id)
        })
      }
                   //机构详情页
      import inidDetailPage from "../institution/Detail";
      const inidDetail = async (id)=> {
        if(!user.value){
          Msg.error("请先登录！")
          return
        }
        const op = Dialog.open(inidDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
          c.render(id)
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

