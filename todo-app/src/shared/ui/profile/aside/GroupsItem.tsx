import { Link } from "react-router-dom"

type GroupsItemType = {
    username:string
    path: string
}

const GroupsItem = ({username, path}:GroupsItemType):React.JSX.Element => {
    
    return <li className="groups-item">
        <Link className="groups-item__link" to={`/profile/${username}/groups/${path}`}>{path}</Link>
    </li> 
}

export default GroupsItem