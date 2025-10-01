export interface TaskType{
    title: string | undefined
    description: string | undefined
    deadline: string | undefined
    group: string | undefined
}

export interface TaskFormType extends TaskType{
    formTitle: string | undefined
    groups: number[] | undefined
}

export interface FormFieldType{
    labelText: string
    inputElement: React.JSX.Element
    name: string
}