import {ref} from 'vue';

const startDate = ref('');
const endDate = ref('');
const categoryId = ref('');
const searchText = ref('');
const page = ref('');

const params = new URLSearchParams(location.search);

/**
 * QueryString 값을 해당 값들에 주입한다.
 */
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
    if (params.has('searchText')) {
        searchText.value = params.get('searchText');
    }
    if (params.has('categoryId')) {
        categoryId.value = params.get('categoryId');
    }
    if (params.has('page')) {
        page.value = params.get('page');
    } else {
        page.value = '0';
    }
}

/**
 * yyyy-MM-dd 형태의 String으로 만들어 반환한다.
 * @param date Date 객체
 * @returns {string}
 */
const makeDate = (date) => {
    const yyyy = date.getFullYear();
    const mm = date.getMonth() + 1 > 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1)
    const dd = date.getDate() > 9 ? date.getDate() : '0' + date.getDate()
    return yyyy + '-' + mm + '-' + dd
}

conditionInit();

export const condition = ref({
    startDate: startDate.value,
    endDate: endDate.value,
    categoryId: categoryId.value,
    searchText: searchText.value,
    page: page.value,
})