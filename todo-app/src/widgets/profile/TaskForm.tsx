import { useForm } from "react-hook-form"
import type { TaskType, TaskFormType } from "../../shared/types/task.types"
import type { SubmitHandler } from "react-hook-form"
import FormField from "../../shared/ui/profile/content/form/FormField"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import TaskService from "../../services/task.service"

const taskService = new TaskService()

const TaskForm = ({groups, closeModal}:TaskFormType):React.JSX.Element => {
    const { register, handleSubmit } = useForm<TaskType>()
    const queryClient = useQueryClient()
    const { mutate } = useMutation({
        mutationFn: async (data: TaskType) => {
            return await taskService.createTask(data.title, data.description, data.deadline, Number(data.group));
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey:["tasks"]})
            closeModal()
        }
    })
    const onSubmit: SubmitHandler<TaskType> = (data) => {
        mutate(data)
    }

    return <form onSubmit={handleSubmit(onSubmit)} className="task-form">
        <div className="task-form__content">
            <FormField name="title" inputElement={<input className="profile-form-input" {...register("title", {required: true})} />} labelText="Task title" />
            <FormField name="description" inputElement={<textarea className="profile-form-input form-text-area" {...register("description", {required:true})} />} labelText="Task description" />
            <div className="task-form__selections">
                <div className="form-selections__select">
                    <div className="select-item">
                        <label htmlFor="deadline" className="task-form__label">Deadline</label>
                        <input className="select-input" type="date" {...register("deadline", {required: true})}/>
                    </div>
                    <div className="select-item">
                        <label htmlFor="group" className="task-form__label">Group</label>
                        <select className="select-input" {...register("group", {required: true})}>
                            {groups?.map((group) => 
                                <option key={group.id} value={group.id}>{group.name}</option>
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