import PageLayout from "./PageLayout";
import Filter from "../../widgets/profile/Filter";
import TaskService from "../../services/task.service";
import Task from "../../shared/ui/profile/content/tasks/Task";
import type { DeadlineTaskType, ITask } from "../../shared/types/task.types";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { getDate } from "../../services/display.service";
import useModal from "../../shared/custom-hooks/useModal";
import CustomModal from "../../shared/ui/CustomModal";
import TaskForm from "../../widgets/profile/TaskForm";
import ChangeProgressForm from "../../widgets/profile/ChangeProgressForm";
import { useRef, useState } from "react";

const Content = () => {
  const [id, setId] = useState(0)
  const taskSerivice = new TaskService();
  useQueryClient();
  const data = useQuery({
    queryKey: ["tasks"],
    queryFn: async () => taskSerivice.getAllEnabledTasks(),
  });
  const modalProperties1 = useModal();
  const modalProperties2 = useModal();
  const statusFormRef = useRef(modalProperties1.openModal)

  return (
    <div className="tasks-list__content">
      <div className="tasks-app__header">
        <div className="profile-title">Tasks list</div>
        <button
          onClick={modalProperties2.openModal}
          className="create-task__button"
        >
          + Create task
        </button>
        <Filter />
      </div>
      <CustomModal title="Change status" modalIsOpen={modalProperties1.modalIsOpen} closeModal={modalProperties1.closeModal} form={<ChangeProgressForm id={id} closeModal={modalProperties1.closeModal} />} />
      <CustomModal
        title="Create task"
        form={
          <TaskForm closeModal={modalProperties2.closeModal} />
        }
        closeModal={modalProperties2.closeModal}
        modalIsOpen={modalProperties2.modalIsOpen}
      />
      <ul className="tasks-list">
        {data?.data?.flatMap((item: DeadlineTaskType, id: number) => (
          <li className="tasks-list__item" key={id}>
            <h5 className="deadline">Deadline: {getDate(item.deadline)}</h5>
            <ul>
              {item.tasks.map((task: ITask, id: number) => (
                <Task
                  key={id}
                  id={task.id}
                  title={task.title}
                  creationDate={getDate(task.creationDate)}
                  progress={task.progress}
                  description={task.description}
                  groupName={task.groupName}
                  ref={statusFormRef}
                  setId={setId}
                />
              ))}
            </ul>
          </li>
        ))}
      </ul>
    </div>
  );
};

const TasksListPage = (): React.JSX.Element => {
  return <PageLayout content={<Content />} />;
};

export default TasksListPage;
