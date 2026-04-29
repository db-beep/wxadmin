<template>
  <el-form label-position="left" label-width="100px" ref="formRef" :model="form" style="max-width: 600px" :rules="rules">
    <el-form-item label="反馈人" prop="userid">
      <x-select-table
        :disabled="user.role==='user'" :header="[{label:'用户',field:'name',width:'100%'}]"
        v-model="form.userid"
        :queryParams="[{name:'name',label:'用户'}/*,{name:'state',label:'状态',defaultValue:'上架',type:'hide'}*/]"
        api="/user/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="机构" prop="inid">
      <x-select-table
        :disabled="user.role==='institution'" :header="[{label:'非遗机构',field:'name',width:'100%'}]"
        v-model="form.inid"
        :queryParams="[{name:'name',label:'非遗机构'}/*,{name:'state',label:'状态',defaultValue:'上架',type:'hide'}*/]"
        api="/institution/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="反馈内容" prop="title">
      <el-input maxlength="255" v-model="form.title" />
    </el-form-item>
  </el-form>
</template>
<script setup>
    import {  reactive, ref } from 'vue'
    let user = Cache.getUser()//当前登录用户
    const formRef = ref();
    let form = ref({
         userid : user.role == 'user'? user.id:'',
         inid : user.role == 'institution'? user.id:'',
        title : '',
    });
    let callBack;//保存成功回调函数
    const rules = reactive({
        inid:{required: true, message: "机构必填", trigger: "blur"},
        title:{required: true, message: "反馈内容必填", trigger: "blur"},
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
            let {success,message} = await Http.post(`/fankui/save`, form.value);
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

