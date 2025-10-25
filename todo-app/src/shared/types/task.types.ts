import type { Dispatch, SetStateAction } from "react"

export interface TaskType{
    id: number,
    title: string,
    description: string,
    deadline: string,
    creationDate: string,
    progress: string,
    groupName: string
}


export interface TaskFormType{
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
    id: number
    title: string
    description: string | undefined
    deadline: string
    progress: string
    creationDate: string
    groupName: string
    ref: React.RefObject<() => void>
    setId: Dispatch<SetStateAction<number>>
}

export interface DeadlineTaskType{
    deadline: string
    tasks: ITask[]
}

export interface ProgressType{
    progress: string | undefined
}