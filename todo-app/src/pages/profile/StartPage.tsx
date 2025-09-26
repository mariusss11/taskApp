import PageLayout from "./PageLayout";
import Modal from "react-modal"
import TaskForm from "../../widgets/profile/TaskForm";
import { useState } from "react";

Modal.setAppElement("#root")
const customStyles = {
  content: {
    top: '50%',
    left: '50%',
    right: 'auto',
    bottom: 'auto',
    marginRight: '-50%',
    transform: 'translate(-50%, -50%)',
    color: "#000"
  },
};
const Content = ():React.JSX.Element => {

  const [modalIsOpen, setIsOpen] = useState(false);

  function openModal() {
    setIsOpen(true);
  }

  function closeModal() {
    setIsOpen(false);
  }

  function onClick(){
    openModal()
  }

  return <div className="start-page">
    <h1 className="title">Create a new task</h1>
    <button onClick={onClick} className="start-work">Let's start</button>
    <Modal
      isOpen={modalIsOpen}
      onRequestClose={closeModal}
      style = {customStyles}
    >
      <TaskForm formTitle="Create task" title={undefined} description={undefined} deadline={undefined} group={undefined} groups={[1,2,3,4]} progress={undefined} />
      <button onClick={closeModal}>close</button>
    </Modal>
  </div>
}

const StartPage = (): React.JSX.Element => {
  return (
    <PageLayout username="Hello" paths={["study", "user"]} content={<Content />} />
  );
};

export default StartPage;
