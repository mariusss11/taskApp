import { Link } from "react-router-dom"

const ProfileLinkButton = ({username}:{username: string}):React.JSX.Element => {
    return <Link to={`/profile/${username}/profile-settings`} className="user-profile__link profile-button"><img src="/assets/user.svg" alt="User" /></Link>
}

export default ProfileLinkButton