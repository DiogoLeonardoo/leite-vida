import React, { useState } from 'react';
import { Translate } from 'react-jhipster';
import { Row, Col, Card, CardHeader, CardBody, Button, Form, FormGroup, Label, Input, Collapse } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChartLine, faBoxes, faFlask, faUsers } from '@fortawesome/free-solid-svg-icons';
import './relatorios.scss';

const Relatorios = () => {
  const [openCard, setOpenCard] = useState(null);
  const [selectedFilter, setSelectedFilter] = useState('');
  const [filterByPeriod, setFilterByPeriod] = useState(false);

  const toggleCard = cardId => {
    setOpenCard(openCard === cardId ? null : cardId);
  };

  const handleFilterChange = e => {
    setSelectedFilter(e.target.value);
  };

  const handlePeriodFilterChange = e => {
    setFilterByPeriod(e.target.checked);
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
                <Form>
                  <Row>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataInicial">Data Inicial</Label>
                        <Input type="date" name="dataInicial" id="dataInicial" />
                      </FormGroup>
                    </Col>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataFinal">Data Final</Label>
                        <Input type="date" name="dataFinal" id="dataFinal" />
                      </FormGroup>
                    </Col>
                  </Row>
                  <div className="text-center">
                    <Button color="primary">Gerar Relatório</Button>
                  </div>
                </Form>
              </CardBody>
            </Collapse>
          </Card>
        </Col>

        {/* Lotes por status, período, tipo de leite e classificação */}
        <Col md={6} lg={5} className="mb-4">
          <Card className="report-card">
            <CardHeader className="d-flex align-items-center" onClick={() => toggleCard('lotes')}>
              <FontAwesomeIcon icon={faBoxes} className="mr-2" />
              <span className="card-title">Lotes</span>
            </CardHeader>
            <Collapse isOpen={openCard === 'lotes'}>
              <CardBody>
                <Form>
                  <FormGroup>
                    <Label for="tipoFiltro">Filtrar por</Label>
                    <Input type="select" name="tipoFiltro" id="tipoFiltro" value={selectedFilter} onChange={handleFilterChange}>
                      <option value="">Selecione</option>
                      <option value="status">Status</option>
                      <option value="tipoLeite">Tipo de Leite</option>
                      <option value="classificacao">Classificação</option>
                    </Input>
                  </FormGroup>

                  {selectedFilter === 'status' && (
                    <FormGroup>
                      <Label for="statusLote">Status</Label>
                      <Input type="select" name="statusLote" id="statusLote">
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
                      <Input type="select" name="tipoLeite" id="tipoLeite">
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
                      <Input type="select" name="classificacao" id="classificacao">
                        <option value="">Selecione a classificação</option>
                        <option value="A">A</option>
                        <option value="B">B</option>
                        <option value="C">C</option>
                      </Input>
                    </FormGroup>
                  )}

                  <FormGroup check className="mb-3">
                    <Label check>
                      <Input
                        type="checkbox"
                        name="filterByPeriod"
                        id="filterByPeriod"
                        checked={filterByPeriod}
                        onChange={handlePeriodFilterChange}
                      />{' '}
                      Filtrar por período?
                    </Label>
                  </FormGroup>

                  {filterByPeriod && (
                    <Row>
                      <Col md={6}>
                        <FormGroup>
                          <Label for="dataInicialLotes">Data Inicial</Label>
                          <Input type="date" name="dataInicialLotes" id="dataInicialLotes" />
                        </FormGroup>
                      </Col>
                      <Col md={6}>
                        <FormGroup>
                          <Label for="dataFinalLotes">Data Final</Label>
                          <Input type="date" name="dataFinalLotes" id="dataFinalLotes" />
                        </FormGroup>
                      </Col>
                    </Row>
                  )}
                  <div className="text-center">
                    <Button color="primary">Gerar Relatório</Button>
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
                <Form>
                  <Row>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataInicialProc">Data Inicial</Label>
                        <Input type="date" name="dataInicialProc" id="dataInicialProc" />
                      </FormGroup>
                    </Col>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataFinalProc">Data Final</Label>
                        <Input type="date" name="dataFinalProc" id="dataFinalProc" />
                      </FormGroup>
                    </Col>
                  </Row>
                  <div className="text-center">
                    <Button color="primary">Gerar Relatório</Button>
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
                <Form>
                  <FormGroup>
                    <Label for="doadora">Doadora</Label>
                    <Input type="select" name="doadora" id="doadora">
                      <option value="">Selecione a doadora</option>
                      {/* Aqui você pode carregar as doadoras do sistema dinamicamente */}
                    </Input>
                  </FormGroup>
                  <Row>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataInicialDoadora">Data Inicial</Label>
                        <Input type="date" name="dataInicialDoadora" id="dataInicialDoadora" />
                      </FormGroup>
                    </Col>
                    <Col md={6}>
                      <FormGroup>
                        <Label for="dataFinalDoadora">Data Final</Label>
                        <Input type="date" name="dataFinalDoadora" id="dataFinalDoadora" />
                      </FormGroup>
                    </Col>
                  </Row>
                  <div className="text-center">
                    <Button color="primary">Gerar Relatório</Button>
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
