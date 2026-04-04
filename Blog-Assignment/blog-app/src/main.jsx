import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { Provider } from 'react-redux'
import './index.css'
import App from './App.jsx'
import { store } from './app/store'
import { UiProvider } from './context/UiContext.jsx'

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <Provider store={store}>
      <UiProvider>
        <App />
      </UiProvider>
    </Provider>
  </StrictMode>,
)
