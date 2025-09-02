import React, { useState } from 'react';
import { Translate } from 'react-jhipster';
import { Row, Col, Card, CardHeader, CardBody, Button, Form, FormGroup, Label, Input, Collapse } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChartLine, faBoxes, faFlask, faUsers } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';
import './relatorios.scss';
// Import validation utilities
import { validateCPF, maskCPF, removeMask, validationMessages } from 'app/shared/util/validation-utils';

const Relatorios = () => {
  const [openCard, setOpenCard] = useState(null);
  const [selectedFilter, setSelectedFilter] = useState('');
  const [filterByPeriod, setFilterByPeriod] = useState(false);
  const [filterDoadoraByPeriod, setFilterDoadoraByPeriod] = useState(false);
  const [dataInicialColeta, setDataInicialColeta] = useState('');
  const [dataFinalColeta, setDataFinalColeta] = useState('');
  const [dataInicialProc, setDataInicialProc] = useState('');
  const [dataFinalProc, setDataFinalProc] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [isLoadingProc, setIsLoadingProc] = useState(false);
  const [isLoadingDoadora, setIsLoadingDoadora] = useState(false);
  const [selectedStatus, setSelectedStatus] = useState('');
  const [selectedTipoLeite, setSelectedTipoLeite] = useState('');
  const [selectedClassificacao, setSelectedClassificacao] = useState('');
  const [isLoadingLotes, setIsLoadingLotes] = useState(false);

  const [cpfDoadora, setCpfDoadora] = useState('');
  const [selectedDoadora, setSelectedDoadora] = useState(null);
  const [cpfError, setCpfError] = useState('');
  const [searchingDoadora, setSearchingDoadora] = useState(false);
  const [dataInicialDoadora, setDataInicialDoadora] = useState('');
  const [dataFinalDoadora, setDataFinalDoadora] = useState('');

  const toggleCard = cardId => {
    setOpenCard(openCard === cardId ? null : cardId);
  };

  const handleFilterChange = e => {
    setSelectedFilter(e.target.value);
  };

  const handlePeriodFilterChange = e => {
    setFilterByPeriod(e.target.checked);
  };

  const handleDoadoraPeriodFilterChange = e => {
    setFilterDoadoraByPeriod(e.target.checked);
  };

  const handleGerarRelatorioColetas = async e => {
    e.preventDefault();

    if (!dataInicialColeta || !dataFinalColeta) {
      alert('Por favor, preencha as datas inicial e final.');
      return;
    }

    setIsLoading(true);

    try {
      const response = await axios.get('/api/coletas/relatorios-periodo/pdf', {
        params: {
          dataInicio: dataInicialColeta,
          dataFim: dataFinalColeta,
        },
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `relatorio_coletas_${dataInicialColeta}_${dataFinalColeta}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Erro ao gerar relatório de coletas:', error);
      alert('Erro ao gerar o relatório. Por favor, tente novamente.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleGerarRelatorioProcessamentos = async e => {
    e.preventDefault();

    if (!dataInicialProc || !dataFinalProc) {
      alert('Por favor, preencha as datas inicial e final.');
      return;
    }

    setIsLoadingProc(true);

    try {
      const response = await axios.get('/api/processamentos/processamento-periodo/pdf', {
        params: {
          dataInicio: dataInicialProc,
          dataFim: dataFinalProc,
        },
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `relatorio_processamentos_${dataInicialProc}_${dataFinalProc}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Erro ao gerar relatório de processamentos:', error);
      alert('Erro ao gerar o relatório. Por favor, tente novamente.');
    } finally {
      setIsLoadingProc(false);
    }
  };

  const searchDoadoraByCpf = async cpf => {
    setSearchingDoadora(true);
    try {
      const response = await axios.get(`/api/doadoras/find-by-cpf/${cpf}`);
      if (response.data) {
        setSelectedDoadora(response.data);
        setCpfError('');
      } else {
        setCpfError('Doadora não encontrada');
        setSelectedDoadora(null);
      }
    } catch (error) {
      if (error.response?.status === 404) {
        setCpfError('Doadora não encontrada');
      } else {
        setCpfError('Erro ao buscar doadora');
      }
      setSelectedDoadora(null);
    } finally {
      setSearchingDoadora(false);
    }
  };

  const handleCpfChange = e => {
    const value = e.target.value;
    const maskedValue = maskCPF(value);
    setCpfDoadora(maskedValue);

    setCpfError('');
    setSelectedDoadora(null);

    if (maskedValue.length === 14) {
      const cleanCpf = removeMask(maskedValue);

      if (!validateCPF(cleanCpf)) {
        setCpfError(validationMessages?.cpf?.invalid || 'CPF inválido');
        return;
      }
      searchDoadoraByCpf(cleanCpf);
    }
  };

  const handleGerarRelatorioDoadora = async e => {
    e.preventDefault();

    if (!selectedDoadora) {
      alert('Por favor, selecione uma doadora válida.');
      return;
    }

    setIsLoadingDoadora(true);

    try {
      const url = `/api/doadoras/pdf/doadora/${selectedDoadora.id}`;
      const params: {
        dataInicio?: string;
        dataFim?: string;
      } = {};

      if (filterDoadoraByPeriod && dataInicialDoadora && dataFinalDoadora) {
        params.dataInicio = dataInicialDoadora;
        params.dataFim = dataFinalDoadora;
      }

      const response = await axios.get(url, {
        params,
        responseType: 'blob',
      });

      const fileUrl = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = fileUrl;
      link.setAttribute('download', `relatorio_coletas_doadora_${selectedDoadora.nome.replace(/\s+/g, '_')}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(fileUrl);
    } catch (error) {
      console.error('Erro ao gerar relatório de coletas por doadora:', error);
      alert('Erro ao gerar o relatório. Por favor, tente novamente.');
    } finally {
      setIsLoadingDoadora(false);
    }
  };

  const handleGerarRelatorioLotes = async e => {
    e.preventDefault();

    if (!selectedFilter) {
      alert('Por favor, selecione pelo menos um filtro.');
      return;
    }

    setIsLoadingLotes(true);

    try {
      const params: {
        tipoLeite?: string;
        statusLote?: string;
        classificacao?: string;
      } = {};

      if (selectedFilter === 'tipoLeite' && selectedTipoLeite) {
        params.tipoLeite = selectedTipoLeite;
      }
      if (selectedFilter === 'status' && selectedStatus) {
        params.statusLote = selectedStatus;
      }
      if (selectedFilter === 'classificacao' && selectedClassificacao) {
        params.classificacao = selectedClassificacao;
      }

      const response = await axios.get('/api/estoques/relatorio/estoque/pdf', {
        params,
        responseType: 'blob',
      });

      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `relatorio_lotes_${new Date().toISOString().split('T')[0]}.pdf`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Erro ao gerar relatório de lotes:', error);
      alert('Erro ao gerar o relatório. Por favor, tente novamente.');
    } finally {
      setIsLoadingLotes(false);
    }
  };

  return (
    <div className="relatorios-page">
      <h2 className="page-title">Relatórios</h2>

      <Row className="justify-content-center">
        <h2 className="text-center w-100 mb-4">Relatórios</h2>
        {/* Coletas por período */}
        <Col md={6} lg={5} className="mb-4">
          <Card className="report-card">
            <CardHeader className="d-flex align-items-center" onClick={() => toggleCard('coletas')}>
              <FontAwesomeIcon icon={faChartLine} className="mr-2" />
              <span className="card-title">Coletas por período</span>
            </CardHeader>
            <Collapse isOpen={openCard === 'coletas'}>
              <CardBody>
                <Form onSubmit={handleGerarRelatorioColetas}>
                  <Row>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataInicial">Data Inicial</Label>
                        <Input
                          type="date"
                          name="dataInicial"
                          id="dataInicial"
                          value={dataInicialColeta}
                          onChange={e => setDataInicialColeta(e.target.value)}
                          required
                          className="form-control border border-secondary"
                        />
                      </FormGroup>
                    </Col>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataFinal">Data Final</Label>
                        <Input
                          type="date"
                          name="dataFinal"
                          id="dataFinal"
                          value={dataFinalColeta}
                          onChange={e => setDataFinalColeta(e.target.value)}
                          required
                          className="form-control border border-secondary"
                        />
                      </FormGroup>
                    </Col>
                  </Row>
                  <div className="text-center">
                    <Button type="submit" color="primary" disabled={isLoading}>
                      {isLoading ? 'Gerando...' : 'Gerar Relatório'}
                    </Button>
                  </div>
                </Form>
              </CardBody>
            </Collapse>
          </Card>
        </Col>

        {/* Lotes por status, tipo de leite e classificação */}
        <Col md={6} lg={5} className="mb-4">
          <Card className="report-card">
            <CardHeader className="d-flex align-items-center" onClick={() => toggleCard('lotes')}>
              <FontAwesomeIcon icon={faBoxes} className="mr-2" />
              <span className="card-title">Lotes</span>
            </CardHeader>
            <Collapse isOpen={openCard === 'lotes'}>
              <CardBody>
                <Form onSubmit={handleGerarRelatorioLotes}>
                  <FormGroup>
                    <Label for="tipoFiltro">Filtrar por</Label>
                    <Input
                      type="select"
                      name="tipoFiltro"
                      id="tipoFiltro"
                      value={selectedFilter}
                      onChange={handleFilterChange}
                      className="form-control border border-secondary"
                    >
                      <option value="">Selecione</option>
                      <option value="status">Status</option>
                      <option value="tipoLeite">Tipo de Leite</option>
                      <option value="classificacao">Classificação</option>
                    </Input>
                  </FormGroup>

                  {selectedFilter === 'status' && (
                    <FormGroup>
                      <Label for="statusLote">Status</Label>
                      <Input
                        type="select"
                        name="statusLote"
                        id="statusLote"
                        value={selectedStatus}
                        onChange={e => setSelectedStatus(e.target.value)}
                        className="form-control border border-secondary"
                      >
                        <option value="">Todos os status</option>
                        <option value="DISPONIVEL">Disponível</option>
                        <option value="RESERVADO">Reservado</option>
                        <option value="DISTRIBUIDO">Distribuído</option>
                        <option value="EXPIRADO">Expirado</option>
                      </Input>
                    </FormGroup>
                  )}

                  {selectedFilter === 'tipoLeite' && (
                    <FormGroup>
                      <Label for="tipoLeite">Tipo de Leite</Label>
                      <Input
                        type="select"
                        name="tipoLeite"
                        id="tipoLeite"
                        value={selectedTipoLeite}
                        onChange={e => setSelectedTipoLeite(e.target.value)}
                        className="form-control border border-secondary"
                      >
                        <option value="">Selecione o tipo de leite</option>
                        <option value="COLOSTRO">Colostro</option>
                        <option value="TRANSICAO">Transição</option>
                        <option value="MADURO">Maduro</option>
                      </Input>
                    </FormGroup>
                  )}

                  {selectedFilter === 'classificacao' && (
                    <FormGroup>
                      <Label for="classificacao">Classificação</Label>
                      <Input
                        type="select"
                        name="classificacao"
                        id="classificacao"
                        value={selectedClassificacao}
                        onChange={e => setSelectedClassificacao(e.target.value)}
                        className="form-control border border-secondary"
                      >
                        <option value="">Selecione a classificação</option>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                      </Input>
                    </FormGroup>
                  )}

                  <div className="text-center">
                    <Button type="submit" color="primary" disabled={isLoadingLotes}>
                      {isLoadingLotes ? 'Gerando...' : 'Gerar Relatório'}
                    </Button>
                  </div>
                </Form>
              </CardBody>
            </Collapse>
          </Card>
        </Col>

        {/* Processamentos por período */}
        <Col md={6} lg={5} className="mb-4">
          <Card className="report-card">
            <CardHeader className="d-flex align-items-center" onClick={() => toggleCard('processamentos')}>
              <FontAwesomeIcon icon={faFlask} className="mr-2" />
              <span className="card-title">Processamentos por período</span>
            </CardHeader>
            <Collapse isOpen={openCard === 'processamentos'}>
              <CardBody>
                <Form onSubmit={handleGerarRelatorioProcessamentos}>
                  <Row>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataInicialProc">Data Inicial</Label>
                        <Input
                          type="date"
                          name="dataInicialProc"
                          id="dataInicialProc"
                          value={dataInicialProc}
                          onChange={e => setDataInicialProc(e.target.value)}
                          required
                          className="form-control border border-secondary"
                        />
                      </FormGroup>
                    </Col>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataFinalProc">Data Final</Label>
                        <Input
                          type="date"
                          name="dataFinalProc"
                          id="dataFinalProc"
                          value={dataFinalProc}
                          onChange={e => setDataFinalProc(e.target.value)}
                          required
                          className="form-control border border-secondary"
                        />
                      </FormGroup>
                    </Col>
                  </Row>
                  <div className="text-center">
                    <Button type="submit" color="primary" disabled={isLoadingProc}>
                      {isLoadingProc ? 'Gerando...' : 'Gerar Relatório'}
                    </Button>
                  </div>
                </Form>
              </CardBody>
            </Collapse>
          </Card>
        </Col>

        {/* Coletas por doadora */}
        <Col md={6} lg={5} className="mb-4">
          <Card className="report-card">
            <CardHeader className="d-flex align-items-center" onClick={() => toggleCard('doadoras')}>
              <FontAwesomeIcon icon={faUsers} className="mr-2" />
              <span className="card-title">Coletas por doadora</span>
            </CardHeader>
            <Collapse isOpen={openCard === 'doadoras'}>
              <CardBody>
                <Form onSubmit={handleGerarRelatorioDoadora}>
                  <Row>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="cpfDoadora">CPF da Doadora</Label>
                        <Input
                          type="text"
                          name="cpfDoadora"
                          id="cpfDoadora"
                          placeholder="000.000.000-00"
                          value={cpfDoadora}
                          onChange={handleCpfChange}
                          disabled={searchingDoadora}
                          className={`form-control border ${cpfError ? 'border-danger' : selectedDoadora ? 'border-success' : 'border-secondary'}`}
                        />
                        {searchingDoadora && (
                          <div className="d-flex align-items-center mt-1">
                            <div className="spinner-border spinner-border-sm text-primary mr-2" role="status">
                              <span className="visually-hidden">Buscando...</span>
                            </div>
                            <small className="text-muted">Buscando doadora...</small>
                          </div>
                        )}
                        {cpfError && <div className="text-danger mt-1">{cpfError}</div>}
                        {selectedDoadora && <div className="text-success mt-1">Doadora encontrada</div>}
                      </FormGroup>
                    </Col>

                    <Col md={6}>
                      <FormGroup>
                        <Label for="doadora">Doadora</Label>
                        <Input
                          type="text"
                          name="doadora"
                          id="doadora"
                          readOnly
                          value={selectedDoadora?.nome || ''}
                          placeholder="Nome será preenchido automaticamente"
                          className="form-control border border-secondary"
                        />
                      </FormGroup>
                    </Col>
                  </Row>

                  <FormGroup check className="mb-3">
                    <Label check>
                      <Input
                        type="checkbox"
                        name="filterDoadoraByPeriod"
                        id="filterDoadoraByPeriod"
                        checked={filterDoadoraByPeriod}
                        onChange={handleDoadoraPeriodFilterChange}
                      />{' '}
                      Filtrar por período?
                    </Label>
                  </FormGroup>

                  {filterDoadoraByPeriod && (
                    <Row>
                      <Col md={6}>
                        <FormGroup>
                          <Label for="dataInicialDoadora">Data Inicial</Label>
                          <Input
                            type="date"
                            name="dataInicialDoadora"
                            id="dataInicialDoadora"
                            value={dataInicialDoadora}
                            onChange={e => setDataInicialDoadora(e.target.value)}
                            className="form-control border border-secondary"
                          />
                        </FormGroup>
                      </Col>
                      <Col md={6}>
                        <FormGroup>
                          <Label for="dataFinalDoadora">Data Final</Label>
                          <Input
                            type="date"
                            name="dataFinalDoadora"
                            id="dataFinalDoadora"
                            value={dataFinalDoadora}
                            onChange={e => setDataFinalDoadora(e.target.value)}
                            className="form-control border border-secondary"
                          />
                        </FormGroup>
                      </Col>
                    </Row>
                  )}

                  <div className="text-center">
                    <Button type="submit" color="primary" disabled={!selectedDoadora || isLoadingDoadora}>
                      {isLoadingDoadora ? 'Gerando...' : 'Gerar Relatório'}
                    </Button>
                  </div>
                </Form>
              </CardBody>
            </Collapse>
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default Relatorios;
