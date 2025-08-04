import React, { useEffect, useState, useCallback } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Modal, ModalHeader, ModalBody, ModalFooter, Row, Col, Input } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp, faEye } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppSelector } from 'app/config/store';
import axios from 'axios';
import './distribuicao.scss';

export const Distribuicao = () => {
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(
      {
        ...getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'),
        itemsPerPage: 4,
      },
      pageLocation.search,
    ),
  );

  const [searchTerm, setSearchTerm] = useState('');
  const [searchTimeout, setSearchTimeout] = useState(null);
  const [distribuicaoList, setDistribuicaoList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalItems, setTotalItems] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedDistribuicao, setSelectedDistribuicao] = useState(null);
  const [modalLoading, setModalLoading] = useState(false);
  const [distribuicaoDetalhes, setDistribuicaoDetalhes] = useState(null);

  const fetchDistribuicoes = async () => {
    setLoading(true);
    try {
      const params = {
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        ...(searchTerm && { searchTerm }),
      };

      const response = await axios.get('/api/distribuicaos/buscar', { params });

      const responseData = response.data;

      let distribuicoes = [];
      let totalCount = 0;

      if (Array.isArray(responseData)) {
        distribuicoes = responseData;
        totalCount = responseData.length;
      } else if (responseData && typeof responseData === 'object') {
        distribuicoes = responseData.content || responseData.data || [];
        totalCount =
          responseData.totalElements || responseData.total || parseInt(response.headers['x-total-count'], 10) || distribuicoes.length;
      }

      setDistribuicaoList(distribuicoes);
      setTotalItems(isNaN(totalCount) ? 0 : totalCount);
    } catch (error) {
      console.error('Error fetching distributions:', error);
      setDistribuicaoList([]);
      setTotalItems(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDistribuicoes();
  }, [paginationState.activePage, paginationState.order, paginationState.sort, paginationState.itemsPerPage]);

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

  const debouncedSearch = useCallback(
    value => {
      if (searchTimeout) {
        clearTimeout(searchTimeout);
      }

      const timeoutId = setTimeout(() => {
        const params = {
          page: paginationState.activePage - 1,
          size: paginationState.itemsPerPage,
          sort: `${paginationState.sort},${paginationState.order}`,
          ...(value && { searchTerm: value }),
        };

        if (paginationState.activePage !== 1) {
          setPaginationState({
            ...paginationState,
            activePage: 1,
          });
        } else {
          setLoading(true);
          axios
            .get('/api/distribuicaos/buscar', { params })
            .then(response => {
              const responseData = response.data;

              let distribuicoes = [];
              let totalCount = 0;

              if (Array.isArray(responseData)) {
                distribuicoes = responseData;
                totalCount = responseData.length;
              } else if (responseData && typeof responseData === 'object') {
                distribuicoes = responseData.content || responseData.data || [];
                totalCount =
                  responseData.totalElements ||
                  responseData.total ||
                  parseInt(response.headers['x-total-count'], 10) ||
                  distribuicoes.length;
              }

              setDistribuicaoList(distribuicoes);
              setTotalItems(isNaN(totalCount) ? 0 : totalCount);
            })
            .catch(error => {
              console.error('Error fetching distributions:', error);
              setDistribuicaoList([]);
              setTotalItems(0);
            })
            .finally(() => {
              setLoading(false);
            });
        }
      }, 500);

      setSearchTimeout(timeoutId);
    },
    [paginationState, setDistribuicaoList, setTotalItems, setLoading],
  );

  const handleSearchChange = e => {
    const value = e.target.value;
    setSearchTerm(value);
    debouncedSearch(value);
  };

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    if (!modalOpen) {
      setSelectedDistribuicao(null);
      setDistribuicaoDetalhes(null);
    }
  };

  const openModal = async distribuicao => {
    setSelectedDistribuicao(distribuicao);
    setModalOpen(true);
    setModalLoading(true);

    try {
      const response = await axios.get(`/api/distribuicaos/detalhes/${distribuicao.id}`);
      setDistribuicaoDetalhes(response.data);
    } catch (error) {
      console.error('Error fetching distribuicao details:', error);
      setDistribuicaoDetalhes(null);
    } finally {
      setModalLoading(false);
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

  return (
    <div className="estoque-list-page">
      <h2 id="distribuicao-heading" className="estoque-list-title" data-cy="DistribuicaoHeading">
        Distribuições
        <div className="d-flex">
          <div className="me-2" style={{ width: '400px' }}>
            <Input
              type="text"
              name="search"
              placeholder="Buscar por ID ou nome do paciente"
              value={searchTerm}
              onChange={handleSearchChange}
              style={{
                height: '48px',
                borderRadius: '4px',
                padding: '0.375rem 0.75rem',
              }}
            />
          </div>

          <Link
            to="/distribuicao/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            style={{ height: '48px' }}
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp; Criar nova distribuição
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {loading ? (
          <div className="d-flex justify-content-center">
            <div className="spinner-border" role="status">
              <span className="sr-only">Carregando...</span>
            </div>
          </div>
        ) : distribuicaoList && distribuicaoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Data de Distribuição</th>
                <th>Volume Distribuído</th>
                <th>Responsável Entrega</th>
                <th>Observações</th>
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
                  <td>{distribuicao.observacoes}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        color="info"
                        style={{ background: '#7ad27d', border: 'none' }}
                        size="sm"
                        onClick={() => openModal(distribuicao)}
                        title="Visualizar detalhes"
                      >
                        <FontAwesomeIcon icon={faEye} />
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">
            <Translate contentKey="leiteVidaApp.distribuicao.home.notFound">No Distribuicaos found</Translate>
          </div>
        )}
      </div>

      {/* Modal */}
      <Modal isOpen={modalOpen} toggle={toggleModal} size="lg">
        <ModalHeader toggle={toggleModal}>Detalhes da Distribuição {selectedDistribuicao?.id}</ModalHeader>
        <ModalBody>
          {modalLoading ? (
            <div className="d-flex justify-content-center">
              <div className="spinner-border" role="status">
                <span className="sr-only">Carregando detalhes...</span>
              </div>
            </div>
          ) : distribuicaoDetalhes ? (
            <div className="distribuicao-detalhes">
              {/* Seção de informações do paciente */}
              <div className="card mb-4">
                <div className="card-header bg-light">
                  <h5 className="mb-0">Informações do Paciente</h5>
                </div>
                <div className="card-body">
                  <Row>
                    <Col md={12}>
                      <div className="mb-3">
                        <label className="text-muted d-block">Nome do Paciente</label>
                        <span className="fw-bold">{distribuicaoDetalhes.nomePaciente}</span>
                      </div>
                    </Col>
                  </Row>
                  <Row>
                    <Col md={6}>
                      <div className="mb-3">
                        <label className="text-muted d-block">Telefone Responsável</label>
                        <span className="fw-bold">{distribuicaoDetalhes.telefoneResponsavel}</span>
                      </div>
                    </Col>
                    <Col md={6}>
                      <div className="mb-3">
                        <label className="text-muted d-block">CPF Responsável</label>
                        <span className="fw-bold">{distribuicaoDetalhes.cpfResponsavel}</span>
                      </div>
                    </Col>
                  </Row>
                  <Row>
                    <Col md={12}>
                      <div className="mb-3">
                        <label className="text-muted d-block">Parentesco do Responsável</label>
                        <span className="fw-bold">{distribuicaoDetalhes.parentescoResponsavel}</span>
                      </div>
                    </Col>
                  </Row>
                </div>
              </div>

              {/* Seção de informações do estoque */}
              <div className="card mb-4">
                <div className="card-header bg-light">
                  <h5 className="mb-0">Informações do Lote</h5>
                </div>
                <div className="card-body">
                  <Row>
                    <Col md={4}>
                      <div className="mb-3">
                        <label className="text-muted d-block">ID do Lote</label>
                        <span className="fw-bold">{distribuicaoDetalhes.estoqueId}</span>
                      </div>
                    </Col>
                    <Col md={4}>
                      <div className="mb-3">
                        <label className="text-muted d-block">Tipo de Leite</label>
                        <span className="fw-bold">
                          <Translate contentKey={`leiteVidaApp.TipoLeite.${distribuicaoDetalhes.tipoLeite}`} />
                        </span>
                      </div>
                    </Col>
                    <Col md={4}>
                      <div className="mb-3">
                        <label className="text-muted d-block">Classificação</label>
                        <span className="fw-bold">
                          <Translate contentKey={`leiteVidaApp.ClassificacaoLeite.${distribuicaoDetalhes.classificacao}`} />
                        </span>
                      </div>
                    </Col>
                  </Row>
                </div>
              </div>

              {/* Seção de informações da doadora */}
              <div className="card">
                <div className="card-header bg-light">
                  <h5 className="mb-0">Informações da Doadora</h5>
                </div>
                <div className="card-body">
                  <Row>
                    <Col md={12}>
                      <div className="mb-3">
                        <label className="text-muted d-block">Nome da Doadora</label>
                        <span className="fw-bold">{distribuicaoDetalhes.nomeDoadora}</span>
                      </div>
                    </Col>
                  </Row>
                </div>
              </div>
            </div>
          ) : (
            <div className="alert alert-warning">Não foi possível carregar os detalhes da distribuição.</div>
          )}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleModal}>
            Fechar
          </Button>
        </ModalFooter>
      </Modal>

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
