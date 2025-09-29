import type { Group, PageLaoutType } from "../../shared/types/profile.types"
import Aside from "../../widgets/profile/Aside"
import useCheckAuth from "../../shared/custom-hooks/useCheckAuth"
import { useQuery, useQueryClient } from "@tanstack/react-query"
import TaskService from "../../services/task.service"

const PageLayout = ({content}:PageLaoutType) => {
    const taskService = new TaskService()
    const queryClient = useQueryClient()
    const groupsQuery = useQuery({queryKey: ["groups"], queryFn: async () => await taskService.getTasks()})
    const auth = useCheckAuth()
    const paths = groupsQuery.data?.map((item: Group) => {
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
        <h1 className="access-denied">Error: 403 <br/> Access denied</h1>
    }
}
export default PageLayout