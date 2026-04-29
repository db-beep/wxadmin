<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="非遗类型名">
      <el-text>{{form.name}}</el-text>
    </el-form-item>
    <el-form-item label="类型简介">
      <el-text>{{form.description}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({  });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/cultureType/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
    }


    defineExpose({render});

</script>

