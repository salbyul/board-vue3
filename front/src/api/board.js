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