import React, { useState, useEffect } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button, Form, FormGroup, Label, Input, Row, Col } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppSelector } from 'app/config/store';
import axios from 'axios';
import { toast } from 'react-toastify';

interface ProcessingModalProps {
  isOpen: boolean;
  toggle: () => void;
  coletaId: number | null;
  onSave: (coletaId: number, data: ProcessingData) => void;
}

interface ProcessingData {
  dataProcessamento: string;
  tecnicoResponsavel: string;
  valorAcidezDornic: string;
  valorCaloricoKcal: string;
  resultadoAnalise: 'APROVADO' | 'REPROVADO' | '';
  statusProcessamento: 'CONCLUIDO' | 'REJEITADO' | '';
  tipoLeite: 'COLOSTRO' | 'TRANSICAO' | 'MADURO' | '';
  classificacaoLeite: 'A' | 'B' | 'C' | '';
  localArmazenamento: string;
  temperaturaArmazenamento: string;
  coletaId: number;
}

export const ProcessingModal: React.FC<ProcessingModalProps> = ({ isOpen, toggle, coletaId, onSave }) => {
  const account = useAppSelector(state => state.authentication.account);
  const [loading, setLoading] = useState(false);

  const getInitialFormData = (): ProcessingData => ({
    dataProcessamento: new Date().toISOString().split('T')[0],
    tecnicoResponsavel: account?.login || '',
    valorAcidezDornic: '',
    valorCaloricoKcal: '',
    resultadoAnalise: '',
    statusProcessamento: '',
    tipoLeite: '',
    classificacaoLeite: '',
    localArmazenamento: '',
    temperaturaArmazenamento: '',
    coletaId: coletaId || 0,
  });

  const [formData, setFormData] = useState<ProcessingData>(getInitialFormData());

  useEffect(() => {
    if (isOpen && coletaId) {
      setFormData(getInitialFormData());
    } else if (!isOpen) {
      // Clear form when modal is closed
      setFormData(getInitialFormData());
    }
  }, [isOpen, coletaId, account?.login]);

  const handleInputChange = (field: keyof ProcessingData, value: string) => {
    setFormData(prev => {
      const newData = {
        ...prev,
        [field]: value,
      };

      if (field === 'resultadoAnalise') {
        if (value === 'APROVADO') {
          newData.statusProcessamento = 'CONCLUIDO';
        } else if (value === 'REPROVADO') {
          newData.statusProcessamento = 'REJEITADO';
        }
      }

      return newData;
    });
  };

  const handleSave = async () => {
    if (!coletaId) return;

    setLoading(true);
    try {
      const processamentoData = {
        dataProcessamento: formData.dataProcessamento,
        tecnicoResponsavel: formData.tecnicoResponsavel,
        valorAcidezDornic: parseFloat(formData.valorAcidezDornic),
        valorCaloricoKcal: parseFloat(formData.valorCaloricoKcal),
        resultadoAnalise: formData.resultadoAnalise,
        statusProcessamento: formData.statusProcessamento,
        tipoLeite: formData.tipoLeite,
        classificacaoLeite: formData.classificacaoLeite,
        localArmazenamento: formData.localArmazenamento,
        temperaturaArmazenamento: parseFloat(formData.temperaturaArmazenamento),
        coletaId: coletaId,
      };

      const response = await axios.post('/api/processamentos/com-estoque', processamentoData);

      toast.success('Processamento criado com sucesso!');

      onSave(coletaId, formData);

      toggle();
    } catch (error) {
      console.error('Erro ao criar processamento:', error);
      toast.error('Erro ao criar processamento. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    toggle();
  };

  const handleToggle = () => {
    toggle();
  };

  return (
    <Modal isOpen={isOpen} toggle={handleToggle} size="lg" centered>
      <ModalHeader toggle={handleToggle} style={{ backgroundColor: '#fff5e1', borderBottom: '2px solid #7ad27d' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', color: '#2c5530' }}>
          <FontAwesomeIcon icon="flask" style={{ color: '#7ad27d' }} />
          Processamento Laboratorial - Coleta ID {coletaId}
        </div>
      </ModalHeader>
      <ModalBody style={{ padding: '2rem', backgroundColor: '#fafafa' }}>
        <Form>
          <Row>
            <Col md={6}>
              <FormGroup>
                <Label for="dataProcessamento" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Data Processamento
                </Label>
                <Input
                  type="date"
                  id="dataProcessamento"
                  value={formData.dataProcessamento}
                  onChange={e => handleInputChange('dataProcessamento', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                />
              </FormGroup>
            </Col>
            <Col md={6}>
              <FormGroup>
                <Label for="profissionalResponsavel" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Profissional Responsável
                </Label>
                <Input
                  type="text"
                  id="profissionalResponsavel"
                  value={formData.tecnicoResponsavel}
                  onChange={e => handleInputChange('tecnicoResponsavel', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                  readOnly
                />
              </FormGroup>
            </Col>
          </Row>
          <Row>
            <Col md={6}>
              <FormGroup>
                <Label for="valorAcidezDornic" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Acidez Dornic (°D)
                </Label>
                <Input
                  type="number"
                  step="0.01"
                  id="valorAcidezDornic"
                  value={formData.valorAcidezDornic}
                  onChange={e => handleInputChange('valorAcidezDornic', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                />
              </FormGroup>
            </Col>
            <Col md={6}>
              <FormGroup>
                <Label for="valorCaloricoKcal" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Valor Calórico (kcal)
                </Label>
                <Input
                  type="number"
                  step="0.01"
                  id="valorCaloricoKcal"
                  value={formData.valorCaloricoKcal}
                  onChange={e => handleInputChange('valorCaloricoKcal', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                />
              </FormGroup>
            </Col>
          </Row>
          <Row>
            <Col md={12}>
              <FormGroup>
                <Label for="resultadoAnalise" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Resultado da Análise
                </Label>
                <Input
                  type="select"
                  id="resultadoAnalise"
                  value={formData.resultadoAnalise}
                  onChange={e => handleInputChange('resultadoAnalise', e.target.value as 'APROVADO' | 'REPROVADO')}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                >
                  <option value="">Selecione o resultado</option>
                  <option value="APROVADO">Aprovado</option>
                  <option value="REPROVADO">Reprovado</option>
                </Input>
              </FormGroup>
            </Col>
          </Row>
          <Row>
            <Col md={3}>
              <FormGroup>
                <Label for="tipoLeite" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Tipo do Leite
                </Label>
                <Input
                  type="select"
                  id="tipoLeite"
                  value={formData.tipoLeite}
                  onChange={e => handleInputChange('tipoLeite', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                  disabled={formData.resultadoAnalise !== 'APROVADO'}
                >
                  <option value="">Selecione o tipo</option>
                  <option value="COLOSTRO">Colostro</option>
                  <option value="TRANSICAO">Transição</option>
                  <option value="MADURO">Maduro</option>
                </Input>
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup>
                <Label for="classificacaoLeite" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Classificação
                </Label>
                <Input
                  type="select"
                  id="classificacaoLeite"
                  value={formData.classificacaoLeite}
                  onChange={e => handleInputChange('classificacaoLeite', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                  disabled={formData.resultadoAnalise !== 'APROVADO'}
                >
                  <option value="">Selecione a classificação</option>
                  <option value="A">Classe A</option>
                  <option value="B">Classe B</option>
                  <option value="C">Classe C</option>
                </Input>
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup>
                <Label for="localArmazenamento" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Local Armazenamento
                </Label>
                <Input
                  type="text"
                  id="localArmazenamento"
                  value={formData.localArmazenamento}
                  onChange={e => handleInputChange('localArmazenamento', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                  placeholder="Ex: FREEZER_PRINCIPAL"
                  disabled={formData.resultadoAnalise !== 'APROVADO'}
                />
              </FormGroup>
            </Col>
            <Col md={3}>
              <FormGroup>
                <Label for="temperaturaArmazenamento" style={{ color: '#2c5530', fontWeight: '600' }}>
                  Temperatura (°C)
                </Label>
                <Input
                  type="number"
                  step="0.1"
                  id="temperaturaArmazenamento"
                  value={formData.temperaturaArmazenamento}
                  onChange={e => handleInputChange('temperaturaArmazenamento', e.target.value)}
                  style={{ borderColor: '#ddeaf0', borderRadius: '8px' }}
                  placeholder="Ex: -18.0"
                  disabled={formData.resultadoAnalise !== 'APROVADO'}
                />
              </FormGroup>
            </Col>
          </Row>
        </Form>
      </ModalBody>
      <ModalFooter style={{ backgroundColor: '#f8f9fa', justifyContent: 'space-between', padding: '1rem 2rem' }}>
        <Button
          color="secondary"
          onClick={handleCancel}
          disabled={loading}
          style={{
            backgroundColor: '#6c757d',
            borderColor: '#6c757d',
            padding: '0.75rem 1.5rem',
            borderRadius: '8px',
          }}
        >
          <FontAwesomeIcon icon="times" style={{ marginRight: '0.5rem' }} />
          Cancelar
        </Button>
        <Button
          onClick={handleSave}
          disabled={
            loading ||
            !formData.resultadoAnalise ||
            !formData.valorAcidezDornic ||
            !formData.valorCaloricoKcal ||
            (formData.resultadoAnalise === 'APROVADO' &&
              (!formData.tipoLeite || !formData.classificacaoLeite || !formData.localArmazenamento || !formData.temperaturaArmazenamento))
          }
          style={{
            backgroundColor: '#7ad27d',
            borderColor: '#7ad27d',
            color: '#2c5530',
            fontWeight: '600',
            padding: '0.75rem 2rem',
            borderRadius: '8px',
            transition: 'all 0.3s ease',
            opacity:
              loading ||
              !formData.resultadoAnalise ||
              !formData.valorAcidezDornic ||
              !formData.valorCaloricoKcal ||
              (formData.resultadoAnalise === 'APROVADO' &&
                (!formData.tipoLeite || !formData.classificacaoLeite || !formData.localArmazenamento || !formData.temperaturaArmazenamento))
                ? 0.6
                : 1,
          }}
        >
          {loading ? (
            <>
              <FontAwesomeIcon icon="spinner" spin style={{ marginRight: '0.5rem' }} />
              Salvando...
            </>
          ) : (
            <>
              <FontAwesomeIcon icon="save" style={{ marginRight: '0.5rem' }} />
              Salvar Análise
            </>
          )}
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ProcessingModal;
