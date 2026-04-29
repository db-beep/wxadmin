<template>
  <x-main>
  <!--搜索查询表单-->
  <el-row class="search-row">
    <div class="search-item">
      <span>活动名称</span>
      <div style="width: 150px">
        <el-input clearable placeholder="请输入活动名称" v-model="searchForm.name" maxlength="18"/>
      </div>
    </div>
    <div v-if="user.role!=='institution'" class="search-item">
      <span>主办机构</span>
      <div style="width: 150px">
        <x-select-table :header="[{label:'非遗机构',field:'name',width:'100%'}]"
          v-model="searchForm.zhubf"
          :queryParams="[{name:'name',label:'非遗机构'}]"
          api="/institution/list"
          labelField="name"
          valueField="id"
        ></x-select-table>
      </div>
    </div>
    
    <button class="x-btn x-btn-m" type="primary" @click="loadThisPage">查询</button>
    <button class="x-btn x-btn-success x-btn-m" v-if="user && ( user.role.toLowerCase() == 'institution'   )" @click="onAdd">发布非遗传承活动</button>
    
  </el-row>
  <!--搜索查询表单 end-->
  
  <div class="statistic-info" v-if="pager.statisticInfo">
    {{pager.statisticInfo}}
  </div>
  
  <!--数据表-->
  <el-table :data="pager.records" style="width: 100%">
    <el-table-column label="活动名称">
      <template #default="scope">
        <span >{{scope.row.name}}</span>
      </template>
    </el-table-column>
    <el-table-column label="主办机构">
      <template #default="scope">
        <el-button link v-if="scope.row.zhubfFrn" type="primary" text bg @click="zhubfDetail(scope.row.zhubf)">{{ scope.row.zhubfFrn.name }}</el-button>
      </template>
    </el-table-column>
    <el-table-column label="活动内容">
      <template #default="scope">
        <span >{{scope.row.content}}</span>
      </template>
    </el-table-column>
    <el-table-column label="报名说明">
      <template #default="scope">
        <span >{{scope.row.baomsm}}</span>
      </template>
    </el-table-column>
    <el-table-column sortable label="活动开始时间">
      <template #default="scope">
        <el-icon><timer/></el-icon><span>{{scope.row.starttime}}</span>
      </template>
    </el-table-column>
    <el-table-column sortable label="结束时间">
      <template #default="scope">
        <el-icon><timer/></el-icon><span>{{scope.row.endtime}}</span>
      </template>
    </el-table-column>
    <el-table-column label="限制报名人数">
      <template #default="scope">
        <span >{{scope.row.count0}}</span>
      </template>
    </el-table-column>
    <el-table-column label="已报名人数">
      <template #default="scope">
        <span >{{scope.row.count}}</span>
      </template>
    </el-table-column>
    
    <el-table-column fixed="right" label="操作">
      <template #default="scope">
        <button class="x-btn" @click="onDetail(scope.row.id)">详情</button>
        <button class="x-btn x-btn-warn" v-if=" user.role.toLowerCase() == 'institution'  " @click="onEdit(scope.row.id)">修改</button>
        <button class="x-btn x-btn-error" v-if="  user.role.toLowerCase() == 'institution'  " @click="onDelete(scope.row.id)">删除</button>
        <button class="x-btn x-btn-primary x-btn-m" v-if="user && ( user.role.toLowerCase() == 'user'   )" @click="onAddBaomByAcid(scope.row.id)">立即报名</button>
        
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

<script setup name="culActivi">
    let conf = codeying
    import {Helper} from "core";
    let user = Cache.getUser()//当前登录用户
    import {Timer,ChatDotSquare,Delete,Edit,InfoFilled} from '@element-plus/icons-vue'
    import {onMounted} from "vue";
    //搜索条件表单
    let searchForm = ref({
            name : "",
            zhubf : "",
    })
    //分页信息 默认查询第一页，20条数据
    let pager = ref({current: 1, size: 20})

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
        let {data,message} = await Http.get(`/culActivi/list`, params);
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
        let {success, message} = await Http.post(`/culActivi/save`, upsForm);
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
        const op = Dialog.open(AddPage, `发布非遗传承活动`).setConfirmText('确认')
        op.mounted(c => {
            c.render(loadThisPage)
        })
        op.confirm(async (c) => {
            c.submit()
        })
    }
    //删除
    const onDelete = async (id) => {
        const op = Msg.confirm('确定删除？')
        op.then(async () => {
            let {success, message} = await Http.post(`/culActivi/delete?id=` + id);
            if (!success) {
                Msg.error(message);
            } else {
                Msg.success(message);
                await loadThisPage()
            }
        })
    }

     //主办机构详情页
    import zhubfDetailPage from "../institution/Detail";
    const zhubfDetail = async (id)=> {
        const op = Dialog.open(zhubfDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }

    //跳转非遗活动报名新增立即报名
    import AddBaomByAcidPage from "../baom/Add";
    const onAddBaomByAcid = async (id)=> {
      const op = Dialog.open(AddBaomByAcidPage, '立即报名').setConfirmText('立即报名')
      op.mounted(c => {
        c.render(()=>{},{acid:id})
      })
      op.confirm(async (c) => {
        c.submit()
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

