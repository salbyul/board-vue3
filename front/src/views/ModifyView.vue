<script setup>
import {onBeforeMount, ref} from 'vue';
import {useRoute, useRouter} from "vue-router";
import {getBoardDetailForModify, modifyBoard} from "../api/board";
import {getDownload} from "../api/file";
import {useDownload} from "../composables/useDownload";
import {condition} from "../composables/useSearchCondition"
import {verifyModify, writer, title, content, init} from "../composables/useHandlingBoardData";

const route = useRoute();
const router = useRouter();

const board = ref({});
const fileCounts = ref(0)
const newFileCounts = ref(0)
const generationTimestamp = ref('')
const modificationTimestamp = ref('')

/**
 * 수정을 위한 게시글의 정보를 요청하는 메소드
 * @returns {Promise<void>}
 */
const fetchDetail = async () => {
  const id = route.params.id;
  try {
    const data = await getBoardDetailForModify(id);
    board.value = data.result;
    fileCounts.value = board.value.fileDTOs.length;
    newFileCounts.value = 4 - fileCounts.value;
    writer.value = data.result.writer;
    title.value = data.result.title;
    content.value = data.result.content;
    generationTimestamp.value = board.value.generationTimestamp.substring(0, 10) + ' ' + board.value.generationTimestamp.substring(11, 16);
    modificationTimestamp.value = board.value.modificationTimestamp === null ? '-' : board.value.modificationTimestamp.substring(0, 10) + ' ' + board.value.modificationTimestamp.substring(11, 16)
  } catch (error) {
    console.log(error)
  }
}

/**
 * 파일이름을 이용해 서버에 파일다운로드를 요청한다.
 * @param fileName 파일 이름
 * @returns {Promise<void>}
 */
const fetchDownloadFile = async (fileName) => {
  const id = route.params.id;
  try {
    const response = await getDownload(fileName, id);
    useDownload(response);
  } catch (error) {
    console.log(error)
  }
}

/**
 * 파일을 선택할 경우 파일 이름을 input에 표시하기 위한 메소드
 * @param index input을 구분하기 위한 index
 */
const onFileChange = (index) => {
  const input = document.getElementById('file' + index);
  const fileInput = document.getElementById('fileInput' + index);
  input.value = fileInput.files[0].name;
}

/**
 * 기존에 업로드한 파일을 지우는 메소드
 * @param index 파일 구분을 위한 index
 */
const deleteFile = (index) => {
  const div = document.getElementById('fileDiv' + index)
  div.remove()
  board.value.fileDTOs.forEach((f) => {
    if (f.fileId === index) {
      board.value.fileDTOs.splice(board.value.fileDTOs.indexOf(f), 1)

    }
  })
  createFileInput()
}

/**
 * 파일 업로드를 위한 Element를 생성하는 메소드
 */
const createFileInput = () => {
  const div = document.createElement('div')
  const nameInput = document.createElement('input')
  const label = document.createElement('label')
  const span = document.createElement('span')
  const fileInput = document.createElement('input')
  const index = newFileCounts.value

  div.className = 'my-1'

  nameInput.className = 'border pl-1 ml-3 w-5/12 bg-white'
  nameInput.disabled = true
  nameInput.type = 'text'
  nameInput.id = 'file' + index

  label.htmlFor = 'fileInput' + index

  span.className = 'border rounded-sm px-2'
  span.innerHTML = '파일 찾기'

  fileInput.className = 'pl-1 ml-3 w-full'
  fileInput.type = 'file'
  fileInput.id = 'fileInput' + index
  fileInput.name = 'file' + index
  fileInput.hidden = true
  fileInput.onchange = () => onFileChange(index)

  label.insertBefore(span, null)
  div.insertBefore(nameInput, null)
  div.insertBefore(label, null)
  div.insertBefore(fileInput, null)
  const parentDiv = document.getElementById('parentDiv')
  parentDiv.insertBefore(div, null)
  newFileCounts.value = newFileCounts.value + 1
}

/**
 * 뒤로가기 버튼을 위한 메소드
 * detail 페이지로 이동한다.
 */
const transferToBack = () => {
  const id = route.params.id;
  router.push({
    name: 'detail',
    params: {
      id
    },
    query: condition.value
  })
}

/**
 * 수정한 데이터 제출 메소드
 * 수정에 성공하면 detail 페이지로 이동한다.
 * @returns {Promise<void>}
 */
const submit = async () => {
  if (!verifyModify()) return;
  const id = route.params.id;
  const form = makeFormData(id);
  try {
    await modifyBoard(id, form);

    alert("수정되었습니다.")
    await router.push({
      name: 'detail',
      params: {
        id
      },
      query: condition.value
    })
  } catch (error) {
    const data = error.response.data;
    if (data.error.code === '135') {
      alert("비밀번호가 틀렸습니다.")
    } else if (data.error.code === '132') {
      alert("비밀번호를 입력해주세요.")
    }
  }
}

/**
 * 입력한 데이터를 FormData 객체에 담고 반환한다.
 * @returns {FormData}
 */
const makeFormData = () => {
  const form = new FormData()
  board.value.writer = writer.value;
  board.value.title = title.value;
  board.value.content = content.value;
  const dataJson = JSON.stringify(board.value)
  const blob = new Blob([dataJson], {type: 'application/json'})
  form.append('boardDTO', blob)

  const fileOne = document.getElementById('fileInput1')
  const fileTwo = document.getElementById('fileInput2')
  const fileThree = document.getElementById('fileInput3')
  const fileList = [fileOne, fileTwo, fileThree]

  fileList.forEach((file) => {
    if (file !== undefined && file !== null) {
      form.append('files', file.files[0])
    }
  })
  return form;
}

onBeforeMount(async () => {
  init();
  await fetchDetail();
})
</script>

<template>
  <div>
    <div>
      <!-- 카테고리 -->
      <div class="flex border-y">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">카테고리</div>
        <div class="py-1 w-9/12 pl-1">{{ board.categoryName }}</div>
      </div>

      <!-- 등록 일시 -->
      <div class="flex border-y">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">등록 일시</div>
        <div class="py-1 w-9/12 pl-1">{{ generationTimestamp }}</div>
      </div>

      <!-- 수정 일시 -->
      <div class="flex border-y">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">수정 일시</div>
        <div class="py-1 w-9/12 pl-1">{{ modificationTimestamp }}
        </div>
      </div>

      <!-- 조회 수 -->
      <div class="flex border-y">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">조회 수</div>
        <div class="py-1 w-9/12 pl-1">{{ board.views }}</div>
      </div>

      <!-- 작성자 -->
      <div class="flex border-b">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">작성자</div>
        <div class="py-1 w-9/12">
          <input
              type="text"
              name="writer"
              class="border pl-1 ml-3 w-44"
              id="writer"
              v-model="writer"
          />
        </div>
      </div>

      <!-- 비밀번호 -->
      <div class="flex border-b">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">비밀번호</div>
        <div class="py-1 w-9/12">
          <input
              type="password"
              name="password"
              placeholder="비밀번호"
              class="border pl-1 ml-3 w-44"
              id="password"
              v-model="board.password"
          />
        </div>
      </div>

      <!-- 제목 -->
      <div class="flex border-b">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">제목</div>
        <div class="py-1 w-9/12">
          <input
              type="text"
              name="title"
              class="border pl-1 ml-3 w-full"
              id="title"
              v-model="title"
          />
        </div>
      </div>

      <!-- 내용 -->
      <div class="flex border-b">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">
          <span class="align-middle">내용</span>
        </div>
        <div class="py-1 w-9/12">
          <textarea
              class="resize-none w-full border pl-1 ml-3 h-32"
              name="content"
              id="content"
              v-model="content"
          ></textarea>
        </div>
      </div>
      <!-- 파일 첨부 -->
      <div class="flex border-b">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">
          <span class="align-middle"> 파일 첨부 </span>
        </div>
        <div class="py-1 w-9/12" id="parentDiv">
          <!-- 기존 파일 예제-->
          <div class="my-1" :id="`fileDiv${file.fileId}`" v-for="file in board.fileDTOs" :key="file.fileId">
            <input
                type="text"
                class="border pl-1 ml-3 w-5/12 bg-white"
                disabled
                :id="`oldFile${file.fileId}`"
                :value="file.realName"
            />
            <a href="#" class="px-3 py-1 border" @click="fetchDownloadFile(file.realName)">Download</a>
            <button class="px-2 py-1 border" id="fileBtnOne" @click="deleteFile(file.fileId)">
              X
            </button>
          </div>
          <!-- 새로운 파일 첨부 -->
          <div class="my-1" v-for="n in 3 - fileCounts" :key="n">
            <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled :id="`file${n}`"/>
            <label :for="`fileInput${n}`">
              <span class="border founded-sm px-2">파일 찾기</span>
            </label>
            <input
                type="file"
                class="pl-1 ml-3 w-full"
                :id="`fileInput${n}`"
                name="file"
                hidden
                @change="onFileChange(n)"
            />
          </div>
        </div>
      </div>
    </div>
    <br/>
    <div class="flex justify-between">
      <button
          type="button"
          class="px-5 rounded-sm border"
          @click="transferToBack">
        취소
      </button>
      <button
          type="button"
          class="px-5 rounded-sm border bg-gray-100 hover:cursor-pointer"
          @click="submit"
      >
        저장
      </button>
    </div>
  </div>
</template>