import axios from "axios"
import type { TaskType } from "../shared/types/task.types"

class TaskService{
    private path = "http://localhost:8010/api/tasks/"

    async createTask(title: string, description: string, deadline: string, groupId: number){
        const token = localStorage.getItem("userAuth")
        try {
            const response = await axios.post(`${this.path}create`, {
                title: title,
                description: description,
                deadline: deadline,
                groupId: groupId
            },{
                headers:{
                    Authorization: `Bearer ${token}`
                }
            })

            if(response.status == 200){
                return "Success"
            }
        } catch (error) {
            console.error(error)
        }
    }

    async getAllEnabledTasks(){
        const token = localStorage.getItem("userAuth")
        try {
            const response = await axios.get(`${this.path}allEnabled`,{
                headers:{
                    Authorization: `Bearer ${token}`
                }
            })
            if(response.status == 200){
                const responseData = response.data
                const organizedData = responseData.map((task) => {
                    return {
                        id: task.id,
                        title: task.title,
                        description: task.description,
                        deadline: task.deadline,
                        creationDate: task.createdAt,
                        progress: task.status,
                        groupName: task.groupName
                    }
                })
                const uniqueDates:string[] = [...new Set(organizedData.map((data: TaskType) => data.deadline))];

                const filteredData = uniqueDates.map((deadline:string) => {
                    return {
                        deadline: deadline,
                        tasks: organizedData.filter((task:TaskType) => {
                            if(task && deadline == task.deadline){
                                return task
                            }
                            return undefined
                        }).filter(Boolean)
                    }
                })

                return filteredData
            }
        } catch (error) {
            console.error(error)
        }
    }
    async changeTaskStatus(id:number, status: string){
        try {
            const token = localStorage.getItem("userAuth")
            const response = await axios.put(`${this.path}changeStatus`,{
                taskId: id,
                newStatus: status
            },
            {
                headers:{
                    Authorization: `Bearer ${token}`
                }
            })
            return response.status
        } catch (error) {
            console.error(error)
        }
    }
}

export default TaskService