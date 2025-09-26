import { useForm } from "react-hook-form"
import type { Auth } from "../../shared/types/auth.types"
import type { SubmitHandler } from "react-hook-form"
import FormTitle from "../../shared/ui/auth/FormTitle"
import type { FormType } from "../../shared/types/auth.types"
import CreateAccount from "../../shared/ui/auth/CreateAccount"

const AuthForm = ({type}:FormType):React.JSX.Element => {
    const {register, handleSubmit, formState: {errors}} = useForm<Auth>()
    const onSubmit: SubmitHandler<Auth> = (data) => {
        if(type === "log-in"){
            return data.username
        }
        if(type==="sign-up"){
            return data.password
        }
    }

    return <div className="auth-content">
        <FormTitle type={type} />
        <form onSubmit={handleSubmit(onSubmit)}>
            <input placeholder="Username" {...register("username", {required: true, minLength: 3})} className="form-input" />
            {type==="sign-up"?errors.username && <p className="input-alert">Username is required and must constain at least 3 letters.</p>: ""}
            
            <input placeholder="Password" {...register("password", {required: true, minLength: 6})} className="form-input"/>
            {type==="sign-up"?errors.password && <p className="input-alert">Password must contain at least one big letter one number and more than 6 characters</p>: ""}
            <button className="auth" type="submit">{type==="log-in"?"Log in": "Sign up"}</button>
        </form>
        {type==="log-in"? <CreateAccount />: ""}
    </div>
     
}

export default AuthForm