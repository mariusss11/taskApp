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