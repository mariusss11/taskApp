import { useForm } from "react-hook-form"
import type { TaskType, TaskFormType } from "../../shared/types/task.types"
import type { SubmitHandler } from "react-hook-form"
import FormField from "../../shared/ui/profile/content/form/FormField"

const TaskForm = ({title, deadline, description, group, groups}:TaskFormType):React.JSX.Element => {
    const { register, handleSubmit } = useForm<TaskType>()

    const onSubmit: SubmitHandler<TaskType> = (data) => {
        console.log(data)
    }

    return <form onSubmit={handleSubmit(onSubmit)} className="task-form">
        <div className="task-form__content">
            <FormField name="title" inputElement={<input className="profile-form-input" value={title} {...register("title")} />} labelText="Task title" />
            <FormField name="description" inputElement={<textarea className="profile-form-input form-text-area" value={description} {...register("description")} />} labelText="Task description" />
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
            </div>
            <button className="submit-form" type="submit">Create</button>
        </div>
    </form>
}

export default TaskForm