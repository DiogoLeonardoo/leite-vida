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

import { getEntities } from './processamento.reducer';

export const Processamento = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const processamentoList = useAppSelector(state => state.processamento.entities);
  const loading = useAppSelector(state => state.processamento.loading);
  const totalItems = useAppSelector(state => state.processamento.totalItems);

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
      <h2 id="processamento-heading" data-cy="ProcessamentoHeading">
        <Translate contentKey="leiteVidaApp.processamento.home.title">Processamentos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="leiteVidaApp.processamento.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/processamento/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="leiteVidaApp.processamento.home.createLabel">Create new Processamento</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {processamentoList && processamentoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="leiteVidaApp.processamento.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('dataProcessamento')}>
                  <Translate contentKey="leiteVidaApp.processamento.dataProcessamento">Data Processamento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataProcessamento')} />
                </th>
                <th className="hand" onClick={sort('tecnicoResponsavel')}>
                  <Translate contentKey="leiteVidaApp.processamento.tecnicoResponsavel">Tecnico Responsavel</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tecnicoResponsavel')} />
                </th>
                <th className="hand" onClick={sort('valorAcidezDornic')}>
                  <Translate contentKey="leiteVidaApp.processamento.valorAcidezDornic">Valor Acidez Dornic</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('valorAcidezDornic')} />
                </th>
                <th className="hand" onClick={sort('valorCaloricoKcal')}>
                  <Translate contentKey="leiteVidaApp.processamento.valorCaloricoKcal">Valor Calorico Kcal</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('valorCaloricoKcal')} />
                </th>
                <th className="hand" onClick={sort('resultadoAnalise')}>
                  <Translate contentKey="leiteVidaApp.processamento.resultadoAnalise">Resultado Analise</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('resultadoAnalise')} />
                </th>
                <th className="hand" onClick={sort('statusProcessamento')}>
                  <Translate contentKey="leiteVidaApp.processamento.statusProcessamento">Status Processamento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statusProcessamento')} />
                </th>
                <th>
                  <Translate contentKey="leiteVidaApp.processamento.estoque">Estoque</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {processamentoList.map((processamento, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/processamento/${processamento.id}`} color="link" size="sm">
                      {processamento.id}
                    </Button>
                  </td>
                  <td>
                    {processamento.dataProcessamento ? (
                      <TextFormat type="date" value={processamento.dataProcessamento} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{processamento.tecnicoResponsavel}</td>
                  <td>{processamento.valorAcidezDornic}</td>
                  <td>{processamento.valorCaloricoKcal}</td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.ResultadoAnalise.${processamento.resultadoAnalise}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.StatusProcessamento.${processamento.statusProcessamento}`} />
                  </td>
                  <td>
                    {processamento.estoque ? <Link to={`/estoque/${processamento.estoque.id}`}>{processamento.estoque.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/processamento/${processamento.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/processamento/${processamento.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/processamento/${processamento.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="leiteVidaApp.processamento.home.notFound">No Processamentos found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={processamentoList && processamentoList.length > 0 ? '' : 'd-none'}>
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

export default Processamento;
