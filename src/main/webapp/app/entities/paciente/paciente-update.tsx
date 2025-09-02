import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './paciente.reducer';
import './paciente-update.scss';
import { toast } from 'react-toastify';
import { validateCPF, validatePhone, maskCPF, maskPhone, removeMask } from 'app/shared/util/validation-utils';

export const PacienteUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pacienteEntity = useAppSelector(state => state.paciente.entity);
  const loading = useAppSelector(state => state.paciente.loading);
  const updating = useAppSelector(state => state.paciente.updating);
  const updateSuccess = useAppSelector(state => state.paciente.updateSuccess);
  const errorMessage = useAppSelector(state => state.paciente.errorMessage);

  const [formData, setFormData] = useState({
    nome: '',
    registroHospitalar: '',
    dataNascimento: '',
    pesoNascimento: '',
    idadeGestacional: '',
    condicaoClinica: '',
    nomeResponsavel: '',
    cpfResponsavel: '',
    telefoneResponsavel: '',
    parentescoResponsavel: '',
  });

  const [formErrors, setFormErrors] = useState({
    nome: '',
    registroHospitalar: '',
    dataNascimento: '',
    pesoNascimento: '',
    idadeGestacional: '',
    condicaoClinica: '',
    nomeResponsavel: '',
    cpfResponsavel: '',
    telefoneResponsavel: '',
    parentescoResponsavel: '',
  });

  const [isFormValid, setIsFormValid] = useState(false);

  const handleClose = () => {
    navigate(`/paciente${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew && pacienteEntity) {
      setFormData({
        nome: pacienteEntity.nome || '',
        registroHospitalar: pacienteEntity.registroHospitalar || '',
        dataNascimento: pacienteEntity.dataNascimento || '',
        pesoNascimento: pacienteEntity.pesoNascimento ? pacienteEntity.pesoNascimento.toString() : '',
        idadeGestacional: pacienteEntity.idadeGestacional ? pacienteEntity.idadeGestacional.toString() : '',
        condicaoClinica: pacienteEntity.condicaoClinica || '',
        nomeResponsavel: pacienteEntity.nomeResponsavel || '',
        cpfResponsavel: pacienteEntity.cpfResponsavel ? maskCPF(pacienteEntity.cpfResponsavel) : '',
        telefoneResponsavel: pacienteEntity.telefoneResponsavel ? maskPhone(pacienteEntity.telefoneResponsavel) : '',
        parentescoResponsavel: pacienteEntity.parentescoResponsavel || '',
      });
    }
  }, [pacienteEntity, isNew]);

  const validateForm = (showToast = false) => {
    const camposObrigatorios = {
      'Nome do Paciente': formData.nome?.trim(),
      'Registro Hospitalar': formData.registroHospitalar?.trim(),
      'Data de Nascimento': formData.dataNascimento?.trim(),
      'Condição Clínica': formData.condicaoClinica?.trim(),
      'Nome do Responsável': formData.nomeResponsavel?.trim(),
      'CPF do Responsável': formData.cpfResponsavel?.trim(),
      'Telefone do Responsável': formData.telefoneResponsavel?.trim(),
      'Parentesco do Responsável': formData.parentescoResponsavel?.trim(),
    };

    const camposVazios = Object.entries(camposObrigatorios)
      .filter(([_, value]) => !value)
      .map(([nome]) => nome);

    // Validate specific format fields if they have values
    const camposComFormato = [];
    if (formData.cpfResponsavel && !validateCPF(formData.cpfResponsavel)) {
      camposComFormato.push('CPF do Responsável inválido');
    }
    if (formData.telefoneResponsavel && !validatePhone(formData.telefoneResponsavel)) {
      camposComFormato.push('Telefone do Responsável inválido');
    }

    if (showToast && camposVazios.length > 0) {
      toast.info(`Preencha os campos obrigatórios: ${camposVazios.join(', ')}`);
      setIsFormValid(false);
      return false;
    }

    if (showToast && camposComFormato.length > 0) {
      toast.info(`Corrija os seguintes campos: ${camposComFormato.join(', ')}`);
      setIsFormValid(false);
      return false;
    }

    const isValid = camposVazios.length === 0 && camposComFormato.length === 0;
    setIsFormValid(isValid);
    return isValid;
  };

  const handleInputChange = (field: string, value: string | boolean) => {
    let processedValue = value;

    if (typeof value === 'string') {
      switch (field) {
        case 'cpfResponsavel':
          processedValue = maskCPF(value);
          break;
        case 'telefoneResponsavel':
          processedValue = maskPhone(value);
          break;
      }
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

    setTimeout(() => validateForm(false), 100);
  };

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.pesoNascimento !== undefined && typeof values.pesoNascimento !== 'number') {
      values.pesoNascimento = Number(values.pesoNascimento);
    }
    if (values.idadeGestacional !== undefined && typeof values.idadeGestacional !== 'number') {
      values.idadeGestacional = Number(values.idadeGestacional);
    }

    const isFormValid = validateForm(true);
    if (!isFormValid) {
      return;
    }

    const mergedValues = {
      ...values,
      ...formData,
    };

    const cleanedValues = {
      ...mergedValues,
      cpfResponsavel: removeMask(formData.cpfResponsavel || ''),
      telefoneResponsavel: removeMask(formData.telefoneResponsavel || ''),
      statusAtivo: true,
    };

    const entity = {
      ...pacienteEntity,
      ...cleanedValues,
    };

    if (isNew) {
      dispatch(createEntity(entity))
        .unwrap()
        .then(() => {
          toast.success('Paciente cadastrado com sucesso!');
          handleClose();
        })
        .catch(error => {
          console.error('Error creating paciente:', error);
          toast.error('Erro ao criar paciente');
        });
    } else {
      dispatch(updateEntity(entity))
        .unwrap()
        .then(() => {
          toast.success('Paciente atualizado com sucesso!');
          handleClose();
        })
        .catch(error => {
          toast.error('Erro ao atualizar paciente');
        });
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          statusAtivo: true,
        }
      : {
          ...pacienteEntity,
        };

  const renderPacienteInfoSection = () => (
    <Card className="mb-4">
      <CardHeader className="bg-primary">
        <h5>
          <FontAwesomeIcon icon="user" className="me-2" />
          Informações do Paciente
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md={6}>
            <div className="form-group">
              <label htmlFor="paciente-nome">
                <Translate contentKey="leiteVidaApp.paciente.nome">Nome</Translate>
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-nome"
                name="nome"
                data-cy="nome"
                type="text"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('nome', e.target.value)}
                value={formData.nome}
              />
            </div>
          </Col>
          <Col md={6}>
            <div className="form-group">
              <label htmlFor="paciente-registroHospitalar">
                <Translate contentKey="leiteVidaApp.paciente.registroHospitalar">Registro Hospitalar</Translate>
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-registroHospitalar"
                name="registroHospitalar"
                data-cy="registroHospitalar"
                type="text"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('registroHospitalar', e.target.value)}
                value={formData.registroHospitalar}
              />
            </div>
          </Col>
        </Row>

        <Row>
          <Col md={4}>
            <div className="form-group">
              <label htmlFor="paciente-dataNascimento">
                <Translate contentKey="leiteVidaApp.paciente.dataNascimento">Data Nascimento</Translate>
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-dataNascimento"
                name="dataNascimento"
                data-cy="dataNascimento"
                type="date"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('dataNascimento', e.target.value)}
                value={formData.dataNascimento}
              />
            </div>
          </Col>
          <Col md={4}>
            <div className="form-group">
              <label htmlFor="paciente-pesoNascimento">Peso Nascimento (g)</label>
              <ValidatedField
                id="paciente-pesoNascimento"
                name="pesoNascimento"
                data-cy="pesoNascimento"
                type="number"
                className="form-control"
                min={500}
                max={6000}
                value={formData.pesoNascimento}
                onChange={e => {
                  const value = e.target.value;
                  if (value.length <= 4) {
                    handleInputChange('pesoNascimento', value);
                  }
                }}
                validate={{
                  required: { value: true, message: 'Peso de nascimento é obrigatório' },
                  min: { value: 500, message: 'Valor mínimo é 500 g' },
                  max: { value: 6000, message: 'Valor máximo é 6000 g' },
                }}
              />
            </div>
          </Col>

          <Col md={4}>
            <div className="form-group">
              <label htmlFor="paciente-idadeGestacional">Idade Gestacional (semanas)</label>
              <ValidatedField
                id="paciente-idadeGestacional"
                name="idadeGestacional"
                data-cy="idadeGestacional"
                type="number"
                className="form-control"
                min={22}
                max={42}
                value={formData.idadeGestacional}
                onChange={e => handleInputChange('idadeGestacional', e.target.value)}
                validate={{
                  required: { value: true, message: 'Idade gestacional é obrigatória' },
                  min: { value: 22, message: 'Mínimo de 22 semanas' },
                  max: { value: 42, message: 'Máximo de 42 semanas' },
                }}
              />
            </div>
          </Col>
        </Row>

        <Row>
          <Col md={12}>
            <div className="form-group">
              <label htmlFor="paciente-condicaoClinica">
                <Translate contentKey="leiteVidaApp.paciente.condicaoClinica">Condição Clínica</Translate>
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-condicaoClinica"
                name="condicaoClinica"
                data-cy="condicaoClinica"
                type="textarea"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('condicaoClinica', e.target.value)}
                value={formData.condicaoClinica}
              />
            </div>
          </Col>
        </Row>
      </CardBody>
    </Card>
  );

  const renderResponsavelInfoSection = () => (
    <Card className="mb-4">
      <CardHeader className="bg-info">
        <h5>
          <FontAwesomeIcon icon="user-friends" className="me-2" />
          Informações do Responsável
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md={6}>
            <div className="form-group">
              <label htmlFor="paciente-nomeResponsavel">
                Nome do Responsável
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-nomeResponsavel"
                name="nomeResponsavel"
                data-cy="nomeResponsavel"
                type="text"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('nomeResponsavel', e.target.value)}
                value={formData.nomeResponsavel}
              />
            </div>
          </Col>
          <Col md={6}>
            <div className="form-group">
              <label htmlFor="paciente-parentescoResponsavel">
                Parentesco do Responsável
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-parentescoResponsavel"
                name="parentescoResponsavel"
                data-cy="parentescoResponsavel"
                type="select"
                className="form-control"
                value={formData.parentescoResponsavel}
                onChange={e => handleInputChange('parentescoResponsavel', e.target.value)}
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              >
                <option value="" disabled>
                  Selecione...
                </option>
                <option value="PAI">Pai</option>
                <option value="MAE">Mãe</option>
                <option value="AVO">Avô / Avó</option>
                <option value="TIO_TIA">Tio / Tia</option>
                <option value="IRMAO_IRMA">Irmão / Irmã</option>
                <option value="PADRASTO_MADRASTA">Padrasto / Madrasta</option>
                <option value="TUTOR">Tutor / Guardião legal</option>
                <option value="OUTRO">Outro</option>
              </ValidatedField>
            </div>
          </Col>
        </Row>

        <Row>
          <Col md={6}>
            <div className="form-group">
              <label htmlFor="paciente-cpfResponsavel">
                CPF do Responsável
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-cpfResponsavel"
                name="cpfResponsavel"
                data-cy="cpfResponsavel"
                type="text"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('cpfResponsavel', e.target.value)}
                value={formData.cpfResponsavel}
              />
              {formErrors.cpfResponsavel && <div className="invalid-feedback d-block">{formErrors.cpfResponsavel}</div>}
            </div>
          </Col>
          <Col md={6}>
            <div className="form-group">
              <label htmlFor="paciente-telefoneResponsavel">
                Telefone do Responsável
                <span className="text-danger">*</span>
              </label>
              <ValidatedField
                id="paciente-telefoneResponsavel"
                name="telefoneResponsavel"
                data-cy="telefoneResponsavel"
                type="text"
                className="form-control"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
                onChange={e => handleInputChange('telefoneResponsavel', e.target.value)}
                value={formData.telefoneResponsavel}
              />
              {formErrors.telefoneResponsavel && <div className="invalid-feedback d-block">{formErrors.telefoneResponsavel}</div>}
            </div>
          </Col>
        </Row>
      </CardBody>
    </Card>
  );

  return (
    <div className="paciente-update-page">
      <div className="container">
        <Row className="justify-content-center">
          <Col lg="10">
            <div className="d-flex align-items-center mb-4">
              <h2 className="mb-0">
                <FontAwesomeIcon icon="baby" className="me-2" />
                {isNew ? 'Cadastrar Novo Paciente' : 'Editar Paciente'}
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
                {!isNew ? (
                  <ValidatedField
                    name="id"
                    required
                    readOnly
                    id="paciente-id"
                    label={translate('global.field.id')}
                    validate={{ required: true }}
                    hidden
                  />
                ) : null}

                {renderPacienteInfoSection()}
                {renderResponsavelInfoSection()}

                <div className="d-flex justify-content-between align-items-center">
                  <Button tag={Link} to="/paciente" color="secondary" size="lg">
                    <FontAwesomeIcon icon="times" className="me-2" />
                    Cancelar
                  </Button>
                  <Button color="primary" type="submit" disabled={updating || !isFormValid} size="lg">
                    <FontAwesomeIcon icon="save" className="me-2" />
                    {updating ? 'Salvando...' : 'Salvar'}
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

export default PacienteUpdate;
