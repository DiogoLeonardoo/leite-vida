import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { TipoDoadora } from 'app/shared/model/enumerations/tipo-doadora.model';
import { LocalPreNatal } from 'app/shared/model/enumerations/local-pre-natal.model';
import { ResultadoExame } from 'app/shared/model/enumerations/resultado-exame.model';
import { createEntity, getEntity, reset, updateEntity } from './doadora.reducer';

export const DoadoraUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const doadoraEntity = useAppSelector(state => state.doadora.entity);
  const loading = useAppSelector(state => state.doadora.loading);
  const updating = useAppSelector(state => state.doadora.updating);
  const updateSuccess = useAppSelector(state => state.doadora.updateSuccess);
  const tipoDoadoraValues = Object.keys(TipoDoadora);
  const localPreNatalValues = Object.keys(LocalPreNatal);
  const resultadoExameValues = Object.keys(ResultadoExame);

  const handleClose = () => {
    navigate(`/doadora${location.search}`);
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

    const entity = {
      ...doadoraEntity,
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
          tipoDoadora: 'DOMICILIAR',
          localPreNatal: 'REDE_PUBLICA',
          resultadoVDRL: 'POSITIVO',
          resultadoHBsAg: 'POSITIVO',
          resultadoFTAabs: 'POSITIVO',
          resultadoHIV: 'POSITIVO',
          ...doadoraEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="leiteVidaApp.doadora.home.createOrEditLabel" data-cy="DoadoraCreateUpdateHeading">
            <Translate contentKey="leiteVidaApp.doadora.home.createOrEditLabel">Create or edit a Doadora</Translate>
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
                  id="doadora-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('leiteVidaApp.doadora.nome')}
                id="doadora-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.cartaoSUS')}
                id="doadora-cartaoSUS"
                name="cartaoSUS"
                data-cy="cartaoSUS"
                type="text"
                validate={{}}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.cpf')}
                id="doadora-cpf"
                name="cpf"
                data-cy="cpf"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.dataNascimento')}
                id="doadora-dataNascimento"
                name="dataNascimento"
                data-cy="dataNascimento"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.cep')}
                id="doadora-cep"
                name="cep"
                data-cy="cep"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.estado')}
                id="doadora-estado"
                name="estado"
                data-cy="estado"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.cidade')}
                id="doadora-cidade"
                name="cidade"
                data-cy="cidade"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.endereco')}
                id="doadora-endereco"
                name="endereco"
                data-cy="endereco"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.telefone')}
                id="doadora-telefone"
                name="telefone"
                data-cy="telefone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.profissao')}
                id="doadora-profissao"
                name="profissao"
                data-cy="profissao"
                type="text"
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.tipoDoadora')}
                id="doadora-tipoDoadora"
                name="tipoDoadora"
                data-cy="tipoDoadora"
                type="select"
              >
                {tipoDoadoraValues.map(tipoDoadora => (
                  <option value={tipoDoadora} key={tipoDoadora}>
                    {translate(`leiteVidaApp.TipoDoadora.${tipoDoadora}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.doadora.localPreNatal')}
                id="doadora-localPreNatal"
                name="localPreNatal"
                data-cy="localPreNatal"
                type="select"
              >
                {localPreNatalValues.map(localPreNatal => (
                  <option value={localPreNatal} key={localPreNatal}>
                    {translate(`leiteVidaApp.LocalPreNatal.${localPreNatal}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.doadora.resultadoVDRL')}
                id="doadora-resultadoVDRL"
                name="resultadoVDRL"
                data-cy="resultadoVDRL"
                type="select"
              >
                {resultadoExameValues.map(resultadoExame => (
                  <option value={resultadoExame} key={resultadoExame}>
                    {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.doadora.resultadoHBsAg')}
                id="doadora-resultadoHBsAg"
                name="resultadoHBsAg"
                data-cy="resultadoHBsAg"
                type="select"
              >
                {resultadoExameValues.map(resultadoExame => (
                  <option value={resultadoExame} key={resultadoExame}>
                    {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.doadora.resultadoFTAabs')}
                id="doadora-resultadoFTAabs"
                name="resultadoFTAabs"
                data-cy="resultadoFTAabs"
                type="select"
              >
                {resultadoExameValues.map(resultadoExame => (
                  <option value={resultadoExame} key={resultadoExame}>
                    {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.doadora.resultadoHIV')}
                id="doadora-resultadoHIV"
                name="resultadoHIV"
                data-cy="resultadoHIV"
                type="select"
              >
                {resultadoExameValues.map(resultadoExame => (
                  <option value={resultadoExame} key={resultadoExame}>
                    {translate(`leiteVidaApp.ResultadoExame.${resultadoExame}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.doadora.transfusaoUltimos5Anos')}
                id="doadora-transfusaoUltimos5Anos"
                name="transfusaoUltimos5Anos"
                data-cy="transfusaoUltimos5Anos"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('leiteVidaApp.doadora.dataRegistro')}
                id="doadora-dataRegistro"
                name="dataRegistro"
                data-cy="dataRegistro"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/doadora" replace color="info">
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

export default DoadoraUpdate;
