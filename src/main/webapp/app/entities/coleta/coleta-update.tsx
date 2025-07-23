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

import './coleta-update.scss';

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
      statusColeta: 'AGUARDANDO_PROCESSAMENTO', // Always set to this status
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
      ? { statusColeta: 'AGUARDANDO_PROCESSAMENTO' }
      : {
          statusColeta: 'AGUARDANDO_PROCESSAMENTO',
          ...coletaEntity,
          processamento: coletaEntity?.processamento?.id,
          doadora: coletaEntity?.doadora?.id,
        };

  return (
    <div className="coleta-update-container">
      <Row className="justify-content-center">
        <Col lg="10" xl="8">
          {loading ? (
            <div className="loading-container">
              <div className="loading-spinner">
                <FontAwesomeIcon icon="spinner" spin size="2x" />
              </div>
              <p>Carregando informações...</p>
            </div>
          ) : (
            <div className="form-card">
              <div className="form-header">
                <div className="header-icon">
                  <FontAwesomeIcon icon="clipboard-list" />
                </div>
                <h2 id="leiteVidaApp.coleta.home.createOrEditLabel" data-cy="ColetaCreateUpdateHeading">
                  <Translate contentKey="leiteVidaApp.coleta.home.createOrEditLabel">Create or edit a Coleta</Translate>
                </h2>
                <div className="header-subtitle">{isNew ? 'Nova Coleta de Leite Materno' : 'Editando Coleta de Leite Materno'}</div>
              </div>

              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                {!isNew ? (
                  <div className="form-section identification-section">
                    <div className="section-title">
                      <FontAwesomeIcon icon="id-card" className="icon" />
                      <span>Identificação</span>
                      <div className="section-indicator"></div>
                    </div>
                    <div className="field-wrapper">
                      <ValidatedField
                        name="id"
                        required
                        readOnly
                        id="coleta-id"
                        label={translate('global.field.id')}
                        validate={{ required: true }}
                        className="readonly-field"
                      />
                    </div>
                  </div>
                ) : null}

                <div className="form-section">
                  <div className="section-title">
                    <FontAwesomeIcon icon="calendar-alt" className="icon" />
                    <span>Informações da Coleta</span>
                    <div className="section-indicator"></div>
                  </div>

                  <div className="form-row">
                    <div className="form-col">
                      <div className="field-wrapper required-field">
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
                        <div className="field-icon">
                          <FontAwesomeIcon icon="calendar" />
                        </div>
                      </div>
                    </div>

                    <div className="form-col">
                      <div className="field-wrapper"></div>
                    </div>
                  </div>

                  <div className="form-row">
                    <div className="form-col-full">
                      <div className="field-wrapper">
                        <ValidatedField
                          label={translate('leiteVidaApp.coleta.localColeta')}
                          id="coleta-localColeta"
                          name="localColeta"
                          data-cy="localColeta"
                          type="text"
                          placeholder="Informe o local onde foi realizada a coleta"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <div className="form-section">
                  <div className="section-title">
                    <FontAwesomeIcon icon="vial" className="icon" />
                    <span>Dados Técnicos</span>
                    <div className="section-indicator"></div>
                  </div>

                  <div className="form-row">
                    <div className="form-col">
                      <div className="field-wrapper required-field">
                        <ValidatedField
                          label={translate('leiteVidaApp.coleta.volumeMl')}
                          id="coleta-volumeMl"
                          name="volumeMl"
                          data-cy="volumeMl"
                          type="text"
                          placeholder="Volume coletado"
                          validate={{
                            required: { value: true, message: translate('entity.validation.required') },
                            validate: v => isNumber(v) || translate('entity.validation.number'),
                          }}
                        />
                        <div className="field-unit">mL</div>
                        <div className="field-icon">
                          <FontAwesomeIcon icon="tint" />
                        </div>
                      </div>
                    </div>

                    <div className="form-col">
                      <div className="field-wrapper required-field">
                        <ValidatedField
                          label={translate('leiteVidaApp.coleta.temperatura')}
                          id="coleta-temperatura"
                          name="temperatura"
                          data-cy="temperatura"
                          type="text"
                          placeholder="Temperatura da amostra"
                          validate={{
                            required: { value: true, message: translate('entity.validation.required') },
                            validate: v => isNumber(v) || translate('entity.validation.number'),
                          }}
                        />
                        <div className="field-unit">°C</div>
                        <div className="field-icon">
                          <FontAwesomeIcon icon="thermometer-half" />
                        </div>
                      </div>
                    </div>
                  </div>

                  <div className="form-row">
                    <div className="form-col-full">
                      <div className="field-wrapper">
                        <ValidatedField
                          label={translate('leiteVidaApp.coleta.observacoes')}
                          id="coleta-observacoes"
                          name="observacoes"
                          data-cy="observacoes"
                          type="textarea"
                          rows="4"
                          placeholder="Observações adicionais sobre a coleta (opcional)"
                        />
                        <div className="field-icon">
                          <FontAwesomeIcon icon="sticky-note" />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="form-section">
                  <div className="section-title">
                    <FontAwesomeIcon icon="users" className="icon" />
                    <span>Relacionamentos</span>
                    <div className="section-indicator"></div>
                  </div>

                  <div className="form-row">
                    <div className="form-col-full">
                      <div className="field-wrapper">
                        <ValidatedField
                          label="Nome da Doadora"
                          id="coleta-doadora-name"
                          name="doadoraName"
                          data-cy="doadoraName"
                          type="text"
                          readOnly
                          placeholder="Nome será preenchido automaticamente"
                          className="readonly-field"
                        />
                        <div className="field-icon">
                          <FontAwesomeIcon icon="user-check" />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="form-actions">
                  <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/coleta" replace className="btn-cancel">
                    <FontAwesomeIcon icon="times" />
                    <span>Cancelar</span>
                  </Button>
                  <Button
                    color="primary"
                    id="save-entity"
                    data-cy="entityCreateSaveButton"
                    type="submit"
                    disabled={updating}
                    className="btn-save"
                  >
                    <FontAwesomeIcon icon={updating ? 'spinner' : 'save'} spin={updating} />
                    <span>{updating ? 'Salvando...' : 'Salvar Coleta'}</span>
                  </Button>
                </div>
              </ValidatedForm>
            </div>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ColetaUpdate;
