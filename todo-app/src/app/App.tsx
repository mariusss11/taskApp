import { BrowserRouter, Routes, Route } from "react-router-dom"
import LogIn from "../pages/auth/LogIn"
import SignUp from "../pages/auth/SignUp"
import "./App.css"
import StartPage from "../pages/profile/StartPage"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import TasksListPage from "../pages/profile/TasksListPage"

const queryClient = new QueryClient()

const App = ():React.JSX.Element => {
    return <QueryClientProvider client={queryClient}>
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<LogIn />}/>
                <Route path="/sign-up" element={<SignUp />}/>
                <Route path="/profile" element={<StartPage/>}/>
                <Route path="/profile/tasks-list" element={<TasksListPage />}/>
                <Route path="/profile/groups/:group" element={<>Hello world</>} />
                <Route path="*" element={<>Error 404</>} />
            </Routes>
        </BrowserRouter>
    </QueryClientProvider>
}

export default App