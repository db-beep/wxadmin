<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="用户编号">
      <el-text>{{form.userid}}</el-text>
    </el-form-item>
    <el-form-item label="用户名">
      <el-text>{{form.username}}</el-text>
    </el-form-item>
    <el-form-item label="用户角色">
      <el-text>{{form.rolech}}</el-text>
    </el-form-item>
    <el-form-item label="内容">
      <el-text>{{form.content}}</el-text>
    </el-form-item>
    <el-form-item label="发布时间">
      <el-text>{{form.createtime}}</el-text>
    </el-form-item>
    <el-form-item label="内容编号">
      <el-text>{{form.ctid}}</el-text>
    </el-form-item>
    <el-form-item label="内容类型">
      <el-text>{{form.type}}</el-text>
    </el-form-item>
    <el-form-item label="状态">
      <el-text>{{form.status}}</el-text>
    </el-form-item>
  </el-form>
  
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({  });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/userComment/detail`, {id});
        console.log(data)
        form.value = data;
        Msg.loading(false)
    }


    defineExpose({render});

</script>

