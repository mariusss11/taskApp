import { useForm } from "react-hook-form"
import ProgressRadio from "../../shared/ui/profile/content/tasks/ProgressButton"
import type { SubmitHandler } from "react-hook-form"
import type { ProgressType } from "../../shared/types/task.types"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import TaskService from "../../services/task.service"

type ChangeType = {
    id: number
    closeModal: () => void
}

const ChangeProgressForm = ({id, closeModal}:ChangeType):React.JSX.Element => {
    const { register, handleSubmit } = useForm<ProgressType>()
    const queryClient = useQueryClient()
    const { mutate } = useMutation({
        mutationFn: async (data:ProgressType) => {
            const taskService = new TaskService()
            const response = await taskService.changeTaskStatus(id, String(data.progress))
            console.log(data.progress)
        },
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["tasks"]})
        }
    })
    const onSubmit:SubmitHandler<ProgressType> = async (data) => {
        mutate(data)
        closeModal()
    }
    return <form onSubmit={handleSubmit(onSubmit)} className="change-progress__form">
        <div className="progress-options radios">
            <ProgressRadio type="done" inputElement={<input type="radio" className="progress-radio__input" id="done" value="DONE" {...register("progress", {required: true})} />}/>
            <ProgressRadio type="not-done" inputElement={<input type="radio" className="progress-radio__input" id="not-done" value="NOT_DONE" {...register("progress", {required: true})} />}/>
            <ProgressRadio type="in-process" inputElement={<input type="radio" className="progress-radio__input" id="in-process" value="IN_PROCESS" {...register("progress", {required: true})} />}/>
        </div>
        <button className="submit-form" type="submit">Change</button>
    </form>
}

export default ChangeProgressForm