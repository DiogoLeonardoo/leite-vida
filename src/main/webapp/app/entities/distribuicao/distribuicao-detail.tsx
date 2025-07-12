import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './distribuicao.reducer';

export const DistribuicaoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const distribuicaoEntity = useAppSelector(state => state.distribuicao.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="distribuicaoDetailsHeading">
          <Translate contentKey="leiteVidaApp.distribuicao.detail.title">Distribuicao</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{distribuicaoEntity.id}</dd>
          <dt>
            <span id="dataDistribuicao">
              <Translate contentKey="leiteVidaApp.distribuicao.dataDistribuicao">Data Distribuicao</Translate>
            </span>
          </dt>
          <dd>
            {distribuicaoEntity.dataDistribuicao ? (
              <TextFormat value={distribuicaoEntity.dataDistribuicao} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="volumeDistribuidoMl">
              <Translate contentKey="leiteVidaApp.distribuicao.volumeDistribuidoMl">Volume Distribuido Ml</Translate>
            </span>
          </dt>
          <dd>{distribuicaoEntity.volumeDistribuidoMl}</dd>
          <dt>
            <span id="responsavelEntrega">
              <Translate contentKey="leiteVidaApp.distribuicao.responsavelEntrega">Responsavel Entrega</Translate>
            </span>
          </dt>
          <dd>{distribuicaoEntity.responsavelEntrega}</dd>
          <dt>
            <span id="responsavelRecebimento">
              <Translate contentKey="leiteVidaApp.distribuicao.responsavelRecebimento">Responsavel Recebimento</Translate>
            </span>
          </dt>
          <dd>{distribuicaoEntity.responsavelRecebimento}</dd>
          <dt>
            <span id="observacoes">
              <Translate contentKey="leiteVidaApp.distribuicao.observacoes">Observacoes</Translate>
            </span>
          </dt>
          <dd>{distribuicaoEntity.observacoes}</dd>
          <dt>
            <Translate contentKey="leiteVidaApp.distribuicao.estoque">Estoque</Translate>
          </dt>
          <dd>{distribuicaoEntity.estoque ? distribuicaoEntity.estoque.id : ''}</dd>
          <dt>
            <Translate contentKey="leiteVidaApp.distribuicao.paciente">Paciente</Translate>
          </dt>
          <dd>{distribuicaoEntity.paciente ? distribuicaoEntity.paciente.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/distribuicao" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/distribuicao/${distribuicaoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DistribuicaoDetail;
