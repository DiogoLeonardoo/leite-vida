import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEstoques } from 'app/entities/estoque/estoque.reducer';
import { getEntities as getPacientes } from 'app/entities/paciente/paciente.reducer';
import { createEntity, getEntity, reset, updateEntity } from './distribuicao.reducer';
import './distribuicao-update.scss';

export const DistribuicaoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estoques = useAppSelector(state => state.estoque.entities);
  const pacientes = useAppSelector(state => state.paciente.entities);
  const distribuicaoEntity = useAppSelector(state => state.distribuicao.entity);
  const loading = useAppSelector(state => state.distribuicao.loading);
  const updating = useAppSelector(state => state.distribuicao.updating);
  const updateSuccess = useAppSelector(state => state.distribuicao.updateSuccess);
  const account = useAppSelector(state => state.authentication.account);

  const [formData, setFormData] = useState({
    estoqueId: '',
    dataDistribuicao: '',
    volumeDistribuidoMl: '',
    responsavelEntrega: account?.firstName || '',
    observacoes: '',
    pacienteId: '',
  });

  const [formErrors, setFormErrors] = useState({
    estoqueId: '',
    dataDistribuicao: '',
    volumeDistribuidoMl: '',
    responsavelEntrega: '',
    observacoes: '',
    pacienteId: '',
  });

  const [estoqueDisponivel, setEstoqueDisponivel] = useState({
    volume: 0,
    tipoLeite: '',
    classificacaoLeite: '',
  });

  // Add state for confirmation modal
  const [confirmModalOpen, setConfirmModalOpen] = useState(false);

  const handleClose = () => {
    navigate(`/distribuicao${location.search}`);
  };

  const handleInputChange = (field: string, value: string) => {
    let processedValue = value;
    if (field === 'volumeDistribuidoMl') {
      processedValue = value.replace(/\D/g, '');

      // Não permitir valor maior que o volume disponível
      if (processedValue && Number(processedValue) > estoqueDisponivel.volume) {
        processedValue = estoqueDisponivel.volume.toString();
      }
    }

    setFormData(prev => ({
      ...prev,
      [field]: processedValue,
    }));

    // Se mudou o estoque, buscar informações do volume disponível
    if (field === 'estoqueId' && value) {
      const estoqueSelected = estoques.find(e => e.id?.toString() === value);
      if (estoqueSelected) {
        const volumeDisponivel = estoqueSelected.volumeDisponivelMl || estoqueSelected.volumeAtualMl || 0;
        setEstoqueDisponivel({
          volume: volumeDisponivel,
          tipoLeite: estoqueSelected.tipoLeite || '',
          classificacaoLeite: estoqueSelected.classificacaoLeite || '',
        });

        // Limpar o volume quando trocar de estoque para forçar nova seleção
        setFormData(prev => ({
          ...prev,
          [field]: processedValue,
          volumeDistribuidoMl: '',
        }));
      }
    } else if (field === 'estoqueId' && !value) {
      setEstoqueDisponivel({
        volume: 0,
        tipoLeite: '',
        classificacaoLeite: '',
      });
      // Limpar e bloquear o volume quando desmarcar o estoque
      setFormData(prev => ({
        ...prev,
        [field]: processedValue,
        volumeDistribuidoMl: '',
      }));
    }

    if (formErrors[field]) {
      setFormErrors(prev => ({
        ...prev,
        [field]: '',
      }));
    }
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstoques({}));
    dispatch(getPacientes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && distribuicaoEntity) {
      setFormData({
        estoqueId: distribuicaoEntity.estoque?.id?.toString() || '',
        dataDistribuicao: distribuicaoEntity.dataDistribuicao || '',
        volumeDistribuidoMl: distribuicaoEntity.volumeDistribuidoMl ? distribuicaoEntity.volumeDistribuidoMl.toString() : '',
        responsavelEntrega: distribuicaoEntity.responsavelEntrega || '',
        observacoes: distribuicaoEntity.observacoes || '',
        pacienteId: distribuicaoEntity.paciente?.id?.toString() || '',
      });

      // Carregar informações do estoque se já existe uma distribuição
      if (distribuicaoEntity.estoque?.id) {
        const estoqueSelected = estoques.find(e => e.id === distribuicaoEntity.estoque?.id);
        if (estoqueSelected) {
          const volumeDisponivel = estoqueSelected.volumeDisponivelMl || estoqueSelected.volumeAtualMl || 0;
          setEstoqueDisponivel({
            volume: volumeDisponivel,
            tipoLeite: estoqueSelected.tipoLeite || '',
            classificacaoLeite: estoqueSelected.classificacaoLeite || '',
          });
        }
      }
    }
  }, [distribuicaoEntity, isNew, estoques]);

  const toggleConfirmModal = () => {
    setConfirmModalOpen(!confirmModalOpen);
  };

  // Fix the async function by removing the async keyword since it doesn't use await
  const saveEntity = values => {
    // Validação antes de enviar
    if (!formData.estoqueId || !formData.pacienteId) {
      alert('Por favor, selecione um estoque e um paciente.');
      return;
    }

    if (!formData.volumeDistribuidoMl || Number(formData.volumeDistribuidoMl) <= 0) {
      alert('Por favor, informe um volume válido para distribuição.');
      return;
    }

    if (Number(formData.volumeDistribuidoMl) > estoqueDisponivel.volume) {
      alert(`Volume não pode ser maior que o disponível no estoque (${estoqueDisponivel.volume} mL)`);
      return;
    }

    // Instead of saving directly, open confirmation modal
    toggleConfirmModal();
  };

  // Separate async function for the actual save operation
  const confirmAndSave = async () => {
    // Estrutura conforme sua API espera
    const entity = {
      estoqueId: Number(formData.estoqueId),
      pacienteId: Number(formData.pacienteId),
      volumeDistribuidoMl: Number(formData.volumeDistribuidoMl),
      responsavelEntrega: formData.responsavelEntrega,
      responsavelRecebimento: formData.responsavelEntrega, // envia o mesmo valor
      observacoes: formData.observacoes || '',
    };

    console.log('Enviando dados:', JSON.stringify(entity, null, 2)); // Para debug

    try {
      let response;
      if (isNew) {
        response = await axios.post('/api/distribuicaos/realizar-distribuicao', entity, {
          responseType: 'blob', // Importante para receber o PDF como blob
          headers: {
            'Content-Type': 'application/json',
          },
        });
      } else {
        response = await axios.put(`/api/distribuicaos/${distribuicaoEntity?.id}`, entity, {
          responseType: 'blob', // Importante para receber o PDF como blob
          headers: {
            'Content-Type': 'application/json',
          },
        });
      }

      // Verifica se a resposta é um PDF
      const contentType = response.headers['content-type'];
      if (contentType && contentType.includes('application/pdf')) {
        // Cria um URL do blob e abre em uma nova aba
        const blob = new Blob([response.data], { type: 'application/pdf' });
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');

        // Libera o objeto URL após um tempo
        setTimeout(() => URL.revokeObjectURL(url), 10000);
      } else {
        console.log('Resposta do servidor:', response.data);
      }

      toggleConfirmModal();
      handleClose();
    } catch (error) {
      console.error('Erro ao salvar:', error);
      console.error('Dados do erro:', error.response?.data);
      toggleConfirmModal();

      // Se o erro retornar um blob, pode ser uma mensagem de erro em formato de texto
      if (error.response?.data instanceof Blob) {
        const reader = new FileReader();
        reader.onload = () => {
          const errorMessage = reader.result as string;
          alert(`Erro ao realizar a distribuição: ${errorMessage}`);
        };
        reader.readAsText(error.response.data);
      } else {
        alert('Erro ao realizar a distribuição. Verifique os dados e tente novamente.');
      }
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...distribuicaoEntity,
          estoque: distribuicaoEntity?.estoque?.id,
          paciente: distribuicaoEntity?.paciente?.id,
        };

  return (
    <div className="distribuicao-update-page">
      {/* Confirmation Modal */}
      <Modal isOpen={confirmModalOpen} toggle={toggleConfirmModal}>
        <ModalHeader toggle={toggleConfirmModal}>Confirmar Distribuição</ModalHeader>
        <ModalBody>
          <div className="confirmation-details">
            <p>
              <strong>Você está prestes a distribuir:</strong>
            </p>
            <ul>
              <li>
                <strong>Volume:</strong> {formData.volumeDistribuidoMl} mL
              </li>
              <li>
                <strong>Estoque ID:</strong> {formData.estoqueId}
                {estoqueDisponivel.tipoLeite && (
                  <span>
                    {' '}
                    ({estoqueDisponivel.tipoLeite} - {estoqueDisponivel.classificacaoLeite})
                  </span>
                )}
              </li>
              <li>
                <strong>Paciente ID:</strong> {formData.pacienteId}
                {pacientes?.find(p => p.id?.toString() === formData.pacienteId)?.nome && (
                  <span> ({pacientes.find(p => p.id?.toString() === formData.pacienteId)?.nome})</span>
                )}
              </li>
              <li>
                <strong>Responsável pela Entrega:</strong> {formData.responsavelEntrega}
              </li>
            </ul>
            <p className="text-danger">Esta ação não pode ser desfeita. Deseja continuar?</p>
          </div>
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleConfirmModal}>
            Cancelar
          </Button>
          <Button color="primary" onClick={confirmAndSave} disabled={updating}>
            {updating ? 'Processando...' : 'Confirmar Distribuição'}
          </Button>
        </ModalFooter>
      </Modal>

      <div className="container">
        <Row className="justify-content-center">
          <Col lg="10">
            <div className="d-flex align-items-center mb-4">
              <h2 className="mb-0">
                <FontAwesomeIcon icon="clipboard-list" className="me-2" />
                {isNew ? 'Nova Distribuição de Leite' : 'Editar Distribuição de Leite'}
              </h2>
            </div>
            {loading ? (
              <div className="text-center">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Carregando...</span>
                </div>
              </div>
            ) : (
              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                <div className="card mb-3">
                  <div className="card-header bg-warning">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="id-card" className="me-2" />
                      Seleção de Estoque e Paciente
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            id="distribuicao-estoque"
                            name="estoque"
                            data-cy="estoque"
                            label={translate('leiteVidaApp.distribuicao.estoque')}
                            type="select"
                            required
                            className="form-select"
                            value={formData.estoqueId}
                            onChange={e => handleInputChange('estoqueId', e.target.value)}
                          >
                            <option value="" key="0">
                              Selecione um estoque
                            </option>
                            {estoques
                              ? estoques
                                  .filter(estoque => estoque.statusLote === 'DISPONIVEL')
                                  .map(estoque => (
                                    <option value={estoque.id} key={estoque.id}>
                                      ID: {estoque.id} | {estoque.tipoLeite} | {estoque.classificacao} |{' '}
                                      {estoque.volumeDisponivelMl || estoque.volumeAtualMl || 0} mL
                                    </option>
                                  ))
                              : null}
                          </ValidatedField>
                          <FormText>
                            <Translate contentKey="entity.validation.required">This field is required.</Translate>
                          </FormText>
                          {estoqueDisponivel.tipoLeite && (
                            <FormText color="success">
                              <strong>Selecionado:</strong> {estoqueDisponivel.tipoLeite} - {estoqueDisponivel.classificacaoLeite}
                              <br />
                              <strong>Volume disponível:</strong> {estoqueDisponivel.volume} mL
                            </FormText>
                          )}
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            id="distribuicao-paciente"
                            name="paciente"
                            data-cy="paciente"
                            label={translate('leiteVidaApp.distribuicao.paciente')}
                            type="select"
                            required
                            className="form-select"
                            value={formData.pacienteId}
                            onChange={e => handleInputChange('pacienteId', e.target.value)}
                          >
                            <option value="" key="0">
                              Selecione um paciente
                            </option>
                            {pacientes
                              ? pacientes.map(paciente => (
                                  <option value={paciente.id} key={paciente.id}>
                                    ID: {paciente.id} | {paciente.nome || 'Nome não informado'}
                                  </option>
                                ))
                              : null}
                          </ValidatedField>
                          <FormText>
                            <Translate contentKey="entity.validation.required">This field is required.</Translate>
                          </FormText>
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-info">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="calendar-alt" className="me-2" />
                      Volume da Distribuição
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.dataDistribuicao')}
                            id="distribuicao-dataDistribuicao"
                            name="dataDistribuicao"
                            data-cy="dataDistribuicao"
                            type="date"
                            className="form-control"
                            value={formData.dataDistribuicao}
                            onChange={e => handleInputChange('dataDistribuicao', e.target.value)}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                            }}
                          />
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.volumeDistribuidoMl')}
                            id="distribuicao-volumeDistribuidoMl"
                            name="volumeDistribuidoMl"
                            data-cy="volumeDistribuidoMl"
                            type="text"
                            className="form-control"
                            placeholder={!formData.estoqueId ? 'Selecione um estoque primeiro' : 'Volume distribuído em mL'}
                            value={formData.volumeDistribuidoMl}
                            onChange={e => handleInputChange('volumeDistribuidoMl', e.target.value)}
                            disabled={!formData.estoqueId}
                            max={estoqueDisponivel.volume}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                              validate: v => {
                                if (!isNumber(v)) return translate('entity.validation.number');
                                if (Number(v) > estoqueDisponivel.volume) {
                                  return `Volume não pode ser maior que ${estoqueDisponivel.volume} mL`;
                                }
                                if (Number(v) <= 0) return 'Volume deve ser maior que zero';
                                return true;
                              },
                            }}
                          />
                          {!formData.estoqueId ? (
                            <FormText color="warning">
                              <FontAwesomeIcon icon="exclamation-triangle" className="me-1" />
                              Selecione um estoque para habilitar este campo
                            </FormText>
                          ) : (
                            <FormText color="info">
                              Volume máximo disponível: <strong>{estoqueDisponivel.volume} mL</strong>
                            </FormText>
                          )}
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-success">
                    <h5 className="mb-0">Responsável</h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.responsavelEntrega')}
                            id="distribuicao-responsavelEntrega"
                            name="responsavelEntrega"
                            data-cy="responsavelEntrega"
                            type="text"
                            className="form-control"
                            placeholder="Responsável pela entrega"
                            value={formData.responsavelEntrega}
                            onChange={e => handleInputChange('responsavelEntrega', e.target.value)}
                            disabled={true}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                            }}
                          />
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-sucess">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="sticky-note" className="me-2" />
                      Observações Adicionais
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="12">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.observacoes')}
                            id="distribuicao-observacoes"
                            name="observacoes"
                            data-cy="observacoes"
                            type="textarea"
                            className="form-control"
                            rows="3"
                            placeholder="Observações adicionais (opcional)"
                            value={formData.observacoes}
                            onChange={e => handleInputChange('observacoes', e.target.value)}
                          />
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="d-flex justify-content-between align-items-center">
                  <Button tag={Link} to="/distribuicao" color="secondary" size="lg">
                    <FontAwesomeIcon icon="times" className="me-2" />
                    Cancelar
                  </Button>
                  <Button color="primary" type="submit" disabled={updating} size="lg">
                    <FontAwesomeIcon icon={updating ? 'spinner' : 'save'} spin={updating} className="me-2" />
                    {updating ? 'Salvando...' : 'Salvar Distribuição'}
                  </Button>
                </div>
              </ValidatedForm>
            )}
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DistribuicaoUpdate;
