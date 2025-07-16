import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset, updateEntity } from './doadora.reducer';
import {
  validateCPF,
  validateCEP,
  validatePhone,
  maskCPF,
  maskCEP,
  maskPhone,
  removeMask,
  validationMessages,
} from 'app/shared/util/validation-utils';
import { buscarEndereco } from 'app/shared/util/cep-busca';
import { PersonalInfoSection } from './components/personal-info-section';
import { AddressSection } from './components/address-section';
import { MedicalInfoSection } from './components/medical-info-section';
import { ExamResultsSection } from './components/exam-results-section';
import './doadora-update.scss';

export const DoadoraUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doadoraEntity = useAppSelector(state => state.doadora.entity);
  const loading = useAppSelector(state => state.doadora.loading);
  const updating = useAppSelector(state => state.doadora.updating);
  const updateSuccess = useAppSelector(state => state.doadora.updateSuccess);

  const [formData, setFormData] = useState({
    cpf: '',
    cep: '',
    telefone: '',
  });

  const [formErrors, setFormErrors] = useState({
    cpf: '',
    cep: '',
    telefone: '',
    nome: '',
    dataNascimento: '',
    estado: '',
    cidade: '',
    endereco: '',
  });

  const [addressLoading, setAddressLoading] = useState(false);

  const handleClose = () => {
    navigate(`/doadora${location.search}`);
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
    if (!isNew && doadoraEntity) {
      setFormData({
        cpf: doadoraEntity.cpf ? maskCPF(doadoraEntity.cpf) : '',
        cep: doadoraEntity.cep ? maskCEP(doadoraEntity.cep) : '',
        telefone: doadoraEntity.telefone ? maskPhone(doadoraEntity.telefone) : '',
      });
    }
  }, [doadoraEntity, isNew]);

  const fetchAddressFromCEP = async (cep: string) => {
    const cleanCEP = removeMask(cep);

    if (cleanCEP.length !== 8) {
      return;
    }

    setAddressLoading(true);

    try {
      const endereco = await buscarEndereco(cep);

      if (!endereco) {
        setFormErrors(prev => ({
          ...prev,
          cep: 'CEP não encontrado',
        }));
        return;
      }

      setFormErrors(prev => ({
        ...prev,
        cep: '',
      }));

      setTimeout(() => {
        const estadoField = document.getElementById('doadora-estado') as HTMLInputElement;
        const cidadeField = document.getElementById('doadora-cidade') as HTMLInputElement;
        const enderecoField = document.getElementById('doadora-endereco') as HTMLInputElement;

        if (estadoField) {
          estadoField.value = endereco.uf;
          estadoField.dispatchEvent(new Event('input', { bubbles: true }));
        }
        if (cidadeField) {
          cidadeField.value = endereco.localidade;
          cidadeField.dispatchEvent(new Event('input', { bubbles: true }));
        }
        if (enderecoField) {
          const enderecoCompleto = `${endereco.logradouro}${endereco.bairro ? ', ' + endereco.bairro : ''}`.trim();
          enderecoField.value = enderecoCompleto;
          enderecoField.dispatchEvent(new Event('input', { bubbles: true }));
        }
      }, 100);
    } catch (error) {
      console.error('Erro ao buscar CEP:', error);
      setFormErrors(prev => ({
        ...prev,
        cep: 'Erro ao buscar CEP',
      }));
    } finally {
      setAddressLoading(false);
    }
  };

  const handleInputChange = (field: string, value: string) => {
    let maskedValue = value;

    switch (field) {
      case 'cpf':
        maskedValue = maskCPF(value);
        break;
      case 'cep':
        maskedValue = maskCEP(value);
        break;
      case 'telefone':
        maskedValue = maskPhone(value);
        break;
    }

    setFormData(prev => ({
      ...prev,
      [field]: maskedValue,
    }));

    if (formErrors[field]) {
      setFormErrors(prev => ({
        ...prev,
        [field]: '',
      }));
    }
  };

  const handleCEPBlur = () => {
    validateForm();
    if (formData.cep && validateCEP(formData.cep)) {
      fetchAddressFromCEP(formData.cep);
    }
  };

  const validateForm = () => {
    const errors = {
      cpf: '',
      cep: '',
      telefone: '',
      nome: '',
      dataNascimento: '',
      estado: '',
      cidade: '',
      endereco: '',
    };

    // Validate CPF
    if (!formData.cpf) {
      errors.cpf = validationMessages.cpf.required;
    } else if (!validateCPF(formData.cpf)) {
      errors.cpf = validationMessages.cpf.invalid;
    }

    // Validate CEP
    if (!formData.cep) {
      errors.cep = validationMessages.cep.required;
    } else if (!validateCEP(formData.cep)) {
      errors.cep = validationMessages.cep.invalid;
    }

    // Validate Phone
    if (!formData.telefone) {
      errors.telefone = validationMessages.phone.required;
    } else if (!validatePhone(formData.telefone)) {
      errors.telefone = validationMessages.phone.invalid;
    }

    // Validate other required fields from form
    const nomeField = document.getElementById('doadora-nome') as HTMLInputElement;
    const dataNascimentoField = document.getElementById('doadora-dataNascimento') as HTMLInputElement;
    const estadoField = document.getElementById('doadora-estado') as HTMLInputElement;
    const cidadeField = document.getElementById('doadora-cidade') as HTMLInputElement;
    const enderecoField = document.getElementById('doadora-endereco') as HTMLInputElement;

    if (!nomeField?.value?.trim()) {
      errors.nome = 'Nome é obrigatório';
    }

    if (!dataNascimentoField?.value) {
      errors.dataNascimento = 'Data de nascimento é obrigatória';
    }

    if (!estadoField?.value?.trim()) {
      errors.estado = 'Estado é obrigatório';
    }

    if (!cidadeField?.value?.trim()) {
      errors.cidade = 'Cidade é obrigatória';
    }

    if (!enderecoField?.value?.trim()) {
      errors.endereco = 'Endereço é obrigatório';
    }

    setFormErrors(errors);
    return !Object.values(errors).some(error => error !== '');
  };

  const saveEntity = values => {
    console.log('Form values:', values);

    const isFormValid = validateForm();
    console.log('Form is valid:', isFormValid);
    console.log('Form errors:', formErrors);

    if (!isFormValid) {
      console.log('Form validation failed');
      return;
    }

    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const cleanedValues = {
      ...values,
      cpf: removeMask(formData.cpf || ''),
      cep: removeMask(formData.cep || ''),
      telefone: removeMask(formData.telefone || ''),
      dataRegistro: new Date().toISOString().split('T')[0],
    };

    const entity = {
      ...doadoraEntity,
      ...cleanedValues,
    };

    console.log('Saving entity:', entity);

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () => {
    const baseValues = isNew
      ? {
          tipoDoadora: 'DOMICILIAR',
          localPreNatal: 'REDE_PUBLICA',
          resultadoVDRL: 'NEGATIVO',
          resultadoHBsAg: 'NEGATIVO',
          resultadoFTAabs: 'NEGATIVO',
          resultadoHIV: 'NEGATIVO',
          transfusaoUltimos5Anos: false,
          cpf: formData.cpf,
          cep: formData.cep,
          telefone: formData.telefone,
        }
      : {
          tipoDoadora: 'DOMICILIAR',
          localPreNatal: 'REDE_PUBLICA',
          resultadoVDRL: 'POSITIVO',
          resultadoHBsAg: 'POSITIVO',
          resultadoFTAabs: 'POSITIVO',
          resultadoHIV: 'POSITIVO',
          ...doadoraEntity,
          cpf: formData.cpf,
          cep: formData.cep,
          telefone: formData.telefone,
        };

    return baseValues;
  };

  return (
    <div className="doadora-update-page">
      <div className="container">
        <Row className="justify-content-center">
          <Col lg="10">
            <div className="d-flex align-items-center mb-4">
              <h2 className="mb-0">
                <FontAwesomeIcon icon="user-plus" className="me-2" />
                {isNew ? 'Cadastrar Nova Doadora' : 'Editar Doadora'}
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
                    id="doadora-id"
                    label="ID"
                    validate={{ required: true }}
                    style={{ display: 'none' }}
                  />
                ) : null}

                <PersonalInfoSection
                  formData={formData}
                  formErrors={formErrors}
                  handleInputChange={handleInputChange}
                  validateForm={validateForm}
                />

                <AddressSection
                  formData={formData}
                  formErrors={formErrors}
                  handleInputChange={handleInputChange}
                  handleCEPBlur={handleCEPBlur}
                  addressLoading={addressLoading}
                />

                <MedicalInfoSection />

                <ExamResultsSection />

                <div className="d-flex justify-content-between align-items-center">
                  <Button tag={Link} to="/doadora" color="secondary" size="lg">
                    <FontAwesomeIcon icon="times" className="me-2" />
                    Cancelar
                  </Button>
                  <Button color="primary" type="submit" disabled={updating} size="lg">
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

export default DoadoraUpdate;
