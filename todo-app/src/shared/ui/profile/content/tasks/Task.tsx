import type { ITask } from "../../../../types/task.types";
import { getProgressName } from "../../../../../services/display.service";
import { useRef } from "react";

const Task = ({
  id,
  title,
  description,
  groupName,
  creationDate,
  progress,
  ref,
  setId,
}: Omit<ITask, "deadline">): React.JSX.Element => {
  const fullInfoRef = useRef<HTMLDivElement | null>(null);
  const dropdownRef = useRef<HTMLImageElement | null>(null);

  function onClick() {
    if (fullInfoRef.current && dropdownRef) {
      fullInfoRef.current.classList.toggle("active");
      dropdownRef.current?.classList.toggle("active");
    }
  }

  function onChangeClick(){
    setId(id)
    ref.current()
  }

  return (
    <div className="task-item">
      <div className="task-general-info">
        <h4 className="task-title"><span>Title:</span> {title}</h4>
        <p className="task-create__date">Creation Date: {creationDate}</p>
        <div className="progress-dropdown">
          <button onClick={onChangeClick}  id={progress.toLowerCase()} className="task-progress">
            {getProgressName(progress)}
          </button>
          <button className="dropdown-button" onClick={onClick}>
            <img
              className="dropdown-image"
              ref={dropdownRef}
              src="/assets/dropdown.svg"
              alt="Drowdown"
            />
          </button>
        </div>
      </div>
      <div ref={fullInfoRef} className="task-detailed__info">
        <p className="task-description">{description}</p>
        <p className="group">Group: {groupName}</p>
      </div>
    </div>
  );
};

export default Task;
