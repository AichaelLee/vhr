const bartData = {
    state: {
      bartInfo: []
    },
    mutations: {
      ADD_DATA: (state, data) => {
          
        state.bartInfo.push(data)
        alert(state.bartInfo[0].id)
      }
    },
    actions: {
     
    }
  }
  
  export default bartData
  