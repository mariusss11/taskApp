import { Link } from "react-router-dom";
import AsideTitle from "../../shared/ui/profile/aside/AsideTitle";
import CloseAside from "../../shared/ui/profile/aside/CloseAside";
import Logo from "../../shared/ui/profile/aside/Logo";
import type { AsideType } from "../../shared/types/profile.types";
import TaskGroups from "./TaskGroups";
import { useRef } from "react";
import LogOutButton from "../../shared/ui/profile/aside/LogOutButton";
import ProfileLinkButton from "../../shared/ui/profile/aside/ProfileLinkButton";
import CustomModal from "../../shared/ui/CustomModal";
import useModal from "../../shared/custom-hooks/useModal";
import CreateGroupForm from "./CreateGroupForm";

const Aside = ({ paths }: AsideType): React.JSX.Element => {
  const modalProperties = useModal()

  const asideRef = useRef(null);

  return (
    <>
      <aside ref={asideRef} className="aside">
        <div className="aside-top">
          <div className="aside-top__header">
            <Logo />
            <CloseAside reference={asideRef} />
          </div>
          <div className="aside-top__footer">
            <div className="aside-footer__top">
              <AsideTitle text="Tasks" />
              <Link
                className="profile-link aside-link"
                to={`/profile/tasks-list`}
              >
                Tasks list
              </Link>
            </div>
            <div className="aside-footer__bottom">
              <AsideTitle text="Task groups" />
              <button onClick={modalProperties.openModal} className="create-group groups-item">
                + Add new group
              </button>
              <TaskGroups paths={paths} />
            </div>
          </div>
        </div>
        <div className="aside-bottom">
          <LogOutButton />
          <ProfileLinkButton />
        </div>
      </aside>
      <CustomModal
        title="Create group"
        form={<CreateGroupForm closeModal={modalProperties.closeModal} />}
        closeModal={modalProperties.closeModal}
        modalIsOpen={modalProperties.modalIsOpen}
      />
    </>
  );
};

export default Aside;
