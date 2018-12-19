<template>
 <div>
   
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
           <div v-if="scope.row.id==='选题意义'"> <el-input v-model="score" placeholder="请输入得分">
          </el-input></div>
          <div v-else>
             <el-select v-model="value4" clearable placeholder="请选择">
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
  </div>
</template>
<script>
  export default {
    data() {
      return {
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
          amount3: 8
        }, {
          id: '选题意义',
          name: '论文选题的理论或实践意义，以及论文对本论题及相关问题的综述',
          amount1: '选题得当；有学术意义或实践意义；论题明确、一目了然；论文在引言或者沿革部分对论题的观点进行概述',
          amount2: 10,
          
        }, {
          id: '文献掌握程度',
          name: '论文参考文献（论文、论著）的丰富程度',
          amount1: '标准为：优＝10篇以上；良＝7－9篇；合格＝4－6篇；差＝3篇以下。评价时可以视论文对同一作者的同类著述的引用情况多少决定。',
          amount2: 15,
          amount3: 9
        }, {
          id: '引用文献可靠',
          name: '论文参考文献（资料）来源的可靠性',
          amount1: '根据论文有关资料数据的运用和学术观点等引用情况判断。标准为：优＝真实，有引注；良＝真实，但引注大多为转引；合格＝真实，但无引注；差＝来源不明',
          amount2: 10,
          amount3: 8
        }, {
          id: '基本观点正确',
          name: '论文基本观点的正确性、新颖性、独特性',
          amount1: '标准为：优＝独特、正确；良＝独特、基本正确；合格＝无错误；差＝不正确',
          amount2: 20,
          amount3: 15
        }]
      };
    },
    mounted(){
      this.gettaskInfo()
      this.getTableData()
    },
    methods: {

      getTableData(){
        this.getRequest('/system/basic/tableData').then(resp=>{
          if(resp && resp.status === 200){
            this.approvItem = resp.data.children;
          }else{
            alert("error")
          }
        })
      },
      gettaskInfo(){
        this.getRequest('/system/basic/taskInfo').then(resp=>{
          if(resp && resp.status === 200){
            alert(resp.data);
          }else{
            alert("error")
          }
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
      }
    }
  };
</script>
