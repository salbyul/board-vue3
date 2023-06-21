<script setup>
import {useRouter, useRoute} from 'vue-router';
const props = defineProps(['board', 'condition'])

const router = useRouter();
const transferDetail = () => {
  router.push({
    path: `/board/${props.board.boardId}`,
    query: {
      startDate: props.condition.startDate,
      endDate: props.condition.endDate,
      category: props.condition.category,
      search: props.condition.search,
      page: props.condition.page
    }
  })
}

</script>

<template>
  <tr class="border-b">
    <td class="py-1">{{ board.categoryName }}</td>
    <td v-if="board.fileCounts != null">O</td>
    <td v-else></td>
    <td>
      <a href="#" @click="transferDetail">{{ board.title }}</a>
    </td>
    <td>{{ board.writer }}</td>
    <td>{{ board.views }}</td>
    <td>{{ board.generationTimestamp.substring(0, 10) }}</td>
    <td v-if="board.modificationTimestamp != null">
      {{ board.modificationTimestamp.substring(0, 10) }}
    </td>
    <td v-else>-</td>
  </tr>
</template>