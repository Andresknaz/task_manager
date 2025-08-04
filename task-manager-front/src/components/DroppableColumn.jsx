import { useDroppable } from '@dnd-kit/core'

export default function DroppableColumn({ id, title, children }) {
  const { setNodeRef } = useDroppable({ id })

  return (
    <div className="col-12 col-md-4">
      <div ref={setNodeRef}>
        <h5 className="text-center">{title}</h5>
        {children}
      </div>
    </div>
  )
}
