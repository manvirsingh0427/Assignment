import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import Toast from './components/Toast'
import HomePage from './pages/HomePage'
import PostDetailsPage from './pages/PostDetailsPage'
import PostFormPage from './pages/PostFormPage'

const App = () => {
  return (
    <BrowserRouter>
      <div className="min-h-screen bg-slate-100 text-slate-900">
        <Toast />
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/posts/new" element={<PostFormPage />} />
          <Route path="/posts/:postId" element={<PostDetailsPage />} />
          <Route path="/posts/:postId/edit" element={<PostFormPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}

export default App
