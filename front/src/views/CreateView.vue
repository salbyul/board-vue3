<script setup>
import {
  category,
  writer,
  content,
  title,
  password,
  passwordVerify,
  fileInput1,
  fileInput2,
  fileInput3,
  fileChange,
  verifyCreate,
  makeFormData
} from "../composables/useHandlingBoardData";
import {getCategories, saveBoard} from "../api/board";
import {onBeforeMount, ref} from "vue";
import {useRouter} from "vue-router";
import {condition} from "../composables/useSearchCondition";

const categories = ref([]);
const router = useRouter();

const fetchCategories = async () => {
  try {
    const data = await getCategories();
    categories.value = data.result;
  } catch (error) {
    console.log(error)
  }
}

const submitBoard = async () => {
  if (!verifyCreate()) return;

  try {
    const formData = makeFormData();
    const data = await saveBoard(formData);
    console.log(data)
    await router.push({
      name: 'detail',
      params: {
        id: data.result
      },
      query: {
        condition: condition.value
      }
    })
  } catch (error) {
    //TODO: 에러처리???
    console.log(error)
    const data = error.response.data;
    if (data.error.code === '134') {
      alert("비밀번호에는 영문, 숫자, 특수문자가 필수로 들어가야합니다.");
    }
  }
}

onBeforeMount(() => {
  fetchCategories();
})
</script>

<template>
  <div>
    <div>
      <div class="flex border-y">
        <div class="bg-gray-100 w-2/12 py-1 pl-2">카테고리</div>
        <div class="py-1 w-9/12">
          <select
              name="category"
              class="border ml-3 w-44"
              v-model="category"
          >
            <option disabled>카테고리 선택</option>
            <option
                v-for="category in categories"
                :key="category.id"
                :value="category.id"
            >
              {{ category.name }}
            </option>
          </select>
        </div>
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
            v-model="password"
        />
        <input
            type="password"
            placeholder="비밀번호 확인"
            class="border pl-1 ml-3 w-44"
            id="passwordVerify"
            v-model="passwordVerify"
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
        <span class="align-middle">파일 첨부</span>
      </div>
      <div class="py-1 w-9/12">
        <div class="my-1">
          <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled v-model="fileInput1"/>
          <label for="fileOne">
            <span class="border rounded-sm px-2">파일 찾기</span>
          </label>
          <input
              type="file"
              class="pl-1 ml-3 w-full"
              id="fileOne"
              name="file"
              hidden
              @change="fileChange(1, $event)"
          />
        </div>
        <div class="my-1">
          <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled v-model="fileInput2"/>
          <label for="fileTwo">
            <span class="border rounded-sm px-2">파일 찾기</span>
          </label>
          <input
              type="file"
              class="pl-1 ml-3 w-full"
              id="fileTwo"
              name="file"
              hidden
              @change="fileChange(2, $event)"
          />
        </div>
        <div class="my-1">
          <input type="text" class="border pl-1 ml-3 w-5/12 bg-white" disabled v-model="fileInput3"/>
          <label for="fileThree">
            <span class="border rounded-sm px-2">파일 찾기</span>
          </label>
          <input
              type="file"
              class="pl-1 ml-3 w-full"
              id="fileThree"
              name="file"
              hidden
              @change="fileChange(3, $event)"
          />
        </div>
      </div>
    </div>
  </div>
  <br/>
  <div class="flex justify-between">
    <button class="px-5 rounded-sm border" @click="() => this.$router.go(-1)">취소</button>
    <button
        type="button"
        class="px-5 rounded-sm border bg-gray-100 hover:cursor-pointer"
        @click="submitBoard"
    >
      저장
    </button>
  </div>
</template>
