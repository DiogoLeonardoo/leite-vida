import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './processamento.reducer';

export const ProcessamentoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const processamentoEntity = useAppSelector(state => state.processamento.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="processamentoDetailsHeading">
          <Translate contentKey="leiteVidaApp.processamento.detail.title">Processamento</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{processamentoEntity.id}</dd>
          <dt>
            <span id="dataProcessamento">
              <Translate contentKey="leiteVidaApp.processamento.dataProcessamento">Data Processamento</Translate>
            </span>
          </dt>
          <dd>
            {processamentoEntity.dataProcessamento ? (
              <TextFormat value={processamentoEntity.dataProcessamento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="tecnicoResponsavel">
              <Translate contentKey="leiteVidaApp.processamento.tecnicoResponsavel">Tecnico Responsavel</Translate>
            </span>
          </dt>
          <dd>{processamentoEntity.tecnicoResponsavel}</dd>
          <dt>
            <span id="valorAcidezDornic">
              <Translate contentKey="leiteVidaApp.processamento.valorAcidezDornic">Valor Acidez Dornic</Translate>
            </span>
          </dt>
          <dd>{processamentoEntity.valorAcidezDornic}</dd>
          <dt>
            <span id="valorCaloricoKcal">
              <Translate contentKey="leiteVidaApp.processamento.valorCaloricoKcal">Valor Calorico Kcal</Translate>
            </span>
          </dt>
          <dd>{processamentoEntity.valorCaloricoKcal}</dd>
          <dt>
            <span id="resultadoAnalise">
              <Translate contentKey="leiteVidaApp.processamento.resultadoAnalise">Resultado Analise</Translate>
            </span>
          </dt>
          <dd>{processamentoEntity.resultadoAnalise}</dd>
          <dt>
            <span id="statusProcessamento">
              <Translate contentKey="leiteVidaApp.processamento.statusProcessamento">Status Processamento</Translate>
            </span>
          </dt>
          <dd>{processamentoEntity.statusProcessamento}</dd>
          <dt>
            <Translate contentKey="leiteVidaApp.processamento.estoque">Estoque</Translate>
          </dt>
          <dd>{processamentoEntity.estoque ? processamentoEntity.estoque.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/processamento" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/processamento/${processamentoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProcessamentoDetail;
