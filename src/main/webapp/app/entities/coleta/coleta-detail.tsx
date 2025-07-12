import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './coleta.reducer';

export const ColetaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const coletaEntity = useAppSelector(state => state.coleta.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="coletaDetailsHeading">
          <Translate contentKey="leiteVidaApp.coleta.detail.title">Coleta</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{coletaEntity.id}</dd>
          <dt>
            <span id="dataColeta">
              <Translate contentKey="leiteVidaApp.coleta.dataColeta">Data Coleta</Translate>
            </span>
          </dt>
          <dd>
            {coletaEntity.dataColeta ? <TextFormat value={coletaEntity.dataColeta} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="volumeMl">
              <Translate contentKey="leiteVidaApp.coleta.volumeMl">Volume Ml</Translate>
            </span>
          </dt>
          <dd>{coletaEntity.volumeMl}</dd>
          <dt>
            <span id="temperatura">
              <Translate contentKey="leiteVidaApp.coleta.temperatura">Temperatura</Translate>
            </span>
          </dt>
          <dd>{coletaEntity.temperatura}</dd>
          <dt>
            <span id="localColeta">
              <Translate contentKey="leiteVidaApp.coleta.localColeta">Local Coleta</Translate>
            </span>
          </dt>
          <dd>{coletaEntity.localColeta}</dd>
          <dt>
            <span id="observacoes">
              <Translate contentKey="leiteVidaApp.coleta.observacoes">Observacoes</Translate>
            </span>
          </dt>
          <dd>{coletaEntity.observacoes}</dd>
          <dt>
            <span id="statusColeta">
              <Translate contentKey="leiteVidaApp.coleta.statusColeta">Status Coleta</Translate>
            </span>
          </dt>
          <dd>{coletaEntity.statusColeta}</dd>
          <dt>
            <Translate contentKey="leiteVidaApp.coleta.processamento">Processamento</Translate>
          </dt>
          <dd>{coletaEntity.processamento ? coletaEntity.processamento.id : ''}</dd>
          <dt>
            <Translate contentKey="leiteVidaApp.coleta.doadora">Doadora</Translate>
          </dt>
          <dd>{coletaEntity.doadora ? coletaEntity.doadora.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/coleta" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/coleta/${coletaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ColetaDetail;
