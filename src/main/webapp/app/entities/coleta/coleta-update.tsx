import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getProcessamentos } from 'app/entities/processamento/processamento.reducer';
import { getEntities as getDoadoras } from 'app/entities/doadora/doadora.reducer';
import { StatusColeta } from 'app/shared/model/enumerations/status-coleta.model';
import { createEntity, getEntity, reset, updateEntity } from './coleta.reducer';

export const ColetaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const processamentos = useAppSelector(state => state.processamento.entities);
  const doadoras = useAppSelector(state => state.doadora.entities);
  const coletaEntity = useAppSelector(state => state.coleta.entity);
  const loading = useAppSelector(state => state.coleta.loading);
  const updating = useAppSelector(state => state.coleta.updating);
  const updateSuccess = useAppSelector(state => state.coleta.updateSuccess);
  const statusColetaValues = Object.keys(StatusColeta);

  const handleClose = () => {
    navigate(`/coleta${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getProcessamentos({}));
    dispatch(getDoadoras({}));
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
    if (values.volumeMl !== undefined && typeof values.volumeMl !== 'number') {
      values.volumeMl = Number(values.volumeMl);
    }
    if (values.temperatura !== undefined && typeof values.temperatura !== 'number') {
      values.temperatura = Number(values.temperatura);
    }

    const entity = {
      ...coletaEntity,
      ...values,
      processamento: processamentos.find(it => it.id.toString() === values.processamento?.toString()),
      doadora: doadoras.find(it => it.id.toString() === values.doadora?.toString()),
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
          statusColeta: 'AGUARDANDO_PROCESSAMENTO',
          ...coletaEntity,
          processamento: coletaEntity?.processamento?.id,
          doadora: coletaEntity?.doadora?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="leiteVidaApp.coleta.home.createOrEditLabel" data-cy="ColetaCreateUpdateHeading">
            <Translate contentKey="leiteVidaApp.coleta.home.createOrEditLabel">Create or edit a Coleta</Translate>
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
                  id="coleta-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('leiteVidaApp.coleta.dataColeta')}
                id="coleta-dataColeta"
                name="dataColeta"
                data-cy="dataColeta"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.coleta.volumeMl')}
                id="coleta-volumeMl"
                name="volumeMl"
                data-cy="volumeMl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.coleta.temperatura')}
                id="coleta-temperatura"
                name="temperatura"
                data-cy="temperatura"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.coleta.localColeta')}
                id="coleta-localColeta"
                name="localColeta"
                data-cy="localColeta"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.coleta.observacoes')}
                id="coleta-observacoes"
                name="observacoes"
                data-cy="observacoes"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.coleta.statusColeta')}
                id="coleta-statusColeta"
                name="statusColeta"
                data-cy="statusColeta"
                type="select"
              >
                {statusColetaValues.map(statusColeta => (
                  <option value={statusColeta} key={statusColeta}>
                    {translate(`leiteVidaApp.StatusColeta.${statusColeta}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="coleta-processamento"
                name="processamento"
                data-cy="processamento"
                label={translate('leiteVidaApp.coleta.processamento')}
                type="select"
              >
                <option value="" key="0" />
                {processamentos
                  ? processamentos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="coleta-doadora"
                name="doadora"
                data-cy="doadora"
                label={translate('leiteVidaApp.coleta.doadora')}
                type="select"
                required
              >
                <option value="" key="0" />
                {doadoras
                  ? doadoras.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/coleta" replace color="info">
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

export default ColetaUpdate;
