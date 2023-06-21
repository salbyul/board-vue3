import {ref} from "vue";

export const category = ref('');
export const writer = ref('')
export const title = ref('')
export const content = ref('')
export const password = ref('')
export const passwordVerify = ref('')
export const fileInput1 = ref('')
export const fileInput2 = ref('')
export const fileInput3 = ref('')
export const file1 = ref(null)
export const file2 = ref(null)
export const file3 = ref(null)

export const fileChange = (index, event) => {
    if (index === 1) {
        console.log(file1.value)
        fileInput1.value = event.target.files[0].name
        file1.value = event.target.files[0];
    } else if (index === 2) {
        fileInput2.value = event.target.files[0].name
        file2.value = event.target.files[0];
    } else if (index === 3) {
        fileInput3.value = event.target.files[0].name
        file3.value = event.target.files[0];
    }
}

export const verifyCreate = () => {
    if (!verifyCategory()) return false
    if (!verifyWriter()) return false
    if (!verifyPassword()) return false
    if (!verifyTitle()) return false
    if (!verifyContent()) return false
    return true
}

function verifyCategory() {
    console.log(category.value)
    if (category.value === '') {
        alert('카테고리를 선택해주세요.')
        return false
    }
    return true
}
function verifyWriter() {
    if (writer.value.length < 3 || writer.value.length >= 5) {
        alert('작성자는 3글자 이상, 5글자 미만이어야 합니다.')
        return false
    }
    return true
}

function verifyPassword() {
    if (password.value.length < 4 || password.value.length > 15) {
        alert('비밀번호는 4글자 이상, 16글자 미만이어야 합니다.')
        return false
    }
    if (password.value !== passwordVerify.value) {
        alert('비밀번호가 같지 않습니다.')
        return false
    }
    return true
}

function verifyTitle() {
    if (title.value.length < 4 || title.value.length > 99) {
        alert('제목은 4글자 이상, 100글자 미만이어야 합니다.')
        return false
    }
    return true
}

function verifyContent() {
    if (content.value.length < 4 || content.value.length > 1999) {
        alert('내용은 4글자 이상, 2000글자 미만이어야 합니다.')
        return false
    }
    return true
}

export const makeFormData = () => {
    const data = new FormData();

    const board = {
        categoryId: category.value,
        title: title.value,
        content: content.value,
        writer: writer.value,
        password: password.value
    }
    const boardJson = JSON.stringify(board);
    const blob = new Blob([boardJson], {type: 'application/json'})
    data.append('boardDTO', blob);

    const fileList = [file1, file2, file3];
    fileList.forEach((file) => {
        if (file.value !== null) {
            data.append("files", file.value);
        }
    })

    return data;
}