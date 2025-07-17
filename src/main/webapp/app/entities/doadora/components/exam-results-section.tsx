import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ResultadoExame } from 'app/shared/model/enumerations/resultado-exame.model';

interface ExamResultsSectionProps {
  formData: {
    resultadoVDRL: string;
    resultadoHBsAg: string;
    resultadoFTAabs: string;
    resultadoHIV: string;
  };
  formErrors: {
    resultadoVDRL: string;
    resultadoHBsAg: string;
    resultadoFTAabs: string;
    resultadoHIV: string;
  };
  handleInputChange: (field: string, value: string | boolean) => void;
}

export const ExamResultsSection: React.FC<ExamResultsSectionProps> = ({ formData, formErrors, handleInputChange }) => {
  const resultadoExameValues = Object.keys(ResultadoExame);

  return (
    <Card className="mb-4 shadow-sm">
      <CardHeader className="bg-warning text-dark">
        <h5 className="mb-0">
          <FontAwesomeIcon icon="flask" className="me-2" />
          Resultados dos Exames
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md="6">
            <ValidatedField
              label="Resultado VDRL"
              id="doadora-resultadoVDRL"
              name="resultadoVDRL"
              data-cy="resultadoVDRL"
              type="select"
              value={formData.resultadoVDRL}
              onChange={e => handleInputChange('resultadoVDRL', e.target.value)}
              validate={{
                required: { value: true, message: 'Resultado VDRL é obrigatório' },
              }}
              errorMessage={formErrors.resultadoVDRL}
            >
              <option value="">Selecione...</option>
              {resultadoExameValues.map(resultado => (
                <option value={resultado} key={resultado}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultado}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
          <Col md="6">
            <ValidatedField
              label="Resultado HBsAg"
              id="doadora-resultadoHBsAg"
              name="resultadoHBsAg"
              data-cy="resultadoHBsAg"
              type="select"
              value={formData.resultadoHBsAg}
              onChange={e => handleInputChange('resultadoHBsAg', e.target.value)}
              validate={{
                required: { value: true, message: 'Resultado HBsAg é obrigatório' },
              }}
              errorMessage={formErrors.resultadoHBsAg}
            >
              <option value="">Selecione...</option>
              {resultadoExameValues.map(resultado => (
                <option value={resultado} key={resultado}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultado}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
        </Row>
        <Row>
          <Col md="6">
            <ValidatedField
              label="Resultado FTA-abs"
              id="doadora-resultadoFTAabs"
              name="resultadoFTAabs"
              data-cy="resultadoFTAabs"
              type="select"
              value={formData.resultadoFTAabs}
              onChange={e => handleInputChange('resultadoFTAabs', e.target.value)}
              validate={{
                required: { value: true, message: 'Resultado FTA-abs é obrigatório' },
              }}
              errorMessage={formErrors.resultadoFTAabs}
            >
              <option value="">Selecione...</option>
              {resultadoExameValues.map(resultado => (
                <option value={resultado} key={resultado}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultado}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
          <Col md="6">
            <ValidatedField
              label="Resultado HIV"
              id="doadora-resultadoHIV"
              name="resultadoHIV"
              data-cy="resultadoHIV"
              type="select"
              value={formData.resultadoHIV}
              onChange={e => handleInputChange('resultadoHIV', e.target.value)}
              validate={{
                required: { value: true, message: 'Resultado HIV é obrigatório' },
              }}
              errorMessage={formErrors.resultadoHIV}
            >
              <option value="">Selecione...</option>
              {resultadoExameValues.map(resultado => (
                <option value={resultado} key={resultado}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultado}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
