import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './paciente.reducer';

export const PacienteUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pacienteEntity = useAppSelector(state => state.paciente.entity);
  const loading = useAppSelector(state => state.paciente.loading);
  const updating = useAppSelector(state => state.paciente.updating);
  const updateSuccess = useAppSelector(state => state.paciente.updateSuccess);

  const handleClose = () => {
    navigate(`/paciente${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.pesoNascimento !== undefined && typeof values.pesoNascimento !== 'number') {
      values.pesoNascimento = Number(values.pesoNascimento);
    }
    if (values.idadeGestacional !== undefined && typeof values.idadeGestacional !== 'number') {
      values.idadeGestacional = Number(values.idadeGestacional);
    }

    const entity = {
      ...pacienteEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pacienteEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="leiteVidaApp.paciente.home.createOrEditLabel" data-cy="PacienteCreateUpdateHeading">
            <Translate contentKey="leiteVidaApp.paciente.home.createOrEditLabel">Create or edit a Paciente</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="paciente-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('leiteVidaApp.paciente.nome')}
                id="paciente-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.registroHospitalar')}
                id="paciente-registroHospitalar"
                name="registroHospitalar"
                data-cy="registroHospitalar"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.dataNascimento')}
                id="paciente-dataNascimento"
                name="dataNascimento"
                data-cy="dataNascimento"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.pesoNascimento')}
                id="paciente-pesoNascimento"
                name="pesoNascimento"
                data-cy="pesoNascimento"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.idadeGestacional')}
                id="paciente-idadeGestacional"
                name="idadeGestacional"
                data-cy="idadeGestacional"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.condicaoClinica')}
                id="paciente-condicaoClinica"
                name="condicaoClinica"
                data-cy="condicaoClinica"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.nomeResponsavel')}
                id="paciente-nomeResponsavel"
                name="nomeResponsavel"
                data-cy="nomeResponsavel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.cpfResponsavel')}
                id="paciente-cpfResponsavel"
                name="cpfResponsavel"
                data-cy="cpfResponsavel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.telefoneResponsavel')}
                id="paciente-telefoneResponsavel"
                name="telefoneResponsavel"
                data-cy="telefoneResponsavel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.parentescoResponsavel')}
                id="paciente-parentescoResponsavel"
                name="parentescoResponsavel"
                data-cy="parentescoResponsavel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.paciente.statusAtivo')}
                id="paciente-statusAtivo"
                name="statusAtivo"
                data-cy="statusAtivo"
                check
                type="checkbox"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/paciente" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PacienteUpdate;
