import {http} from "./http";

export function saveComment(comment) {
    return http.post("/comment/save", comment);
}
