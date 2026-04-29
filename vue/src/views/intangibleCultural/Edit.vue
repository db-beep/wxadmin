<template>
  <el-form label-position="left" label-width="100px" ref="formRef" :model="form" style="max-width: 600px" :rules="rules">
    <el-form-item label="科普首图" prop="showpicFile">
      <x-uploader :count="1" v-model="showpicFile"></x-uploader>
    </el-form-item>
    <el-form-item label="非遗文化标题" prop="showtitle">
      <el-input maxlength="25" v-model="form.showtitle" />
    </el-form-item>
    <el-form-item label="发布机构" prop="fabjg">
      <x-select-table
        :disabled="user.role==='institution'" :header="[{label:'非遗机构',field:'name',width:'100%'}]"
        v-model="form.fabjg"
        :queryParams="[{name:'name',label:'非遗机构'}]"
        api="/institution/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="非遗文化标签" prop="labels">
      <x-select-mul
      v-model="form.labels"
      api="/cultureType/list"
      labelField="name"
      valueField="id"
      ></x-select-mul>
    </el-form-item>
    <el-form-item label="非遗文化简述" prop="showdesc">
      <el-input maxlength="30" v-model="form.showdesc" />
    </el-form-item>
    <el-form-item label="科普内容详情" prop="showdetail">
      <el-input v-model="form.showdetail" autosize type="textarea"/>
    </el-form-item>
    <el-form-item label="非遗文化展示" prop="vvFile">
      <x-uploader :count="1" v-model="vvFile"></x-uploader>
    </el-form-item>
  </el-form>
</template>
<script setup>
    import {  reactive, ref } from 'vue'
    const formRef = ref();
    let user = Cache.getUser()//当前登录用户
    let form = ref({});
    let callBack;//保存成功回调函数
    const rules = reactive({
        showtitle:{required: true, message: "非遗文化标题必填", trigger: "blur"},
        showdesc:{required: true, message: "非遗文化简述必填", trigger: "blur"},
        showdetail:{required: true, message: "科普内容详情必填", trigger: "blur"},
    })

    const showpicFile = ref([]) //科普首图
    const vvFile = ref([]) //非遗文化展示

    const render = async (id,loadThisPage) => {
        Msg.loading("加载中..")
        let {data} = await Http.get(`/intangibleCultural/detail`, {id: id});
        form.value = data
        callBack = loadThisPage
        //回显文件：科普首图
        if(form.value.showpic){
            showpicFile.value.push({fileId:form.value.showpic})
        }
        //回显文件：非遗文化展示
        if(form.value.vv){
            vvFile.value.push({fileId:form.value.vv})
        }
        Msg.loading(false)
    }
    const submit = async () => {
    //文件：科普首图
    form.value.showpic = showpicFile.value.length>0 ? showpicFile.value[0].fileId : ''
    //文件：非遗文化展示
    form.value.vv = vvFile.value.length>0 ? vvFile.value[0].fileId : ''

        await formRef.value.validate(async (isValid, invalidFields) => {
            if(! isValid) {
                Msg.error(Helper.getFirstMessage(invalidFields));
                return;
            }
            let {success,message} = await Http.post(`/intangibleCultural/save`, form.value);
            if(! success) {
                Msg.error(message);
                return;
            }
            Msg.success(message);
            Dialog.close();
            callBack(form.value)

            if(user.id === form.value.id){//用户修改自己的信息后刷新
              window.location.reload();
            }
        });
    }

    defineExpose({render,submit});

</script>

