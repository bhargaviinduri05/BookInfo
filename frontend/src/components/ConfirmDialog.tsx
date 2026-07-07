interface ConfirmDialogProps {
  title: string;
  message: string;
  onConfirm: () => void;
  onCancel: () => void;
}

function ConfirmDialog({ title, message, onConfirm, onCancel }: ConfirmDialogProps) {
  return (
    <div className="modal-overlay" role="dialog" aria-modal="true" aria-labelledby="confirm-dialog-title">
      <div className="modal">
        <h3 id="confirm-dialog-title" className="modal__title">
          {title}
        </h3>
        <p className="modal__message">{message}</p>
        <div className="modal__actions">
          <button className="btn btn--secondary" onClick={onCancel}>
            Cancel
          </button>
          <button className="btn btn--danger" onClick={onConfirm}>
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmDialog;
