<template>
  <div>
    <h1>
      查看所有process 实例流程
      //processDefinitionId
      deploymentKey "thesisapproval"
      state "running"
      sort "created-desc"
      page 0
      size
      返回的数据为：
      {{instanceData}}
    </h1>
  </div>
</template>
<script>
export default {
  data(){
    return {
      instanceData:''
    }
  },
  mounted(){
    this.getProcessInstance();
  },
  methods:{
    getProcessInstance(){
      // get all process-instance 
    this.postRequest('/system/basic/query/process-instances', {
          deploymentKey: 'thesisapproval',
          state: 'running',
          sort: 'created-desc',
          page:0
        }).then(resp=> {
          if (resp && resp.status == 200) {
            this.instanceData = resp.data;
          
           
          }
        });

    }

  }
}
</script>

