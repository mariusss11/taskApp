import { useEffect } from "react"
import { useNavigate } from "react-router-dom"

const useGetRedirect = () => {
    const navigate = useNavigate()
    
    useEffect(()=>{
        const token = localStorage.getItem("userAuth")
        if(token != null){
            navigate("/profile")
        }
    },[navigate])
}

export default useGetRedirect