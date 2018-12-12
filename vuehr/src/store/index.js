import Vue from 'vue'
import Vuex from 'vuex'
import bartData from './bartData'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    bartData
  }
})

export default store
