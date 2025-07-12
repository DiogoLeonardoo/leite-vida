import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEstoques } from 'app/entities/estoque/estoque.reducer';
import { ResultadoAnalise } from 'app/shared/model/enumerations/resultado-analise.model';
import { StatusProcessamento } from 'app/shared/model/enumerations/status-processamento.model';
import { createEntity, getEntity, reset, updateEntity } from './processamento.reducer';

export const ProcessamentoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estoques = useAppSelector(state => state.estoque.entities);
  const processamentoEntity = useAppSelector(state => state.processamento.entity);
  const loading = useAppSelector(state => state.processamento.loading);
  const updating = useAppSelector(state => state.processamento.updating);
  const updateSuccess = useAppSelector(state => state.processamento.updateSuccess);
  const resultadoAnaliseValues = Object.keys(ResultadoAnalise);
  const statusProcessamentoValues = Object.keys(StatusProcessamento);

  const handleClose = () => {
    navigate(`/processamento${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstoques({}));
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
    if (values.valorAcidezDornic !== undefined && typeof values.valorAcidezDornic !== 'number') {
      values.valorAcidezDornic = Number(values.valorAcidezDornic);
    }
    if (values.valorCaloricoKcal !== undefined && typeof values.valorCaloricoKcal !== 'number') {
      values.valorCaloricoKcal = Number(values.valorCaloricoKcal);
    }

    const entity = {
      ...processamentoEntity,
      ...values,
      estoque: estoques.find(it => it.id.toString() === values.estoque?.toString()),
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
          resultadoAnalise: 'APROVADO',
          statusProcessamento: 'EM_ANALISE',
          ...processamentoEntity,
          estoque: processamentoEntity?.estoque?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="leiteVidaApp.processamento.home.createOrEditLabel" data-cy="ProcessamentoCreateUpdateHeading">
            <Translate contentKey="leiteVidaApp.processamento.home.createOrEditLabel">Create or edit a Processamento</Translate>
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
                  id="processamento-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('leiteVidaApp.processamento.dataProcessamento')}
                id="processamento-dataProcessamento"
                name="dataProcessamento"
                data-cy="dataProcessamento"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.processamento.tecnicoResponsavel')}
                id="processamento-tecnicoResponsavel"
                name="tecnicoResponsavel"
                data-cy="tecnicoResponsavel"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.processamento.valorAcidezDornic')}
                id="processamento-valorAcidezDornic"
                name="valorAcidezDornic"
                data-cy="valorAcidezDornic"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.processamento.valorCaloricoKcal')}
                id="processamento-valorCaloricoKcal"
                name="valorCaloricoKcal"
                data-cy="valorCaloricoKcal"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.processamento.resultadoAnalise')}
                id="processamento-resultadoAnalise"
                name="resultadoAnalise"
                data-cy="resultadoAnalise"
                type="select"
              >
                {resultadoAnaliseValues.map(resultadoAnalise => (
                  <option value={resultadoAnalise} key={resultadoAnalise}>
                    {translate(`leiteVidaApp.ResultadoAnalise.${resultadoAnalise}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.processamento.statusProcessamento')}
                id="processamento-statusProcessamento"
                name="statusProcessamento"
                data-cy="statusProcessamento"
                type="select"
              >
                {statusProcessamentoValues.map(statusProcessamento => (
                  <option value={statusProcessamento} key={statusProcessamento}>
                    {translate(`leiteVidaApp.StatusProcessamento.${statusProcessamento}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="processamento-estoque"
                name="estoque"
                data-cy="estoque"
                label={translate('leiteVidaApp.processamento.estoque')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/processamento" replace color="info">
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

export default ProcessamentoUpdate;
