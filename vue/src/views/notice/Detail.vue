<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="标题">
      <el-text>{{form.title}}</el-text>
    </el-form-item>
    <el-form-item label="内容">
      <el-text>{{form.content}}</el-text>
    </el-form-item>
    <el-form-item label="发布时间">
      <el-text>{{form.createtime}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({  });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/notice/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
    }


    defineExpose({render});

</script>

