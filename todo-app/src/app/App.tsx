import { BrowserRouter, Routes, Route } from "react-router-dom"
import LogIn from "../pages/auth/LogIn"
import SignUp from "../pages/auth/SignUp"
import "./App.css"
import StartPage from "../pages/profile/StartPage"

const App = ():React.JSX.Element => {
    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<LogIn />}/>
            <Route path="/sign-up" element={<SignUp />}/>
            <Route path="/profile" element={<StartPage/>}/>
        </Routes>
    </BrowserRouter>
}

export default App