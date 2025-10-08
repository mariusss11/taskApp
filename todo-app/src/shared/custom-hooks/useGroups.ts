import GroupService from "../../services/group.service"
import { useQueryClient, useQuery } from "@tanstack/react-query"
import type { Group } from "../types/profile.types"

const useGroups = () => {
    const groupService = new GroupService()
    useQueryClient()
    const groupsQuery = useQuery({queryKey: ["groups"], queryFn: async () => await groupService.getGroups()})
   
    const paths:Group[] = groupsQuery.data?.map((item: Group) => {
        return {
            id:item.id,
            name: item.name
        }
    })

    return paths
}

export default useGroups