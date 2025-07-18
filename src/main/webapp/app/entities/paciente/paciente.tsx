import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Row, Table, Input, InputGroup, InputGroupText } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch } from 'app/config/store';
import axios from 'axios';
import './paciente.scss';

export const Paciente = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState('');

  const [pacienteList, setPacienteList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalItems, setTotalItems] = useState(0);

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

  const fetchPacientes = async () => {
    setLoading(true);
    try {
      const response = await axios.get('/api/pacientes/buscar-pacientes', {
        params: {
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
          search: debouncedSearchTerm || undefined,
        },
      });

      // Handle the nested content array in the response
      const responseData = response.data;
      const pacientes = responseData.content || responseData;

      setPacienteList(pacientes);

      // Get total count from either the response headers or the response itself
      const totalCount = responseData.totalElements || parseInt(response.headers['x-total-count'], 10);
      setTotalItems(isNaN(totalCount) ? 0 : totalCount);
    } catch (error) {
      console.error('Error fetching pacientes:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPacientes();
  }, [debouncedSearchTerm, paginationState.activePage, paginationState.order, paginationState.sort]);

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

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
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
    <div className="paciente-list-page">
      <Row>
        <h2 className="paciente-list-title">
          <Translate contentKey="leiteVidaApp.paciente.home.title">Pacientes</Translate>
          <div className="d-flex align-items-center gap-3">
            <InputGroup className="search-input">
              <InputGroupText>
                <FontAwesomeIcon icon="search" />
              </InputGroupText>
              <Input type="text" placeholder="Pesquisar pacientes..." value={searchTerm} onChange={handleSearch} />
            </InputGroup>
            <Link to="/paciente/new" className="btn btn-primary jh-create-entity">
              <FontAwesomeIcon icon="plus" />
              &nbsp; <Translate contentKey="leiteVidaApp.paciente.home.createLabel">Create new Paciente</Translate>
            </Link>
          </div>
        </h2>
      </Row>
      <div className="table-responsive">
        {pacienteList && pacienteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Nome Paciente</th>
                <th>Data de Nascimento</th>
                <th>Contato Responsável</th>
                <th>Nome Responsável</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {pacienteList.map((paciente, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{paciente.nome}</td>
                  <td>
                    {paciente.dataNascimento ? (
                      <TextFormat type="date" value={paciente.dataNascimento} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{paciente.telefoneResponsavel}</td>
                  <td>{paciente.nomeResponsavel}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/paciente/${paciente.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
              <Translate contentKey="leiteVidaApp.paciente.home.notFound">No Pacientes found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={pacienteList && pacienteList.length > 0 ? '' : 'd-none'}>
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

export default Paciente;
