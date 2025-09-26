export interface TaskType{
    title: string | undefined
    description: string | undefined
    deadline: string | undefined
    group: string | undefined
    progress: string | undefined
}

export interface TaskFormType extends TaskType{
    formTitle: string | undefined
    groups: number[] | undefined
}

export interface TaskFormFieldType{
    labelText: string
    inputElement: React.JSX.Element
    name: string
}