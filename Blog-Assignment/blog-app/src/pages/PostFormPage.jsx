import { useMemo, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { addPost, selectPostById, updatePost } from '../features/posts/postsSlice'
import { useUi } from '../hooks/useUi'

const getInitialFormState = (post) => ({
  title: post?.title ?? '',
  categories: post?.categories?.join(', ') ?? '',
  content: post?.content ?? '',
})

const PostFormPage = () => {
  const { postId } = useParams()
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const { showMessage } = useUi()
  const existingPost = useSelector((state) => selectPostById(state, postId))
  const isEditMode = Boolean(postId)
  const [formData, setFormData] = useState(() => getInitialFormState(existingPost))
  const [errors, setErrors] = useState({})

  const cancelLink = useMemo(() => {
    if (isEditMode && existingPost) {
      return `/posts/${existingPost.id}`
    }

    return '/'
  }, [existingPost, isEditMode])

  if (isEditMode && !existingPost) {
    return (
      <main className="page-shell">
        <div className="soft-panel text-center">
          <h1 className="text-2xl font-bold text-slate-900">Post not found</h1>
          <p className="mt-2 text-sm text-slate-500">The post you want to edit is unavailable.</p>
          <Link
            to="/"
            className="mt-6 inline-flex rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"
          >
            Back to Home
          </Link>
        </div>
      </main>
    )
  }

  const handleChange = (event) => {
    const { name, value } = event.target

    setFormData((currentData) => ({
      ...currentData,
      [name]: value,
    }))

    setErrors((currentErrors) => ({
      ...currentErrors,
      [name]: '',
    }))
  }

  const validateForm = () => {
    const nextErrors = {}

    if (!formData.title.trim()) {
      nextErrors.title = 'Title is required.'
    } else if (formData.title.trim().length < 3) {
      nextErrors.title = 'Title must be at least 3 characters.'
    } else if (formData.title.trim().length > 100) {
      nextErrors.title = 'Title cannot exceed 100 characters.'
    }

    if (!formData.categories.trim()) {
      nextErrors.categories = 'Categories are required.'
    }

    if (!formData.content.trim()) {
      nextErrors.content = 'Content is required.'
    } else if (formData.content.trim().length < 10) {
      nextErrors.content = 'Content must be at least 10 characters.'
    } else if (formData.content.trim().length > 5000) {
      nextErrors.content = 'Content cannot exceed 5000 characters.'
    }

    setErrors(nextErrors)

    return Object.keys(nextErrors).length === 0
  }

  const handleSubmit = (event) => {
    event.preventDefault()

    if (!validateForm()) {
      return
    }

    if (isEditMode) {
      dispatch(
        updatePost({
          id: existingPost.id,
          ...formData,
        }),
      )
      showMessage('Blog post updated.', 'success')
      navigate(`/posts/${existingPost.id}`)
      return
    }

    dispatch(addPost(formData))
    showMessage('New blog post added.', 'success')
    navigate('/')
  }

  return (
    <main className="page-shell">
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <button
          type="button"
          onClick={() => navigate('/')}
          className="inline-flex items-center justify-center rounded-lg border border-slate-300 bg-white px-5 py-3 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-slate-400 hover:text-slate-950"
        >
          Back to Home Page
        </button>

        <div>
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">
            {isEditMode ? 'Edit blog post' : 'Add blog post'}
          </p>
          <h1 className="mt-2 text-3xl font-bold text-slate-950">
            {isEditMode ? 'Update your post' : 'Create a new post'}
          </h1>
        </div>
      </div>

      <section className="soft-panel max-w-3xl">
        <form className="space-y-6" onSubmit={handleSubmit}>
          <div>
            <label htmlFor="title" className="mb-2 block text-sm font-semibold text-slate-700">
              Title
            </label>
            <input
              id="title"
              name="title"
              type="text"
              value={formData.title}
              onChange={handleChange}
              className="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-400 focus:ring-4 focus:ring-sky-100"
              placeholder="Enter blog title"
            />
            {errors.title ? <p className="mt-2 text-sm font-medium text-rose-500">{errors.title}</p> : null}
          </div>

          <div>
            <label htmlFor="categories" className="mb-2 block text-sm font-semibold text-slate-700">
              Categories
            </label>
            <input
              id="categories"
              name="categories"
              type="text"
              value={formData.categories}
              onChange={handleChange}
              className="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-400 focus:ring-4 focus:ring-sky-100"
              placeholder="Enter categories separated by commas"
            />
            {errors.categories ? (
              <p className="mt-2 text-sm font-medium text-rose-500">{errors.categories}</p>
            ) : null}
          </div>

          <div>
            <label htmlFor="content" className="mb-2 block text-sm font-semibold text-slate-700">
              Content
            </label>
            <textarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleChange}
              rows="10"
              className="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-sky-400 focus:ring-4 focus:ring-sky-100"
              placeholder="Write your blog content"
            />
            {errors.content ? <p className="mt-2 text-sm font-medium text-rose-500">{errors.content}</p> : null}
          </div>

          <div className="flex flex-wrap gap-3 pt-2">
            <button
              type="submit"
              className="inline-flex items-center justify-center rounded-lg bg-slate-950 px-6 py-3 text-sm font-semibold text-white transition hover:bg-slate-800"
            >
              {isEditMode ? 'Edit' : 'Submit'}
            </button>
            <Link
              to={cancelLink}
              className="inline-flex items-center justify-center rounded-lg border border-slate-300 bg-white px-6 py-3 text-sm font-semibold text-slate-700 transition hover:border-slate-400 hover:text-slate-950"
            >
              Cancel
            </Link>
          </div>
        </form>
      </section>
    </main>
  )
}

export default PostFormPage
