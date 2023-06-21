<script setup>
import {getBoardDetail} from "../api/board";
import {getDownload} from "../api/file";
import {saveComment} from "../api/comment";
import {useRoute} from "vue-router";
import {onBeforeMount, ref} from "vue";
import {useDownload} from "../composables/useDownload";

const route = useRoute();
const board = ref({});
const comment = ref({writer: '', content: ''})

const fetchBoardDetail = async () => {
  const id = route.params.id
  try {
    const data = await getBoardDetail(id);
    board.value = data.result;
    board.value.generationTimestamp = board.value.generationTimestamp.substring(0, 10) + ' ' + board.value.generationTimestamp.substring(11, 16);
    if (board.value.modificationTimestamp !== null) {
      board.value.modificationTimestamp = board.value.modificationTimestamp.substring(0, 10) + ' ' + board.value.modificationTimestamp.substring(11, 16);
    } else {
      board.value.modificationTimestamp = '-'
    }
    console.log(data)
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

const submitComment = async () => {
  const commentDTO = {
    writer: comment.value.writer,
    content: comment.value.content,
    boardId: route.params.id
  }

  try {
    await saveComment(commentDTO);
    window.location.reload();
  } catch (error) {
    console.log(error)
  }
}

onBeforeMount(() => {
  fetchBoardDetail();
})
</script>

<template>
  <div class="h-screen w-7/12 mx-auto">
    <!-- 제목 -->
    <div class="border-b-2 pb-3 mb-3">
      <div class="flex justify-between text-sm text-gray-600 mb-5">
        <div>{{ board.writer }}</div>
        <div>등록일시 {{ board.generationTimestamp }} 수정일시 {{ board.modificationTimestamp }}</div>
      </div>
      <div class="flex justify-between text-gray-700">
        <div class="flex">
          <div class="mr-4">[{{ board.categoryName }}]</div>
          <div class="text-gray-900">{{ board.title }}</div>
        </div>
        <div>조회수: {{ board.views }}</div>
      </div>
    </div>

    <!-- 내용 -->
    <div class="mb-3">
      <div class="border pl-1 whitespace-pre-wrap mb-4">{{ board.content }}</div>

      <!-- 파일 -->
      <div v-for="fileName in board.fileNames" :key="fileName">
        <a href="#" @click="fetchDownloadFile(fileName)">{{ fileName }}</a>
      </div>
    </div>

    <!-- 댓글 -->
    <div class="border-b pb-2 bg-gray-50 px-1">
      <div v-for="comment in board.commentDTOs" :key="comment.commentId">
        <div class="border-b-2 pb-2 mb-2">
          <div class="flex justify-between text-gray-600 text-xs">
            <div class="pl-3">{{ comment.writer }}</div>
            <div class="pr-3">{{ comment.generationTimestamp }}</div>
          </div>
          <div class="pl-3 whitespace-pre-wrap">{{ comment.content }}</div>
        </div>
      </div>
      <div>
        <div>
          <div>
            <input
                type="text"
                placeholder="작성자"
                class="border pl-1 mb-1"
                name="writer"
                id="writer"
                v-model="comment.writer"
            />
          </div>
          <div class="flex">
            <textarea
                class="resize-none border h-20 pl-1 w-11/12"
                name="content"
                id="content"
                v-model="comment.content"
            ></textarea>
            <button
                class="border w-1/12 h-20 rounded-sm hover:cursor-pointer"
                @click="submitComment"
            >
              등록
            </button>
          </div>
        </div>
      </div>
    </div>

    <!--    &lt;!&ndash; 하단 버튼 섹션 &ndash;&gt;-->
    <!--    <div class="text-center mt-2">-->
    <!--      <button class="px-3 py-1 border rounded-sm bg-gray-100" @click="transferToHome">목록</button>-->
    <!--      <button class="px-3 py-1 border rounded-sm" @click="changeVisibilityPopup('modify')">-->
    <!--        수정-->
    <!--      </button>-->
    <!--      <button class="px-3 py-1 border rounded-sm" @click="changeVisibilityPopup('delete')">-->
    <!--        삭제-->
    <!--      </button>-->
    <!--    </div>-->
    <!--  </div>-->

    <!--  &lt;!&ndash; 수정 팝업 &ndash;&gt;-->
    <!--  <div-->
    <!--      class="border fixed z-10 w-3/12 text-center top-1/2 left-1/2 -translate-y-1/2 -translate-x-1/2 shadow-md bg-gray-50 invisible"-->
    <!--      id="modifyPopup"-->
    <!--  >-->
    <!--    <h1 class="my-10">비밀번호 입력</h1>-->
    <!--    <input-->
    <!--        type="password"-->
    <!--        placeholder="비밀번호를 입력해주세요."-->
    <!--        class="pl-1 border w-5/12"-->
    <!--        name="password"-->
    <!--        v-model="passwordForModify"-->
    <!--    />-->
    <!--    <br/>-->
    <!--    <div class="flex justify-evenly my-10">-->
    <!--      <button type="button" class="bg-gray-100 px-3 py-1" @click="changeVisibilityPopup('modify')">-->
    <!--        취소-->
    <!--      </button>-->
    <!--      <button type="button" class="bg-gray-100 px-3 py-1" @click="modifyBoard">확인</button>-->
    <!--    </div>-->
    <!--  </div>-->

    <!--  &lt;!&ndash; 삭제 팝업 &ndash;&gt;-->
    <!--  <div-->
    <!--      class="border fixed z-10 w-3/12 text-center top-1/2 left-1/2 -translate-y-1/2 -translate-x-1/2 shadow-md bg-gray-50 invisible"-->
    <!--      id="deletePopup"-->
    <!--  >-->
    <!--    <h1 class="my-10">비밀번호 입력</h1>-->
    <!--    <input-->
    <!--        type="password"-->
    <!--        placeholder="비밀번호를 입력해주세요."-->
    <!--        class="pl-1 border w-5/12"-->
    <!--        v-model="passwordForDelete"-->
    <!--        name="password"-->
    <!--    />-->
    <!--    <br/>-->
    <!--    <div class="flex justify-evenly my-10">-->
    <!--      <button type="button" class="bg-gray-100 px-3 py-1" @click="changeVisibilityPopup('delete')">-->
    <!--        취소-->
    <!--      </button>-->
    <!--      <button type="button" class="bg-gray-100 px-3 py-1" @click="deleteBoard">확인</button>-->
    <!--    </div>-->
  </div>
</template>