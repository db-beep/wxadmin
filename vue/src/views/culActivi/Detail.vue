<template>
  <el-form
    label-position="left"
    label-width="100px"
    :model="form"
    style="max-width: 600px"
    >
    <el-form-item label="活动名称">
      <el-text>{{form.name}}</el-text>
    </el-form-item>
    <el-form-item label="主办机构">
      <el-button link v-if="form.zhubfFrn" type="primary" text bg @click="zhubfDetail(form.zhubf)">{{ form.zhubfFrn.name }}</el-button>
    </el-form-item>
    <el-form-item label="活动内容">
      <el-text>{{form.content}}</el-text>
    </el-form-item>
    <el-form-item label="报名说明">
      <el-text>{{form.baomsm}}</el-text>
    </el-form-item>
    <el-form-item label="活动开始时间">
      <el-text>{{form.starttime}}</el-text>
    </el-form-item>
    <el-form-item label="结束时间">
      <el-text>{{form.endtime}}</el-text>
    </el-form-item>
    <el-form-item label="限制报名人数">
      <el-text>{{form.count0}}</el-text>
    </el-form-item>
    <el-form-item label="已报名人数">
      <el-text>{{form.count}}</el-text>
    </el-form-item>
  </el-form>
  
  <x-comment ref="commentRef"></x-comment>
  
</template>
<script setup>
    let commentRef = ref(null)
    let form = ref({ zhubfFrn:null, });

    const render = async (id) => {
        Msg.loading("loading...")
        let {data} = await Http.get(`/culActivi/detail`, {id});
        console.log(data)
        form.value = data;
        commentRef.value.init(form.value.id,'culActivi')
        Msg.loading(false)
    }

     //主办机构详情页
    import zhubfDetailPage from "../institution/Detail";
    const zhubfDetail = async (id)=> {
        const op = Dialog.open(zhubfDetailPage, '详情').setCancelText('').setConfirmText('')
        op.mounted(c => {
            c.render(id)
        })
    }

    defineExpose({render});

</script>

