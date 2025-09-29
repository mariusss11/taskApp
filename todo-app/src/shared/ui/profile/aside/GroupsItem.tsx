import { Link } from "react-router-dom"

type GroupsItemType = {
    path: string
}

const GroupsItem = ({path}:GroupsItemType):React.JSX.Element => {
    
    return <li className="groups-item">
        <Link className="groups-item__link" to={`/profile/groups/${path}`}>{path}</Link>
    </li> 
}

export default GroupsItem