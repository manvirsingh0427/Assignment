import { createSlice } from '@reduxjs/toolkit'

const STORAGE_KEY = 'blog-app-posts'

const createPostId = () => {
  if (typeof crypto !== 'undefined' && crypto.randomUUID) {
    return crypto.randomUUID()
  }

  return `${Date.now()}`
}

const normalizeCategories = (categories) =>
  categories
    .split(',')
    .map((category) => category.trim())
    .filter(Boolean)

const loadPostsFromStorage = () => {
  if (typeof window === 'undefined') {
    return []
  }

  try {
    const storedPosts = window.localStorage.getItem(STORAGE_KEY)

    if (!storedPosts) {
      return []
    }

    const parsedPosts = JSON.parse(storedPosts)

    if (!Array.isArray(parsedPosts) || parsedPosts.length === 0) {
      return []
    }

    return parsedPosts
  } catch {
    return []
  }
}

export const savePostsToStorage = (posts) => {
  if (typeof window === 'undefined') {
    return
  }

  try {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(posts))
  } catch {
    // Ignore storage write failures.
  }
}

const postsSlice = createSlice({
  name: 'posts',
  initialState: {
    items: loadPostsFromStorage(),
  },
  reducers: {
    addPost: {
      reducer(state, action) {
        state.items.unshift(action.payload)
      },
      prepare(post) {
        return {
          payload: {
            id: createPostId(),
            title: post.title.trim(),
            categories: normalizeCategories(post.categories),
            content: post.content.trim(),
            liked: false,
            createdAt: new Date().toISOString(),
          },
        }
      },
    },
    updatePost(state, action) {
      const postToUpdate = state.items.find((post) => post.id === action.payload.id)

      if (!postToUpdate) {
        return
      }

      postToUpdate.title = action.payload.title.trim()
      postToUpdate.categories = normalizeCategories(action.payload.categories)
      postToUpdate.content = action.payload.content.trim()
      postToUpdate.updatedAt = new Date().toISOString()
    },
    deletePost(state, action) {
      state.items = state.items.filter((post) => post.id !== action.payload)
    },
    toggleLike(state, action) {
      const post = state.items.find((item) => item.id === action.payload)

      if (post) {
        post.liked = !post.liked
      }
    },
  },
})

export const { addPost, updatePost, deletePost, toggleLike } = postsSlice.actions

export const selectAllPosts = (state) => state.posts.items
export const selectPostById = (state, postId) =>
  state.posts.items.find((post) => post.id === postId)

export default postsSlice.reducer
