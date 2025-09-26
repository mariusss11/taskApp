const useCheckAuth = ():boolean => {
    const token = localStorage.getItem("userAuth")
    if(token!=null){
        return true
    }
    return false
}

export default useCheckAuth