import AuthForm from "../../widgets/auth/AuthForm"

const LogIn = ():React.JSX.Element => {
    return <div className="auth-page__layout">
        <AuthForm type="log-in" />
    </div>
}

export default LogIn