import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Input, InputGroup, InputGroupText, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import axios from 'axios';
import FrascoLabModal from './frascolab-modal';
import ProcessingModal from './processing-modal';
import './coleta.scss';

const ITEMS_PER_PAGE = 6;

export const Coleta = () => {
  const pageLocation = useLocation();
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);
  const isLab = hasAnyAuthority(account.authorities, [AUTHORITIES.LAB]);

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [searchId, setSearchId] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [debouncedSearchId, setDebouncedSearchId] = useState('');

  const [coletaList, setColetaList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalItems, setTotalItems] = useState(0);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [selectedColetaId, setSelectedColetaId] = useState(null);
  const [showFrascoLabModal, setShowFrascoLabModal] = useState(false);
  const [selectedFrascoLabId, setSelectedFrascoLabId] = useState(null);
  const [showProcessingModal, setShowProcessingModal] = useState(false);
  const [selectedProcessingId, setSelectedProcessingId] = useState(null);

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

  const handleCancelClick = coletaId => {
    setSelectedColetaId(coletaId);
    setShowCancelModal(true);
  };

  const handleConfirmCancel = async () => {
    try {
      await axios.patch(`/api/coletas/${selectedColetaId}/cancelar`);

      // Refresh the coleta list after successful cancellation
      await fetchColetas();

      console.log('Coleta cancelled successfully:', selectedColetaId);
    } catch (error) {
      console.error('Error cancelling coleta:', error);
      // You could add a toast notification here for error handling
    } finally {
      setShowCancelModal(false);
      setSelectedColetaId(null);
    }
  };

  const handleCloseCancelModal = () => {
    setShowCancelModal(false);
    setSelectedColetaId(null);
  };

  const handleFrascoLabClick = coletaId => {
    setSelectedFrascoLabId(coletaId);
    setShowFrascoLabModal(true);
  };

  const handleCloseFrascoLabModal = () => {
    setShowFrascoLabModal(false);
    setSelectedFrascoLabId(null);
  };

  const handleConfirmFrascoLab = coletaId => {
    try {
      setSelectedProcessingId(coletaId);
      setShowProcessingModal(true);

      console.log('Opening processing modal for coleta:', coletaId);
    } catch (error) {
      console.error('Error opening processing modal:', error);
    }
  };

  const handleCloseProcessingModal = () => {
    setShowProcessingModal(false);
    setSelectedProcessingId(null);
  };

  const handleSaveProcessing = async (coletaId, processingData) => {
    try {
      // Add your API call here for saving processing data
      console.log('Saving processing data for coleta:', coletaId, processingData);
      // await axios.patch(`/api/coletas/${coletaId}/processar-laboratorio`, processingData);

      // Refresh the coleta list after successful processing
      await fetchColetas();

      console.log('Processing data saved successfully:', coletaId);
    } catch (error) {
      console.error('Error saving processing data:', error);
      // You could add a toast notification here for error handling
    }
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

          {!isLab && (
            <Link to="/coleta/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
              <FontAwesomeIcon icon="plus" />
              &nbsp; Criar nova coleta
            </Link>
          )}
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
                {isLab && <th>Analisar</th>}
                {!isLab && <th />}
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
                      {isLab ? (
                        coleta.statusColeta === 'AGUARDANDO_PROCESSAMENTO' && (
                          <Button
                            size="sm"
                            className="btn-frascolab"
                            data-cy="entityFrascoLabButton"
                            style={{ background: '#7AD27D' }}
                            onClick={() => handleFrascoLabClick(coleta.id)}
                          >
                            <img src="content/images/frascolab.svg" alt="Frascolab" style={{ width: '16px', height: '16px' }} />
                          </Button>
                        )
                      ) : (
                        <>
                          {coleta.statusColeta !== 'CANCELADA' && coleta.statusColeta !== 'PROCESSADA' && (
                            <Button
                              size="sm"
                              style={{
                                backgroundColor: '#fff',
                                borderColor: '#D27A7A',
                                borderRadius: '6px',
                                marginRight: '8px',
                                color: '#D27A7A',
                                fontWeight: '500',
                                fontSize: '12px',
                                padding: '6px 12px',
                                display: 'flex',
                                alignItems: 'center',
                                gap: '4px',
                                transition: 'all 0.2s ease-in-out',
                                border: '1px solid #D27A7A',
                              }}
                              data-cy="entityCancelButton"
                              onClick={() => handleCancelClick(coleta.id)}
                              onMouseEnter={e => {
                                (e.target as HTMLButtonElement).style.backgroundColor = '#D27A7A';
                                (e.target as HTMLButtonElement).style.color = '#fff';
                              }}
                              onMouseLeave={e => {
                                (e.target as HTMLButtonElement).style.backgroundColor = '#fff';
                                (e.target as HTMLButtonElement).style.color = '#D27A7A';
                              }}
                            >
                              <FontAwesomeIcon icon="times" />
                              Cancelar
                            </Button>
                          )}
                          {coleta.statusColeta !== 'PROCESSADA' && coleta.statusColeta !== 'CANCELADA' && (
                            <Button
                              tag={Link}
                              to={`/coleta/${coleta.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                              color="primary"
                              size="sm"
                              data-cy="entityEditButton"
                            >
                              <FontAwesomeIcon icon="pencil-alt" />{' '}
                            </Button>
                          )}
                        </>
                      )}
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

      <ProcessingModal
        isOpen={showProcessingModal}
        toggle={handleCloseProcessingModal}
        coletaId={selectedProcessingId}
        onSave={handleSaveProcessing}
      />

      <FrascoLabModal
        isOpen={showFrascoLabModal}
        toggle={handleCloseFrascoLabModal}
        coletaId={selectedFrascoLabId}
        onConfirm={handleConfirmFrascoLab}
      />

      <Modal isOpen={showCancelModal} toggle={handleCloseCancelModal}>
        <ModalHeader toggle={handleCloseCancelModal}>Confirmar Cancelamento</ModalHeader>
        <ModalBody>Tem certeza que deseja cancelar a coleta de ID {selectedColetaId}?</ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={handleCloseCancelModal}>
            Não
          </Button>
          <Button color="danger" onClick={handleConfirmCancel} style={{ backgroundColor: '#D27A7A', borderColor: '#D27A7A' }}>
            Sim, Cancelar
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default Coleta;
