import axios from "axios";

function createInstance(baseURL, options) {
    return axios.create(Object.assign({baseURL: baseURL}, options));
}

export const http = createInstance("http://localhost:8080");

/**
 * 요청에 성공하였을 경우 data를 바로 반환한다.
 */
http.interceptors.response.use(
    (response) => {
        return response.data
    },
    (error) => {
        return Promise.reject(error)
    }
)