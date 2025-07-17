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

      // Update form state instead of direct DOM manipulation
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

  const validateForm = (showToast = false) => {
    const camposObrigatorios = {
      CPF: formData.cpf?.trim(),
      CEP: formData.cep?.trim(),
      Telefone: formData.telefone?.trim(),
      'Nome Completo': formData.nome?.trim(),
      'Cartão SUS': formData.cartaoSUS?.trim(),
      'Data de Nascimento': formData.dataNascimento?.trim(),
      Profissão: formData.profissao?.trim(),
      Estado: formData.estado?.trim(),
      Cidade: formData.cidade?.trim(),
      Endereço: formData.endereco?.trim(),
      'Tipo de Doadora': formData.tipoDoadora,
      'Local do Pré-Natal': formData.localPreNatal,
      'Resultado VDRL': formData.resultadoVDRL,
      'Resultado HBsAg': formData.resultadoHBsAg,
      'Resultado FTA-abs': formData.resultadoFTAabs,
      'Resultado HIV': formData.resultadoHIV,
    };

    const camposVazios = Object.entries(camposObrigatorios)
      .filter(([_, value]) => !value)
      .map(([nome]) => nome);

    // Validate specific format fields if they have values
    const camposComFormato = [];
    if (formData.cpf && !validateCPF(formData.cpf)) {
      camposComFormato.push('CPF inválido');
    }
    if (formData.cep && !validateCEP(formData.cep)) {
      camposComFormato.push('CEP inválido');
    }
    if (formData.telefone && !validatePhone(formData.telefone)) {
      camposComFormato.push('Telefone inválido');
    }
    if (formData.dataNascimento) {
      const today = new Date();
      const birthDate = new Date(formData.dataNascimento);
      const age = today.getFullYear() - birthDate.getFullYear();
      if (age < 18) {
        camposComFormato.push('Doadora deve ser maior de 18 anos');
      }
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

    // Apply masks only for specific fields
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

    // Validate form after each change without showing toast
    setTimeout(() => validateForm(false), 100);
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
    console.log('Form values from ValidatedForm:', values);
    console.log('Current formData state:', formData);

    const isFormValid = validateForm(true); // Show toast messages when submitting
    console.log('Form is valid:', isFormValid);

    if (!isFormValid) {
      console.log('Form validation failed');
      return;
    }

    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    // Merge form state with form values, prioritizing current state for controlled fields
    const mergedValues = {
      ...values,
      ...formData, // This ensures all state data is included
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
          toast.error('Erro ao criar doadora:', error);
          setFormErrors(prev => ({
            ...prev,
            cpf: error,
          }));
        });
    } else {
      dispatch(updateEntity(entity))
        .unwrap()
        .then(() => {
          handleClose();
        })
        .catch(error => {
          toast.error('Erro ao atualizar doadora:', error);
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
