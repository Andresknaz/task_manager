import { useDraggable } from '@dnd-kit/core'

export default function DraggableWrapper({ id, children }) {
  const { attributes, listeners, setNodeRef } = useDraggable({ id })

  return (
    <div
      ref={setNodeRef}
      {...attributes}
      {...listeners}
      style={{
        touchAction: 'none',
        cursor: 'grab',
      }}
      onClick={(e) => {
        if (e.target.tagName === 'BUTTON') e.stopPropagation()
      }}
    >
      {children}
    </div>
  )
}
