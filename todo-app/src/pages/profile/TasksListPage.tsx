import PageLayout from "./PageLayout"
import useGroups from "../../shared/custom-hooks/useGroups"
import Filter from "../../widgets/profile/Filter"
import TaskService from "../../services/task.service"
import Task from "../../shared/ui/profile/content/tasks/Task"
import type { DeadlineTaskType, ITask} from "../../shared/types/task.types"
import { useQuery, useQueryClient } from "@tanstack/react-query"
import { getDate } from "../../services/display.service"
import useModal from "../../shared/custom-hooks/useModal"
import CustomModal from "../../shared/ui/CustomModal"
import TaskForm from "../../widgets/profile/TaskForm"

const Content = () => {
    const taskSerivice = new TaskService()
    useQueryClient()
    const data = useQuery({queryKey:["tasks"], queryFn: async () => taskSerivice.getAllEnabledTasks()})
    const modalProperties = useModal()
    const groups = useGroups()

    return <div className="tasks-list__content">
        <div className="tasks-list__header">
            <div className="profile-title">Tasks list</div>
            <button onClick={modalProperties.openModal} className="create-task__button">+ Create task</button>
            <Filter />
        </div>
        <CustomModal
            title="Create task"
            form={<TaskForm groups={groups} closeModal={modalProperties.closeModal}/>}
            closeModal={modalProperties.closeModal}
            modalIsOpen={modalProperties.modalIsOpen}
        />
        <ul className="tasks-list">
                {data?.data?.flatMap((item: DeadlineTaskType,id: number) =>
                    <li className="tasks-list__item" key={id}>
                        <h5 className="deadline">{getDate(item.deadline)}</h5>
                        <ul>
                            {item.tasks.map((task: ITask, id: number) =>
                            <Task key={id} title={task.title} creationDate={getDate(task.creationDate)} progress={task.progress} description={task.description} groupName={task.groupName} />)}
                        </ul>
                    </li>
                    
                )}
        </ul>
    </div>
}

const TasksListPage = ():React.JSX.Element => {
    const paths = useGroups()

    return <PageLayout paths={paths} content={<Content />} />
}

export default TasksListPage