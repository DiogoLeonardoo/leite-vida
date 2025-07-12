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

import { getEntities } from './doadora.reducer';

export const Doadora = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const doadoraList = useAppSelector(state => state.doadora.entities);
  const loading = useAppSelector(state => state.doadora.loading);
  const totalItems = useAppSelector(state => state.doadora.totalItems);

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
      <h2 id="doadora-heading" data-cy="DoadoraHeading">
        <Translate contentKey="leiteVidaApp.doadora.home.title">Doadoras</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="leiteVidaApp.doadora.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/doadora/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="leiteVidaApp.doadora.home.createLabel">Create new Doadora</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {doadoraList && doadoraList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="leiteVidaApp.doadora.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('nome')}>
                  <Translate contentKey="leiteVidaApp.doadora.nome">Nome</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nome')} />
                </th>
                <th className="hand" onClick={sort('cartaoSUS')}>
                  <Translate contentKey="leiteVidaApp.doadora.cartaoSUS">Cartao SUS</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cartaoSUS')} />
                </th>
                <th className="hand" onClick={sort('cpf')}>
                  <Translate contentKey="leiteVidaApp.doadora.cpf">Cpf</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('cpf')} />
                </th>
                <th className="hand" onClick={sort('dataNascimento')}>
                  <Translate contentKey="leiteVidaApp.doadora.dataNascimento">Data Nascimento</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataNascimento')} />
                </th>
                <th className="hand" onClick={sort('cep')}>
                  <Translate contentKey="leiteVidaApp.doadora.cep">Cep</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('cep')} />
                </th>
                <th className="hand" onClick={sort('estado')}>
                  <Translate contentKey="leiteVidaApp.doadora.estado">Estado</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estado')} />
                </th>
                <th className="hand" onClick={sort('cidade')}>
                  <Translate contentKey="leiteVidaApp.doadora.cidade">Cidade</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cidade')} />
                </th>
                <th className="hand" onClick={sort('endereco')}>
                  <Translate contentKey="leiteVidaApp.doadora.endereco">Endereco</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endereco')} />
                </th>
                <th className="hand" onClick={sort('telefone')}>
                  <Translate contentKey="leiteVidaApp.doadora.telefone">Telefone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('telefone')} />
                </th>
                <th className="hand" onClick={sort('profissao')}>
                  <Translate contentKey="leiteVidaApp.doadora.profissao">Profissao</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profissao')} />
                </th>
                <th className="hand" onClick={sort('tipoDoadora')}>
                  <Translate contentKey="leiteVidaApp.doadora.tipoDoadora">Tipo Doadora</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tipoDoadora')} />
                </th>
                <th className="hand" onClick={sort('localPreNatal')}>
                  <Translate contentKey="leiteVidaApp.doadora.localPreNatal">Local Pre Natal</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('localPreNatal')} />
                </th>
                <th className="hand" onClick={sort('resultadoVDRL')}>
                  <Translate contentKey="leiteVidaApp.doadora.resultadoVDRL">Resultado VDRL</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('resultadoVDRL')} />
                </th>
                <th className="hand" onClick={sort('resultadoHBsAg')}>
                  <Translate contentKey="leiteVidaApp.doadora.resultadoHBsAg">Resultado H Bs Ag</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('resultadoHBsAg')} />
                </th>
                <th className="hand" onClick={sort('resultadoFTAabs')}>
                  <Translate contentKey="leiteVidaApp.doadora.resultadoFTAabs">Resultado FT Aabs</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('resultadoFTAabs')} />
                </th>
                <th className="hand" onClick={sort('resultadoHIV')}>
                  <Translate contentKey="leiteVidaApp.doadora.resultadoHIV">Resultado HIV</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('resultadoHIV')} />
                </th>
                <th className="hand" onClick={sort('transfusaoUltimos5Anos')}>
                  <Translate contentKey="leiteVidaApp.doadora.transfusaoUltimos5Anos">Transfusao Ultimos 5 Anos</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transfusaoUltimos5Anos')} />
                </th>
                <th className="hand" onClick={sort('dataRegistro')}>
                  <Translate contentKey="leiteVidaApp.doadora.dataRegistro">Data Registro</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dataRegistro')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {doadoraList.map((doadora, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/doadora/${doadora.id}`} color="link" size="sm">
                      {doadora.id}
                    </Button>
                  </td>
                  <td>{doadora.nome}</td>
                  <td>{doadora.cartaoSUS}</td>
                  <td>{doadora.cpf}</td>
                  <td>
                    {doadora.dataNascimento ? (
                      <TextFormat type="date" value={doadora.dataNascimento} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{doadora.cep}</td>
                  <td>{doadora.estado}</td>
                  <td>{doadora.cidade}</td>
                  <td>{doadora.endereco}</td>
                  <td>{doadora.telefone}</td>
                  <td>{doadora.profissao}</td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.TipoDoadora.${doadora.tipoDoadora}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.LocalPreNatal.${doadora.localPreNatal}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.ResultadoExame.${doadora.resultadoVDRL}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.ResultadoExame.${doadora.resultadoHBsAg}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.ResultadoExame.${doadora.resultadoFTAabs}`} />
                  </td>
                  <td>
                    <Translate contentKey={`leiteVidaApp.ResultadoExame.${doadora.resultadoHIV}`} />
                  </td>
                  <td>{doadora.transfusaoUltimos5Anos ? 'true' : 'false'}</td>
                  <td>
                    {doadora.dataRegistro ? <TextFormat type="date" value={doadora.dataRegistro} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/doadora/${doadora.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/doadora/${doadora.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/doadora/${doadora.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="leiteVidaApp.doadora.home.notFound">No Doadoras found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={doadoraList && doadoraList.length > 0 ? '' : 'd-none'}>
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

export default Doadora;
