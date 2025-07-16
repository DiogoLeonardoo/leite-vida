import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { TipoDoadora } from 'app/shared/model/enumerations/tipo-doadora.model';
import { LocalPreNatal } from 'app/shared/model/enumerations/local-pre-natal.model';

export const MedicalInfoSection: React.FC = () => {
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
            <ValidatedField label="Tipo de Doadora" id="doadora-tipoDoadora" name="tipoDoadora" data-cy="tipoDoadora" type="select">
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
            >
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
            />
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
