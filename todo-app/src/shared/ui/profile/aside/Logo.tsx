import { Link } from "react-router-dom"

const Logo = ():React.JSX.Element => {
    return <Link to="/profile" className="logo">
        <img className="logo-img" src="/assets/logo.svg" alt="Logo" />
    </Link>
}

export default Logo