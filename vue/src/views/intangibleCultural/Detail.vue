<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="科普首图">
      <x-file-view :disabled="true" :list="form.showpic"></x-file-view>
    </el-form-item>
    <el-form-item label="非遗文化标题">
      <el-text>{{form.showtitle}}</el-text>
    </el-form-item>
    <el-form-item label="发布机构">
      <el-button link v-if="form.fabjgFrn" type="primary" text bg @click="fabjgDetail(form.fabjg)">{{ form.fabjgFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="非遗文化标签">
      <el-tag type="success" v-if="form.labelsLabels" v-for="i in form.labelsLabels">{{i.name}}</el-tag>
    </el-form-item>
    <el-form-item label="非遗文化简述">
      <el-text>{{form.showdesc}}</el-text>
    </el-form-item>
    <el-form-item label="科普内容详情">
      <el-text>{{form.showdetail}}</el-text>
    </el-form-item>
    <el-form-item label="发布时间">
      <el-text>{{form.publishtime}}</el-text>
    </el-form-item>
    <el-form-item label="非遗文化展示">
      <x-file-view :disabled="true" :list="form.vv"></x-file-view>
    </el-form-item>
  </el-form>
  
  <x-comment ref="commentRef"></x-comment>
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({ fabjgFrn:null,labelsFrn:null, });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/intangibleCultural/detail`, {id});
        console.log(data)
        form.value = data;
        commentRef.value.init(form.value.id,'intangibleCultural')
        Msg.loading(false)
    }

     //发布机构详情页
    import fabjgDetailPage from "../institution/Detail";
    const fabjgDetail = async (id)=> {
        const op = Dialog.open(fabjgDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }
     //非遗文化标签详情页
    import labelsDetailPage from "../cultureType/Detail";
    const labelsDetail = async (id)=> {
        const op = Dialog.open(labelsDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }

    defineExpose({render});

</script>

