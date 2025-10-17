import type { FilterType } from "../../shared/types/task.types"
import { useForm } from "react-hook-form"
import type { SubmitHandler } from "react-hook-form"
import ProgressRadio from "../../shared/ui/profile/content/tasks/ProgressButton"
import { useRef } from "react"

const Filter = ():React.JSX.Element => {
    const {register, handleSubmit} = useForm<FilterType>()
    const filterFormRef = useRef<HTMLFormElement>(null)
    const onSubmit: SubmitHandler<FilterType> = (data) => {console.log(data)}

    function onClick(){
        if (filterFormRef.current) {
            filterFormRef.current.classList.toggle("active")
        }
    }

    return <div className="filter">
        <button onClick={onClick} className="filter-button">
            <img src="/assets/filter.svg" alt="Filter image" className="filter-img" />
            Filter
        </button>
        <form ref={filterFormRef} onSubmit={handleSubmit(onSubmit)} className="filter-form">
            <div className="filter-content">
                <div className="radios">
                    <ProgressRadio type="done" inputElement={<input value="DONE" className="progress-radio__input" type="radio" {...register("progress")} id="done" />} />
                    <ProgressRadio type="in-process" inputElement={<input value="IN_PROCESS" className="progress-radio__input" type="radio" {...register("progress")} id="in-process" />}/>
                    <ProgressRadio type="not-done" inputElement={<input value="NOT_DONE" className="progress-radio__input" type="radio" {...register("progress")} id="not-done" />}/>
                </div>
                <div className="filter-dates">
                    <div className="select-item">
                        <label htmlFor="deadline" className="task-form__label">Deadline</label>
                        <input className="select-input" type="date" {...register("deadline")}/>
                    </div>
                    <div className="select-item">
                        <label htmlFor="creationDate" className="task-form__label">Creation Date</label>
                        <input className="select-input" type="date" {...register("creationDate")}/>
                    </div>
                </div>
            </div>
            <button className="submit-form" type="submit">Apply</button>
        </form>
    </div>
}

export default Filter