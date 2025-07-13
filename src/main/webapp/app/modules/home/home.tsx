import React from 'react';
import { Alert, Badge, Button, Card, CardBody, Col, Container, Row } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <div
      style={{
        background: 'linear-gradient(135deg, #FFF5E1 0%, #FFE4B5 50%, #FFEAA7 100%)',
        paddingTop: '1.5rem',
        paddingBottom: '1.5rem',
      }}
    >
      <Container>
        {/* Header Compacto */}
        <Row className="justify-content-center mb-4">
          <Col md="8" lg="6">
            <div className="d-flex align-items-center justify-content-center mb-2">
              <div style={{ display: 'flex', justifyContent: 'center' }}>
                <img
                  src="content/images/home-logo.svg"
                  alt="Leite Vida Logo"
                  style={{
                    width: '50%',
                    height: 'auto',
                    objectFit: 'contain',
                  }}
                />
              </div>
            </div>
          </Col>
        </Row>

        {/* Stats Cards Compactas */}
        <Row className="mb-4">
          <Col md="4" className="mb-3">
            <Card
              className="shadow-sm border-0"
              style={{
                borderRadius: '15px',
              }}
            >
              <CardBody className="p-3">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <Badge color="secondary" className="mb-1" style={{ fontSize: '0.7rem' }}>
                      DOADORAS ATIVAS
                    </Badge>
                    <h3 className="h1 font-weight-bold mb-0 text-dark">230</h3>
                  </div>
                  <div
                    style={{
                      backgroundColor: '#e9ecef',
                      width: '45px',
                      height: '45px',
                      borderRadius: '50%',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: '1.2rem',
                    }}
                  >
                    👥
                  </div>
                </div>
              </CardBody>
            </Card>
          </Col>

          <Col md="4" className="mb-3">
            <Card
              className="shadow-sm border-0"
              style={{
                borderRadius: '15px',
                background: 'linear-gradient(135deg, #28a745 0%, #20c997 100%)',
              }}
            >
              <CardBody className="p-3 text-white">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <Badge color="light" className="mb-1 text-success" style={{ fontSize: '0.7rem' }}>
                      ESTOQUE
                    </Badge>
                    <h3 className="h1 font-weight-bold mb-0">
                      2300<small className="h5">mL</small>
                    </h3>
                  </div>
                  <div
                    style={{
                      backgroundColor: 'rgba(255, 255, 255, 0.25)',
                      width: '45px',
                      height: '45px',
                      borderRadius: '50%',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: '1.2rem',
                    }}
                  >
                    📦
                  </div>
                </div>
              </CardBody>
            </Card>
          </Col>

          <Col md="4" className="mb-3">
            <Card
              className="shadow-sm border-0"
              style={{
                borderRadius: '15px',
                background: 'linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%)',
              }}
            >
              <CardBody className="p-3">
                <div className="d-flex justify-content-between align-items-center">
                  <div>
                    <Badge color="warning" className="mb-1" style={{ fontSize: '0.7rem' }}>
                      PROCESSAMENTO
                    </Badge>
                    <h3 className="h1 font-weight-bold mb-0 text-dark">
                      230<small className="h5">mL</small>
                    </h3>
                  </div>
                  <div
                    style={{
                      backgroundColor: '#fff3cd',
                      width: '45px',
                      height: '45px',
                      borderRadius: '50%',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      fontSize: '1.2rem',
                    }}
                  >
                    🧪
                  </div>
                </div>
              </CardBody>
            </Card>
          </Col>
        </Row>

        {/* Action Buttons Principais */}
        <Row className="mb-4">
          <Col lg="4" md="6" className="mb-3">
            <Button
              color="dark"
              size="lg"
              block
              className="d-flex justify-content-between align-items-center"
              style={{
                minHeight: '80px',
                borderRadius: '15px',
                border: 'none',
                background: 'linear-gradient(135deg, #343a40 0%, #495057 100%)',
              }}
            >
              <div className="text-left">
                <h5 className="mb-0 font-weight-bold">Nova Coleta</h5>
                <small className="text-light">Registrar coleta</small>
              </div>
              <span style={{ fontSize: '1.5rem' }}>➕</span>
            </Button>
          </Col>

          <Col lg="4" md="6" className="mb-3">
            <Button
              color="success"
              size="lg"
              block
              className="d-flex justify-content-between align-items-center"
              style={{
                minHeight: '80px',
                borderRadius: '15px',
                border: 'none',
                background: 'linear-gradient(135deg, #28a745 0%, #20c997 100%)',
              }}
            >
              <div className="text-left">
                <h5 className="mb-0 font-weight-bold">Nova Doadora</h5>
                <small className="text-light">Cadastrar doadora</small>
              </div>
              <span style={{ fontSize: '1.5rem' }}>👤</span>
            </Button>
          </Col>

          <Col lg="4" md="6" className="mb-3">
            <Button
              color="primary"
              size="lg"
              block
              className="d-flex justify-content-between align-items-center"
              style={{
                minHeight: '80px',
                borderRadius: '15px',
                border: 'none',
                background: 'linear-gradient(135deg, #007bff 0%, #0056b3 100%)',
              }}
            >
              <div className="text-left">
                <h5 className="mb-0 font-weight-bold">Novo Paciente</h5>
                <small className="text-light">Cadastrar paciente</small>
              </div>
              <span style={{ fontSize: '1.5rem' }}>👶</span>
            </Button>
          </Col>
        </Row>

        {/* Secondary Actions Compactas */}
        <Row>
          <Col lg="4" md="6" className="mb-3">
            <Button
              color="light"
              size="md"
              block
              className="d-flex justify-content-between align-items-center text-dark"
              style={{
                minHeight: '60px',
                borderRadius: '12px',
                backgroundColor: '#ffffff',
                border: '2px solid #e9ecef',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              }}
            >
              <div className="text-left">
                <h6 className="mb-0 font-weight-bold">Consultar Estoque</h6>
                <small className="text-muted">Verificar disponibilidade</small>
              </div>
              <span style={{ fontSize: '1.2rem', color: '#28a745' }}>🔍</span>
            </Button>
          </Col>

          <Col lg="4" md="6" className="mb-3">
            <Button
              color="light"
              size="md"
              block
              className="d-flex justify-content-between align-items-center text-dark"
              style={{
                minHeight: '60px',
                borderRadius: '12px',
                backgroundColor: '#ffffff',
                border: '2px solid #e9ecef',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              }}
            >
              <div className="text-left">
                <h6 className="mb-0 font-weight-bold">Usuários</h6>
                <small className="text-muted">Gerenciar usuários</small>
              </div>
              <span style={{ fontSize: '1.2rem', color: '#6c757d' }}>👥</span>
            </Button>
          </Col>

          <Col lg="4" md="6" className="mb-3">
            <Button
              color="light"
              size="md"
              block
              className="d-flex justify-content-between align-items-center text-dark"
              style={{
                minHeight: '60px',
                borderRadius: '12px',
                backgroundColor: '#ffffff',
                border: '2px solid #e9ecef',
                boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
              }}
            >
              <div className="text-left">
                <h6 className="mb-0 font-weight-bold">Nova Distribuição</h6>
                <small className="text-muted">Realizar distribuição</small>
              </div>
              <span style={{ fontSize: '1.2rem', color: '#28a745' }}>📊</span>
            </Button>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
