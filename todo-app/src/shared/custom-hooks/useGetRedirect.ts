import { useNavigate } from "react-router-dom"

const useGetRedirect = () => {
    const navigate = useNavigate()
    const token = localStorage.getItem("userAuth")
    if(token != null){
        navigate("/profile")
    }
}

export default useGetRedirect