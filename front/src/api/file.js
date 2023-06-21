import axios from "axios";

function createInstance(baseURL, options) {
    return axios.create(Object.assign({baseURL: baseURL}, options));
}

export const getDownload = (fileName, boardId) => {
    const instance = createInstance("http://localhost:8080");
    return instance.get(`/file/download/${boardId}/${fileName}`)
}