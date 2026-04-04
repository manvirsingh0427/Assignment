import { Link } from 'react-router-dom'
import { useSelector } from 'react-redux'
import { selectAllPosts } from '../features/posts/postsSlice'

const getExcerpt = (content) => {
  if (content.length <= 180) {
    return content
  }

  return `${content.slice(0, 180).trim()}...`
}

const HomePage = () => {
  const posts = useSelector(selectAllPosts)

  return (
    <main className="page-shell page-shell-wide">
      <div className="mb-6 flex justify-end">
        <Link
          to="/posts/new"
          className="inline-flex items-center justify-center rounded-lg bg-sky-200 px-5 py-3 text-sm font-semibold text-sky-900 transition hover:bg-sky-300"
        >
          New Post
        </Link>
      </div>

      <section>
        <div className="mb-4 flex items-center justify-between">
          <div>
            <h2 className="text-2xl font-bold text-slate-900">All Blog Posts</h2>
            <p className="text-sm text-slate-500">Click any blog card to view the full post details.</p>
          </div>
        </div>

        {posts.length === 0 ? (
          <div className="soft-panel text-center">
            <h3 className="text-xl font-semibold text-slate-900">No posts yet</h3>
            <p className="mt-2 text-sm text-slate-500">Create your first blog post to see it here.</p>
          </div>
        ) : (
          <div className="mx-auto flex w-full flex-col gap-5">
            {posts.map((post) => (
              <Link
                key={post.id}
                to={`/posts/${post.id}`}
                className="soft-panel block w-full transition duration-200 hover:-translate-y-1 hover:shadow-xl"
              >
                <div className="mb-4 flex flex-wrap gap-2">
                  {post.categories.map((category) => (
                    <span
                      key={category}
                      className="rounded-full bg-slate-100 px-3 py-1 text-xs font-semibold text-slate-600"
                    >
                      {category}
                    </span>
                  ))}
                </div>

                <div className="flex min-h-44 flex-col">
                  <div className="mb-3 flex items-start justify-between gap-4">
                    <h3 className="text-xl font-bold text-slate-900">{post.title}</h3>
                    <span className="rounded-full bg-rose-50 px-3 py-1 text-xs font-semibold text-rose-500">
                      {post.liked ? 'Liked' : 'Not liked'}
                    </span>
                  </div>

                  <p className="blog-card-excerpt flex-1 text-sm leading-7 text-slate-600">{getExcerpt(post.content)}</p>

                  <div className="mt-5 flex items-center justify-end text-sm text-slate-500">
                    <span className="font-semibold text-sky-600">Read more →</span>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        )}
      </section>
    </main>
  )
}

export default HomePage
