<template>
  <x-main>
  <!--搜索查询表单-->
  <el-row class="search-row">
    <div v-if="user.role!=='user'" class="search-item">
      <span>反馈人</span>
      <div style="width: 150px">
        <x-select-table :header="[{label:'用户',field:'name',width:'100%'}]"
          v-model="searchForm.userid"
          :queryParams="[{name:'name',label:'用户'}]"
          api="/user/list"
          labelField="name"
          valueField="id"
        ></x-select-table>
      </div>
    </div>
    <div v-if="user.role!=='institution'" class="search-item">
      <span>机构</span>
      <div style="width: 150px">
        <x-select-table :header="[{label:'非遗机构',field:'name',width:'100%'}]"
          v-model="searchForm.inid"
          :queryParams="[{name:'name',label:'非遗机构'}]"
          api="/institution/list"
          labelField="name"
          valueField="id"
        ></x-select-table>
      </div>
    </div>
    <div class="search-item">
      <span>反馈内容</span>
      <div style="width: 150px">
        <el-input clearable placeholder="请输入反馈内容" v-model="searchForm.title" maxlength="255"/>
      </div>
    </div>
    <div class="search-item">
      <span>回复状态</span>
      <div style="width: 150px">
        <el-select filterable v-model="searchForm.state" clearable placeholder="请选择">
        <el-option v-for="item in stateOptionList" :key="item.name" :label="item.name" :value="item.name"/>
        </el-select>
      </div>
    </div>
    
    <button class="x-btn x-btn-m" type="primary" @click="loadThisPage">查询</button>
    <button class="x-btn x-btn-success x-btn-m" v-if="user && ( user.role.toLowerCase() == 'user'   )" @click="onAdd">我要反馈</button>
    
  </el-row>
  <!--搜索查询表单 end-->
  
  <div class="statistic-info" v-if="pager.statisticInfo">
    {{pager.statisticInfo}}
  </div>
  
  <!--数据表-->
  <el-table :data="pager.records" style="width: 100%">
    <el-table-column label="反馈人">
      <template #default="scope">
        <el-button link v-if="scope.row.useridFrn" type="primary" text bg @click="useridDetail(scope.row.userid)">{{ scope.row.useridFrn.name }}</el-button>
      </template>
    </el-table-column>
    <el-table-column label="机构">
      <template #default="scope">
        <el-button link v-if="scope.row.inidFrn" type="primary" text bg @click="inidDetail(scope.row.inid)">{{ scope.row.inidFrn.name }}</el-button>
      </template>
    </el-table-column>
    <el-table-column label="反馈内容">
      <template #default="scope">
        <span >{{scope.row.title}}</span>
      </template>
    </el-table-column>
    <el-table-column sortable label="反馈时间">
      <template #default="scope">
        <el-icon><timer/></el-icon><span>{{scope.row.createtime}}</span>
      </template>
    </el-table-column>
    <el-table-column label="回复状态">
      <template #default="scope">
        <span >{{scope.row.state}}</span>
      </template>
    </el-table-column>
    <el-table-column label="回复内容">
      <template #default="scope">
        <span >{{scope.row.content}}</span>
      </template>
    </el-table-column>
    
    <el-table-column fixed="right" label="操作">
      <template #default="scope">
        <button class="x-btn" @click="onDetail(scope.row.id)">详情</button>
        <button class="x-btn x-btn-warn" v-if=" user.role.toLowerCase() == 'institution'  " @click="onEdit(scope.row.id)">回复</button>
        <button class="x-btn x-btn-error" v-if="  user.role.toLowerCase() == ''  " @click="onDelete(scope.row.id)">删除反馈</button>
        
        <!-- <button v-if="user.role.toLowerCase() == 'user' && scope.row.state == '待缴纳'" -->
        <!-- class="x-btn" @click="ups(scope.row.id,'确认支付','state','已缴纳')">立即支付</button>-->
      </template>
    </el-table-column>
  </el-table>
  
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
  </x-main>
</template>

<script setup name="fankui">
    let conf = codeying
    import {Helper} from "core";
    let user = Cache.getUser()//当前登录用户
    import {Timer,ChatDotSquare,Delete,Edit,InfoFilled} from '@element-plus/icons-vue'
    import {onMounted} from "vue";
    //搜索条件表单
    let searchForm = ref({
            userid : "",
            inid : "",
            title : "",
            state : "",
    })
    //分页信息 默认查询第一页，20条数据
    let pager = ref({current: 1, size: 20})

    let stateOptionList = ref([  { name:'待回复'}, { name:'已回复'}, ]) //回复状态 下拉框数据
    //钩子函数，挂载
    onMounted(() => {
        loadThisPage()
    })

    //加载本页数据
    const loadThisPage = async () => {
        Msg.loading("加载中...")
        //表单查询参数和分页参数
        let params = Object.assign(searchForm.value, {
            current: pager.value.current,
            size: pager.value.size
        });
        let {data,message} = await Http.get(`/fankui/list`, params);
        console.log("查询条件")
        console.log(params)
        console.log("查询结果")
        console.log(data)
        if (data) {
            pager.value = data
        }else {
            Msg.error(message);
        }
        Msg.loading(false)
    }
    import DetailPage from "./Detail"
    //详情页
    const onDetail = async (id) => {
        const op = Dialog.open(DetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }
    import EditPage from "./Edit"
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
        let {success, message} = await Http.post(`/fankui/save`, upsForm);
        if (!success) {
          Msg.error(message);
        }else{
          Msg.success(message);
          await loadThisPage()
        }
      })
    }
    import AddPage from "./Add"
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
        const op = Msg.confirm('确定删除反馈？')
        op.then(async () => {
            let {success, message} = await Http.post(`/fankui/delete?id=` + id);
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
        const op = Dialog.open(useridDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }
     //机构详情页
    import inidDetailPage from "../institution/Detail";
    const inidDetail = async (id)=> {
        const op = Dialog.open(inidDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }



</script>


<style>
    /*分页右对齐*/
    .el-pagination {
        float: right;
    }
    .el-form--inline .el-form-item{
        margin-right: 0!important;
    }
    .search-row{
        display: flex;
        margin-bottom: 10px;
    }
    .search-item{
        display: flex;
    }
    .search-item span{
        white-space: nowrap;
        line-height: 32px;
        margin: 0 5px;
    }
</style>

