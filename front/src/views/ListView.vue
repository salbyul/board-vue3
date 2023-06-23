<script setup>
import {startDate, endDate, categoryId, searchText, page, limit} from "../composables/useSearchCondition.js";
import {getCategories, getBoards, getBoardCounts} from "../api/board";
import {onBeforeMount, ref} from "vue";
import SmallBoard from "../components/SmallBoard.vue";
import {useRouter} from "vue-router";

const categories = ref([]);
const boards = ref({});
const condition = ref({});
const counts = ref(0);
const router = useRouter();

const transferToCreateView = () => {
  router.push({
    path: '/create',
    query: {
      startDate: condition.value.startDate,
      endDate: condition.value.endDate,
      category: condition.value.category,
      search: condition.value.search,
      page: condition.value.page
    }
  })
}

const fetchCategories = async () => {
  try {
    const data = await getCategories();
    categories.value = data.result;
  } catch (error) {
    console.log(error);
  }
}

const fetchBoards = async () => {
  try {
    const data = await getBoards(condition.value);
    boards.value = data.result;
  } catch (error) {
    console.log(error)
  }
}

const fetchCounts = async () => {
  try {
    const data = await getBoardCounts(condition.value);
    counts.value = data.result;
  } catch (error) {
    console.log(error);
  }
}

const doSearch = () => {
  fetchBoards();
  fetchCounts();
}

onBeforeMount(() => {
  condition.value = {
    startDate,
    endDate,
    categoryId,
    searchText,
    page,
    limit
  }
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