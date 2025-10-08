type ProgressRadioType = {
    type: string
    inputElement: React.JSX.Element
}

const ProgressRadio = ({type, inputElement}:ProgressRadioType):React.JSX.Element => {
    return <div className="progress-radio">
        <label id={type} htmlFor={type} className="progress-radio__label">{type==="not-done"?"Not done": type==="in-progress"?"In Progress":"Done"}</label>
        {inputElement}
    </div>
}

export default ProgressRadio