import { ref } from 'vue'
import axios from 'axios'

const getAllPosts = () => {
  const posts = ref(null)
  const error = ref(null)

  const load = async () => {
    try {
      const { data } = await axios.get('https://jsonplaceholder.typicode.com/posts')
      console.log(data)
      posts.value = data
      console.log(posts.value)
    } catch (err) {
      console.log(err)
      error.value = err.message
    }
  }

  return { posts, error, load }
}

export default getAllPosts
