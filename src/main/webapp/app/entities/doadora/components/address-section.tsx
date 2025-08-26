import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { validateCEP } from 'app/shared/util/validation-utils';

interface AddressSectionProps {
  formData: {
    cep: string;
    estado: string;
    cidade: string;
    endereco: string;
  };
  formErrors: {
    cep: string;
    estado: string;
    cidade: string;
    endereco: string;
  };
  handleInputChange: (field: string, value: string | boolean) => void;
  handleCEPBlur: () => void;
  addressLoading: boolean;
}

export const AddressSection: React.FC<AddressSectionProps> = ({
  formData,
  formErrors,
  handleInputChange,
  handleCEPBlur,
  addressLoading,
}) => {
  return (
    <Card className="mb-4 shadow-sm">
      <CardHeader className="bg-info text-white">
        <h5 className="mb-0">
          <FontAwesomeIcon icon="map-marker-alt" className="me-2" />
          Endereço
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md="4">
            <ValidatedField
              label="CEP"
              id="doadora-cep"
              name="cep"
              data-cy="cep"
              type="text"
              value={formData.cep}
              onChange={e => handleInputChange('cep', e.target.value)}
              onBlur={handleCEPBlur}
              maxLength={9}
              placeholder="00000-000"
              validate={{
                required: { value: true, message: 'CEP é obrigatório' },
                validate: value => validateCEP(value) || 'CEP inválido',
              }}
            />
            {addressLoading && (
              <div className="text-center mt-2">
                <div className="spinner-border spinner-border-sm" role="status">
                  <span className="sr-only">Carregando...</span>
                </div>
              </div>
            )}
          </Col>
          <Col md="4">
            <ValidatedField
              label="Estado"
              id="doadora-estado"
              name="estado"
              data-cy="estado"
              type="text"
              disabled
              value={formData.estado}
              onChange={e => handleInputChange('estado', e.target.value)}
              validate={{
                required: { value: true, message: 'Estado é obrigatório' },
                minLength: { value: 2, message: 'Estado deve ter pelo menos 2 caracteres' },
              }}
            />
          </Col>
          <Col md="4">
            <ValidatedField
              label="Cidade"
              id="doadora-cidade"
              name="cidade"
              data-cy="cidade"
              disabled
              type="text"
              value={formData.cidade}
              onChange={e => handleInputChange('cidade', e.target.value)}
              validate={{
                required: { value: true, message: 'Cidade é obrigatória' },
                minLength: { value: 2, message: 'Cidade deve ter pelo menos 2 caracteres' },
              }}
            />
          </Col>
        </Row>
        <Row>
          <Col md="12">
            <ValidatedField
              label="Endereço Completo"
              id="doadora-endereco"
              name="endereco"
              data-cy="endereco"
              type="text"
              value={formData.endereco}
              onChange={e => handleInputChange('endereco', e.target.value)}
              validate={{
                required: { value: true, message: 'Endereço é obrigatório' },
                minLength: { value: 5, message: 'Endereço deve ter pelo menos 5 caracteres' },
              }}
            />
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
