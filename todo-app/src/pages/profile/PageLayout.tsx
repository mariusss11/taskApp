import type { PageLaoutType } from "../../shared/types/profile.types"
import Aside from "../../widgets/profile/Aside"
import useCheckAuth from "../../shared/custom-hooks/useCheckAuth"
import { useNavigate } from "react-router-dom"

const PageLayout = ({username, paths, content}:PageLaoutType) => {
    const auth = useCheckAuth()
    const navigate = useNavigate()
    if(auth){
        return <div className="profile-page__layout">
        <Aside username={username} paths={paths} />
        <div className="profile-content">
           {content}
        </div>
        </div>
    }
    else{
        alert("Unauthorized access")
        navigate("/")
    }
}
export default PageLayout