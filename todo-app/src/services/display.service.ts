export function getProgressName(progress: string){
    if(progress=="not_done"||progress=="NOT_DONE"){
        return "Not done"
    }else if(progress=="IN_PROCESS"){
        return "In process"
    }
    else if(progress=="DONE"){
        return "Done"
    }
}

export function getDate(date:string){
    const newDate = new Date(date)
    const parsedDate = newDate.toLocaleDateString('de-DE')
    return parsedDate
}