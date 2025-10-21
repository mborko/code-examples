<template>
  <div v-if="posts">
    <v-data-table
        v-model:items-per-page="itemsPerPage"
        :headers="headers"
        :items="posts"
        item-value="userId"
        class="elevation-1"
    ></v-data-table>
  </div>
</template>

<script>
import { ref } from 'vue'
import getAllPosts from '../services/getAllPosts'

export default {
  setup() {
    const { posts, error, load } = getAllPosts()
    const itemsPerPage = ref(10)

    load()

    return {
      posts,
      error,
      itemsPerPage,
      headers: [
        { title: 'User Id', align: 'start', key: 'userId' },
        { title: 'Title', align: 'start', sortable: false, key: 'title' },
        { title: 'Body', align: 'start', sortable: false, key: 'body' },
      ],
    }
  },
}
</script>
