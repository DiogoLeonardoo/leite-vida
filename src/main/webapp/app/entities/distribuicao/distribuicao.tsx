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

import { getEntities } from './distribuicao.reducer';
import './distribuicao.scss';

export const Distribuicao = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const distribuicaoList = useAppSelector(state => state.distribuicao.entities);
  const loading = useAppSelector(state => state.distribuicao.loading);
  const totalItems = useAppSelector(state => state.distribuicao.totalItems);

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
    <div className="estoque-list-page">
      <h2 id="distribuicao-heading" className="estoque-list-title" data-cy="DistribuicaoHeading">
        <Translate contentKey="leiteVidaApp.distribuicao.home.title">Distribuicaos</Translate>
        <div className="d-flex">
          <Button className="me-2 btn-info" color="info" onClick={handleSyncList} disabled={loading} style={{ height: '48px' }}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="leiteVidaApp.distribuicao.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/distribuicao/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            style={{ height: '48px' }}
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="leiteVidaApp.distribuicao.home.createLabel">Create new Distribuicao</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {distribuicaoList && distribuicaoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="leiteVidaApp.distribuicao.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dataDistribuicao')}>
                  <Translate contentKey="leiteVidaApp.distribuicao.dataDistribuicao">Data Distribuicao</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataDistribuicao')} />
                </th>
                <th className="hand" onClick={sort('volumeDistribuidoMl')}>
                  <Translate contentKey="leiteVidaApp.distribuicao.volumeDistribuidoMl">Volume Distribuido Ml</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('volumeDistribuidoMl')} />
                </th>
                <th className="hand" onClick={sort('responsavelEntrega')}>
                  <Translate contentKey="leiteVidaApp.distribuicao.responsavelEntrega">Responsavel Entrega</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('responsavelEntrega')} />
                </th>
                <th className="hand" onClick={sort('responsavelRecebimento')}>
                  <Translate contentKey="leiteVidaApp.distribuicao.responsavelRecebimento">Responsavel Recebimento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('responsavelRecebimento')} />
                </th>
                <th className="hand" onClick={sort('observacoes')}>
                  <Translate contentKey="leiteVidaApp.distribuicao.observacoes">Observacoes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('observacoes')} />
                </th>
                <th>
                  <Translate contentKey="leiteVidaApp.distribuicao.estoque">Estoque</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="leiteVidaApp.distribuicao.paciente">Paciente</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {distribuicaoList.map((distribuicao, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/distribuicao/${distribuicao.id}`} color="link" size="sm">
                      {distribuicao.id}
                    </Button>
                  </td>
                  <td>
                    {distribuicao.dataDistribuicao ? (
                      <TextFormat type="date" value={distribuicao.dataDistribuicao} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{distribuicao.volumeDistribuidoMl}</td>
                  <td>{distribuicao.responsavelEntrega}</td>
                  <td>{distribuicao.responsavelRecebimento}</td>
                  <td>{distribuicao.observacoes}</td>
                  <td>{distribuicao.estoque ? <Link to={`/estoque/${distribuicao.estoque.id}`}>{distribuicao.estoque.id}</Link> : ''}</td>
                  <td>
                    {distribuicao.paciente ? <Link to={`/paciente/${distribuicao.paciente.id}`}>{distribuicao.paciente.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/distribuicao/${distribuicao.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                        className="btn-info"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/distribuicao/${distribuicao.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                        className="btn-primary"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/distribuicao/${distribuicao.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                        className="btn-danger"
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
              <Translate contentKey="leiteVidaApp.distribuicao.home.notFound">No Distribuicaos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={distribuicaoList && distribuicaoList.length > 0 ? '' : 'd-none'}>
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

export default Distribuicao;
