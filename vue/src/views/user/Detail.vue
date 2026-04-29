<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="用户名">
      <el-text>{{form.username}}</el-text>
    </el-form-item>
    <el-form-item label="姓名">
      <el-text>{{form.name}}</el-text>
    </el-form-item>
    <el-form-item label="头像">
      <x-file-view :disabled="true" :list="form.avatar"></x-file-view>
    </el-form-item>
    <el-form-item label="性别">
      <el-text>{{form.gender}}</el-text>
    </el-form-item>
    <el-form-item label="兴趣标签">
      <el-tag type="success" v-if="form.labelsLabels" v-for="i in form.labelsLabels">{{i.name}}</el-tag>
    </el-form-item>
    <el-form-item label="年龄">
      <el-text>{{form.age}}</el-text>
    </el-form-item>
    <el-form-item label="电话">
      <el-text>{{form.tele}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({ labelsFrn:null, });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/user/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
    }

     //兴趣标签详情页
    import labelsDetailPage from "../cultureType/Detail";
    const labelsDetail = async (id)=> {
        const op = Dialog.open(labelsDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }

    defineExpose({render});

</script>

