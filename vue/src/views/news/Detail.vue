<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="资讯首图">
      <x-file-view :disabled="true" :list="form.showpic"></x-file-view>
    </el-form-item>
    <el-form-item label="资讯标题">
      <el-text>{{form.showtitle}}</el-text>
    </el-form-item>
    <el-form-item label="资讯描述">
      <el-text>{{form.showdesc}}</el-text>
    </el-form-item>
    <el-form-item label="资讯详情">
      <el-text>{{form.showdetail}}</el-text>
    </el-form-item>
    <el-form-item label="发布时间">
      <el-text>{{form.publishtime}}</el-text>
    </el-form-item>
    <el-form-item label="展示">
      <x-file-view :disabled="true" :list="form.vv"></x-file-view>
    </el-form-item>
  </el-form>
  
  <x-comment ref="commentRef"></x-comment>
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({  });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/news/detail`, {id});
        console.log(data)
        form.value = data;
        commentRef.value.init(form.value.id,'news')
        Msg.loading(false)
    }


    defineExpose({render});

</script>

