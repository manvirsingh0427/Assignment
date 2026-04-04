import { useContext } from 'react'
import { UiContext } from '../context/uiContext'

export const useUi = () => {
  const context = useContext(UiContext)

  if (!context) {
    throw new Error('useUi must be used within UiProvider')
  }

  return context
}
