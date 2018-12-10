<template>
  <div>
    <el-row>
       <!-- 信息栏 begin -->
      <el-row :gutter="10">
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
        <el-col :offset="3" :span="4">
            <el-card class="box-card" style="height:38px;padding-top:0px" >
            受试者信息管理
          </el-card>
        </el-col>
        <el-col :span="3">
           
           <el-button type="info"  @click="openNotice" plain round>信息按钮</el-button>
        </el-col>
        
      <el-col :offset="1" :span="4">
        <el-form-item  prop="name">
        <el-input
          placeholder="姓名"
          v-model="ruleForm.name"
          clearable>
        </el-input>
        </el-form-item>
        </el-col>
        <el-col :span="2">
           <el-form-item  prop="gender">
            <el-select v-model="ruleForm.gender" clearable placeholder="性别">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
           </el-form-item>
        </el-col>
        <el-col :span="2">
           <el-form-item  prop="age">
          <el-input
          placeholder="年龄"
          v-model.number="ruleForm.age"
          clearable>
        </el-input>
           </el-form-item>
        </el-col>
        <el-col :span="3">
           <el-button type="primary" plain @click="saveInfo('ruleForm')" :disabled="infoDisabled">开始</el-button>
        </el-col>
        </el-form>
    </el-row>
      <!-- 信息栏 end -->
      <br><br>
    </el-row>

    <!-- 中间部分 -->
    <el-row>
      <!-- 奖励等级 -->
      <el-col :offset="2" :span="6">
        <el-row>
          <el-col :span="12">特等奖励 - - -></el-col>
          <el-col :span="12" class="colHeight">
            <el-card class="box-card1">
            </el-card>
          </el-col>
        </el-row>
          <el-row>
              <el-col :span="12">大奖励 - - -></el-col>
              <el-col :span="12" class="colHeight">
                 <el-card class="box-card2">
                 </el-card>
              </el-col>
        </el-row>
            <el-row>
              <el-col :span="12">中奖励- - -></el-col>
              <el-col :span="12" class="colHeight">
            <el-card class="box-card3">
            </el-card>
          </el-col>
        </el-row>
                <el-row>
                  <el-col :span="12">小奖励- - -></el-col>
                <el-col :span="12" class="colHeight">
                <el-card class="box-card4">
                </el-card>
             </el-col>
        </el-row>
        


      </el-col>
      <!-- 气球 -->
      <el-col :span="12" class="balloonPosition">
        <el-row>
          <el-col class="imgPosi" :span="24" :style="{ 'margin-top':magrintop+'px'}">
            <!-- <div id="div1" @click="clickBom"></div> -->
            <img src="./balloon.png" class="ballimg" :style="{ height: balloonHeight+'px', width: balloonWidth + 'px' }" @click="clickBalloon">
          </el-col>
        </el-row>
           
        
        </el-col>
      <!-- 信息 -->
      <el-col :span="3">
        <el-row>
          <el-col :span="24">
           <el-row>
             <el-col :span="8"><div><img src="./score.jpg" class="scoreSize"></div></el-col>
             <el-col :span="8" style="height:50px;padding-top:20px;color:red">{{initialScore}}</el-col>
           </el-row>
          </el-col>
        </el-row>
            <el-row style="margin-top:15px">
          <el-col :span="24">
            <el-row>
             <el-col :span="8"><div><img src="./score.jpg" class="scoreSize"></div></el-col>
             <el-col :span="8" style="height:50px;padding-top:20px;color:red">{{sumScore}}</el-col>
           </el-row>
          </el-col>
        </el-row>
            <el-row>
          <el-col :span="16" class="leftTime" style="margin-top:30px">
            游戏还剩<span class="singleFont">{{leftTime}}</span>局
          </el-col>
        </el-row>
        <el-row style="margin-top:30px">
          <el-col :span="16">
            <el-button type="primary" @click="getReward" plain :disabled="ifDisabled">获得奖励</el-button>
          </el-col>
        </el-row>
        
      </el-col>
    </el-row>
   
    
    <!-- <el-row>
      <el-col :xs="8" :sm="8" :md="8" :lg="8" :xl="8">总分:</el-col>
      <el-col :offset="8" :xs="8" :sm="8" :md="8" :lg="8" :xl="8"> {{sumScore}}分</el-col></el-row>
     <el-row>
       <el-col :xs="8" :sm="8" :md="8" :lg="8" :xl="8">第{{bumNum}}个气球</el-col>
       <el-col :offset="8" :xs="8" :sm="8" :md="8" :lg="8" :xl="8">充了{{times}}下气</el-col>
       </el-row>
       < 这里是气球
       <el-row>
         <div id="div1"></div>
       </el-row>
</el-row> -->
  </div>
</template>
<script>
export default {
   mounted(){
    this.over = this.RandomNum()
  },
    data () {
    return {
      ruleForm:{
        name:'',
        gender:'',
        age:''
      },
      name:'',
      times: 0,
      initialScore :0,
      sumScore: 0,
      bumNum:0,
       options: [{
          value: '1',
          label: '男'
        }, {
          value: '2',
          label: '女'
        }
        ],
        value4: '',
        age:'',
        leftTime:30,
        // 气球的高度和宽度
        balloonHeight:40,
        balloonWidth:40,
        magrintop:350,
        infoDone:false,
        over:'',
        rules: {
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' },
          ],
          gender: [
            { required: true, message: '请选择性别', trigger: 'change' }
          ],
          age: [
            { required: true, message: '年龄不能为空'},
            { type: 'number', message: '年龄必须为数字值'}
          ]
    }

    }
  },
    
  computed:{
    ifDisabled(){
      return this.initialScore===0;
    },
    infoDisabled(){
      return this.infoDone
    },
    balloonSize(){
      return{
        'height':`${this.balloonHeight}px`,
        'width' :`${this.balloonWidth}px`
      }

      
    }
  },
 
  methods:{
    openNotice(){
           this.$alert('气球模拟决策任务的实验，.......', '气球模拟决策任务', {
          confirmButtonText: '确定',
          callback: action => {
            this.$message({
              type: 'success',
              message: '已经知悉'
            });
          }
        });
    },
    saveInfo(formName){
      this.$refs[formName].validate((valid) => {
          if (valid) {
            this.infoDone = true;
            this.$message({
          message: '信息录入成功，请您开始测试',
          type: 'success'
        });
          } else {
            console.log('error submit!!');
            return false;
          }
        });
  
    },
    clickBalloon(){
      if(this.infoDone){
         if(this.leftTime>0){
         this.magrintop-=10;
        this.balloonWidth+=10;
        this.balloonHeight+=10;
        this.initialScore+=1;

            if(this.initialScore<=this.over){
              // 未爆炸
            }else{
              // 已经爆炸了
              //恢复气球到初始大小
               this.balloonWidth=40;
               this.balloonHeight=40;
               this.magrintop=350;

               // 重置本次得分
              this.initialScore = 0;
              this.hadBoomed();
              this.leftTime-=1;
              this.over = this.RandomNum()

            }
      }

      }
      else{
        this.$message.error('请您输入个人信息!');
      }
     
    
    },
     // 获得奖励
    getReward(){
      //操作建立在剩余测试数大于0的情况
      if(this.leftTime>0){
          // 增加总分数
          this.sumScore += this.initialScore;
          // 把本次得分改为0
          this.initialScore = 0;
          // 测试次数减少1
          this.leftTime -= 1;
          // 恢复原始大小
          this.balloonWidth=40;
          this.balloonHeight=40;
          this.magrintop=350;

      }
     

    },
    hadBoomed() {
        this.$message({
          showClose: true,
          message: '气球爆炸啦！',
          center: true,
          type: 'warning',
          duration:'2000'
        });
      },
      RandomNum() {
            let Min = 0;
            let Max = 30;
             var Range = Max - Min;
             var Rand = Math.random();
             if(Math.round(Rand * Range)==0){             
                 return Min + 1;
             }
             var num = Min + Math.round(Rand * Range);
             return num;
       }
  }
}
</script>

<style type="text/css">
.imgPosi{
  margin: auto
}
.el-card__body{
  padding:7px;
}
.scoreSize{
  height: 50px;
  width: 40px;
}

.leftTime{
  color:rgb(6, 30, 136);
  font-size:110%
}
.singleFont{
  color: rgb(179, 20, 20) !important;
  padding:5px;
  font-size: 120%;
}
.balloonPosition{
  text-align:center
}
/* .ballimg {
  position: absolute;
  left: 30px;
  top: 20px;
  bottom: 0px;
} */
        #div1{
          
            width:100px;
            height:100px;
            border-radius: 50%;
            background-color: skyblue;
           /* background-image: url('qq.jpg');*/
        }
        .proHeight{
          height: 120px;
          width: 70px;
          margin:0px;
        }
        .colHeight{
          height: 100px !important;
          width: 35px !important;
        }
        .box-card1{
          background-color: rgb(141, 3, 3);
          height: 100%;
      
        }
         .box-card2{
          background-color: rgb(92, 21, 139);
          height: 100%;
        }
         .box-card3{
          background-color: rgb(36, 46, 184);
          height: 100%;
        } 
        .box-card4{
          background-color: rgb(18, 105, 145);
          height: 100%;
        }
    </style>

