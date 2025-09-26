import type { PageLaoutType } from "../../shared/types/profile.types"
import Aside from "../../widgets/profile/Aside"

const PageLayout = ({username, paths, content}:PageLaoutType) => {
    return <div className="profile-page__layout">
        <Aside username={username} paths={paths} />
        <div className="profile-content">
           {content}
        </div>
    </div>
}
export default PageLayout