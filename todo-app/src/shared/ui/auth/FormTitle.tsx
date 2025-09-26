import type { FormType } from "../../types/auth.types"

const FormTitle = ({type}: FormType) => {
    return <h1 className="form-title">{type==="log-in"?"Log in":"Sign up"}</h1>
}

export default FormTitle