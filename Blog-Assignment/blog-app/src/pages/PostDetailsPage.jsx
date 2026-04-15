import { useDispatch, useSelector } from 'react-redux'
import { Link, useNavigate, useParams } from 'react-router-dom'
import { deletePost, selectPostById, toggleLike } from '../features/posts/postsSlice'
import { useUi } from '../hooks/useUi'

const formatDate = (value) =>
  new Date(value).toLocaleDateString('en-IN', {
    day: 'numeric',
    month: 'long',
    year: 'numeric',
  })

const PostDetailsPage = () => {
  const { postId } = useParams()
  const dispatch = useDispatch()
  const navigate = useNavigate()
  const { showMessage } = useUi()
  const post = useSelector((state) => selectPostById(state, postId))

  if (!post) {
    return (
      <main className="page-shell">
        <div className="soft-panel text-center">
          <p className="text-sm font-semibold uppercase tracking-[0.2em] text-slate-500">Post not found</p>
          <h1 className="mt-3 text-2xl font-bold text-slate-900">The blog post you opened does not exist.</h1>
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

  const handleDelete = () => {
    const shouldDelete = window.confirm('Delete this blog post?')

    if (!shouldDelete) {
      return
    }

    dispatch(deletePost(post.id))
    showMessage('Blog post deleted.', 'danger')
    navigate('/')
  }

  const handleLikeToggle = () => {
    dispatch(toggleLike(post.id))
    showMessage(post.liked ? 'Blog post unliked.' : 'Blog post liked.', 'info')
  }

  return (
    <main className="page-shell">
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <button
          type="button"
          onClick={() => navigate(-1)}
          className="inline-flex items-center justify-center rounded-lg border border-slate-300 bg-white px-5 py-3 text-sm font-semibold text-slate-700 shadow-sm transition hover:border-slate-400 hover:text-slate-950"
        >
          Back To Home Page
        </button>

        <div className="flex flex-wrap gap-3 sm:justify-end">
          <button
            type="button"
            onClick={handleLikeToggle}
            className={`inline-flex items-center justify-center rounded-lg px-5 py-3 text-sm font-semibold transition ${post.liked ? 'bg-rose-500 text-white hover:bg-rose-400' : 'bg-rose-50 text-rose-600 hover:bg-rose-100'}`}
          >
            {post.liked ? 'Unlike' : 'Like'}
          </button>
          <Link
            to={`/posts/${post.id}/edit`}
            className="inline-flex items-center justify-center rounded-lg bg-sky-500 px-5 py-3 text-sm font-semibold text-white transition hover:bg-sky-400"
          >
            Edit
          </Link>
          <button
            type="button"
            onClick={handleDelete}
            className="inline-flex items-center justify-center rounded-lg bg-rose-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-rose-500"
          >
            Delete
          </button>
        </div>
      </div>

      <article className="soft-panel overflow-hidden">
        <div className="mb-4 flex flex-wrap gap-2">
          {post.categories.map((category) => (
            <span
              key={category}
              className="rounded-full bg-sky-50 px-3 py-1 text-xs font-semibold text-sky-700"
            >
              {category}
            </span>
          ))}
        </div>

        <h1 className="break-words text-3xl font-bold text-slate-950 sm:text-4xl">{post.title}</h1>
        <div className="mt-4 flex flex-wrap items-center gap-3 text-sm text-slate-500">
          <span>Published on {formatDate(post.createdAt)}</span>
          <span className="rounded-full bg-slate-100 px-3 py-1 font-semibold text-slate-700">
            {post.liked ? 'Liked' : 'Not liked'}
          </span>
          {post.updatedAt ? <span>Updated recently</span> : null}
        </div>

        <div className="prose-content mt-8 whitespace-pre-line break-words text-base leading-8 text-slate-700 [overflow-wrap:anywhere]">
          {post.content}
        </div>
      </article>
    </main>
  )
}

export default PostDetailsPage
