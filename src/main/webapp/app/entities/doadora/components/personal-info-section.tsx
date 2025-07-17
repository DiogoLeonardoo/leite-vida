import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { validateCPF, validatePhone } from 'app/shared/util/validation-utils';
import { toast } from 'react-toastify';

interface PersonalInfoSectionProps {
  formData: {
    cpf: string;
    telefone: string;
    nome: string;
    cartaoSUS: string;
    dataNascimento: string;
    profissao: string;
  };
  formErrors: {
    cpf: string;
    telefone: string;
    nome: string;
    dataNascimento: string;
  };
  handleInputChange: (field: string, value: string | boolean) => void;
  validateForm: () => boolean;
}

export const PersonalInfoSection: React.FC<PersonalInfoSectionProps> = ({ formData, formErrors, handleInputChange, validateForm }) => {
  const handleCPFBlur = () => {
    if (formData.cpf && !validateCPF(formData.cpf)) {
      toast.info('CPF inválido. Verifique o formato.');
    }
    validateForm();
  };

  const handlePhoneBlur = () => {
    if (formData.telefone && !validatePhone(formData.telefone)) {
      toast.info('Telefone inválido. Verifique o formato.');
    }
    validateForm();
  };

  return (
    <Card className="mb-4 shadow-sm">
      <CardHeader className="bg-primary text-white">
        <h5 className="mb-0">
          <FontAwesomeIcon icon="user" className="me-2" />
          Informações Pessoais
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md="6">
            <ValidatedField
              label="Nome Completo"
              id="doadora-nome"
              name="nome"
              data-cy="nome"
              type="text"
              value={formData.nome}
              onChange={e => handleInputChange('nome', e.target.value)}
              validate={{
                required: { value: true, message: 'Nome é obrigatório' },
                minLength: { value: 2, message: 'Nome deve ter pelo menos 2 caracteres' },
              }}
            />
          </Col>
          <Col md="6">
            <ValidatedField
              label="CPF"
              id="doadora-cpf"
              name="cpf"
              data-cy="cpf"
              type="text"
              value={formData.cpf}
              onChange={e => handleInputChange('cpf', e.target.value)}
              onBlur={handleCPFBlur}
              maxLength={14}
              placeholder="000.000.000-00"
              invalid={!!formErrors.cpf}
              validate={{
                required: { value: true, message: 'CPF é obrigatório' },
                validate: value => validateCPF(value) || 'CPF inválido',
              }}
            />
            {formErrors.cpf && <div className="invalid-feedback d-block">{formErrors.cpf}</div>}
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <ValidatedField
              label="Cartão SUS"
              id="doadora-cartaoSUS"
              name="cartaoSUS"
              data-cy="cartaoSUS"
              type="text"
              value={formData.cartaoSUS}
              onChange={e => handleInputChange('cartaoSUS', e.target.value)}
              validate={{
                required: { value: true, message: 'Cartão SUS é obrigatório' },
                minLength: { value: 10, message: 'Cartão SUS deve ter pelo menos 10 caracteres' },
              }}
            />
          </Col>
          <Col md="6">
            <ValidatedField
              label="Data de Nascimento"
              id="doadora-dataNascimento"
              name="dataNascimento"
              data-cy="dataNascimento"
              type="date"
              value={formData.dataNascimento}
              onChange={e => handleInputChange('dataNascimento', e.target.value)}
              validate={{
                required: { value: true, message: 'Data de nascimento é obrigatória' },
                validate: value => {
                  const today = new Date();
                  const birthDate = new Date(value);
                  const age = today.getFullYear() - birthDate.getFullYear();
                  return age >= 18 || 'Doadora deve ser maior de 18 anos';
                },
              }}
            />
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <ValidatedField
              label="Telefone"
              id="doadora-telefone"
              name="telefone"
              data-cy="telefone"
              type="text"
              value={formData.telefone}
              onChange={e => handleInputChange('telefone', e.target.value)}
              onBlur={handlePhoneBlur}
              maxLength={15}
              placeholder="(00) 00000-0000"
              validate={{
                required: { value: true, message: 'Telefone é obrigatório' },
                validate: value => validatePhone(value) || 'Telefone inválido',
              }}
            />
          </Col>
          <Col md="6">
            <ValidatedField
              label="Profissão"
              id="doadora-profissao"
              name="profissao"
              data-cy="profissao"
              type="text"
              value={formData.profissao}
              onChange={e => handleInputChange('profissao', e.target.value)}
              validate={{
                required: { value: true, message: 'Profissão é obrigatória' },
                minLength: { value: 2, message: 'Profissão deve ter pelo menos 2 caracteres' },
              }}
            />
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
