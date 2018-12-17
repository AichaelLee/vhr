<template>
  <div>
    <h1>
      提交论文

      <el-form ref="form" label-width="80px">
          <el-form-item label="论文题目">
            <el-input
            placeholder="请输入论文题目"
            v-model="title"
            clearable>
          </el-input>
          </el-form-item>
          
          <el-form-item label="论文内容">

               <el-input
                  type="textarea"
                  :rows="2"
                  placeholder="请输入论文内容"
                  v-model="content">
                </el-input>

          </el-form-item>
      </el-form>

         
          <el-button type="primary" @click="beginInstance">开始论文审批流程</el-button>
          <el-button type="primary" @click="claim">声明</el-button>

          <el-button type="primary" @click="submitThesis">提交</el-button>
    </h1>
  </div>
</template>
<script>
export default {
  data(){
    return {
      title:'',
      content:''
    }
   

  },
  methods:{
    beginInstance(){
             this.getRequest("/system/basic/startProcess").then(resp=> {
                if (resp && resp.status == 200) {
                  alert("开始实例成功！")
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
      
    },
    claim(){
      this.getRequest("/system/basic/claim").then(resp=> {
                if (resp && resp.status == 200) {
                  alert("论文提交成功！")
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })

    },
    submitThesis(){}
  }
}
</script>

