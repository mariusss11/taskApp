import axios from "axios"

class AuthService{
    async login(email:string, password:string){
        try {
            const response = await axios.post("http://localhost:8010/api/auth/login", {
                email: email,
                password: password
            })
            if(response.status == 200){
                const responseData = response.data
                localStorage.setItem("userAuth", responseData.message)
                localStorage.setItem("userId", responseData.data.id)
                return "Success"
            }
        } catch (error) {
            console.error(error)
        }
    }

    async register(username:string, email:string, password: string){
        try {
                const response = await axios.post("http://localhost:8010/api/auth/register",{
                name:username,
                email:email,
                password:password
            })
            if(response.status == 200){
                return await this.login(email, password)
            }
        } catch (error) {
            console.error(error)
        }
        
    }

    async whoAmI(){
       
    }

    async logout(){
        localStorage.removeItem("userAuth")
    }
}

export default AuthService