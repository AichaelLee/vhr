<template>
  <el-form :rules="rules" class="login-container" label-position="left"
           label-width="0px" v-loading="loading">
    <h3 class="login_title">系统登录</h3>
    <el-form-item prop="account">
      <el-input type="text" v-model="loginForm.username"
                auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>
    <el-form-item prop="checkPass">
      <el-input type="password" v-model="loginForm.password"
                auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>
      <el-radio-group v-model="loginForm.userType">
    <el-radio label="1">管理员</el-radio>
    <el-radio label="2">教师</el-radio>
    <el-radio label="3">学生</el-radio>
  </el-radio-group>
    <!-- <el-checkbox class="login_remember" v-model="checked"
                 label-position="left">记住密码</el-checkbox> -->
                 <br>
    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 100%" @click="submitClick">登录</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
  export default{
    data(){
      return {
        rules: {
          account: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          checkPass: [{required: true, message: '请输入密码', trigger: 'blur'}]
        },
        checked: true,
        loginForm: {
          username: 'teacher',
          password: '123',
          userType: '2'
        },
        loading: false
        
      }
    },
    methods: {
      submitClick () {
        var _this = this;
        this.loading = true;
        this.postRequest('/login', {
          username: this.loginForm.username,
          password: this.loginForm.password,
          userType: this.loginForm.userType
        }).then(resp=> {
          _this.loading = false;
          if (resp && resp.status == 200) {
            var data = resp.data;
           // _this.$store.commit('login', data.obj);
            var path = _this.$route.query.redirect;
            // _this.$router
            //   .replace({path: path == '/' || path == undefined ? '/Role' : path});
            if(this.loginForm.userType==="2"){
               _this.$router
              .replace({path: '/Role'});
            }else {
            
               _this.$router
              .push({path: '/home'});
            }
           
          }
        });
      }
    }
  }
</script>
<style>
  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }
  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }
  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }
</style>
