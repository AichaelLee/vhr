import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Role from '@/components/Role'
import Bart from '@/components/Bart'
import Home from '@/components/Home'
// import EmpAdv from '@/components/emp/EmpAdv'
// import EmpBasic from '@/components/emp/EmpBasic'
// import PerEc from '@/components/personnel/PerEc'
// import PerEmp from '@/components/personnel/PerEmp'
// import PerMv from '@/components/personnel/PerMv'
// import PerSalary from '@/components/personnel/PerSalary'
// import PerTrain from '@/components/personnel/PerTrain'
// import SalMonth from '@/components/salary/SalMonth'
// import SalSearch from '@/components/salary/SalSearch'
// import SalSob from '@/components/salary/SalSob'
// import SalSobCfg from '@/components/salary/SalSobCfg'
// import SalTable from '@/components/salary/SalTable'
// import StaAll from '@/components/statistics/StaAll'
// import StaPers from '@/components/statistics/StaPers'
// import StaRecord from '@/components/statistics/StaRecord'
// import StaScore from '@/components/statistics/StaScore'
// import SysBasic from '@/components/system/SysBasic'
// import SysCfg from '@/components/system/SysCfg'
// import SysData from '@/components/system/SysData'
// import SysHr from '@/components/system/SysHr'
// import SysInit from '@/components/system/SysInit'
// import SysLog from '@/components/system/SysLog'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
      hidden: true
    }, 
    {
      path: '/role',
      name: 'Role',
      component: Role,
      hidden: true
    }, {
      path: '/home',
      name: '主页',
      component: Home,
      hidden: true,
      meta: {
        requireAuth: true
      }
    },
    {
      path: '/bart',
      name: 'Bart',
      component: Bart,
      hidden: true
    }
  ]
})
