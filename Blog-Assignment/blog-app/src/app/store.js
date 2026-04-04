import { configureStore } from '@reduxjs/toolkit'
import postsReducer, { savePostsToStorage } from '../features/posts/postsSlice'

export const store = configureStore({
  reducer: {
    posts: postsReducer,
  },
})

store.subscribe(() => {
  savePostsToStorage(store.getState().posts.items)
})
