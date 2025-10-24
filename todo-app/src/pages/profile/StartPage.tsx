import PageLayout from "./PageLayout";
import TaskForm from "../../widgets/profile/TaskForm";
import CustomModal from "../../shared/ui/CustomModal";
import useModal from "../../shared/custom-hooks/useModal";
import type { Group } from "../../shared/types/profile.types";
import useGroups from "../../shared/custom-hooks/useGroups";

type ContentType = {
  groups: Group[]
}

const Content = ({groups}:ContentType): React.JSX.Element => {
  const modalProperties = useModal()

  return (
    <div className="start-page">
      <h1 className="title">Create a new task</h1>
      <button onClick={modalProperties.openModal} className="start-work">
        Let's start
      </button>
      <CustomModal
      title="Create task"
        form={
          <TaskForm
            closeModal={modalProperties.closeModal}
            groups={groups}
          />
        }
        closeModal={modalProperties.closeModal}
        modalIsOpen={modalProperties.modalIsOpen}
      />
    </div>
  );
};

const StartPage = (): React.JSX.Element => {
  const paths = useGroups()

  return <PageLayout paths={paths} content={<Content groups={paths} />} />;
};

export default StartPage;
