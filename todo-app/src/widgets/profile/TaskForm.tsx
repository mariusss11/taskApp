import { useForm } from "react-hook-form"
import type { TaskType, TaskFormType } from "../../shared/types/task.types"
import type { SubmitHandler } from "react-hook-form"
import TaskFormField from "../../shared/ui/profile/content/form/TaskFormField"

const TaskForm = ({title, deadline, description, group, progress, groups}:TaskFormType):React.JSX.Element => {
    const { register, handleSubmit } = useForm<TaskType>()

    const onSubmit: SubmitHandler<TaskType> = (data) => {
        console.log(data)
    }

    return <form onSubmit={handleSubmit(onSubmit)} className="task-form">
        <div className="task-form__content">
            <TaskFormField name="title" inputElement={<input className="task-form__input" value={title} {...register("title")} />} labelText="Task title" />
            <TaskFormField name="description" inputElement={<textarea className="task-form__text-area" value={description} {...register("description")} />} labelText="Task description" />
            <div className="task-form__selections">
                <div className="form-selections__select">
                    <div className="select-item">
                        <label htmlFor="deadline" className="task-form__label">Deadline</label>
                        <input type="date" {...register("deadline")} value={deadline}/>
                    </div>
                    <div className="select-item">
                        <label htmlFor="deadline" className="task-form__label">Group</label>
                        <select {...register("group")} value={group}>
                            {groups?.map((id) => 
                                <option key={id} value={id}>{id}</option>
                            )}
                        </select>
                    </div>
                </div>
                <div className="form-selection__progress"></div>
            </div>
        </div>
    </form>
}

export default TaskForm