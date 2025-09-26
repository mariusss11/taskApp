import AuthForm from "../../widgets/auth/AuthForm"

const SignUp = ():React.JSX.Element => {
    return <div className="auth-page__layout">
        <AuthForm type="sign-up"/>
    </div>
}

export default SignUp