import {ref} from 'vue';

const startDate = ref('');
const endDate = ref('');
const categoryId = ref('');
const searchText = ref('');
const page = ref('');
const limit = ref('10');

const params = new URLSearchParams(location.search);

const conditionInit = () => {
    if (params.has('startDate')) {
        startDate.value = params.get('startDate');
    } else {
        const newStartDate = new Date();
        newStartDate.setFullYear(newStartDate.getFullYear() - 1);
        startDate.value = makeDate(newStartDate);
    }
    if (params.has('endDate')) {
        endDate.value = params.get('endDate');
    } else {
        const newEndDate = new Date();
        newEndDate.setDate(newEndDate.getDate() + 1);
        endDate.value = makeDate(newEndDate);
    }
    if (params.has('search')) {
        searchText.value = params.get('search');
    }
    if (params.has('category')) {
        categoryId.value = params.get('category');
    }
    if (params.has('page')) {
        page.value = params.get('page');
    } else {
        page.value = '0';
    }
}

const makeDate = (date) => {
    const yyyy = date.getFullYear();
    const mm = date.getMonth() + 1 > 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
    const dd = date.getDate() > 9 ? date.getDate() : '0' + date.getDate()
    return yyyy + '-' + mm + '-' + dd
}

conditionInit();

export {startDate, endDate, categoryId, searchText, page, limit}