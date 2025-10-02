import type { Group, PageLaoutType } from "../../shared/types/profile.types"
import Aside from "../../widgets/profile/Aside"
import useCheckAuth from "../../shared/custom-hooks/useCheckAuth"
import { useQuery, useQueryClient } from "@tanstack/react-query"
import GroupService from "../../services/group.service"

const PageLayout = ({content}:PageLaoutType) => {
    const groupService = new GroupService()
    useQueryClient()
    const groupsQuery = useQuery({queryKey: ["groups"], queryFn: async () => await groupService.getGroups()})
    const auth = useCheckAuth()
    const paths:Group[] = groupsQuery.data?.map((item: Group) => {
        return {
            id:item.id,
            name: item.name
        }
    })
    if(auth){
        return <div className="profile-page__layout">
        <Aside paths={paths ?? null} />
        <div className="profile-content">
           {content}
        </div>
        </div>
    }
    else{
        return <h1 className="access-denied">Error: 403 <br/> Access denied</h1>
    }
}
export default PageLayout