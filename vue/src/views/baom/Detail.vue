<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="报名人">
      <el-button link v-if="form.useridFrn" type="primary" text bg @click="useridDetail(form.userid)">{{ form.useridFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="报名活动">
      <el-button link v-if="form.acidFrn" type="primary" text bg @click="acidDetail(form.acid)">{{ form.acidFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="非遗机构">
      <el-button link v-if="form.jigFrn" type="primary" text bg @click="jigDetail(form.jig)">{{ form.jigFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="报名备注">
      <el-text>{{form.remark}}</el-text>
    </el-form-item>
    <el-form-item label="报名时间">
      <el-text>{{form.createtime}}</el-text>
    </el-form-item>
    <el-form-item label="审核">
      <el-text>{{form.state}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({ useridFrn:null,acidFrn:null,jigFrn:null, });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/baom/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
    }

     //报名人详情页
    import useridDetailPage from "../user/Detail";
    const useridDetail = async (id)=> {
        const op = Dialog.open(useridDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }
     //报名活动详情页
    import acidDetailPage from "../culActivi/Detail";
    const acidDetail = async (id)=> {
        const op = Dialog.open(acidDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }
     //非遗机构详情页
    import jigDetailPage from "../institution/Detail";
    const jigDetail = async (id)=> {
        const op = Dialog.open(jigDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }

    defineExpose({render});

</script>

