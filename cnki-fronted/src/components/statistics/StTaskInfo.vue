<template>
  <div>

      <div v-if="theRole === 'ROLE_student' && !submitStatus">
          <h1>
        学生填写部分
          </h1>
          <br><br>
        <el-row>
          <el-col :span="12" :offset="6">
              <el-form ref="form" label-width="80px">
                  <el-form-item label="学生姓名">
                    <el-input
                    placeholder="请输入姓名"
                    v-model="stuName"
                    clearable>
                  </el-input>
                  </el-form-item>
                  
                  <el-form-item label="学号">

                      <el-input
                        
                          :rows="2"
                          placeholder="请输入学号"
                          v-model="stuNo">
                        </el-input>

                  </el-form-item>

                  <el-form-item label="课程题目">
                    <el-input
                    placeholder="课程题目"
                    v-model="title"
                    clearable>
                  </el-input>
                  </el-form-item>

                 
                  <el-form-item label="查阅文献">
                    <el-input
                    placeholder="查阅文献"
                    v-model="article"
                    clearable>
                  </el-input>
                  </el-form-item>

                  <el-form-item label="开题报告">
                    <el-input
                    placeholder="开题报告"
                    v-model="report"
                    clearable>
                  </el-input>
                  </el-form-item>
                   <el-form-item label="论文内容">
                    <el-input
                    placeholder="论文内容"
                    type="textarea"
                    v-model="content"
                    clearable>
                  </el-input>
                  </el-form-item>


              </el-form>
          </el-col>
        </el-row>
        <br>
         
          <el-button type="primary" @click="beginInstance">开始审批流程</el-button>
          <el-button type="primary" @click="studentClaim">提交</el-button>


      </div>
      <div v-else-if="submitStatus">
         论文提交已成功，您可以随时查看审批情况
        <el-steps :active="2" finish-status="success" simple style="margin-top: 20px">
            <el-step title="提交论文" ></el-step>
            <el-step title="教师审批" ></el-step>
            <el-step title="院长审批" ></el-step>
      </el-steps>

      
       
      </div>
      <div v-else>当前角色h还未指派此流程中的任务</div>
      
  </div>
</template>
<script>
export default {
  data(){
    return {
      processInstanceId:'',
      stuName:'',
      stuNo:'',
      article:'',
      report:'',
      title:'',
      content:'',
      processInfo:'',
      qualityEst:'',
      stuAttitude:'',
      qa:'',
      checkResult:'',
      stuInfo:'',
      teacherEvaluation:'',
      submitStatus:false,
       approvItem:'',
        itemTableData:'',
        score:'',
        value4:''
    }
   
  },
  mounted(){
    this.stuInfo  = this.$route.params.taskData.variables
  },
  computed:{
    theRole(){
      return this.$store.state.user.roles[0].name
    }
  },
  methods:{
    beginInstance(){
             this.getRequest("/system/basic/startProcess").then(resp=> {
                if (resp && resp.status == 200) {
                  // 得到此次的流程ID
                  this.processInstanceId = resp.data
                 
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
      
    },
     
    studentClaim(){
       this.postRequest('/system/basic/claim/'+this.processInstanceId, {
          'stuName': this.stuName,
          'stuNo': this.stuNo,
          'article': this.article,
          'report': this.report,
          'title': this.title,
          'content': this.content
        }).then(resp=> {
                if (resp && resp.status == 200) {
                  this.$message({
                    message: '论文提交成功！',
                    type:'success'
                  });
                  this.submitStatus = true
              
             
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
    }
  }
}
</script>
