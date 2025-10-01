import axios from "axios";



class GroupService{
    async getGroups(){
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

    async createGroup(name: string, description: string){
        try{
            const token = localStorage.getItem("userAuth")
            const response = await axios.post("http://localhost:8010/api/groups/create", {
                name: name,
                description: description
            },
            {
                headers:{
                    Authorization: `Bearer ${token}`
                }
            })
            if(response.status == 200){
                return "Success"
            }
        }catch(error){
            console.error("Something went wrong ", error)
        }
    }
}
export default GroupService