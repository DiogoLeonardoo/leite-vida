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
import { toast } from 'react-toastify';

export const DoadoraUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doadoraEntity = useAppSelector(state => state.doadora.entity);
  const loading = useAppSelector(state => state.doadora.loading);
  const updating = useAppSelector(state => state.doadora.updating);
  const updateSuccess = useAppSelector(state => state.doadora.updateSuccess);
  const errorMessage = useAppSelector(state => state.doadora.errorMessage);

  const [formData, setFormData] = useState({
    cpf: '',
    cep: '',
    telefone: '',
    nome: '',
    cartaoSUS: '',
    dataNascimento: '',
    profissao: '',
    estado: '',
    cidade: '',
    endereco: '',
    tipoDoadora: '',
    localPreNatal: '',
    transfusaoUltimos5Anos: false,
    resultadoVDRL: '',
    resultadoHBsAg: '',
    resultadoFTAabs: '',
    resultadoHIV: '',
  });

  const [formErrors, setFormErrors] = useState({
    cpf: '',
    cep: '',
    telefone: '',
    nome: '',
    cartaoSUS: '',
    dataNascimento: '',
    profissao: '',
    estado: '',
    cidade: '',
    endereco: '',
    tipoDoadora: '',
    localPreNatal: '',
    resultadoVDRL: '',
    resultadoHBsAg: '',
    resultadoFTAabs: '',
    resultadoHIV: '',
  });

  const [addressLoading, setAddressLoading] = useState(false);
  const [isFormValid, setIsFormValid] = useState(false);

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
        nome: doadoraEntity.nome || '',
        cartaoSUS: doadoraEntity.cartaoSUS || '',
        dataNascimento: doadoraEntity.dataNascimento || '',
        profissao: doadoraEntity.profissao || '',
        estado: doadoraEntity.estado || '',
        cidade: doadoraEntity.cidade || '',
        endereco: doadoraEntity.endereco || '',
        tipoDoadora: doadoraEntity.tipoDoadora || '',
        localPreNatal: doadoraEntity.localPreNatal || '',
        transfusaoUltimos5Anos: doadoraEntity.transfusaoUltimos5Anos || false,
        resultadoVDRL: doadoraEntity.resultadoVDRL || '',
        resultadoHBsAg: doadoraEntity.resultadoHBsAg || '',
        resultadoFTAabs: doadoraEntity.resultadoFTAabs || '',
        resultadoHIV: doadoraEntity.resultadoHIV || '',
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

      setFormData(prev => ({
        ...prev,
        estado: endereco.uf || '',
        cidade: endereco.localidade || '',
        endereco: `${endereco.logradouro}${endereco.bairro ? ', ' + endereco.bairro : ''}`.trim() || '',
      }));
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

  // Add this useEffect to validate form whenever formData changes
  useEffect(() => {
    validateForm(false);
  }, [formData]);

  const validateForm = (showToast = false) => {
    // Define required fields
    const requiredFields = {
      cpf: 'CPF',
      nome: 'Nome Completo',
      telefone: 'Telefone',
      dataNascimento: 'Data de Nascimento',
      tipoDoadora: 'Tipo de Doadora',
      cep: 'CEP',
      estado: 'Estado',
      cidade: 'Cidade',
      endereco: 'Endereço',
    };

    // Check for empty required fields
    const emptyFields = Object.entries(requiredFields)
      .filter(([field]) => !formData[field]?.trim())
      .map(([_, label]) => label);

    // Validate specific format fields
    const formatErrors = [];
    if (formData.cpf && !validateCPF(formData.cpf)) {
      formatErrors.push('CPF inválido');
    }
    if (formData.cep && !validateCEP(formData.cep)) {
      formatErrors.push('CEP inválido');
    }
    if (formData.telefone && !validatePhone(formData.telefone)) {
      formatErrors.push('Telefone inválido');
    }
    if (formData.dataNascimento) {
      const today = new Date();
      const birthDate = new Date(formData.dataNascimento);
      const age = today.getFullYear() - birthDate.getFullYear();
      const monthDiff = today.getMonth() - birthDate.getMonth();
      if (age < 18 || (age === 18 && monthDiff < 0) || (age === 18 && monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        formatErrors.push('Doadora deve ser maior de 18 anos');
      }
    }

    // Display toast messages if requested
    if (showToast) {
      if (emptyFields.length > 0) {
        toast.error(`Preencha os campos obrigatórios: ${emptyFields.join(', ')}`);
      } else if (formatErrors.length > 0) {
        toast.info(`Corrija os seguintes campos: ${formatErrors.join(', ')}`);
      }
    }

    const isValid = emptyFields.length === 0 && formatErrors.length === 0;
    setIsFormValid(isValid);
    return isValid;
  };

  const handleInputChange = (field: string, value: string | boolean) => {
    let processedValue = value;

    if (typeof value === 'string') {
      switch (field) {
        case 'cpf':
          processedValue = maskCPF(value);
          break;
        case 'cep':
          processedValue = maskCEP(value);
          break;
        case 'telefone':
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

    // Remove this setTimeout as we now use useEffect for validation
    // setTimeout(() => validateForm(false), 100);
  };

  const handleCEPBlur = () => {
    validateForm(false);
    if (formData.cep && validateCEP(formData.cep)) {
      fetchAddressFromCEP(formData.cep);
    } else if (formData.cep && !validateCEP(formData.cep)) {
      toast.info('CEP inválido. Deve conter 8 dígitos.');
    }
  };

  const saveEntity = values => {
    const isFormValid = validateForm(true);

    if (!isFormValid) {
      return;
    }

    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const mergedValues = {
      ...values,
      ...formData,
    };

    const cleanedValues = {
      ...mergedValues,
      cpf: removeMask(formData.cpf || ''),
      cep: removeMask(formData.cep || ''),
      telefone: removeMask(formData.telefone || ''),
      dataRegistro: new Date().toISOString().split('T')[0],
    };

    const entity = {
      ...doadoraEntity,
      ...cleanedValues,
    };

    if (isNew) {
      dispatch(createEntity(entity))
        .unwrap()
        .then(() => {
          handleClose();
        })
        .catch(error => {
          console.error('Error creating doadora:', error);

          if (
            error === 'CPF já existe no sistema' ||
            (typeof error === 'string' &&
              (error.includes('CPF já cadastrado') ||
                error.includes('cpfexists') ||
                error.includes('ux_doadora__cpf') ||
                error.includes('duplicar valor da chave') ||
                error.includes('Chave (cpf)') ||
                error.includes('já existe')))
          ) {
            toast.error('CPF de doadora já existe no sistema');
            setFormErrors(prev => ({
              ...prev,
              cpf: 'CPF já existe no sistema',
            }));
          } else {
            toast.error('Erro ao criar doadora');
          }
        });
    } else {
      dispatch(updateEntity(entity))
        .unwrap()
        .then(() => {
          handleClose();
        })
        .catch(error => {
          toast.error('Erro ao atualizar doadora');
        });
    }
  };

  const defaultValues = () => {
    return {
      ...formData,
    };
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
                <PersonalInfoSection
                  formData={formData}
                  formErrors={formErrors}
                  handleInputChange={handleInputChange}
                  validateForm={() => validateForm(false)}
                />

                <AddressSection
                  formData={formData}
                  formErrors={formErrors}
                  handleInputChange={handleInputChange}
                  handleCEPBlur={handleCEPBlur}
                  addressLoading={addressLoading}
                />

                <MedicalInfoSection formData={formData} formErrors={formErrors} handleInputChange={handleInputChange} />

                <ExamResultsSection formData={formData} formErrors={formErrors} handleInputChange={handleInputChange} />

                <div className="d-flex justify-content-between align-items-center">
                  <Button tag={Link} to="/doadora" color="secondary" size="lg">
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

export default DoadoraUpdate;
