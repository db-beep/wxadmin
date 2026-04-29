<template>
  <x-web ref="webLayout">
  <div class="main-content">
    <div class="wlist-main-content">
      <div class="wlist-m-body">
        <div class="wlist-content-title">
          <h2>非遗活动报名列表</h2>
          <div class="wlist-count">共 {{pager.total}} 条非遗活动报名</div>
        </div>
        <!-- 搜索 -->
        <div class="wlist-search-filter-section">
          <span class="wlist-search-title">筛选条件</span>
          <div class="wlist-search-form">
            <input id="pageIndex" type="hidden" name="pageIndex" value="1">
            
            <div class="wlist-form-group" v-if="!(user && user.role == 'user')">
              <label for="userid">报名人：</label>
              <select id="userid" v-model="form.userid">
              <option value="" selected>全部</option>
              <option v-for="i in useridFrnList" :value="i.id">{{ i.name }}</option>
              </select>
            </div>
            <div class="wlist-form-group" >
              <label for="acid">报名活动：</label>
              <select id="acid" v-model="form.acid">
              <option value="" selected>全部</option>
              <option v-for="i in acidFrnList" :value="i.id">{{ i.name }}</option>
              </select>
            </div>
            <div class="wlist-form-group" v-if="!(user && user.role == 'institution')">
              <label for="jig">非遗机构：</label>
              <select id="jig" v-model="form.jig">
              <option value="" selected>全部</option>
              <option v-for="i in jigFrnList" :value="i.id">{{ i.name }}</option>
              </select>
            </div>
            <div class="wlist-form-group">
              <label >报名备注</label>
              <input maxlength="255" type="text" v-model="form.remark" placeholder="请输入报名备注" >
            </div>
            <div class="wlist-form-group">
              <label for="state">审核：</label>
              <select id="state" v-model="form.state">
              <option value="" selected>全部</option>
              <option value="待审核">待审核</option>
              <option value="通过">通过</option>
              <option value="拒绝">拒绝</option>
              </select>
            </div>
            
            
            <button type="button" @click="loadThisPage" class="wlist-form-btn wlist-form-btn-wlist-primary"><i class="fas fa-search"></i> 查询</button>
            <a class="wlist-form-btn wlist-form-btn-wlist-success" v-if="user && ( user.role.toLowerCase() == 'user'   )" @click="onAdd"><i class="fas fa-plus-square"></i> 立即报名</a>
          </div>
        </div>
        
        <p v-if="pager.statisticInfo" class="wlist-statistics-info">{{ pager.statisticInfo }}</p>
        
        <!-- 数据表格 -->
        <div class="wlist-content-table-container">
          <table class="wlist-content-table">
          <thead>
          <tr>
          <th>#</th>
          <th>报名人</th>
          <th>报名活动</th>
          <th>非遗机构</th>
          <th>报名备注</th>
          <th>报名时间</th>
          <th>审核</th>
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
          <el-button link v-if="i.acidFrn" type="primary" text bg @click="acidDetail(i.acid)">{{ i.acidFrn?.name }}</el-button>
          </td>
          <td>
          <el-button link v-if="i.jigFrn" type="primary" text bg @click="jigDetail(i.jig)">{{ i.jigFrn?.name }}</el-button>
          </td>
          <td>{{ i.remark}}</td>
          <td>{{ i.createtime}}</td>
          <td>{{ i.state}}</td>
          
          <td class="wlist-action-buttons">
          <a @click="onDetail(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-success"><i class="fas fa-info-circle"></i> 查看</a>
          <a v-if="user &&( user.role.toLowerCase() == 'institution'  )" @click="onEdit(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-primary"><i class="fas fa-edit"></i> 处理报名</a>
          <a v-if="user &&(  user.role.toLowerCase() == 'user'  )" @click="onDelete(i.id)" class="wlist-form-btn wlist-form-btn-wlist-xs wlist-form-btn-wlist-danger"><i class="far fa-trash-alt"></i> 取消报名</a>
          
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
        const acidFrnList = ref([]);
        const jigFrnList = ref([]);
    
  onMounted(async () => {
    user.value = Cache.getUser()
          let useridFrnResp = await Http.get("/webu/user/list/select");
        useridFrnList.value = useridFrnResp.data
          let acidFrnResp = await Http.get("/webu/culActivi/list/select");
        acidFrnList.value = acidFrnResp.data
          let jigFrnResp = await Http.get("/webu/institution/list/select");
        jigFrnList.value = jigFrnResp.data
        await loadThisPage()
    webLayout.value.activeNav("baom");
  })

                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
  //加载数据
  const loadThisPage = async () => {
    Msg.loading("正在加载...")
    let params = Object.assign(form.value, {
      current: pager.value.current,
      size: pager.value.size
    });
    let {data,message} = await Http.get("/webu/baom/list", params);
    if (data) {
      pager.value = data
    }else {
      Msg.error(message);
    }
    Msg.loading(false)
  }

  import DetailPage from "../baom/Detail"
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
  import EditPage from "../baom/Edit"
  //编辑页
  const onEdit = async (id) => {
    const op = Dialog.open(EditPage, `处理报名`).setConfirmText('保存')
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
      let {success, message} = await Http.post(`/webu/baom/save`, upsForm);
      if (!success) {
        Msg.error(message);
      }else{
        Msg.success(message);
        await loadThisPage()
      }
    })
  }
  import AddPage from "../baom/Add"
  //新增页
  const onAdd = async () => {
    const op = Dialog.open(AddPage, `立即报名`).setConfirmText('立即报名')
    op.mounted(c => {
      c.render(loadThisPage)
    })
    op.confirm(async (c) => {
      c.submit()
    })
  }
  //删除
  const onDelete = async (id) => {
    const op = Msg.confirm('确定取消报名?')
    op.then(async () => {
      let {success, message} = await Http.post(`/webu/baom/delete?id=` + id);
      if (!success) {
        Msg.error(message);
      } else {
        Msg.success(message);
        await loadThisPage()
      }
    })
  }

    
    
                       //报名人详情页
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
                   //报名活动详情页
      import acidDetailPage from "../culActivi/Detail";
      const acidDetail = async (id)=> {
        if(!user.value){
          Msg.error("请先登录！")
          return
        }
        const op = Dialog.open(acidDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
          c.render(id)
        })
      }
                   //非遗机构详情页
      import jigDetailPage from "../institution/Detail";
      const jigDetail = async (id)=> {
        if(!user.value){
          Msg.error("请先登录！")
          return
        }
        const op = Dialog.open(jigDetailPage, '详情').setCancelText('').setConfirmText('')
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

