<template>
  <div>
    <el-row>
       <!-- 信息栏 begin -->
      <el-row :gutter="10">
        <el-col :span="4">
            <el-card class="box-card" style="height:38px;padding-top:0px" >
            硕士生毕设系统
          </el-card>
        </el-col>
        <el-col :span="3">
           <el-button type="info" plain round>信息按钮</el-button>
        </el-col>
        
      <el-col :offset="1" :span="4"><el-input
          placeholder="姓名"
          v-model="name"
          clearable>
        </el-input></el-col>
        <el-col :span="2">
            <el-select v-model="value4" clearable placeholder="性别">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2">
          <el-input
          placeholder="年龄"
          v-model="age"
          clearable>
        </el-input>
        </el-col>
        <el-col :span="3">
           <el-button type="primary" plain>开始</el-button>
        </el-col>
    </el-row>
      <!-- 信息栏 end -->
      <br><br>
    </el-row>

    <!-- 中间部分 -->
    <el-row>
      <!-- 奖励等级 -->
      <el-col :span="6">
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
        <el-row style="margin-top:300px;">
          <el-col :offset="8" :span="8" >
            <div id="div1" @click="clickBom"></div>
          </el-col>
        </el-row>
           
        
        </el-col>
      <!-- 信息 -->
      <el-col :span="6">
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
            测试还剩<span class="singleFont">{{leftTime}}</span>次
          </el-col>
        </el-row>
        <el-row style="margin-top:30px">
          <el-col :span="16">
            <el-button type="primary" plain>获得奖励</el-button>
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
    data () {
    return {
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
        leftTime:30

    }
  },
  computed:{
    // sumScore(){
    //   return this.initialNum
    // }
  },
  methods:{
    clickBom(){
      if(this.leftTime>0){
        this.initialScore+=1;
            this.sumScore+=1;
            let over = this.RandomNum();
            if(this.initialScore<over){
            
            }else{
              this.initialScore = 0;
              this.hadBoomed();
              this.leftTime-=1;

            }
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

