<template>
  <el-form label-position="left" label-width="100px" ref="formRef" :model="form" style="max-width: 600px" :rules="rules">
    <el-form-item label="报名人" prop="userid">
      <x-select-table
        disabled :header="[{label:'用户',field:'name',width:'100%'}]"
        v-model="form.userid"
        :queryParams="[{name:'name',label:'用户'}]"
        api="/user/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="报名活动" prop="acid">
      <x-select-table
        disabled :header="[{label:'非遗传承活动',field:'name',width:'100%'}]"
        v-model="form.acid"
        :queryParams="[{name:'name',label:'非遗传承活动'}]"
        api="/culActivi/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="非遗机构" prop="jig">
      <x-select-table
        disabled :header="[{label:'非遗机构',field:'name',width:'100%'}]"
        v-model="form.jig"
        :queryParams="[{name:'name',label:'非遗机构'}]"
        api="/institution/list"
        labelField="name"
        valueField="id"
      ></x-select-table>
    </el-form-item>
    <el-form-item label="报名备注" prop="remark">
      <el-input disabled maxlength="255" v-model="form.remark" />
    </el-form-item>
    <el-form-item label="报名时间" prop="createtime">
      <el-date-picker disabled value-format="YYYY-MM-DD HH:mm:ss" v-model="form.createtime" type="datetime" placeholder="选择时间" :shortcuts="shortcuts"/>
    </el-form-item>
    <el-form-item label="审核" prop="state">
      <el-select filterable v-model="form.state" clearable placeholder="Select">
      <el-option v-for="item in stateOptionList" :key="item.name" :label="item.name" :value="item.name"/>
      </el-select>
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
        userid:{required: true, message: "报名人必填", trigger: "blur"},
        acid:{required: true, message: "报名活动必填", trigger: "blur"},
        remark:{required: true, message: "报名备注必填", trigger: "blur"},
        state:{required: true, message: "审核必填", trigger: "blur"},
    })

     let stateOptionList = ref([  { name:'待审核'}, { name:'通过'}, { name:'拒绝'}, ]) //审核 下拉框数据

    const render = async (id,loadThisPage) => {
        Msg.loading("加载中..")
        let {data} = await Http.get(`/baom/detail`, {id: id});
        form.value = data
        callBack = loadThisPage
        Msg.loading(false)
    }
    const submit = async () => {

        await formRef.value.validate(async (isValid, invalidFields) => {
            if(! isValid) {
                Msg.error(Helper.getFirstMessage(invalidFields));
                return;
            }
            let {success,message} = await Http.post(`/baom/save`, form.value);
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

