import GroupsItem from "../../shared/ui/profile/aside/GroupsItem"
import type { AsideType, Group } from "../../shared/types/profile.types"

const TaskGroups = ({paths}:AsideType) => {
    console.log(paths)
    return <ul className="task-groups">
        {paths?.map((link:Group) =>
            <GroupsItem key={link.id} path={link.name}/>
        )}
    </ul>
}

export default TaskGroups