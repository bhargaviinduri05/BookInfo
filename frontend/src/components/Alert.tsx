interface AlertProps {
  type: 'success' | 'error' | 'info';
  message: string;
  onClose?: () => void;
}

function Alert({ type, message, onClose }: AlertProps) {
  if (!message) return null;

  return (
    <div className={`alert alert--${type}`} role="alert">
      <span>{message}</span>
      {onClose && (
        <button className="alert__close" onClick={onClose} aria-label="Dismiss message">
          ×
        </button>
      )}
    </div>
  );
}

export default Alert;
