import PageLayout from "./PageLayout";
import TaskForm from "../../widgets/profile/TaskForm";
import CustomModal from "../../shared/ui/CustomModal";
import useModal from "../../shared/custom-hooks/useModal";

const Content = (): React.JSX.Element => {
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
            formTitle="Create task"
            title={undefined}
            description={undefined}
            deadline={undefined}
            group={undefined}
            groups={[1, 2, 3, 4]}
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
