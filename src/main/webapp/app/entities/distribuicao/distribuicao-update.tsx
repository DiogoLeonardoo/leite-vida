import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getEstoques } from 'app/entities/estoque/estoque.reducer';
import { getEntities as getPacientes } from 'app/entities/paciente/paciente.reducer';
import { createEntity, getEntity, reset, updateEntity } from './distribuicao.reducer';
import './distribuicao-update.scss';

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
    <div className="distribuicao-update-page">
      <div className="container">
        <Row className="justify-content-center">
          <Col lg="10">
            <div className="d-flex align-items-center mb-4">
              <h2 className="mb-0">
                <FontAwesomeIcon icon="clipboard-list" className="me-2" />
                {isNew ? 'Nova Distribuição de Leite' : 'Editar Distribuição de Leite'}
              </h2>
            </div>
            {loading ? (
              <div className="text-center">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Carregando...</span>
                </div>
              </div>
            ) : (
              <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
                <div className="card mb-3">
                  <div className="card-header bg-info">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="calendar-alt" className="me-2" />
                      Informações da Distribuição
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
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
                            className="form-control"
                          />
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
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
                            className="form-control"
                            placeholder="Volume distribuído em mL"
                          />
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-success">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="users" className="me-2" />
                      Responsáveis
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.responsavelEntrega')}
                            id="distribuicao-responsavelEntrega"
                            name="responsavelEntrega"
                            data-cy="responsavelEntrega"
                            type="text"
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                            }}
                            className="form-control"
                            placeholder="Responsável pela entrega"
                          />
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.responsavelRecebimento')}
                            id="distribuicao-responsavelRecebimento"
                            name="responsavelRecebimento"
                            data-cy="responsavelRecebimento"
                            type="text"
                            validate={{
                              required: { value: true, message: translate('entity.validation.required') },
                            }}
                            className="form-control"
                            placeholder="Responsável pelo recebimento"
                          />
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="card mb-3">
                  <div className="card-header bg-warning">
                    <h5 className="mb-0">
                      <FontAwesomeIcon icon="id-card" className="me-2" />
                      Observações e Destino
                    </h5>
                  </div>
                  <div className="card-body">
                    <Row>
                      <Col md="12">
                        <div className="form-group">
                          <ValidatedField
                            label={translate('leiteVidaApp.distribuicao.observacoes')}
                            id="distribuicao-observacoes"
                            name="observacoes"
                            data-cy="observacoes"
                            type="textarea"
                            className="form-control"
                            rows="3"
                            placeholder="Observações adicionais (opcional)"
                          />
                        </div>
                      </Col>
                    </Row>
                    <Row>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            id="distribuicao-estoque"
                            name="estoque"
                            data-cy="estoque"
                            label={translate('leiteVidaApp.distribuicao.estoque')}
                            type="select"
                            required
                            className="form-select"
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
                        </div>
                      </Col>
                      <Col md="6">
                        <div className="form-group">
                          <ValidatedField
                            id="distribuicao-paciente"
                            name="paciente"
                            data-cy="paciente"
                            label={translate('leiteVidaApp.distribuicao.paciente')}
                            type="select"
                            required
                            className="form-select"
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
                        </div>
                      </Col>
                    </Row>
                  </div>
                </div>

                <div className="d-flex justify-content-between align-items-center">
                  <Button tag={Link} to="/distribuicao" color="secondary" size="lg">
                    <FontAwesomeIcon icon="times" className="me-2" />
                    Cancelar
                  </Button>
                  <Button color="primary" type="submit" disabled={updating} size="lg">
                    <FontAwesomeIcon icon={updating ? 'spinner' : 'save'} spin={updating} className="me-2" />
                    {updating ? 'Salvando...' : 'Salvar Distribuição'}
                  </Button>
                </div>
              </ValidatedForm>
            )}
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default DistribuicaoUpdate;
