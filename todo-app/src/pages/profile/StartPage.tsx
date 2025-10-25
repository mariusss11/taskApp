import PageLayout from "./PageLayout";
import TaskForm from "../../widgets/profile/TaskForm";
import CustomModal from "../../shared/ui/CustomModal";
import useModal from "../../shared/custom-hooks/useModal";


const Content = (): React.JSX.Element => {
  const modalProperties = useModal()

  return (
    <div className="start-page">
      <h1 className="title">Create new task</h1>
      <button onClick={modalProperties.openModal} className="start-work">
        Let's start
      </button>
      <CustomModal
      title="Create task"
        form={
          <TaskForm
            closeModal={modalProperties.closeModal}
          />
        }
        closeModal={modalProperties.closeModal}
        modalIsOpen={modalProperties.modalIsOpen}
      />
    </div>
  );
};

const StartPage = (): React.JSX.Element => {
  return <PageLayout content={<Content />} />;
};

export default StartPage;
