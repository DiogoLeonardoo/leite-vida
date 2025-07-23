import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Input, InputGroup, InputGroupText } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import axios from 'axios';
import './coleta.scss';

const ITEMS_PER_PAGE = 6;

export const Coleta = () => {
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [searchId, setSearchId] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [debouncedSearchId, setDebouncedSearchId] = useState('');

  const [coletaList, setColetaList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalItems, setTotalItems] = useState(0);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearchId(searchId);
      setPaginationState(prev => ({
        ...prev,
        activePage: 1,
      }));
    }, 500);
    return () => clearTimeout(handler);
  }, [searchId]);

  const fetchColetas = async () => {
    setLoading(true);
    try {
      const params = {
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        id: debouncedSearchId ? String(debouncedSearchId) : undefined,
        status: statusFilter || undefined,
      };

      const response = await axios.get('/api/coletas/buscar-coletas', {
        params,
      });

      const responseData = response.data;
      const coletas = responseData.content || responseData;

      setColetaList(coletas);
      const totalCount = responseData.totalElements || parseInt(response.headers['x-total-count'], 10);
      setTotalItems(isNaN(totalCount) ? 0 : totalCount);
    } catch (error) {
      console.error('Error fetching coletas:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchColetas();
  }, [
    debouncedSearchId,
    statusFilter,
    paginationState.activePage,
    paginationState.order,
    paginationState.sort,
    paginationState.itemsPerPage,
  ]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);

    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState(prev => ({
        ...prev,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      }));
    }
  }, [pageLocation.search]);

  useEffect(() => {
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  }, [paginationState.activePage, paginationState.sort, paginationState.order, navigate, pageLocation.pathname, pageLocation.search]);

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

  const handleSearchId = event => {
    setSearchId(event.target.value);
  };

  const handleStatusFilter = event => {
    const newStatus = event.target.value;
    console.log('Status filter changed to:', newStatus);
    setStatusFilter(newStatus);
    setPaginationState(prev => ({
      ...prev,
      activePage: 1,
    }));
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'PROCESSADA':
        return '#7AD27D';
      case 'AGUARDANDO_PROCESSAMENTO':
        return '#FFF3B0';
      case 'CANCELADA':
        return '#D27A7A';
      default:
        return '#f8f9fa';
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case 'AGUARDANDO_PROCESSAMENTO':
        return 'AGUARDANDO PROCESSAMENTO';
      default:
        return status;
    }
  };

  const StatusBadge = ({ status }) => (
    <span
      style={{
        backgroundColor: getStatusColor(status),
        padding: '4px 8px',
        borderRadius: '4px',
        fontSize: '12px',
        fontWeight: '500',
        color: '#000',
        display: 'inline-block',
        minWidth: '120px',
        textAlign: 'center',
      }}
    >
      {getStatusText(status)}
    </span>
  );

  return (
    <div className="coleta-list-page">
      <h2 className="coleta-list-title">
        <Translate contentKey="leiteVidaApp.coleta.home.title">Coletas</Translate>
        <div className="d-flex">
          <InputGroup className="search-input me-2" style={{ width: '200px' }}>
            <InputGroupText>
              <FontAwesomeIcon icon="search" />
            </InputGroupText>
            <Input type="text" placeholder="Buscar por ID..." value={searchId} onChange={handleSearchId} />
          </InputGroup>

          <Input
            type="select"
            className="me-2"
            style={{ width: '200px', height: '48px' }}
            value={statusFilter}
            onChange={handleStatusFilter}
          >
            <option value="">Todos os Status</option>
            <option value="AGUARDANDO_PROCESSAMENTO">Aguardando Processamento</option>
            <option value="PROCESSADA">Processada</option>
            <option value="CANCELADA">Cancelada</option>
          </Input>

          <Link to="/coleta/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Criar nova coleta
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {coletaList && coletaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Data Coleta</th>
                <th>Volume (mL)</th>
                <th>Local Coleta</th>
                <th>Status Coleta</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {coletaList.map(coleta => (
                <tr key={`entity-${coleta.id}`} data-cy="entityTable">
                  <td>{coleta.id}</td>
                  <td>{coleta.dataColeta ? <TextFormat type="date" value={coleta.dataColeta} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{coleta.volumeMl}</td>
                  <td>{coleta.localColeta}</td>
                  <td>
                    <StatusBadge status={coleta.statusColeta} />
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/coleta/${coleta.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">Nenhuma coleta encontrada</div>
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
