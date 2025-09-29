import { Link } from "react-router-dom";
import AsideTitle from "../../shared/ui/profile/aside/AsideTitle";
import CloseAside from "../../shared/ui/profile/aside/CloseAside";
import Logo from "../../shared/ui/profile/aside/Logo";
import type { AsideType } from "../../shared/types/profile.types";
import TaskGroups from "./TaskGroups";
import { useRef } from "react";
import LogOutButton from "../../shared/ui/profile/aside/LogOutButton";
import ProfileLinkButton from "../../shared/ui/profile/aside/ProfileLinkButton";

const Aside = ({paths}:AsideType): React.JSX.Element => {

  const asideRef = useRef(null)

  return (
    <aside ref={asideRef} className="aside">
      <div className="aside-top">
        <div className="aside-top__header">
          <Logo />
          <CloseAside reference={asideRef} />
        </div>
        <div className="aside-top__footer">
            <div className="aside-footer__top">
                <AsideTitle text="Tasks" />
                <Link className="profile-link aside-link" to={`/profile/tasks-list`}>Tasks list</Link>  
            </div>
            <div className="aside-footer__bottom">
                <AsideTitle text="Task groups" />
                <TaskGroups paths={paths}/>
            </div>
            
        </div>
      </div>
      <div className="aside-bottom">
        <LogOutButton />
        <ProfileLinkButton/>
      </div>
    </aside>
  );
};

export default Aside;
