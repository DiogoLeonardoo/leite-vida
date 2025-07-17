import React, { useEffect, useState } from 'react';
import { Alert, Badge, Button, Card, CardBody, Col, Container, Row } from 'reactstrap';
import { useAppSelector } from 'app/config/store';
import axios from 'axios';
import './home.scss';
import { useNavigate } from 'react-router';

export const Home = () => {
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);
  const [doadorasCount, setDoadorasCount] = useState<number | null>(null);
  const [volumeSum, setVolumeSum] = useState<number | null>(null);
  const [processamentoSum, setProcessamentoSum] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);

        const doadorasResponse = await axios.get('api/doadoras/count-doadora');
        setDoadorasCount(doadorasResponse.data);

        const volumeResponse = await axios.get('api/estoques/soma-volume');
        setVolumeSum(volumeResponse.data);

        const processamentoResponse = await axios.get('api/coletas/volume-aguardando-processamento');
        setProcessamentoSum(processamentoResponse.data);

        setError(null);
      } catch (err) {
        console.error('Erro ao buscar dados:', err);
        setError('Erro ao carregar dados');
        setDoadorasCount(null);
        setVolumeSum(null);
        setProcessamentoSum(null);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleNovaColeta = () => {
    // Implementar navegação para nova coleta
    console.log('Nova Coleta');
  };

  const handleNovaDoadora = () => {
    // Implementar navegação para nova doadora
    navigate('/doadora/new');
  };

  const handleNovoPaciente = () => {
    // Implementar navegação para novo paciente
    navigate('/paciente/new');
  };

  const handleConsultarEstoque = () => {
    // Implementar navegação para consultar estoque
    console.log('Consultar Estoque');
  };

  const handleUsuarios = () => {
    // Implementar navegação para usuários
    console.log('Usuários');
  };

  const handleNovaDistribuicao = () => {
    // Implementar navegação para nova distribuição
    console.log('Nova Distribuição');
  };

  const handleRealizarAnalises = () => {
    // Implementar navegação para realizar análises
    console.log('Realizar Análises');
  };

  const isLabUser = account?.authorities?.includes('ROLE_LAB');

  return (
    <div className="home-page">
      <Container fluid className="py-4 px-5">
        {/* Header com boas vindas */}
        <Row className="mb-4">
          <Col>
            <div className="welcome-section d-flex align-items-center">
              <div className="welcome-icon me-3">
                <div className="icon-circle">
                  <i className="fas fa-tint text-success">
                    <img src="./content/images/criancanobraco.svg" style={{ width: '40px', height: '40px' }} />
                  </i>
                </div>
              </div>
              <div>
                <h2 className="welcome-title mb-1">Bem-vindo ao LeiteVida</h2>
                <p className="welcome-subtitle text-muted mb-0">
                  Olá, {account?.firstName}! Gerencie doações e distribuições de leite materno
                </p>
              </div>
            </div>
          </Col>
        </Row>

        {/* Cards de estatísticas */}
        <Row className="mb-4">
          <Col md={4} className="mb-3">
            <Card className="stats-card h-100">
              <CardBody className="d-flex align-items-center">
                <div className="text-center flex-grow-1">
                  <div className="stats-icon mb-2">
                    <i className="fas fa-users text-primary"></i>
                  </div>
                  <div className="stats-number">
                    {loading ? (
                      <div className="spinner-border spinner-border-sm text-primary" role="status">
                        <span className="visually-hidden">Carregando...</span>
                      </div>
                    ) : error ? (
                      <span className="text-danger">--</span>
                    ) : (
                      doadorasCount || 0
                    )}
                  </div>
                  <div className="stats-label">DOADORAS CADASTRADAS</div>
                </div>
                <div className="ms-3">
                  <img src="./content/images/pessoas.svg" style={{ width: '60px', height: '60px' }} />
                </div>
              </CardBody>
            </Card>
          </Col>
          <Col md={4} className="mb-3">
            <Card className="stats-card stats-card-success h-100">
              <CardBody className="d-flex align-items-center">
                <div className="text-center flex-grow-1">
                  <div className="stats-icon mb-2">
                    <i className="fas fa-battery-full text-white"></i>
                  </div>
                  <div className="stats-number text-white">
                    {loading ? (
                      <div className="spinner-border spinner-border-sm text-white" role="status">
                        <span className="visually-hidden">Carregando...</span>
                      </div>
                    ) : error ? (
                      <span className="text-white">--</span>
                    ) : (
                      `${volumeSum || 0}mL`
                    )}
                  </div>
                  <div className="stats-label text-white">DE LEITE EM ESTOQUE</div>
                </div>
                <div className="ms-3">
                  <img src="./content/images/mamadeira.svg" style={{ width: '60px', height: '60px' }} />
                </div>
              </CardBody>
            </Card>
          </Col>
          <Col md={4} className="mb-3">
            <Card className="stats-card h-100">
              <CardBody className="d-flex align-items-center">
                <div className="text-center flex-grow-1">
                  <div className="stats-icon mb-2">
                    <i className="fas fa-flask text-info"></i>
                  </div>
                  <div className="stats-number">
                    {loading ? (
                      <div className="spinner-border spinner-border-sm text-info" role="status">
                        <span className="visually-hidden">Carregando...</span>
                      </div>
                    ) : error ? (
                      <span className="text-danger">--</span>
                    ) : (
                      `${processamentoSum || 0}mL`
                    )}
                  </div>
                  <div className="stats-label">DE LEITE EM PROCESSAMENTO</div>
                </div>
                <div className="ms-3">
                  <img src="./content/images/frascolab.svg" style={{ width: '60px', height: '60px' }} />
                </div>
              </CardBody>
            </Card>
          </Col>
        </Row>

        {/* Botões de ação */}
        {isLabUser ? (
          <Row>
            <Col md={4} className="mb-3 offset-md-4">
              <Button color="primary" block size="lg" className="action-button text-center" onClick={handleRealizarAnalises}>
                <i className="fas fa-flask me-2"></i>
                Realizar Análises
                <div className="button-subtitle">Avalie os leites coletados</div>
              </Button>
            </Col>
          </Row>
        ) : (
          <>
            <Row>
              <Col md={4} className="mb-3">
                <Button color="secondary" block size="lg" className="action-button" onClick={handleNovaColeta}>
                  <i className="fas fa-plus-circle me-2"></i>
                  Nova Coleta
                  <div className="button-subtitle">Registrar nova coleta de leite materno</div>
                </Button>
              </Col>
              <Col md={4} className="mb-3">
                <Button color="success" block size="lg" className="action-button" onClick={handleNovaDoadora}>
                  <i className="fas fa-user-plus me-2"></i>
                  Nova Doadora
                  <div className="button-subtitle">Cadastrar nova doadora</div>
                </Button>
              </Col>
              <Col md={4} className="mb-3">
                <Button color="primary" block size="lg" className="action-button" onClick={handleNovoPaciente}>
                  <i className="fas fa-baby me-2"></i>
                  Novo Paciente
                  <div className="button-subtitle">Registrar novo paciente</div>
                </Button>
              </Col>
            </Row>

            <Row>
              <Col md={4} className="mb-3">
                <Button color="success" block size="lg" className="action-button" onClick={handleConsultarEstoque}>
                  <i className="fas fa-search me-2"></i>
                  Consultar Estoque
                  <div className="button-subtitle">Verificar o estoque de leite materno</div>
                </Button>
              </Col>
              <Col md={4} className="mb-3">
                <Button color="light" block size="lg" className="action-button" onClick={handleUsuarios}>
                  <i className="fas fa-users-cog me-2"></i>
                  Usuários
                  <div className="button-subtitle">Gerenciar usuários do sistema</div>
                </Button>
              </Col>
              <Col md={4} className="mb-3">
                <Button color="success" block size="lg" className="action-button" onClick={handleNovaDistribuicao}>
                  <i className="fas fa-share-alt me-2"></i>
                  Nova Distribuição
                  <div className="button-subtitle">Realizar uma nova distribuição</div>
                </Button>
              </Col>
            </Row>
          </>
        )}
      </Container>
    </div>
  );
};

export default Home;
