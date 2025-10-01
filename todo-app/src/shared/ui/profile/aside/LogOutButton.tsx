import { useNavigate } from "react-router-dom"

const LogOutButton = ():React.JSX.Element => {
    const navigate = useNavigate()

    function onClick(){
        localStorage.removeItem("userAuth")
        navigate("/")
    }

    return <button onClick={onClick} className="log-out__button profile-button">
        Log out <img src="/assets/arrow.svg" alt="Arrow" />
    </button>
}

export default LogOutButton