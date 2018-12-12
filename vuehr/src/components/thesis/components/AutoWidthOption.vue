<template>
  <div style="display:inline-block;maigin-left:0px">
    <el-row>
      <el-col el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12">
    <label class="radio-label">是否自动调整宽度: </label>
    </el-col>
     <el-col el-col :xs="12" :sm="12" :md="12" :lg="12" :xl="12">
    <el-radio-group v-model="autoWidth">
      <el-radio :label="true" border>是</el-radio>
      <el-radio :label="false" border>否</el-radio>
    </el-radio-group>
     </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  props: {
    value: {
      type: Boolean,
      default: true
    }
  },
  computed: {
    autoWidth: {
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
