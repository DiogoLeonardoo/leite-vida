import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEstoques } from 'app/entities/estoque/estoque.reducer';
import { getEntities as getPacientes } from 'app/entities/paciente/paciente.reducer';
import { createEntity, getEntity, reset, updateEntity } from './distribuicao.reducer';

export const DistribuicaoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estoques = useAppSelector(state => state.estoque.entities);
  const pacientes = useAppSelector(state => state.paciente.entities);
  const distribuicaoEntity = useAppSelector(state => state.distribuicao.entity);
  const loading = useAppSelector(state => state.distribuicao.loading);
  const updating = useAppSelector(state => state.distribuicao.updating);
  const updateSuccess = useAppSelector(state => state.distribuicao.updateSuccess);

  const handleClose = () => {
    navigate(`/distribuicao${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstoques({}));
    dispatch(getPacientes({}));
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
    if (values.volumeDistribuidoMl !== undefined && typeof values.volumeDistribuidoMl !== 'number') {
      values.volumeDistribuidoMl = Number(values.volumeDistribuidoMl);
    }

    const entity = {
      ...distribuicaoEntity,
      ...values,
      estoque: estoques.find(it => it.id.toString() === values.estoque?.toString()),
      paciente: pacientes.find(it => it.id.toString() === values.paciente?.toString()),
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
          ...distribuicaoEntity,
          estoque: distribuicaoEntity?.estoque?.id,
          paciente: distribuicaoEntity?.paciente?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="leiteVidaApp.distribuicao.home.createOrEditLabel" data-cy="DistribuicaoCreateUpdateHeading">
            <Translate contentKey="leiteVidaApp.distribuicao.home.createOrEditLabel">Create or edit a Distribuicao</Translate>
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
                  id="distribuicao-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('leiteVidaApp.distribuicao.dataDistribuicao')}
                id="distribuicao-dataDistribuicao"
                name="dataDistribuicao"
                data-cy="dataDistribuicao"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.distribuicao.volumeDistribuidoMl')}
                id="distribuicao-volumeDistribuidoMl"
                name="volumeDistribuidoMl"
                data-cy="volumeDistribuidoMl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.distribuicao.responsavelEntrega')}
                id="distribuicao-responsavelEntrega"
                name="responsavelEntrega"
                data-cy="responsavelEntrega"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.distribuicao.responsavelRecebimento')}
                id="distribuicao-responsavelRecebimento"
                name="responsavelRecebimento"
                data-cy="responsavelRecebimento"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.distribuicao.observacoes')}
                id="distribuicao-observacoes"
                name="observacoes"
                data-cy="observacoes"
                type="text"
              />
              <ValidatedField
                id="distribuicao-estoque"
                name="estoque"
                data-cy="estoque"
                label={translate('leiteVidaApp.distribuicao.estoque')}
                type="select"
                required
              >
                <option value="" key="0" />
                {estoques
                  ? estoques.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="distribuicao-paciente"
                name="paciente"
                data-cy="paciente"
                label={translate('leiteVidaApp.distribuicao.paciente')}
                type="select"
                required
              >
                <option value="" key="0" />
                {pacientes
                  ? pacientes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/distribuicao" replace color="info">
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

export default DistribuicaoUpdate;
