import Modal from "react-modal"
import type { ModalType } from "../types/profile.types";

Modal.setAppElement("#root")
const customStyles = {
  content: {
    top: '50%',
    left: '50%',
    right: 'auto',
    bottom: 'auto',
    marginRight: '-50%',
    transform: 'translate(-50%, -50%)',
    color: "#000",
    maxWidth: 524,
    width: "100%",
    borderRadius: 5,
    padding: 40
  },
};

const CustomModal = ({form, modalIsOpen, closeModal,title}:ModalType) => {

    return <Modal isOpen={modalIsOpen} onRequestClose={closeModal} style = {customStyles}>
      <div className="modal-header">
        <h2 className="modal-title">{title}</h2>
        <button className="close-modal" onClick={closeModal}><img src="/assets/close-form.svg" alt="Close form" /></button>
      </div>
      {form}
    </Modal>
}

export default CustomModal