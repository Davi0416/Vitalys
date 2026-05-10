import type { ReactNode, MouseEvent } from 'react';

interface ModalProps {
  titulo: string;
  onClose: () => void;
  children: ReactNode;
}

export default function Modal({ titulo, onClose, children }: ModalProps) {
  function handleOverlayClick(e: MouseEvent<HTMLDivElement>) {
    if (e.target === e.currentTarget) onClose();
  }

  return (
    <div className="modal-overlay aberto" onClick={handleOverlayClick}>
      <div className="modal">
        <div className="modal-header">
          <h2>{titulo}</h2>
          <button className="modal-fechar" onClick={onClose}>
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2"
              strokeLinecap="round" width="16" height="16">
              <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}
