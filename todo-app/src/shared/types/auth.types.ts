import type { NavigateFunction } from "react-router-dom"

export interface FormType{
    type: string
}

export interface CreateAccountType{
    path: string
    navigate: NavigateFunction
}

export interface Auth{
    username: string
    password: string
}