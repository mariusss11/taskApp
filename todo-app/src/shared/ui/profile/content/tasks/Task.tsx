import type { ITask } from "../../../../types/task.types"
import { getProgressName } from "../../../../../services/display.service"
import { useRef } from "react"

const Task = ({title,description, groupName, creationDate, progress}: Omit<ITask, "deadline" >):React.JSX.Element => {
    const fullInfoRef = useRef<HTMLDivElement | null>(null)
    const dropdownRef = useRef<HTMLImageElement | null>(null)

    function onClick() {
        if (fullInfoRef.current && dropdownRef) {
            fullInfoRef.current.classList.toggle("active");
            dropdownRef.current?.classList.toggle("active");
        }
    }

    return <div className="task-item">
        <div className="task-general-info">
            <h4 className="task-title">{title}</h4>
            <p className="task-create__date">{creationDate}</p>
            <div className="progress-dropdown">
                <button id={progress.toLowerCase()} className="task-progress">
                    {getProgressName(progress)}
                </button>
                <button className="dropdown-button" onClick={onClick}>
                    <img className="dropdown-image" ref={dropdownRef} src="/assets/dropdown.svg" alt="Drowdown" />
                </button>
            </div>
        </div>
        <div ref={fullInfoRef} className="task-detailed__info">
            <p className="task-description">{description}</p>
            <p className="group">Group: {groupName}</p>
        </div>
    </div>
}

export default Task