export default function TaskItem({ task, onDelete }) {
    const handleDeleteClick = (e) => {
      e.stopPropagation()
      onDelete(task.id)
    }
  
    return (
      <div className="card mb-2 shadow-sm">
        <div className="card-body d-flex justify-content-between align-items-center">
          <span className="me-2">{task.title}</span>
          <button
            className="btn btn-sm btn-outline-danger"
            onClick={handleDeleteClick}
          >
            Eliminar
          </button>
        </div>
      </div>
    )
  }
  