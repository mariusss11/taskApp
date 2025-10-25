import type { AsideRef } from "../../../types/profile.types"
import { useRef } from "react"

const CloseAside = ({reference}:AsideRef):React.JSX.Element => {

    const closeButton = useRef<HTMLButtonElement | null>(null)

    function onClick(){
        if(reference.current && closeButton.current){
            reference.current.classList.toggle("active")
            closeButton.current.classList.toggle("active")
        }
    }

    return <button ref={closeButton} onClick={onClick} className="close-aside">
        <div className="l-1" ></div>
        <div className="l-2"></div>
        <div className="l-3"></div>
    </button>
}

export default CloseAside