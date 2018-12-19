<template>
  <div>


  <div v-if="theRole !== 'ROLE_student'">
    <el-collapse v-model="activeNames">
        <el-collapse-item title="基本信息" name="1">
         <el-row>
            <el-col :span="6"  style="text-align:left">学生姓名：{{stuInfo.stuName}}</el-col>
            <el-col :span="6">学年：2017-1018</el-col>
          </el-row>

            <el-row>
            <el-col :span="6" style="text-align:left">论文题目：{{stuInfo.title}}</el-col>
          </el-row>
            <el-row>
              <el-col :span="6" style="text-align:left">查阅文献：{{stuInfo.article}}</el-col>
            </el-row>
        </el-collapse-item>
        <el-collapse-item title="开题报告" name="2">
           <el-row>
            <el-col :span="24" style="text-align:left" > {{stuInfo.report}}</el-col>
          </el-row>
          
        </el-collapse-item>
        <el-collapse-item title="论文内容" name="3">
          <el-row>
            <el-col :span="24" style="text-align:left" >  {{stuInfo.content}}</el-col>
          </el-row>
         
        </el-collapse-item>
        <el-collapse-item title="指导教师意见" name="4" v-if="theRole === 'ROLE_dean'">
          <el-row>
            <el-col :span="22" style="text-align:left" > 学生进度完成情况:{{stuInfo.processInfo | estimationLevel}}</el-col>
          </el-row>
           <el-row>
            <el-col :span="22" style="text-align:left" > 学生态度:{{stuInfo.qualityEst| estimationLevel}}</el-col>
          </el-row>
          <el-row>
           <el-col :span="22" style="text-align:left" > 质量评价:{{stuInfo.stuAttitude| estimationLevel}}</el-col>
          </el-row>
            <el-row>
           <el-col :span="22" style="text-align:left" >   存在问题及建议:{{stuInfo.qa}}</el-col>
          </el-row>
            <el-row>
           <el-col :span="22" style="text-align:left" >  检查结果:{{stuInfo.checkResult| filterResult}}</el-col>
          </el-row>
         
          
          
        
         
        </el-collapse-item>
    </el-collapse>
  </div>
      <!-- 学生显示的界面 -->
      <div v-if="theRole === 'ROLE_guideTeacher'">
        <h1>指导教师填写部分</h1>
        <el-row>
          <el-col :span="12" :offset="6">
            <el-row>
               <el-col :span="6" style="text-align:left">学生进度完成情况</el-col>
                <el-col :span="18"  style="text-align:left">
                      <el-radio-group v-model="processInfo">
                        <el-radio label="1">提前完成</el-radio>
                        <el-radio label="2">按照计划完成</el-radio>
                        <el-radio label="3">延期完成</el-radio>
                        <el-radio label="4">没有完成</el-radio>
                      </el-radio-group>
                </el-col>
            </el-row>
              

              <el-row style="marigin-top:10px" >
               <el-col :span="6" style="text-align:left" >学生态度</el-col>
                <el-col :span="18" style="text-align:left">
                 
                  <el-radio-group v-model="stuAttitude">
                    <el-radio label="1">非常认真</el-radio>
                    <el-radio label="2">较认真</el-radio>
                    <el-radio label="3">一般</el-radio>
                    <el-radio label="4">不认真</el-radio>
                  </el-radio-group>
                </el-col>
              </el-row>

              <el-row style="marigin-top:10px">
                 <el-col :span="6" style="text-align:left">质量评价</el-col>
                <el-col :span="18"  style="text-align:left">
                    <el-radio-group v-model="qualityEst">
                      <el-radio label="1">非常认真</el-radio>
                      <el-radio label="2">较认真</el-radio>
                      <el-radio label="3">一般</el-radio>
                      <el-radio label="4">不认真</el-radio>
                    </el-radio-group>
                </el-col>
              </el-row>

            <el-row style="margin-top:20px">
              <el-col :span="6" style="margin-top:13px;text-align:left">存在问题及建议</el-col>
              <el-col :span="18">
                <el-input v-model="qa" placeholder="存在问题和建议" type="textarea"></el-input>
              </el-col>
            </el-row>

            <el-row style="margin-top:20px">
              <el-col :span="6" style="text-align:left">检查结果</el-col>
              <el-col :span="18" style="text-align:left">
                <el-radio-group v-model="checkResult">
                  <el-radio label="1">合格</el-radio>
                  <el-radio label="2">警告</el-radio>
                  <el-radio label="3">不予通过</el-radio>
                </el-radio-group>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
       <br>
        <el-button type="primary" @click="teacherClaim">提交</el-button>
      </div>
      <!-- 院长部分！!! -->
      <div v-else-if="theRole === 'ROLE_dean'">



    <el-table
      :data="tableData6"
      :span-method="objectSpanMethod"
      border
      style="width: 100%; margin-top: 20px">
      <el-table-column
        prop="id"
        label="评价指标"
        width="200">
        
      </el-table-column>
      <el-table-column
        prop="name"
        label="评价指标"
        width="300">
      </el-table-column>
      <el-table-column
        prop="amount1"
        label="评价要素"
        width="360">
      </el-table-column>
      <el-table-column
        prop="amount2"
        label="分值"
        width="180">
      </el-table-column>
      <el-table-column
       width="180"
        label="得分">
         <template slot-scope="scope">
           <div v-if="scope.row.id==='选题意义'"> <el-input v-model="scope.row.amount3" placeholder="请输入得分">
          </el-input></div>
          <div v-else>
             <el-select v-model="scope.row.score" clearable placeholder="请选择">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
          </div>

          
           </template>
        
      </el-table-column>
    </el-table>
    <br>
         <el-button type="primary" @click="claim">提交</el-button>


      </div>
      <div v-else-if="theRole === 'ROLE_student'">
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
      <div v-else>当前角色h还未指派此流程中的任务</div>
      
  </div>
</template>
<script>
export default {
  data(){
    return {
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
       approvItem:'',
        itemTableData:'',
        score:'',
        value4:'',
        options: [{
          value: '1',
          label: '优秀'
        }, {
          value: '2',
          label: '良好'
        }, {
          value: '3',
          label: '一般'
        }, {
          value: '4',
          label: '较差'
        }],

        tableData6: [{
          id: '选题意义',
          name: '论文选题的理论或实践意义，以及论文对本论题及相关问题的综述',
          amount1: '选题得当；有学术意义或实践意义；论题明确、一目了然；论文在引言或者沿革部分对论题的观点进行概述',
          amount2: 10,
          amount3: ''
        }, {
          id: '选题意义',
          name: '论文选题的理论或实践意义，以及论文对本论题及相关问题的综述',
          amount1: '选题得当；有学术意义或实践意义；论题明确、一目了然；论文在引言或者沿革部分对论题的观点进行概述',
          amount2: 10,
           amount3: ''
          
        }, {
          id: '文献掌握程度',
          name: '论文参考文献（论文、论著）的丰富程度',
          amount1: '标准为：优＝10篇以上；良＝7－9篇；合格＝4－6篇；差＝3篇以下。评价时可以视论文对同一作者的同类著述的引用情况多少决定。',
          amount2: 15,
          amount3: ''
        }, {
          id: '引用文献可靠',
          name: '论文参考文献（资料）来源的可靠性',
          amount1: '根据论文有关资料数据的运用和学术观点等引用情况判断。标准为：优＝真实，有引注；良＝真实，但引注大多为转引；合格＝真实，但无引注；差＝来源不明',
          amount2: 10,
          amount3: '',
        }, {
          id: '基本观点正确',
          name: '论文基本观点的正确性、新颖性、独特性',
          amount1: '标准为：优＝独特、正确；良＝独特、基本正确；合格＝无错误；差＝不正确',
          amount2: 20,
           amount3: '',
        }]
    }
   
  },
  filters:{
    estimationLevel(val){
      if(val === '1'){
        return "优秀"
      }else if(val === '2'){
         return "优秀"
      }else if(val === '3'){
         return "一般"
      }else if(val === '4'){
         return "较差"
      }

    },
    filterResult(val){
      if(val == '1'){
        return "通过"
      }else if(val == '2'){
        return "警告"
      } else if(val == '3'){
        return "不通过"
      }else {
        return "什么玩意，赶紧回去改"
      }


    }
  },
  mounted(){
    this.getTaskInfo();
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
                 // alert("开始实例成功！")
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
      
    },
     objectSpanMethod({ row, column, rowIndex, columnIndex }) {
        if (columnIndex === 0) {
          if (rowIndex % 2 === 0) {
            return {
              rowspan: 2,
              colspan: 1
            };
          } else {
            return {
              rowspan: 0,
              colspan: 0
            };
          }
        }
      },
    claim(){
       this.postRequest('/system/basic/claim', {
          'processInfo': this.processInfo,
          'qualityEst': this.qualityEst,
          'stuAttitude': this.stuAttitude,
          'qa': this.qa,
          'checkResult': this.checkResult
        }).then(resp=> {
                if (resp && resp.status == 200) {
                   this.$message({
                      message: '院长审核通过！',
                      type: 'success'
                    });
               
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
    },
     teacherClaim(){
       this.postRequest('/system/basic/claim', {
          'processInfo': this.processInfo,
          'qualityEst': this.qualityEst,
          'stuAttitude': this.stuAttitude,
          'qa': this.qa,
          'checkResult': this.checkResult
        }).then(resp=> {
                if (resp && resp.status == 200) {
                  this.$message({
                    message:'教师审核已完成',
                    type: 'success'
                  })
               
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
    },
    studentClaim(){
       this.postRequest('/system/basic/claim', {
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
                  })
             
                }
              }).catch(reson=>{
                alert("reget menu error"+reson)
              })
    },
    submitThesis(){},
    getTaskInfo(){
      this.getRequest('/system/basic/taskInfo').then(resp=>{
        if(resp && resp.status===200){
           this.stuInfo = resp.data[0].variables;
          // if(this.$store.state.user.roles[0].name==='ROLE_guideTeacher'){
           
          // }else if(this.$store.state.user.roles[0].name == 'ROLE_dean'){
          //    this.teacherEvaluation=resp.data[0].variables;
          //    alert(this.teacherEvaluation)
          // }
         
        
        }
      }).catch(reson=>{
        alert("error and reson is"`${reson}`)
      })
    }
  }
}
</script>
