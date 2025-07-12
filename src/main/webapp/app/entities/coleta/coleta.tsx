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

import { getEntities } from './coleta.reducer';

export const Coleta = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const coletaList = useAppSelector(state => state.coleta.entities);
  const loading = useAppSelector(state => state.coleta.loading);
  const totalItems = useAppSelector(state => state.coleta.totalItems);

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
      <h2 id="coleta-heading" data-cy="ColetaHeading">
        <Translate contentKey="leiteVidaApp.coleta.home.title">Coletas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="leiteVidaApp.coleta.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/coleta/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="leiteVidaApp.coleta.home.createLabel">Create new Coleta</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {coletaList && coletaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="leiteVidaApp.coleta.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dataColeta')}>
                  <Translate contentKey="leiteVidaApp.coleta.dataColeta">Data Coleta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataColeta')} />
                </th>
                <th className="hand" onClick={sort('volumeMl')}>
                  <Translate contentKey="leiteVidaApp.coleta.volumeMl">Volume Ml</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('volumeMl')} />
                </th>
                <th className="hand" onClick={sort('temperatura')}>
                  <Translate contentKey="leiteVidaApp.coleta.temperatura">Temperatura</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('temperatura')} />
                </th>
                <th className="hand" onClick={sort('localColeta')}>
                  <Translate contentKey="leiteVidaApp.coleta.localColeta">Local Coleta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('localColeta')} />
                </th>
                <th className="hand" onClick={sort('observacoes')}>
                  <Translate contentKey="leiteVidaApp.coleta.observacoes">Observacoes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('observacoes')} />
                </th>
                <th className="hand" onClick={sort('statusColeta')}>
                  <Translate contentKey="leiteVidaApp.coleta.statusColeta">Status Coleta</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statusColeta')} />
                </th>
                <th>
                  <Translate contentKey="leiteVidaApp.coleta.processamento">Processamento</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="leiteVidaApp.coleta.doadora">Doadora</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {coletaList.map((coleta, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/coleta/${coleta.id}`} color="link" size="sm">
                      {coleta.id}
                    </Button>
                  </td>
                  <td>{coleta.dataColeta ? <TextFormat type="date" value={coleta.dataColeta} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{coleta.volumeMl}</td>
                  <td>{coleta.temperatura}</td>
                  <td>{coleta.localColeta}</td>
                  <td>{coleta.observacoes}</td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.StatusColeta.${coleta.statusColeta}`} />
                  </td>
                  <td>
                    {coleta.processamento ? <Link to={`/processamento/${coleta.processamento.id}`}>{coleta.processamento.id}</Link> : ''}
                  </td>
                  <td>{coleta.doadora ? <Link to={`/doadora/${coleta.doadora.id}`}>{coleta.doadora.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/coleta/${coleta.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/coleta/${coleta.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/coleta/${coleta.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="leiteVidaApp.coleta.home.notFound">No Coletas found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={coletaList && coletaList.length > 0 ? '' : 'd-none'}>
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

export default Coleta;
