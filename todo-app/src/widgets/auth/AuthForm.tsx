import { useForm } from "react-hook-form"
import type { Auth } from "../../shared/types/auth.types"
import type { SubmitHandler } from "react-hook-form"
import FormTitle from "../../shared/ui/auth/FormTitle"
import type { FormType } from "../../shared/types/auth.types"
import CreateAccount from "../../shared/ui/auth/CreateAccount"
import AuthService from "../../services/auth.service"
import useGetRedirect from "../../shared/custom-hooks/useGetRedirect"
import { useNavigate } from "react-router-dom"

const AuthForm = ({type}:FormType):React.JSX.Element => {
    useGetRedirect()
    const {register, handleSubmit, formState: {errors}} = useForm<Auth>()
    const navigate = useNavigate()
    const onSubmit: SubmitHandler<Auth> = async (data) => {
        const authService = new AuthService()
        if(type === "log-in"){
            const response = await authService.login(data.email, data.password)
            if(response === "Success"){
                navigate("/profile")
            }
        }
        if(type==="sign-up"){
            const response = await authService.register(data.username, data.email, data.password)
            if(response === "Success"){
                navigate("/profile")
            }
        }
    }

    return <div className="auth-content">
        <FormTitle type={type} />
        <form onSubmit={handleSubmit(onSubmit)}>
            <input placeholder="Email" className="form-input" {...register("email", {required:true})} />
            {type==="sign-up"?errors.username && <p className="input-alert">Username is required and must constain at least 3 letters.</p>: ""}
            {type===""&&(<input placeholder="Username" {...register("username", {required: true, minLength: 3})} className="form-input" />)}
            <input placeholder="Password" type="password" {...register("password", {required: true})} className="form-input"/>
            {type==="sign-up"?errors.password && <p className="input-alert">Password must contain at least one big letter one number and more than 6 characters</p>: ""}
            <button className="auth" type="submit">{type==="log-in"?"Log in": "Sign up"}</button>
        </form>
        {type==="log-in"? <CreateAccount />: ""}
    </div>
     
}

export default AuthForm