import axios from "axios";

class TaskService{
    async getTasks(){
        try {
            const token = localStorage.getItem("userAuth")
            const response = await axios.get("http://localhost:8010/api/groups/all", {headers: {Authorization: `Bearer ${token}`}})
            if(response.status == 200){
                return response.data
            }
        } catch (error) {
            return "Something went wrong " + error;
            
        }
    }
}
export default TaskService