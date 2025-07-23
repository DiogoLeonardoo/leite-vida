import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProcessamentos } from 'app/entities/processamento/processamento.reducer';
import { getEntities as getDoadoras } from 'app/entities/doadora/doadora.reducer';
import { StatusColeta } from 'app/shared/model/enumerations/status-coleta.model';
import { createEntity, getEntity, reset, updateEntity } from './coleta.reducer';
import { validateCPF, maskCPF, removeMask, validationMessages } from 'app/shared/util/validation-utils';

import './coleta-update.scss';

export const ColetaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doadoras = useAppSelector(state => state.doadora.entities);
  const coletaEntity = useAppSelector(state => state.coleta.entity);
  const loading = useAppSelector(state => state.coleta.loading);
  const updating = useAppSelector(state => state.coleta.updating);
  const updateSuccess = useAppSelector(state => state.coleta.updateSuccess);

  const [cpfInput, setCpfInput] = useState('');
  const [selectedDoadora, setSelectedDoadora] = useState(null);
  const [cpfError, setCpfError] = useState('');
  const [searchingDoadora, setSearchingDoadora] = useState(false);
  const [doadoraTestError, setDoadoraTestError] = useState('');

  const [formData, setFormData] = useState({
    dataColeta: '',
    localColeta: '',
    volumeMl: '',
    temperatura: '',
    observacoes: '',
  });

  const [formErrors, setFormErrors] = useState({
    dataColeta: '',
    localColeta: '',
    volumeMl: '',
    temperatura: '',
    observacoes: '',
  });

  const [isFormValid, setIsFormValid] = useState(false);

  const handleClose = () => {
    navigate(`/coleta${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProcessamentos({}));
    dispatch(getDoadoras({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && coletaEntity) {
      setFormData({
        dataColeta: coletaEntity.dataColeta || '',
        localColeta: coletaEntity.localColeta || '',
        volumeMl: coletaEntity.volumeMl ? coletaEntity.volumeMl.toString() : '',
        temperatura: coletaEntity.temperatura ? coletaEntity.temperatura.toString() : '',
        observacoes: coletaEntity.observacoes || '',
      });

      if (coletaEntity.doadora && coletaEntity.doadora.id) {
        if (coletaEntity.doadora.cpf) {
          setSelectedDoadora(coletaEntity.doadora);
          setCpfInput(maskCPF(coletaEntity.doadora.cpf));
        } else {
          fetchDoadoraById(coletaEntity.doadora.id);
        }
      }
    }
  }, [coletaEntity, isNew]);

  const checkDoadoraTestResults = doadora => {
    const positiveTests = [];
    const notInformedTests = [];

    // Check for positive results
    if (doadora.resultadoVDRL === 'POSITIVO') {
      positiveTests.push('VDRL');
    }
    if (doadora.resultadoHBsAg === 'POSITIVO') {
      positiveTests.push('HBsAg');
    }
    if (doadora.resultadoFTAabs === 'POSITIVO') {
      positiveTests.push('FTA-abs');
    }
    if (doadora.resultadoHIV === 'POSITIVO') {
      positiveTests.push('HIV');
    }

    // Check for not informed results
    if (doadora.resultadoVDRL === 'NAO_INFORMADO') {
      notInformedTests.push('VDRL');
    }
    if (doadora.resultadoHBsAg === 'NAO_INFORMADO') {
      notInformedTests.push('HBsAg');
    }
    if (doadora.resultadoFTAabs === 'NAO_INFORMADO') {
      notInformedTests.push('FTA-abs');
    }
    if (doadora.resultadoHIV === 'NAO_INFORMADO') {
      notInformedTests.push('HIV');
    }

    if (positiveTests.length > 0) {
      const testsString = positiveTests.join(', ');
      setDoadoraTestError(`Doadora não pode fazer coleta. Resultado(s) positivo(s): ${testsString}`);
      return false;
    }

    if (notInformedTests.length > 0) {
      const testsString = notInformedTests.join(', ');
      setDoadoraTestError(`Doadora não pode fazer coleta. Exame(s) não informado(s): ${testsString}`);
      return false;
    }

    setDoadoraTestError('');
    return true;
  };

  const searchDoadoraByCpf = async (cpf: string) => {
    setSearchingDoadora(true);
    try {
      const response = await axios.get(`/api/doadoras/find-by-cpf/${cpf}`);
      if (response.data) {
        const isValidForColeta = checkDoadoraTestResults(response.data);
        setSelectedDoadora(response.data);
        setCpfError('');
      } else {
        setCpfError('Doadora não encontrada');
        setSelectedDoadora(null);
        setDoadoraTestError('');
      }
    } catch (error) {
      if (error.response?.status === 404) {
        setCpfError('Doadora não encontrada');
      } else {
        setCpfError('Erro ao buscar doadora');
      }
      setSelectedDoadora(null);
      setDoadoraTestError('');
    } finally {
      setSearchingDoadora(false);
    }
  };

  const fetchDoadoraById = async (doadoraId: number) => {
    try {
      const response = await axios.get(`/api/doadoras/${doadoraId}`);
      if (response.data) {
        const isValidForColeta = checkDoadoraTestResults(response.data);
        setSelectedDoadora(response.data);
        if (response.data.cpf) {
          setCpfInput(maskCPF(response.data.cpf));
        }
      }
    } catch (error) {
      console.error('Error fetching doadora:', error);
    }
  };

  const handleInputChange = (field: string, value: string) => {
    let processedValue = value;

    switch (field) {
      case 'volumeMl':
        processedValue = value.replace(/\D/g, '');
        break;
      case 'temperatura':
        processedValue = value.replace(/[^0-9.-]/g, '');
        break;
    }

    setFormData(prev => ({
      ...prev,
      [field]: processedValue,
    }));

    if (formErrors[field]) {
      setFormErrors(prev => ({
        ...prev,
        [field]: '',
      }));
    }
  };

  const handleCpfChange = e => {
    const value = e.target.value;
    const maskedValue = maskCPF(value);
    setCpfInput(maskedValue);

    setCpfError('');
    setSelectedDoadora(null);
    setDoadoraTestError('');

    if (maskedValue.length === 14) {
      const cleanCpf = removeMask(maskedValue);

      if (!validateCPF(cleanCpf)) {
        setCpfError(validationMessages.cpf.invalid);
        return;
      }
      searchDoadoraByCpf(cleanCpf);
    }
  };

  const saveEntity = async values => {
    if (doadoraTestError) {
      return;
    }

    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const mergedValues = {
      ...values,
      ...formData,
    };

    if (mergedValues.volumeMl !== undefined && typeof mergedValues.volumeMl !== 'number') {
      mergedValues.volumeMl = Number(mergedValues.volumeMl);
    }
    if (mergedValues.temperatura !== undefined && typeof mergedValues.temperatura !== 'number') {
      mergedValues.temperatura = Number(mergedValues.temperatura);
    }

    const entity = {
      ...coletaEntity,
      ...mergedValues,
      statusColeta: 'AGUARDANDO_PROCESSAMENTO',
      doadora: selectedDoadora || doadoras.find(it => it.id.toString() === values.doadora?.toString()),
    };

    try {
      let response;
      if (isNew) {
        response = await dispatch(createEntity(entity));
      } else {
        response = await dispatch(updateEntity(entity));
      }

      // response.payload é o axios response do passo 1
      if (response && response.payload && response.payload.data) {
        const blob = new Blob([response.payload.data], { type: 'application/pdf' });
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');

        // Opcional: libera o objeto URL após um tempo
        setTimeout(() => URL.revokeObjectURL(url), 10000);
      }
    } catch (error) {
      console.error('Erro ao salvar a coleta:', error);
    }
  };

  const defaultValues = () =>
    isNew
      ? { statusColeta: 'AGUARDANDO_PROCESSAMENTO' }
      : {
          statusColeta: 'AGUARDANDO_PROCESSAMENTO',
          ...coletaEntity,
          doadora: coletaEntity?.doadora?.id,
        };

  return (
    <div className="coleta-update-page">
      <div className="container">
        <Row className="justify-content-center">
          <Col lg="10">
            <div className="d-flex align-items-center mb-4">
              <h2 className="mb-0">
                <FontAwesomeIcon icon="clipboard-list" className="me-2" />
                {isNew ? 'Nova Coleta de Leite Materno' : 'Editar Coleta de Leite Materno'}
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
                  <div className="card-header bg-info">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="calendar-alt" className="me-2" />
                      Informações da Coleta
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.coleta.dataColeta')}
                            id="coleta-dataColeta"
                            name="dataColeta"
                            data-cy="dataColeta"
                            type="date"
                            className="form-control"
                            value={formData.dataColeta}
                            onChange={e => handleInputChange('dataColeta', e.target.value)}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                            }}
                          />
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.coleta.localColeta')}
                            id="coleta-localColeta"
                            name="localColeta"
                            data-cy="localColeta"
                            type="text"
                            value={formData.localColeta}
                            onChange={e => handleInputChange('localColeta', e.target.value)}
                            className="form-control"
                            placeholder="Informe o local onde foi realizada a coleta"
                          />
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-success">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="vial" className="me-2" />
                      Dados Técnicos
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.coleta.volumeMl')}
                            id="coleta-volumeMl"
                            name="volumeMl"
                            data-cy="volumeMl"
                            type="text"
                            className="form-control"
                            maxLength={6}
                            placeholder="Volume coletado em mL"
                            value={formData.volumeMl}
                            onChange={e => handleInputChange('volumeMl', e.target.value)}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                              validate: v => isNumber(v) || translate('entity.validation.number'),
                            }}
                          />
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.coleta.temperatura')}
                            id="coleta-temperatura"
                            name="temperatura"
                            data-cy="temperatura"
                            maxLength={4}
                            type="text"
                            className="form-control"
                            placeholder="Temperatura da amostra em °C"
                            value={formData.temperatura}
                            onChange={e => handleInputChange('temperatura', e.target.value)}
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                              validate: v => isNumber(v) || translate('entity.validation.number'),
                            }}
                          />
                        </div>
                      </Col>
                    </Row>
                    <div className="form-group">
                      <ValidatedField
                        label={translate('leiteVidaApp.coleta.observacoes')}
                        id="coleta-observacoes"
                        name="observacoes"
                        data-cy="observacoes"
                        type="textarea"
                        className="form-control"
                        rows="4"
                        placeholder="Observações adicionais sobre a coleta (opcional)"
                        value={formData.observacoes}
                        onChange={e => handleInputChange('observacoes', e.target.value)}
                      />
                    </div>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-warning">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="users" className="me-2" />
                      Doadora
                    </h5>
                  </div>
                  <div className="card-body">
                    <div className="form-group">
                      <Row>
                        <Col md="6">
                          <ValidatedField
                            label="CPF da Doadora"
                            id="coleta-doadora-cpf"
                            name="doadoraCpf"
                            data-cy="doadoraCpf"
                            type="text"
                            className={`form-control ${cpfError ? 'is-invalid' : selectedDoadora ? 'is-valid' : ''}`}
                            placeholder="000.000.000-00"
                            value={cpfInput}
                            onChange={handleCpfChange}
                            disabled={searchingDoadora}
                            validate={{
                              required: { value: true, message: validationMessages.cpf.required },
                              validate: v => {
                                const cleanCpf = removeMask(v);
                                return validateCPF(cleanCpf) || validationMessages.cpf.invalid;
                              },
                            }}
                          />
                          {searchingDoadora && (
                            <div className="d-flex align-items-center mt-1">
                              <div className="spinner-border spinner-border-sm text-primary me-2" role="status">
                                <span className="visually-hidden">Buscando...</span>
                              </div>
                              <small className="text-muted">Buscando doadora...</small>
                            </div>
                          )}
                          {cpfError && <div className="invalid-feedback d-block">{cpfError}</div>}
                        </Col>
                        <Col md="6">
                          <ValidatedField
                            label="Nome da Doadora"
                            id="coleta-doadora-nome"
                            name="doadoraNome"
                            data-cy="doadoraNome"
                            type="text"
                            readOnly
                            className="form-control"
                            value={selectedDoadora?.nome || ''}
                            placeholder="Nome será preenchido automaticamente"
                          />
                        </Col>
                      </Row>
                      {selectedDoadora && !doadoraTestError && (
                        <div className="mt-2">
                          <small className="text-success">
                            <FontAwesomeIcon icon="check-circle" className="me-1" />
                            Doadora encontrada: {selectedDoadora.nome}
                          </small>
                        </div>
                      )}
                      {selectedDoadora && doadoraTestError && (
                        <div className="mt-2">
                          <small className="text-danger">
                            <FontAwesomeIcon icon="exclamation-triangle" className="me-1" />
                            {doadoraTestError}
                          </small>
                        </div>
                      )}
                    </div>
                  </div>
                </div>

                <div className="d-flex justify-content-between align-items-center">
                  <Button tag={Link} to="/coleta" color="secondary" size="lg">
                    <FontAwesomeIcon icon="times" className="me-2" />
                    Cancelar
                  </Button>
                  <Button color="primary" type="submit" disabled={updating || doadoraTestError} size="lg">
                    <FontAwesomeIcon icon={updating ? 'spinner' : 'save'} spin={updating} className="me-2" />
                    {updating ? 'Salvando...' : 'Salvar Coleta'}
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

export default ColetaUpdate;
