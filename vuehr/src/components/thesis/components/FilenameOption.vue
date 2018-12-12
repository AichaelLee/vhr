<template>
  <div style="display:inline-block;">
 <el-row>
    <el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12">  <label class="radio-label" style="padding-left:0;">导出文件名: </label></el-col>
      <el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12">  <el-input placeholder="文件名" v-model="filename"  prefix-icon="el-icon-document"/></el-col>
 </el-row>
    
   
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    filename: {
      get() {
        return this.value
      },
      set(val) {
        this.$emit('input', val)
      }
    }
  }
}
</script>
