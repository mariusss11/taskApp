import type { FormFieldType } from "../../../../types/task.types"

const FormField = ({labelText, inputElement, name}:FormFieldType) => {
    return <div className="form-field">
        <label htmlFor={name} className="form-field__label">{labelText}</label>
        {inputElement}
    </div>
}

export default FormField