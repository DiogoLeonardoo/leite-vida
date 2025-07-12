import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './doadora.reducer';

export const DoadoraDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const doadoraEntity = useAppSelector(state => state.doadora.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="doadoraDetailsHeading">
          <Translate contentKey="leiteVidaApp.doadora.detail.title">Doadora</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="leiteVidaApp.doadora.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.nome}</dd>
          <dt>
            <span id="cartaoSUS">
              <Translate contentKey="leiteVidaApp.doadora.cartaoSUS">Cartao SUS</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.cartaoSUS}</dd>
          <dt>
            <span id="cpf">
              <Translate contentKey="leiteVidaApp.doadora.cpf">Cpf</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.cpf}</dd>
          <dt>
            <span id="dataNascimento">
              <Translate contentKey="leiteVidaApp.doadora.dataNascimento">Data Nascimento</Translate>
            </span>
          </dt>
          <dd>
            {doadoraEntity.dataNascimento ? (
              <TextFormat value={doadoraEntity.dataNascimento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="cep">
              <Translate contentKey="leiteVidaApp.doadora.cep">Cep</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.cep}</dd>
          <dt>
            <span id="estado">
              <Translate contentKey="leiteVidaApp.doadora.estado">Estado</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.estado}</dd>
          <dt>
            <span id="cidade">
              <Translate contentKey="leiteVidaApp.doadora.cidade">Cidade</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.cidade}</dd>
          <dt>
            <span id="endereco">
              <Translate contentKey="leiteVidaApp.doadora.endereco">Endereco</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.endereco}</dd>
          <dt>
            <span id="telefone">
              <Translate contentKey="leiteVidaApp.doadora.telefone">Telefone</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.telefone}</dd>
          <dt>
            <span id="profissao">
              <Translate contentKey="leiteVidaApp.doadora.profissao">Profissao</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.profissao}</dd>
          <dt>
            <span id="tipoDoadora">
              <Translate contentKey="leiteVidaApp.doadora.tipoDoadora">Tipo Doadora</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.tipoDoadora}</dd>
          <dt>
            <span id="localPreNatal">
              <Translate contentKey="leiteVidaApp.doadora.localPreNatal">Local Pre Natal</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.localPreNatal}</dd>
          <dt>
            <span id="resultadoVDRL">
              <Translate contentKey="leiteVidaApp.doadora.resultadoVDRL">Resultado VDRL</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.resultadoVDRL}</dd>
          <dt>
            <span id="resultadoHBsAg">
              <Translate contentKey="leiteVidaApp.doadora.resultadoHBsAg">Resultado H Bs Ag</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.resultadoHBsAg}</dd>
          <dt>
            <span id="resultadoFTAabs">
              <Translate contentKey="leiteVidaApp.doadora.resultadoFTAabs">Resultado FT Aabs</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.resultadoFTAabs}</dd>
          <dt>
            <span id="resultadoHIV">
              <Translate contentKey="leiteVidaApp.doadora.resultadoHIV">Resultado HIV</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.resultadoHIV}</dd>
          <dt>
            <span id="transfusaoUltimos5Anos">
              <Translate contentKey="leiteVidaApp.doadora.transfusaoUltimos5Anos">Transfusao Ultimos 5 Anos</Translate>
            </span>
          </dt>
          <dd>{doadoraEntity.transfusaoUltimos5Anos ? 'true' : 'false'}</dd>
          <dt>
            <span id="dataRegistro">
              <Translate contentKey="leiteVidaApp.doadora.dataRegistro">Data Registro</Translate>
            </span>
          </dt>
          <dd>
            {doadoraEntity.dataRegistro ? (
              <TextFormat value={doadoraEntity.dataRegistro} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/doadora" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/doadora/${doadoraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DoadoraDetail;
