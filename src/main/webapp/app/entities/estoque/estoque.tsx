import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './estoque.reducer';

export const Estoque = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const estoqueList = useAppSelector(state => state.estoque.entities);
  const loading = useAppSelector(state => state.estoque.loading);
  const totalItems = useAppSelector(state => state.estoque.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="estoque-heading" data-cy="EstoqueHeading">
        <Translate contentKey="leiteVidaApp.estoque.home.title">Estoques</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="leiteVidaApp.estoque.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/estoque/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="leiteVidaApp.estoque.home.createLabel">Create new Estoque</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {estoqueList && estoqueList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="leiteVidaApp.estoque.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dataProducao')}>
                  <Translate contentKey="leiteVidaApp.estoque.dataProducao">Data Producao</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataProducao')} />
                </th>
                <th className="hand" onClick={sort('dataValidade')}>
                  <Translate contentKey="leiteVidaApp.estoque.dataValidade">Data Validade</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataValidade')} />
                </th>
                <th className="hand" onClick={sort('tipoLeite')}>
                  <Translate contentKey="leiteVidaApp.estoque.tipoLeite">Tipo Leite</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tipoLeite')} />
                </th>
                <th className="hand" onClick={sort('classificacao')}>
                  <Translate contentKey="leiteVidaApp.estoque.classificacao">Classificacao</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('classificacao')} />
                </th>
                <th className="hand" onClick={sort('volumeTotalMl')}>
                  <Translate contentKey="leiteVidaApp.estoque.volumeTotalMl">Volume Total Ml</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('volumeTotalMl')} />
                </th>
                <th className="hand" onClick={sort('volumeDisponivelMl')}>
                  <Translate contentKey="leiteVidaApp.estoque.volumeDisponivelMl">Volume Disponivel Ml</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('volumeDisponivelMl')} />
                </th>
                <th className="hand" onClick={sort('localArmazenamento')}>
                  <Translate contentKey="leiteVidaApp.estoque.localArmazenamento">Local Armazenamento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('localArmazenamento')} />
                </th>
                <th className="hand" onClick={sort('temperaturaArmazenamento')}>
                  <Translate contentKey="leiteVidaApp.estoque.temperaturaArmazenamento">Temperatura Armazenamento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('temperaturaArmazenamento')} />
                </th>
                <th className="hand" onClick={sort('statusLote')}>
                  <Translate contentKey="leiteVidaApp.estoque.statusLote">Status Lote</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statusLote')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {estoqueList.map((estoque, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/estoque/${estoque.id}`} color="link" size="sm">
                      {estoque.id}
                    </Button>
                  </td>
                  <td>
                    {estoque.dataProducao ? <TextFormat type="date" value={estoque.dataProducao} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {estoque.dataValidade ? <TextFormat type="date" value={estoque.dataValidade} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.TipoLeite.${estoque.tipoLeite}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.ClassificacaoLeite.${estoque.classificacao}`} />
                  </td>
                  <td>{estoque.volumeTotalMl}</td>
                  <td>{estoque.volumeDisponivelMl}</td>
                  <td>{estoque.localArmazenamento}</td>
                  <td>{estoque.temperaturaArmazenamento}</td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.StatusLote.${estoque.statusLote}`} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/estoque/${estoque.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/estoque/${estoque.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/estoque/${estoque.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="leiteVidaApp.estoque.home.notFound">No Estoques found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={estoqueList && estoqueList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Estoque;
