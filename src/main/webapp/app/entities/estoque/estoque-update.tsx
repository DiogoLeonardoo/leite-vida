import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { TipoLeite } from 'app/shared/model/enumerations/tipo-leite.model';
import { ClassificacaoLeite } from 'app/shared/model/enumerations/classificacao-leite.model';
import { StatusLote } from 'app/shared/model/enumerations/status-lote.model';
import { createEntity, getEntity, reset, updateEntity } from './estoque.reducer';

export const EstoqueUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estoqueEntity = useAppSelector(state => state.estoque.entity);
  const loading = useAppSelector(state => state.estoque.loading);
  const updating = useAppSelector(state => state.estoque.updating);
  const updateSuccess = useAppSelector(state => state.estoque.updateSuccess);
  const tipoLeiteValues = Object.keys(TipoLeite);
  const classificacaoLeiteValues = Object.keys(ClassificacaoLeite);
  const statusLoteValues = Object.keys(StatusLote);

  const handleClose = () => {
    navigate(`/estoque${location.search}`);
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
    if (values.volumeTotalMl !== undefined && typeof values.volumeTotalMl !== 'number') {
      values.volumeTotalMl = Number(values.volumeTotalMl);
    }
    if (values.volumeDisponivelMl !== undefined && typeof values.volumeDisponivelMl !== 'number') {
      values.volumeDisponivelMl = Number(values.volumeDisponivelMl);
    }
    if (values.temperaturaArmazenamento !== undefined && typeof values.temperaturaArmazenamento !== 'number') {
      values.temperaturaArmazenamento = Number(values.temperaturaArmazenamento);
    }

    const entity = {
      ...estoqueEntity,
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
          tipoLeite: 'COLOSTRO',
          classificacao: 'A',
          statusLote: 'DISPONIVEL',
          ...estoqueEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="leiteVidaApp.estoque.home.createOrEditLabel" data-cy="EstoqueCreateUpdateHeading">
            <Translate contentKey="leiteVidaApp.estoque.home.createOrEditLabel">Create or edit a Estoque</Translate>
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
                  id="estoque-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('leiteVidaApp.estoque.dataProducao')}
                id="estoque-dataProducao"
                name="dataProducao"
                data-cy="dataProducao"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.estoque.dataValidade')}
                id="estoque-dataValidade"
                name="dataValidade"
                data-cy="dataValidade"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.estoque.tipoLeite')}
                id="estoque-tipoLeite"
                name="tipoLeite"
                data-cy="tipoLeite"
                type="select"
              >
                {tipoLeiteValues.map(tipoLeite => (
                  <option value={tipoLeite} key={tipoLeite}>
                    {translate(`leiteVidaApp.TipoLeite.${tipoLeite}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.estoque.classificacao')}
                id="estoque-classificacao"
                name="classificacao"
                data-cy="classificacao"
                type="select"
              >
                {classificacaoLeiteValues.map(classificacaoLeite => (
                  <option value={classificacaoLeite} key={classificacaoLeite}>
                    {translate(`leiteVidaApp.ClassificacaoLeite.${classificacaoLeite}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('leiteVidaApp.estoque.volumeTotalMl')}
                id="estoque-volumeTotalMl"
                name="volumeTotalMl"
                data-cy="volumeTotalMl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.estoque.volumeDisponivelMl')}
                id="estoque-volumeDisponivelMl"
                name="volumeDisponivelMl"
                data-cy="volumeDisponivelMl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.estoque.localArmazenamento')}
                id="estoque-localArmazenamento"
                name="localArmazenamento"
                data-cy="localArmazenamento"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.estoque.temperaturaArmazenamento')}
                id="estoque-temperaturaArmazenamento"
                name="temperaturaArmazenamento"
                data-cy="temperaturaArmazenamento"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('leiteVidaApp.estoque.statusLote')}
                id="estoque-statusLote"
                name="statusLote"
                data-cy="statusLote"
                type="select"
              >
                {statusLoteValues.map(statusLote => (
                  <option value={statusLote} key={statusLote}>
                    {translate(`leiteVidaApp.StatusLote.${statusLote}`)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/estoque" replace color="info">
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

export default EstoqueUpdate;
