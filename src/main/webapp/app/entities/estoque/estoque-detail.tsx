import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './estoque.reducer';

export const EstoqueDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const estoqueEntity = useAppSelector(state => state.estoque.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="estoqueDetailsHeading">
          <Translate contentKey="leiteVidaApp.estoque.detail.title">Estoque</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.id}</dd>
          <dt>
            <span id="dataProducao">
              <Translate contentKey="leiteVidaApp.estoque.dataProducao">Data Producao</Translate>
            </span>
          </dt>
          <dd>
            {estoqueEntity.dataProducao ? (
              <TextFormat value={estoqueEntity.dataProducao} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dataValidade">
              <Translate contentKey="leiteVidaApp.estoque.dataValidade">Data Validade</Translate>
            </span>
          </dt>
          <dd>
            {estoqueEntity.dataValidade ? (
              <TextFormat value={estoqueEntity.dataValidade} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="tipoLeite">
              <Translate contentKey="leiteVidaApp.estoque.tipoLeite">Tipo Leite</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.tipoLeite}</dd>
          <dt>
            <span id="classificacao">
              <Translate contentKey="leiteVidaApp.estoque.classificacao">Classificacao</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.classificacao}</dd>
          <dt>
            <span id="volumeTotalMl">
              <Translate contentKey="leiteVidaApp.estoque.volumeTotalMl">Volume Total Ml</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.volumeTotalMl}</dd>
          <dt>
            <span id="volumeDisponivelMl">
              <Translate contentKey="leiteVidaApp.estoque.volumeDisponivelMl">Volume Disponivel Ml</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.volumeDisponivelMl}</dd>
          <dt>
            <span id="localArmazenamento">
              <Translate contentKey="leiteVidaApp.estoque.localArmazenamento">Local Armazenamento</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.localArmazenamento}</dd>
          <dt>
            <span id="temperaturaArmazenamento">
              <Translate contentKey="leiteVidaApp.estoque.temperaturaArmazenamento">Temperatura Armazenamento</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.temperaturaArmazenamento}</dd>
          <dt>
            <span id="statusLote">
              <Translate contentKey="leiteVidaApp.estoque.statusLote">Status Lote</Translate>
            </span>
          </dt>
          <dd>{estoqueEntity.statusLote}</dd>
        </dl>
        <Button tag={Link} to="/estoque" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/estoque/${estoqueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EstoqueDetail;
