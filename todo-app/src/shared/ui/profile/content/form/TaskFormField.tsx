import type { TaskFormFieldType } from "../../../../types/task.types"

const TaskFormField = ({labelText, inputElement, name}:TaskFormFieldType):React.JSX.Element => {
    return <div className="task-form__field">
        <label htmlFor={name} className="form-field__label">{labelText}</label>
        {inputElement}
    </div>
}

export default TaskFormField