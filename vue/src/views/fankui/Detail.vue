<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="反馈人">
      <el-button link v-if="form.useridFrn" type="primary" text bg @click="useridDetail(form.userid)">{{ form.useridFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="机构">
      <el-button link v-if="form.inidFrn" type="primary" text bg @click="inidDetail(form.inid)">{{ form.inidFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="反馈内容">
      <el-text>{{form.title}}</el-text>
    </el-form-item>
    <el-form-item label="反馈时间">
      <el-text>{{form.createtime}}</el-text>
    </el-form-item>
    <el-form-item label="回复状态">
      <el-text>{{form.state}}</el-text>
    </el-form-item>
    <el-form-item label="回复内容">
      <el-text>{{form.content}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({ useridFrn:null,inidFrn:null, });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/fankui/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
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

    defineExpose({render});

</script>

