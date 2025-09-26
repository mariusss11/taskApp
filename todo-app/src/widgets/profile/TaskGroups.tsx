import GroupsItem from "../../shared/ui/profile/aside/GroupsItem"
import type { AsideType } from "../../shared/types/profile.types"

const TaskGroups = ({username, paths}:AsideType) => {
    return <ul className="task-groups">
        {paths?.map((link:string, id: number) =>
            <GroupsItem key={id} username={username} path={link}/>
        )}
    </ul>
}

export default TaskGroups