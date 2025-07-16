import React from 'react';
import { Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { ValidatedField, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ResultadoExame } from 'app/shared/model/enumerations/resultado-exame.model';

export const ExamResultsSection: React.FC = () => {
  const resultadoExameValues = Object.keys(ResultadoExame);

  return (
    <Card className="mb-4 shadow-sm">
      <CardHeader className="bg-warning text-dark">
        <h5 className="mb-0">
          <FontAwesomeIcon icon="flask" className="me-2" />
          Resultados de Exames
        </h5>
      </CardHeader>
      <CardBody>
        <Row>
          <Col md="6">
            <ValidatedField label="Resultado VDRL" id="doadora-resultadoVDRL" name="resultadoVDRL" data-cy="resultadoVDRL" type="select">
              {resultadoExameValues.map(resultadoExame => (
                <option value={resultadoExame} key={resultadoExame}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
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
            >
              {resultadoExameValues.map(resultadoExame => (
                <option value={resultadoExame} key={resultadoExame}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
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
            >
              {resultadoExameValues.map(resultadoExame => (
                <option value={resultadoExame} key={resultadoExame}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
          <Col md="6">
            <ValidatedField label="Resultado HIV" id="doadora-resultadoHIV" name="resultadoHIV" data-cy="resultadoHIV" type="select">
              {resultadoExameValues.map(resultadoExame => (
                <option value={resultadoExame} key={resultadoExame}>
                  {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
                </option>
              ))}
            </ValidatedField>
          </Col>
        </Row>
      </CardBody>
    </Card>
  );
};
