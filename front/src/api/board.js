import {http} from "./http";

export function getCategories() {
    return http.get("/categories");
}

export function getBoards(searchCondition) {
    return http.get("/board/boards", {params: searchCondition});
}

export function getBoardCounts(searchCondition) {
    return http.get("/board/counts", {params: searchCondition});
}

export function getBoardDetail(id) {
    return http.get(`/board/detail/${id}`);
}

export function saveBoard(formData) {
    return http.post('/board/create', formData)
}

export function deleteBoard(id, password) {
    return http.delete(`/board/delete/${id}?password=${password}`)
}
export function passwordCheck(id, password) {
    return http.get(`/board/password/${id}`, {params: {password}})
}

export function modifyBoard(id, form) {
    return http.put(`/board/modify/${id}`, form)
}