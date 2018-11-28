<template>
        <el-row>
          <el-col >
           <el-select v-model="value" placeholder="请选择" @change="changeRole">
              <el-option
                v-for="item in roles"
                :key="item.id"
                :label="item.nameZh"
                :value="item.nameZh">
              </el-option>
        </el-select>
          </el-col>
        </el-row>
</template>
<script>
  export default{
    mounted: function () {
      this.loadAllRoles()
    },
    methods: {
      changeRole(){
         var path = this.$route.query.redirect;
         //alert(path)
        // this.$router
        //       .push({path: path == '/' || path == undefined ? '/home' : path, params: {role: "value"}});
       // this.$router.push({name: '/home', params: {role: value}})
        this.$router.push({
            path: '/', 
            name: '主页',
            params: { 
                role: 'ro', 
                dataObj: "this.msg"
            }
        })
      },
      
      loadAllRoles(){
                this.getRequest("/system/basic/roles").then(resp=> {
        
          if (resp && resp.status == 200) {
             this.roles = resp.data;
          }
        })
      }
    },
    data(){
      return {
        roles:'',
        isDot: false,
        value: ''
      }
    },
    computed: {
    
    }
  }
</script>
<style>
</style>
