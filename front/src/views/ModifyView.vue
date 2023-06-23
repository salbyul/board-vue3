<script setup>
import {onBeforeMount, ref} from 'vue';
import {useRoute, useRouter} from "vue-router";
import {getBoardDetail, modifyBoard} from "../api/board";
import {getDownload} from "../api/file";
import {useDownload} from "../composables/useDownload";
import {condition} from "../composables/useSearchCondition"
import {verifyModify, writer, title, content} from "../composables/useHandlingBoardData";

const route = useRoute();
const router = useRouter();

const board = ref({});
const fileCounts = ref(0)
const newFileCounts = ref(0)
// TODO: 게시글의 수정값들이 서버로 전송되는 것 까지 확인됨 (파일 핸들링해야 됨, CLEAN CODE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!)
const fetchDetail = async () => {
  const id = route.params.id;
  try {
    const data = await getBoardDetail(id);
    board.value = data.result;
    fileCounts.value = board.value.fileDTOs.length;
    newFileCounts.value = 4 - fileCounts.value;
    writer.value = data.result.writer;
    title.value = data.result.title;
    content.value = data.result.content;
  } catch (error) {
    console.log(error)
  }
}

const fetchDownloadFile = async (fileName) => {
  const id = route.params.id;
  try {
    const response = await getDownload(fileName, id);
    useDownload(response);
  } catch (error) {
    console.log(error)
  }
}

const onFileChange = (index) => {
  const input = document.getElementById('file' + index);
  const fileInput = document.getElementById('fileInput' + index);
  input.value = fileInput.files[0].name;
}

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
  fileInput.onchange = () => this.onFileChange(index)

  label.insertBefore(span, null)
  div.insertBefore(nameInput, null)
  div.insertBefore(label, null)
  div.insertBefore(fileInput, null)
  const parentDiv = document.getElementById('parentDiv')
  parentDiv.insertBefore(div, null)
  newFileCounts.value = newFileCounts.value + 1
}

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

const submit = async () => {
  if (!verifyModify()) return;
  const id = route.params.id;
  const form = new FormData()
  board.value.writer = writer.value;
  board.value.title = title.value;
  board.value.content = content.value;
  const dataJson = JSON.stringify(board.value)
  const blob = new Blob([dataJson], {type: 'application/json'})
  form.append('boardDTO', blob)

  // const fileOne = document.getElementById('fileInput1')
  // const fileTwo = document.getElementById('fileInput2')
  // const fileThree = document.getElementById('fileInput3')
  // const fileList = [fileOne, fileTwo, fileThree]
  //
  // fileList.forEach((file) => {
  //   if (file !== undefined && file !== null) {
  //     form.append('files', file.files[0])
  //   }
  // })

  try {
    const data = await modifyBoard(id, form);
    console.log(data)
  } catch (error) {
    console.log(error)
  }
}

onBeforeMount(async () => {
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
        <div class="py-1 w-9/12 pl-1">{{ board.generationTimestamp }}</div>
      </div>

      <!-- 수정 일시 -->
      <div class="flex border-y">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">수정 일시</div>
        <div class="py-1 w-9/12 pl-1">{{
            board.modificationTimestamp === null ? '-' : board.modificationTimestamp
          }}
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
                :value="file.fileName"
            />
            <a href="#" class="px-3 py-1 border" @click="fetchDownloadFile(file.fileName)">Download</a>
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