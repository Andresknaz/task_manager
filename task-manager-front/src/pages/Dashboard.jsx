import { useState } from 'react'
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
} from '@dnd-kit/core'

import TaskItem from '../components/TaskItem'
import TaskModal from '../components/TaskModal'
import DraggableWrapper from '../components/DraggableWrapper'
import DroppableColumn from '../components/DroppableColumn'

export default function Dashboard() {
  const [tasks, setTasks] = useState([])
  const [showModal, setShowModal] = useState(false)
  const [taskToEdit, setTaskToEdit] = useState(null)

  const handleOpenModal = () => {
    setTaskToEdit(null)
    setShowModal(true)
  }

  const handleSaveTask = (task) => {
    if (task.id) {
      setTasks(tasks.map(t => (t.id === task.id ? task : t)))
    } else {
      const newTask = {
        id: crypto.randomUUID(),
        ...task,
        done: false,
      }
      setTasks([...tasks, newTask])
    }
  }

  const handleDelete = (id) => {
    debugger
    setTasks(tasks.filter(task => task.id !== id))
  }

  const sensors = useSensors(useSensor(PointerSensor))

  const handleDragEnd = (event) => {
    const { active, over } = event
    if (!over || !active) return
    const draggedId = active.id
    const targetStatus = over.id

    setTasks(prev =>
      prev.map(task =>
        task.id === draggedId ? { ...task, status: targetStatus } : task
      )
    )
  }

  const groupedTasks = {
    todo: tasks.filter(t => t.status === 'todo'),
    inProgress: tasks.filter(t => t.status === 'inProgress'),
    done: tasks.filter(t => t.status === 'done'),
  }

  return (
    <section className="py-5 bg-light min-vh-100">
      <div className="container-fluid px-4">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h2 className="fw-bold mb-0">Dashboard de Tareas</h2>
          <button className="btn btn-success" onClick={handleOpenModal}>
            + Nueva tarea
          </button>
        </div>

        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
        >
          <div className="row g-4">
            <DroppableColumn id="todo" title="🕒 Por hacer">
              {groupedTasks.todo.map(task => (
                <DraggableWrapper key={task.id} id={task.id}>
                  <TaskItem task={task} onDelete={handleDelete} />
                </DraggableWrapper>
              ))}
            </DroppableColumn>

            <DroppableColumn id="inProgress" title="🔄 En progreso">
              {groupedTasks.inProgress.map(task => (
                <DraggableWrapper key={task.id} id={task.id}>
                  <TaskItem task={task} onDelete={handleDelete} />
                </DraggableWrapper>
              ))}
            </DroppableColumn>

            <DroppableColumn id="done" title="✅ Completadas">
              {groupedTasks.done.map(task => (
                <DraggableWrapper key={task.id} id={task.id}>
                  <TaskItem task={task} onDelete={handleDelete} />
                </DraggableWrapper>
              ))}
            </DroppableColumn>
          </div>
        </DndContext>

        <TaskModal
          show={showModal}
          onClose={() => setShowModal(false)}
          onSave={handleSaveTask}
          task={taskToEdit}
        />
      </div>
    </section>
  )
}
