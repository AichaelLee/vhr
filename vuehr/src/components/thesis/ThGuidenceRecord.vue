<template>
  <div>

    <div>
      <FilenameOption v-model="filename" />
      <AutoWidthOption v-model="autoWidth" />
      <BookTypeOption v-model="bookType" />
      <el-button  :loading="downloadLoading" style="margin:0 0 20px 20px;" type="primary" icon="document" @click="handleDownload">导出文件</el-button>
    </div>
    <br>
    <!-- 表格 -->
    <el-row>
      <el-col :span="12" :offset="6">
         <el-table v-loading="listLoading" :data="list" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="Id" width="95">
        <template slot-scope="scope">
          {{ scope.row.id}}
        </template>
      </el-table-column>
       
      <el-table-column label="年龄" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.age }}
        </template>
      </el-table-column>

       <el-table-column label="性别" width="110" align="center">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.gender=='1'?'男':'女'}}</el-tag>
        </template>
      </el-table-column>
           <el-table-column label="未爆气球数" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.unBoomedNum }}
        </template>
      </el-table-column>

        <el-table-column label="总收益" width="110" align="center">
        <template slot-scope="scope">
          {{ scope.row.sumScore }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="Readings" width="115" align="center">
        <template slot-scope="scope">
          {{ scope.row.pageviews }}
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="日期" width="290">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          <span>{{ scope.row.timestamp}}</span>
        </template>
      </el-table-column>
    </el-table>
      </el-col>
    </el-row>
   
  </div>
</template>

<script>
import { parseTime } from '@/utils/index'

// options components
import FilenameOption from './components/FilenameOption'
import AutoWidthOption from './components/AutoWidthOption'
import BookTypeOption from './components/BookTypeOption'

export default {
  name: 'ExportExcel',
  components: { FilenameOption, AutoWidthOption, BookTypeOption },
  computed:{
    bartInfo(){
      
        return this.$store.state.bartData;
      }
     
  },
  data() {
    return {
      // list: [{ id: 1, name:'张三',age: '24', gender: '男', sumScore:'80' ,timestamp: '2016-05-04'},
      //   { id: 2,name:'李四',age: '24', gender: '女', sumScore:'90', timestamp: '2016-05-04' }],
      list:[],
      listLoading: true,
      downloadLoading: false,
      filename: '',
      autoWidth: true,
      bookType: 'xlsx'
    }
  },
  created() {
    this.fetchData();
    this.create_websql();
    
    
  },
  filters: {
  parseTimef: function (value) {
    return parseTime(value)
  },
  pargeGender(value){
    alert(value)
    return value===1?value='男':value='女';
  }
},
  methods: {
  
 create_websql() {
        //
          this.db = openDatabase('bartdb', '1.0', 'bartdb DB', 2 * 1024 * 1024);
          let _this = this;
          this.db.transaction(function (tx) {
              tx.executeSql('SELECT * FROM BART', [], function (tx, results) {
                  var len = results.rows.length, i;
                  let msg = "<p>查询记录条数: " + len + "</p>";
                  for (i = 0; i < len; i++){
                    //alert(results.rows.item(i).id );
                    _this.list.push(results.rows.item(i))
                  
                  }
              }, null);
            });
      },
    fetchData() {
      this.listLoading = true
      this.listLoading = false
     
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['Id', '年龄', '性别', '未爆炸气球数','总收益','日期']
        const filterVal = ['id', 'age', 'gender','unBoomedNum','sumScore', 'timestamp']
        const list = this.list
        const data = this.formatJson(filterVal, list)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: this.filename,
          autoWidth: this.autoWidth,
          bookType: this.bookType
        })
        this.downloadLoading = false
      })
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>

<style>
.radio-label {
  font-size: 14px;
  color: #606266;
  line-height: 40px;
  padding: 0 12px 0 30px;
}
</style>

