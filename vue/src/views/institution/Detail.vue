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
    <el-form-item label="非遗机构名称">
      <el-text>{{form.name}}</el-text>
    </el-form-item>
    <el-form-item label="机构头像">
      <x-file-view :disabled="true" :list="form.avatar"></x-file-view>
    </el-form-item>
    <el-form-item label="电话">
      <el-text>{{form.tele}}</el-text>
    </el-form-item>
    <el-form-item label="地址">
      <el-text>{{form.place}}</el-text>
    </el-form-item>
    <el-form-item label="机构简介">
      <el-text>{{form.description}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({  });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/institution/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
    }


    defineExpose({render});

</script>

