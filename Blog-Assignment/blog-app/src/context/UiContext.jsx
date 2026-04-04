import { useEffect, useMemo, useState } from 'react'
import { UiContext } from './uiContext'

export const UiProvider = ({ children }) => {
  const [toast, setToast] = useState(null)

  useEffect(() => {
    if (!toast) {
      return undefined
    }

    const timer = window.setTimeout(() => {
      setToast(null)
    }, 2500)

    return () => window.clearTimeout(timer)
  }, [toast])

  const value = useMemo(
    () => ({
      toast,
      showMessage: (text, type = 'success') => {
        setToast({
          id: Date.now(),
          text,
          type,
        })
      },
      clearMessage: () => setToast(null),
    }),
    [toast],
  )

  return <UiContext.Provider value={value}>{children}</UiContext.Provider>
}
