import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)
// TODO: 모든 로직에 주석 작성해야 함
app.use(router)

app.mount('#app')
