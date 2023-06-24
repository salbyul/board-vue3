<script setup>
import {condition} from "../composables/useSearchCondition.js";
import {getCategories, getBoards, getBoardCounts} from "../api/board";
import {onBeforeMount, ref} from "vue";
import SmallBoard from "../components/SmallBoard.vue";
import {useRouter} from "vue-router";

const categories = ref([]);
const boards = ref({});
const conditionForRequest = ref(Object.assign({limit: 10}, condition.value));
const counts = ref(0);
const router = useRouter();

/**
 * create 페이지로 이동하기 위한 메소드
 */
const transferToCreateView = () => {
  router.push({
    path: '/create',
    query: condition.value
  })
}
/**
 * 카테고리 목록을 요청한다.
 * @returns {Promise<void>}
 */
const fetchCategories = async () => {
  try {
    const data = await getCategories();
    categories.value = data.result;
  } catch (error) {
    console.log(error);
  }
}

/**
 * 검색조건을 이용해 게시글들을 요청한다.
 * @returns {Promise<void>}
 */
const fetchBoards = async () => {
  try {
    const data = await getBoards(conditionForRequest.value);
    boards.value = data.result;
  } catch (error) {
    console.log(error)
  }
}

/**
 * 검색조건을 이용해 게시글 수를 요청한다.
 * @returns {Promise<void>}
 */
const fetchCounts = async () => {
  try {
    const data = await getBoardCounts(conditionForRequest.value);
    counts.value = data.result;
  } catch (error) {
    console.log(error);
  }
}

/**
 * 검색 메소드
 * 주소창에 쿼리를 추가한 후, 게시글과 게시글 수를 요청한다.
 * @returns {Promise<void>}
 */
const doSearch = async () => {
  await router.push({
    name: 'list',
    query: condition.value
  })
  conditionForRequest.value = Object.assign(Object.assign({limit: 10}, condition.value));
  await fetchBoards();
  await fetchCounts();
}

onBeforeMount(() => {
  fetchCategories();
  fetchBoards();
  fetchCounts();
})

</script>
<template>
  <div class="mb-5 w-7/12 mx-auto">

    <!--    검색 조건-->
    <div class="py-1 text-center">
      등록일
      <input
          type="date"
          name="startDate"
          class="border mx-7"
          id="startDate"
          v-model="condition.startDate"
      />
      <input
          type="date"
          name="endDate"
          class="border mx-7"
          id="endDate"
          v-model="condition.endDate"
      />
      <select name="category" class="border p-1" id="category" v-model="condition.categoryId">
        <option value="">전체 카테고리</option>
        <option
            v-for="category in categories"
            :key="category.id"
            :value="category.id"
            :selected="category.name === condition.category"
        >
          {{ category.name }}
        </option>
      </select>
      <input
          type="text"
          placeholder="검색어를 입력하세요. (제목 + 작성자 + 내용)"
          name="search"
          class="border pl-2 w-5/12"
          id="search"
          v-model="condition.searchText"
      />
      <button
          type="button"
          class="border rounded-sm bg-gray-100 px-5 duration-300 hover:duration-300 hover:bg-gray-200 hover:cursor-pointer"
          @click="doSearch"
      >
        검색
      </button>
    </div>

    <!--    게시글 목록-->
    <div>
      <div class="ml-3 mt-3">총 {{ counts }}건</div>
      <br/>
      <div>
        <table class="mx-auto text-center w-full">
          <thead>
          <tr class="border-y">
            <th class="py-1">카테고리</th>
            <th></th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회 수</th>
            <th>등록 일시</th>
            <th>수정 일시</th>
          </tr>
          </thead>
          <tbody>
          <SmallBoard
              v-for="board in boards"
              :key="board.boardId"
              :board="board"
              :condition="condition"
          />
          </tbody>
        </table>
      </div>
    </div>
    <br/>

    <!-- TODO: Pagination -->

    <div class="flex justify-end">
      <button
          type="button"
          class="px-5 bg-gray-200 rounded-sm duration-300 hover:duration-300 hover:bg-gray-300"
          @click="transferToCreateView"
      >
        등록
      </button>
    </div>
  </div>
</template>