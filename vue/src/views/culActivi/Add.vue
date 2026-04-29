<template>
  <el-form label-position="left" label-width="100px" ref="formRef" :model="form" style="max-width: 600px" :rules="rules">
    <el-form-item label="活动名称" prop="name">
      <el-input maxlength="18" v-model="form.name" />
    </el-form-item>
    <el-form-item label="主办机构" prop="zhubf">
      <x-select-table
        :disabled="user.role==='institution'" :header="[{label:'非遗机构',field:'name',width:'100%'}]"
        v-model="form.zhubf"
        :queryParams="[{name:'name',label:'非遗机构'}/*,{name:'state',label:'状态',defaultValue:'上架',type:'hide'}*/]"
        api="/institution/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="活动内容" prop="content">
      <el-input maxlength="255" v-model="form.content" />
    </el-form-item>
    <el-form-item label="报名说明" prop="baomsm">
      <el-input maxlength="255" v-model="form.baomsm" />
    </el-form-item>
    <el-form-item label="活动开始时间" prop="starttime">
      <el-date-picker value-format="YYYY-MM-DD HH:mm:ss" v-model="form.starttime" type="datetime" placeholder="选择时间" :shortcuts="shortcuts"/>
    </el-form-item>
    <el-form-item label="结束时间" prop="endtime">
      <el-date-picker value-format="YYYY-MM-DD HH:mm:ss" v-model="form.endtime" type="datetime" placeholder="选择时间" :shortcuts="shortcuts"/>
    </el-form-item>
    <el-form-item label="限制报名人数" prop="count0">
      <el-input maxlength="255" v-model.number="form.count0" type="number"/>
    </el-form-item>
  </el-form>
</template>
<script setup>
    import {  reactive, ref } from 'vue'
    let user = Cache.getUser()//当前登录用户
    const formRef = ref();
    let form = ref({
        name : '',
         zhubf : user.role == 'institution'? user.id:'',
        content : '',
        baomsm : '',
        starttime : '',
        endtime : '',
        count0 : '',
    });
    let callBack;//保存成功回调函数
    const rules = reactive({
        name:{required: true, message: "活动名称必填", trigger: "blur"},
        content:{required: true, message: "活动内容必填", trigger: "blur"},
        starttime:{required: true, message: "活动开始时间必填", trigger: "blur"},
        count0:{required: true, message: "限制报名人数必填", trigger: "blur"},
    })

    //初始化
    const render = (loadThisPage,params) => {
      callBack = loadThisPage
      if(params){
        //将params的所有属性拷贝给form.value
        Object.assign(form.value,params)
      }
    }


    //提交
    const submit = async () => {

        await formRef.value.validate(async (isValid, invalidFields) => {
            if(! isValid) {
                Msg.error(Helper.getFirstMessage(invalidFields));
                return;
            }
            let {success,message} = await Http.post(`/culActivi/save`, form.value);
            if(! success) {
                Msg.error(message);
                return;
            }
            Msg.success(message);
            Dialog.close();
            if(callBack) {
                callBack()
            }
        });
    }

    defineExpose({render,submit});

</script>

