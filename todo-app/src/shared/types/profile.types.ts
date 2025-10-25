import type { MouseEventHandler } from "react"

export interface AsideType{
    paths: Group[] | null
}

export interface AsideRef{
    reference: React.RefObject<HTMLElement | null>
}

export interface PageLaoutType{
    content: React.JSX.Element
}

export interface Group{
    id: number,
    name: string
}

export interface CreateGroupType{
    name: string,
    description: string
}

export interface ModalType{
  form: React.JSX.Element
  closeModal: MouseEventHandler<HTMLButtonElement> | undefined
  modalIsOpen: boolean
  title: string
}