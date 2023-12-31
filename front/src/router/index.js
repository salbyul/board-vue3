import {createRouter, createWebHistory} from 'vue-router'
import ListView from '../views/ListView.vue'
import DetailView from '../views/DetailView.vue'
import CreateView from '../views/CreateView.vue'
import ModifyView from '../views/ModifyView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'list',
            component: ListView
        },
        {
            path: '/board/:id',
            name: 'detail',
            component: DetailView
        },
        {
            path: '/create',
            name: 'create',
            component: CreateView
        }, {
            path: '/board/modify/:id',
            name: 'modify',
            component: ModifyView
        }
    ]
})

export default router
