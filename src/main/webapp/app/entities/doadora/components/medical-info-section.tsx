import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TipoDoadora } from 'app/shared/model/enumerations/tipo-doadora.model';
import { LocalPreNatal } from 'app/shared/model/enumerations/local-pre-natal.model';

interface MedicalInfoSectionProps {
  formData: {
    tipoDoadora: string;
    localPreNatal: string;
    transfusaoUltimos5Anos: boolean;
  };
  formErrors: {
    tipoDoadora: string;
    localPreNatal: string;
  };
  handleInputChange: (field: string, value: string | boolean) => void;
}

export const MedicalInfoSection: React.FC<MedicalInfoSectionProps> = ({ formData, formErrors, handleInputChange }) => {
  const tipoDoadoraValues = Object.keys(TipoDoadora);
  const localPreNatalValues = Object.keys(LocalPreNatal);

  return (
    <Card className="mb-4 shadow-sm">
      <CardHeader className="bg-success text-white">
        <h5 className="mb-0">
          <FontAwesomeIcon icon="heartbeat" className="me-2" />
          Informações Médicas
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md="6">
            <ValidatedField
              label="Tipo de Doadora"
              id="doadora-tipoDoadora"
              name="tipoDoadora"
              data-cy="tipoDoadora"
              type="select"
              value={formData.tipoDoadora}
              onChange={e => handleInputChange('tipoDoadora', e.target.value)}
              validate={{
                required: { value: true, message: 'Tipo de doadora é obrigatório' },
              }}
              errorMessage={formErrors.tipoDoadora}
            >
              <option value="">Selecione...</option>
              {tipoDoadoraValues.map(tipoDoadora => (
                <option value={tipoDoadora} key={tipoDoadora}>
                  {translate(`leiteVidaApp.TipoDoadora.${tipoDoadora}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
          <Col md="6">
            <ValidatedField
              label="Local do Pré-Natal"
              id="doadora-localPreNatal"
              name="localPreNatal"
              data-cy="localPreNatal"
              type="select"
              value={formData.localPreNatal}
              onChange={e => handleInputChange('localPreNatal', e.target.value)}
              validate={{
                required: { value: true, message: 'Local do pré-natal é obrigatório' },
              }}
              errorMessage={formErrors.localPreNatal}
            >
              <option value="">Selecione...</option>
              {localPreNatalValues.map(localPreNatal => (
                <option value={localPreNatal} key={localPreNatal}>
                  {translate(`leiteVidaApp.LocalPreNatal.${localPreNatal}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
        </Row>
        <Row>
          <Col md="12">
            <ValidatedField
              label="Transfusão nos últimos 5 anos?"
              id="doadora-transfusaoUltimos5Anos"
              name="transfusaoUltimos5Anos"
              data-cy="transfusaoUltimos5Anos"
              check
              type="checkbox"
              checked={formData.transfusaoUltimos5Anos}
              onChange={e => handleInputChange('transfusaoUltimos5Anos', e.target.checked)}
            />
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
