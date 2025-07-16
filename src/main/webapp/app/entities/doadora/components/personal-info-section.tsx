import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { validateCPF, validatePhone } from 'app/shared/util/validation-utils';

interface PersonalInfoSectionProps {
  formData: {
    cpf: string;
    telefone: string;
  };
  formErrors: {
    cpf: string;
    telefone: string;
  };
  handleInputChange: (field: string, value: string) => void;
  validateForm: () => boolean;
}

export const PersonalInfoSection: React.FC<PersonalInfoSectionProps> = ({ formData, formErrors, handleInputChange, validateForm }) => {
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
              onChange={e => handleInputChange('cpf', e.target.value)}
              onBlur={() => validateForm()}
              maxLength={14}
              placeholder="000.000.000-00"
              validate={{
                required: { value: true, message: 'CPF é obrigatório' },
                validate: value => validateCPF(value) || 'CPF inválido',
              }}
            />
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <ValidatedField label="Cartão SUS" id="doadora-cartaoSUS" name="cartaoSUS" data-cy="cartaoSUS" type="text" />
          </Col>
          <Col md="6">
            <ValidatedField
              label="Data de Nascimento"
              id="doadora-dataNascimento"
              name="dataNascimento"
              data-cy="dataNascimento"
              type="date"
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
              onChange={e => handleInputChange('telefone', e.target.value)}
              onBlur={() => validateForm()}
              maxLength={15}
              placeholder="(00) 00000-0000"
              validate={{
                required: { value: true, message: 'Telefone é obrigatório' },
                validate: value => validatePhone(value) || 'Telefone inválido',
              }}
            />
          </Col>
          <Col md="6">
            <ValidatedField label="Profissão" id="doadora-profissao" name="profissao" data-cy="profissao" type="text" />
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
