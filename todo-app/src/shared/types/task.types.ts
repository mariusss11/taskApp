import type { Group } from "./profile.types"

export interface TaskType{
    title: string
    description: string
    deadline: string
    group: number
}


export interface TaskFormType{
    groups: Group[] | undefined
    closeModal: () => void
}

export interface FormFieldType{
    labelText: string
    inputElement: React.JSX.Element
    name: string
}

export interface FilterType{
    deadline: string | undefined
    progress: string | undefined
    creationDate: string | undefined
}

export interface ITask{
    title: string
    description: string | undefined
    deadline: string
    progress: string
    creationDate: string
    groupName: string
}

export interface DeadlineTaskType{
    deadline: string
    tasks: ITask[]
}