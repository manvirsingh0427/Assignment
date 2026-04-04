import { useUi } from '../hooks/useUi'

const toastStyles = {
  success: 'border-emerald-200 bg-emerald-50 text-emerald-700',
  info: 'border-sky-200 bg-sky-50 text-sky-700',
  danger: 'border-rose-200 bg-rose-50 text-rose-700',
}

const Toast = () => {
  const { toast } = useUi()

  if (!toast) {
    return null
  }

  return (
    <div className="pointer-events-none fixed inset-x-0 top-4 z-50 flex justify-center px-4">
      <div
        className={`pointer-events-auto rounded-2xl border px-4 py-3 shadow-lg backdrop-blur ${toastStyles[toast.type] ?? toastStyles.info}`}
      >
        <p className="text-sm font-semibold">{toast.text}</p>
      </div>
    </div>
  )
}

export default Toast
