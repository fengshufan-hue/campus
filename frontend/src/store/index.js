import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

function getStoredUser() {
  try {
    return JSON.parse(localStorage.getItem('user'))
  } catch (e) {
    localStorage.removeItem('user')
    return null
  }
}

const storedUser = getStoredUser()

export default new Vuex.Store({
  state: {
    user: storedUser,
    isLogin: !!storedUser
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      state.isLogin = true
      localStorage.setItem('user', JSON.stringify(user))
    },
    LOGOUT(state) {
      state.user = null
      state.isLogin = false
      localStorage.removeItem('user')
    }
  },
  actions: {
    login({ commit }, user) {
      commit('SET_USER', user)
    },
    logout({ commit }) {
      commit('LOGOUT')
    }
  }
})
