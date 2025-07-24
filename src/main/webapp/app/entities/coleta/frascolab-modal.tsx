import React from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

interface FrascoLabModalProps {
  isOpen: boolean;
  toggle: () => void;
  coletaId: number | null;
  onConfirm: (coletaId: number) => void;
}

export const FrascoLabModal: React.FC<FrascoLabModalProps> = ({ isOpen, toggle, coletaId, onConfirm }) => {
  const handleConfirm = () => {
    if (coletaId) {
      onConfirm(coletaId);
    }
    toggle();
  };

  return (
    <Modal isOpen={isOpen} toggle={toggle} centered>
      <ModalHeader toggle={toggle} style={{ backgroundColor: '#fff5e1', borderBottom: '2px solid #7ad27d' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#2c5530' }}>
          <img src="content/images/frascolab.svg" alt="Frascolab" style={{ width: '24px', height: '24px' }} />
          Análise de Laboratório
        </div>
      </ModalHeader>
      <ModalBody style={{ padding: '2rem', textAlign: 'center', color: '#2c5530' }}>
        <div style={{ marginBottom: '1rem' }}>
          <FontAwesomeIcon icon="flask" size="3x" style={{ color: '#7ad27d', marginBottom: '1rem' }} />
        </div>
        <h5 style={{ marginBottom: '1rem', color: '#2c5530' }}>Iniciar análise da coleta ID {coletaId}?</h5>
        <p style={{ color: '#666', fontSize: '14px' }}>Esta ação irá abrir o formulário de processamento laboratorial.</p>
      </ModalBody>
      <ModalFooter style={{ backgroundColor: '#f8f9fa', justifyContent: 'center', gap: '1rem' }}>
        <Button
          color="secondary"
          onClick={toggle}
          style={{
            backgroundColor: '#6c757d',
            borderColor: '#6c757d',
            padding: '0.5rem 1.5rem',
            borderRadius: '8px',
          }}
        >
          Cancelar
        </Button>
        <Button
          onClick={handleConfirm}
          style={{
            backgroundColor: '#7ad27d',
            borderColor: '#7ad27d',
            color: '#2c5530',
            fontWeight: '600',
            padding: '0.5rem 1.5rem',
            borderRadius: '8px',
            transition: 'all 0.3s ease',
          }}
        >
          <FontAwesomeIcon icon="check" style={{ marginRight: '0.5rem' }} />
          Iniciar Processamento
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default FrascoLabModal;
