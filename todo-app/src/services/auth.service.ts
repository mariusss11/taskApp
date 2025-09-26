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
            }
        } catch (error) {
            console.error(error)
        }
    }

    async register(username:string, email:string, password: string){
        try {
                const response = await axios.post("http://localhost:8010/api/auth/",{
                name:username,
                email:email,
                password:password
            })
            if(response.status == 200){
                await this.login(email, password)
            }
        } catch (error) {
            console.error(error)
        }
        
    }
}

export default AuthService