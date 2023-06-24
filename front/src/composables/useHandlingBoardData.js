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

/**
 * 입력된 데이터들을 초기화한다.
 */
export const init = () => {
    category.value = '';
    writer.value = '';
    title.value = '';
    content.value = '';
    password.value = '';
    passwordVerify.value = '';
    fileInput1.value = '';
    fileInput2.value = '';
    fileInput3.value = '';
    file1.value = null;
    file2.value = null;
    file3.value = null;
}

/**
 * 입력된 파일의 이름을 input에 출력하고, 파일의 값을 변경한다.
 * @param index 파일을 구분한 index
 * @param event
 */
export const fileChange = (index, event) => {
    console.log(index)
    if (index === 1) {
        fileInput1.value = event.target.files[0].name
        file1.value = event.target.files[0];
        console.log(file1.value)
    } else if (index === 2) {
        fileInput2.value = event.target.files[0].name
        file2.value = event.target.files[0];
        console.log(file2.value)
    } else if (index === 3) {
        fileInput3.value = event.target.files[0].name
        file3.value = event.target.files[0];
        console.log(file3.value)
    }
}

/**
 * 게시글 생성을 위한 유효성 검사 메소드
 * @returns {boolean}
 */
export const verifyCreate = () => {
    if (!verifyCategory()) return false
    if (!verifyWriter()) return false
    if (!verifyPassword()) return false
    if (!verifyTitle()) return false
    if (!verifyContent()) return false
    return true
}

/**
 * 게시글 수정을 위한 유효성 검사
 * @returns {boolean}
 */
export const verifyModify = () => {
    if (!verifyWriter()) return false;
    if (!verifyTitle()) return false;
    if (!verifyContent()) return false;
    return true;
}

/**
 * 카테고리 유효성 검사 메소드
 * 카테고리가 선택되었는지 확인한다.
 * @returns {boolean}
 */
function verifyCategory() {
    console.log(category.value)
    if (category.value === '') {
        alert('카테고리를 선택해주세요.')
        return false
    }
    return true
}

/**
 * 작성자 유효성 검사 메소드
 * 작성자의 길이 유효성 검사를 한다.
 * @returns {boolean}
 */
function verifyWriter() {
    if (writer.value.length < 3 || writer.value.length >= 5) {
        alert('작성자는 3글자 이상, 5글자 미만이어야 합니다.')
        return false
    }
    return true
}

/**
 * 비밀번호 유효성 검사 메소드
 * 비밀번호의 길이, 비밀번호 재입력값과 같은지 확인한다.
 * @returns {boolean}
 */
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

/**
 * 제목 유효성 검사 메소드
 * 제목의 길이 유효성 검사를 한다.
 * @returns {boolean}
 */
function verifyTitle() {
    if (title.value.length < 4 || title.value.length > 99) {
        alert('제목은 4글자 이상, 100글자 미만이어야 합니다.')
        return false
    }
    return true
}

/**
 * 내용 유효성 검사 메소드
 * 내용의 길이 유효성 검사를 한다.
 * @returns {boolean}
 */
function verifyContent() {
    if (content.value.length < 4 || content.value.length > 1999) {
        alert('내용은 4글자 이상, 2000글자 미만이어야 합니다.')
        return false
    }
    return true
}

/**
 * 입력된 데이터를 FormData 객체에 담아 반환한다.
 * @returns {FormData}
 */
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