import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table, Input, Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp, faEye } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import axios from 'axios';
import './estoque.scss';

const ITEMS_PER_PAGE = 20;

export const Estoque = () => {
  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const [tipoLeiteFilter, setTipoLeiteFilter] = useState('');
  const [classificacaoFilter, setClassificacaoFilter] = useState('');

  const [estoqueList, setEstoqueList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [totalItems, setTotalItems] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedEstoque, setSelectedEstoque] = useState(null);
  const [modalLoading, setModalLoading] = useState(false);
  const [estoqueDetalhes, setEstoqueDetalhes] = useState(null);

  const fetchEstoques = async () => {
    setLoading(true);
    try {
      const params = {
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
        ...(tipoLeiteFilter && { tipoLeite: tipoLeiteFilter }),
        ...(classificacaoFilter && { classificacao: classificacaoFilter }),
      };

      console.log('Fetching estoques with params:', params);

      const response = await axios.get('/api/estoques/buscar-estoques', {
        params,
      });

      console.log('API Response:', response);

      const responseData = response.data;

      // Handle different response structures
      let estoques = [];
      let totalCount = 0;

      if (Array.isArray(responseData)) {
        // If response is directly an array
        estoques = responseData;
        totalCount = responseData.length;
      } else if (responseData && typeof responseData === 'object') {
        // If response is paginated object
        estoques = responseData.content || responseData.data || [];
        totalCount = responseData.totalElements || responseData.total || parseInt(response.headers['x-total-count'], 10) || estoques.length;
      }

      console.log('Processed estoques:', estoques);
      console.log('Total count:', totalCount);

      setEstoqueList(estoques);
      setTotalItems(isNaN(totalCount) ? 0 : totalCount);
    } catch (error) {
      console.error('Error fetching estoques:', error);
      setEstoqueList([]);
      setTotalItems(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchEstoques();
  }, [
    tipoLeiteFilter,
    classificacaoFilter,
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

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    if (!modalOpen) {
      setSelectedEstoque(null);
      setEstoqueDetalhes(null);
    }
  };

  const openModal = async estoque => {
    setSelectedEstoque(estoque);
    setModalOpen(true);
    setModalLoading(true);

    try {
      const response = await axios.get(`/api/estoques/estoque-doadora/${estoque.id}`);
      setEstoqueDetalhes(response.data);
    } catch (error) {
      console.error('Error fetching estoque details:', error);
      setEstoqueDetalhes(null);
    } finally {
      setModalLoading(false);
    }
  };

  return (
    <div className="estoque-list-page">
      <h2 className="estoque-list-title">
        Lotes
        <div className="d-flex">
          <Input
            type="select"
            className="me-2"
            style={{ width: '200px', height: '48px' }}
            value={tipoLeiteFilter}
            onChange={e => setTipoLeiteFilter(e.target.value)}
          >
            <option value="">Todos os tipos</option>
            <option value="COLOSTRO">Colostro</option>
            <option value="TRANSICAO">Transição</option>
            <option value="MADURO">Maduro</option>
          </Input>

          <Input
            type="select"
            className="me-2"
            style={{ width: '200px', height: '48px' }}
            value={classificacaoFilter}
            onChange={e => setClassificacaoFilter(e.target.value)}
          >
            <option value="">Todas as classificações</option>
            <option value="A">A</option>
            <option value="B">B</option>
            <option value="C">C</option>
          </Input>

          <Button
            className="me-2"
            style={{ height: '48px', background: '#7ad27d', border: 'none' }}
            onClick={() => {
              setTipoLeiteFilter('');
              setClassificacaoFilter('');
            }}
          >
            Limpar Filtros
          </Button>
        </div>
      </h2>

      <div className="table-responsive">
        {loading ? (
          <div className="d-flex justify-content-center">
            <div className="spinner-border" role="status">
              <span className="sr-only">Carregando...</span>
            </div>
          </div>
        ) : estoqueList && estoqueList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Data validade</th>
                <th>Tipo de Leite</th>
                <th>Classificação</th>
                <th>Volume Total (mL)</th>
                <th>Volume Disponível (mL)</th>
                <th>Status Lote</th>
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
                  <td>
                    <Translate contentKey={`leiteVidaApp.StatusLote.${estoque.statusLote}`} />
                  </td>
                  <td>
                    <Button
                      color="info"
                      style={{ background: '#7ad27d', border: 'none' }}
                      size="sm"
                      onClick={() => openModal(estoque)}
                      title="Visualizar detalhes"
                    >
                      <FontAwesomeIcon icon={faEye} />
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          <div className="alert alert-warning">
            <Translate contentKey="leiteVidaApp.estoque.home.notFound">Não encontramos lotes</Translate>
          </div>
        )}
      </div>

      {/* Modal */}
      <Modal isOpen={modalOpen} toggle={toggleModal} size="lg">
        <ModalHeader toggle={toggleModal}>Detalhes do Lote {selectedEstoque?.id}</ModalHeader>
        <ModalBody>
          {modalLoading ? (
            <div className="d-flex justify-content-center">
              <div className="spinner-border" role="status">
                <span className="sr-only">Carregando detalhes...</span>
              </div>
            </div>
          ) : estoqueDetalhes ? (
            <div>
              <div className="row">
                <div className="col-md-6">
                  <h5>Informações do Lote</h5>
                  <p>
                    <strong>ID:</strong> {estoqueDetalhes.estoqueId}
                  </p>
                  <p>
                    <strong>Data de Validade:</strong>{' '}
                    {estoqueDetalhes.dataValidade ? (
                      <TextFormat type="date" value={estoqueDetalhes.dataValidade} format={APP_LOCAL_DATE_FORMAT} />
                    ) : (
                      'N/A'
                    )}
                  </p>
                  <p>
                    <strong>Tipo de Leite:</strong> <Translate contentKey={`leiteVidaApp.TipoLeite.${estoqueDetalhes.tipoLeite}`} />
                  </p>
                  <p>
                    <strong>Classificação:</strong>{' '}
                    <Translate contentKey={`leiteVidaApp.ClassificacaoLeite.${estoqueDetalhes.classificacao}`} />
                  </p>
                  <p>
                    <strong>Volume Disponível (mL):</strong> {estoqueDetalhes.volumeDisponivelMl}
                  </p>
                </div>
                <div className="col-md-6">
                  <h5>Informações da Doadora</h5>
                  <p>
                    <strong>Nome:</strong> {estoqueDetalhes.nomeDoadora}
                  </p>
                  <p>
                    <strong>CPF:</strong> {estoqueDetalhes.cpfDoadora}
                  </p>
                  <p>
                    <strong>Telefone:</strong> {estoqueDetalhes.telefoneDoadora}
                  </p>
                </div>
              </div>
            </div>
          ) : (
            <div className="alert alert-warning">Não foi possível carregar os detalhes do lote.</div>
          )}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleModal}>
            Fechar
          </Button>
        </ModalFooter>
      </Modal>

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
