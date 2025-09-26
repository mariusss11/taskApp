export interface AsideType{
    username: string
    paths: string[] | null
}

export interface AsideRef{
    reference: React.RefObject<HTMLElement | null>
}

export interface PageLaoutType{
    username: string
    paths: string[] | null
    content: React.JSX.Element
}