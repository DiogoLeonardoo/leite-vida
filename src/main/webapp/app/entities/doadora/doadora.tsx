import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Row, Table, Input, InputGroup, InputGroupText } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './doadora.reducer';
import { maskCPF, maskPhone } from 'app/shared/util/validation-utils';
import './doadora.scss';

const ITEMS_PER_PAGE = 6;

export const Doadora = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState('');

  const doadoraList = useAppSelector(state => state.doadora.entities);
  const loading = useAppSelector(state => state.doadora.loading);
  const totalItems = useAppSelector(state => state.doadora.totalItems);

  // Debounce for search filter
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
      setPaginationState(prev => ({
        ...prev,
        activePage: 1, // Reset page when searching
      }));
    }, 500);
    return () => clearTimeout(handler);
  }, [searchTerm]);

  useEffect(() => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        search: debouncedSearchTerm || undefined,
      }),
    );
  }, [dispatch, debouncedSearchTerm, paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    const search = params.get('search');

    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState(prev => ({
        ...prev,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      }));
    }

    if (search && search !== searchTerm) {
      setSearchTerm(search);
    }
  }, [pageLocation.search]);

  useEffect(() => {
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}${debouncedSearchTerm ? `&search=${encodeURIComponent(debouncedSearchTerm)}` : ''}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  }, [
    paginationState.activePage,
    paginationState.sort,
    paginationState.order,
    debouncedSearchTerm,
    navigate,
    pageLocation.pathname,
    pageLocation.search,
  ]);

  const handleSearch = event => {
    setSearchTerm(event.target.value);
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  return (
    <div className="doadora-list-page">
      <Row>
        <h2 className="doadora-list-title">
          Doadoras
          <div className="d-flex align-items-center gap-3">
            <InputGroup className="search-input">
              <InputGroupText>
                <FontAwesomeIcon icon="search" />
              </InputGroupText>
              <Input type="text" placeholder="Pesquisar doadoras..." value={searchTerm} onChange={handleSearch} />
            </InputGroup>
            <Link to="/doadora/new" className="btn btn-primary jh-create-entity">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Criar Nova Doadora
            </Link>
          </div>
        </h2>
      </Row>
      <div className="table-responsive">
        {doadoraList && doadoraList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Nome</th>
                <th>CPF</th>
                <th>Data de Nascimento</th>
                <th>Telefone</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {doadoraList.map((doadora, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{doadora.nome}</td>
                  <td>{maskCPF(doadora.cpf)}</td>
                  <td>
                    {doadora.dataNascimento ? (
                      <TextFormat type="date" value={doadora.dataNascimento} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{maskPhone(doadora.telefone)}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
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
