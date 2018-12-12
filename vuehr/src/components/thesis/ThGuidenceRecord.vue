<template>
  <div>

    <el-row>
      <el-col :xs="24" :sm="24" :md="6" :lg="6" :xl="6"  style="text-aligin:left"><FilenameOption v-model="filename" /></el-col>
      
      <el-col :xs="24"  :sm="24" :md="6" :lg="6" :xl="6"><AutoWidthOption v-model="autoWidth" /></el-col>
     <el-col :xs="24"  :sm="24" :md="4" :lg="4" :xl="4"> <BookTypeOption v-model="bookType" /></el-col>
      <el-col :xs="24"  :sm="24" :md="4" :lg="4" :xl="4"><el-button  :loading="downloadLoading" style="margin:0 0 20px 20px;" type="primary" icon="document" @click="handleDownload">导出文件</el-button></el-col>
    </el-row>
    <br>
    <!-- 表格 -->
    <el-row>
      <el-col :sm="4" :md="4" :lg="4" :xl="4"  class="hidden-sm-and-down" >&nbsp;</el-col>
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
         <el-table v-loading="listLoading" :data="tableDataView" element-loading-text="拼命加载中" border fit highlight-current-row 
         style="width: 930px;"

         :default-sort = "{prop: 'id', order: 'descending',prop:'age',order:'descending',prop:'unba',order:'descending',prop:'sumba',order:'descending'}"
         >
      <el-table-column align="center"  prop="id" label="Id" sortable width="110">
        <template slot-scope="scope">
          {{ scope.row.id}}
        </template>
      </el-table-column>
       
      <el-table-column label="年龄" prop="age" width="110" sortable align="center">
        <template slot-scope="scope">
          {{ scope.row.age }}
        </template>
      </el-table-column>

       <el-table-column label="性别" width="110" sortable align="center">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.gender=='1'?'男':'女'}}</el-tag>
        </template>
      </el-table-column>
           <el-table-column prop="unba" label="未爆气球数" sortable width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.unBoomedNum }}
        </template>
      </el-table-column>

        <el-table-column prop="sumba" label="总收益" width="110" sortable align="center">
        <template slot-scope="scope">
          {{ scope.row.sumScore }}
        </template>
      </el-table-column>
      <!-- <el-table-column label="Readings" width="115" align="center">
        <template slot-scope="scope">
          {{ scope.row.pageviews }}
        </template>
      </el-table-column> -->
      <el-table-column align="center" label="日期" sortable width="200">
        <template slot-scope="scope">
          <i class="el-icon-time"/>
          <span>{{ scope.row.timestamp | parseTimestamp }}</span>
        </template>
      </el-table-column>
             <el-table-column label="操作" width="140" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" plain  @click="handleDelete(scope.$index, scope.row)"><i class="fa fa-trash-o"></i></el-button>
          </template>
          </el-table-column>
    </el-table>
      </el-col>

    </el-row>
          <el-row >
        <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24"  style="margin-top:10px">
          <el-pagination background  @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagenum"
          :page-sizes="[5,10,15,20,25]"
          :page-size="pageSize"
          :layout="pagelayout"
          :total="tableSize">
        </el-pagination>
        </el-col>
      </el-row>
   
  </div>
</template>

<script>
import { parseTime } from '@/utils/index'
import 'element-ui/lib/theme-chalk/display.css';
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
      },
            // 页数小于2的时候去掉jumper
      pagelayout: function() {
        let sumpagenum = (this.tableSize / this.pageSize) + 1
        if ((this.tableSize % this.pageSize) === 0) {
          sumpagenum = (this.tableSize / this.pageSize)
        }
        if (sumpagenum < 2) {
          return 'total, sizes, prev, pager, next'
        } else {
          return 'total, sizes, prev, pager, next, jumper'
        }
      },
          // 分页的筛选
      tableDataView: function() {
        return this.list.slice((this.pagenum - 1) * this.pageSize, this.pagenum * this.pageSize)
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
      bookType: 'xlsx',
       // 当前第几页
        pagenum: 1,
        // 每页条数
        pageSize: 5,
        tableSize:5
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
  parseTimestamp(val) {
          if (val != null) {
                  var date = new Date(val);
                  return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
              }
      },
  pargeGender(value){
    alert(value)
    return value===1?value='男':value='女';
  }
},
  methods: {
      // 改变每页条数
      handleSizeChange(val) {
        this.pageSize = val
      },
        // 计算开始数和结束的数
      handleCurrentChange(val) {
        this.pagenum = val
      },
  
      create_websql() {
        //
          this.db = openDatabase('bartdb', '1.0', 'bartdb DB', 2 * 1024 * 1024);
          let _this = this;
          _this.list=[];
          this.db.transaction(function (tx) {
              tx.executeSql('SELECT * FROM BART', [], function (tx, results) {
                  var len = results.rows.length,i;
                  _this.tableSize = len
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
          // table数据的删除
      handleDelete(index, row) {
        this.$confirm('请问要删除此条数据吗', '注意', {
          confirmButtonText: '是',
          cancelButtonText: '否',
          type: 'warning'
        }).then(() => {
             let _this = this
            this.db = openDatabase('bartdb', '1.0', 'bartdb DB', 2 * 1024 * 1024);
            this.db.transaction(function (tx) {
            tx.executeSql('DELETE FROM BART WHERE id=?',[row.id]);
            
             });
            this.create_websql();
        }).catch(() => {})
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

<style lang="scss">
.radio-label {
  font-size: 14px;
  color: #606266;
  line-height: 40px;
  padding: 0 12px 0 30px;
}

</style>

