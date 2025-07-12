import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './paciente.reducer';

export const PacienteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pacienteEntity = useAppSelector(state => state.paciente.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pacienteDetailsHeading">
          <Translate contentKey="leiteVidaApp.paciente.detail.title">Paciente</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="leiteVidaApp.paciente.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.nome}</dd>
          <dt>
            <span id="registroHospitalar">
              <Translate contentKey="leiteVidaApp.paciente.registroHospitalar">Registro Hospitalar</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.registroHospitalar}</dd>
          <dt>
            <span id="dataNascimento">
              <Translate contentKey="leiteVidaApp.paciente.dataNascimento">Data Nascimento</Translate>
            </span>
          </dt>
          <dd>
            {pacienteEntity.dataNascimento ? (
              <TextFormat value={pacienteEntity.dataNascimento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="pesoNascimento">
              <Translate contentKey="leiteVidaApp.paciente.pesoNascimento">Peso Nascimento</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.pesoNascimento}</dd>
          <dt>
            <span id="idadeGestacional">
              <Translate contentKey="leiteVidaApp.paciente.idadeGestacional">Idade Gestacional</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.idadeGestacional}</dd>
          <dt>
            <span id="condicaoClinica">
              <Translate contentKey="leiteVidaApp.paciente.condicaoClinica">Condicao Clinica</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.condicaoClinica}</dd>
          <dt>
            <span id="nomeResponsavel">
              <Translate contentKey="leiteVidaApp.paciente.nomeResponsavel">Nome Responsavel</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.nomeResponsavel}</dd>
          <dt>
            <span id="cpfResponsavel">
              <Translate contentKey="leiteVidaApp.paciente.cpfResponsavel">Cpf Responsavel</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.cpfResponsavel}</dd>
          <dt>
            <span id="telefoneResponsavel">
              <Translate contentKey="leiteVidaApp.paciente.telefoneResponsavel">Telefone Responsavel</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.telefoneResponsavel}</dd>
          <dt>
            <span id="parentescoResponsavel">
              <Translate contentKey="leiteVidaApp.paciente.parentescoResponsavel">Parentesco Responsavel</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.parentescoResponsavel}</dd>
          <dt>
            <span id="statusAtivo">
              <Translate contentKey="leiteVidaApp.paciente.statusAtivo">Status Ativo</Translate>
            </span>
          </dt>
          <dd>{pacienteEntity.statusAtivo ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/paciente" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/paciente/${pacienteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PacienteDetail;
