import { useForm } from "react-hook-form"
import type { CreateGroupType } from "../../shared/types/profile.types"
import FormField from "../../shared/ui/profile/content/form/FormField"
import type { SubmitHandler } from "react-hook-form"
import GroupService from "../../services/group.service"
import { useMutation, useQueryClient } from "@tanstack/react-query"

const groupService = new GroupService()

const CreateGroupForm = () => {
    const {register, handleSubmit, formState: {errors}} = useForm<CreateGroupType>()
    const queryClient = useQueryClient()
    const { mutate } = useMutation({
        mutationFn: async (data: CreateGroupType) => await groupService.createGroup(data.name, data.description),
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ["groups"]})
        }
    })
    const onSubmit: SubmitHandler<CreateGroupType> = async (data) => {
        mutate(data)
    }

    return <form onSubmit={(handleSubmit(onSubmit))} className="create-form">
        <FormField name="name" labelText="Name" inputElement={<input className="profile-form-input" {...register("name", {required: true, minLength: 3})}  />} />
        {errors.name && <p className="profile-alert input-alert">Name is required and must be longer than three characters</p>}
        <FormField name="description" labelText="Description" inputElement={<textarea className="profile-form-input form-text-area"></textarea>} />
        <button className="submit-form" type="submit">Create</button>
    </form>
}

export default CreateGroupForm