import React, { useState } from 'react';
import { Button, Form, FormGroup, Label, Input, Container, Row, Col, Card, CardBody, Alert } from 'reactstrap';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';
import './password-change.scss';
import { toast } from 'react-toastify';

export const PasswordChange = () => {
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async e => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      setError('As senhas não coincidem');
      return;
    }

    if (newPassword.length < 6) {
      setError('A senha deve ter pelo menos 6 caracteres');
      return;
    }

    setLoading(true);
    setError('');

    try {
      await axios.post('/api/account/change-password-first-login', {
        newPassword,
      });

      toast.info('Senha alterada com sucesso!');
      window.location.href = '/';
    } catch (error) {
      console.error('Erro ao alterar senha:', error);
      setError('Erro ao alterar a senha. Tente novamente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="password-change-page">
      <Container>
        <Row className="justify-content-center">
          <Col md={8} lg={6}>
            <Card className="password-change-card">
              <CardBody>
                <div className="text-center mb-4">
                  <FontAwesomeIcon icon="lock" className="text-primary mb-3" size="3x" />
                  <h3 className="password-change-title">Primeira Configuração de Senha</h3>
                </div>

                <div className="security-info">
                  <FontAwesomeIcon icon="shield-alt" className="info-icon" />
                  <p className="mb-2">
                    <strong>Por segurança, você deve alterar sua senha padrão antes de continuar.</strong>
                  </p>
                  <small>
                    Senha atual: <strong>leitevida123</strong>
                  </small>
                </div>

                {error && (
                  <Alert color="danger">
                    <FontAwesomeIcon icon="exclamation-triangle" className="me-2" />
                    {error}
                  </Alert>
                )}

                <Form onSubmit={handleSubmit}>
                  <FormGroup>
                    <Label for="newPassword">
                      <FontAwesomeIcon icon="key" className="me-2" />
                      Nova Senha *
                    </Label>
                    <Input
                      type="password"
                      id="newPassword"
                      value={newPassword}
                      onChange={e => setNewPassword(e.target.value)}
                      required
                      minLength={6}
                      placeholder="Digite sua nova senha (mín. 6 caracteres)"
                    />
                  </FormGroup>

                  <FormGroup>
                    <Label for="confirmPassword">
                      <FontAwesomeIcon icon="lock" className="me-2" />
                      Confirmar Nova Senha *
                    </Label>
                    <Input
                      type="password"
                      id="confirmPassword"
                      value={confirmPassword}
                      onChange={e => setConfirmPassword(e.target.value)}
                      required
                      minLength={6}
                      placeholder="Confirme sua nova senha"
                    />
                  </FormGroup>

                  <Button type="submit" color="primary" block disabled={loading} className="mt-3">
                    {loading ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                        Alterando Senha...
                      </>
                    ) : (
                      <>
                        <FontAwesomeIcon icon="check" className="me-2" />
                        Alterar Senha
                      </>
                    )}
                  </Button>
                </Form>

                <div className="text-center mt-3">
                  <small className="text-muted">
                    <FontAwesomeIcon icon="info-circle" className="me-1" />
                    Esta ação é obrigatória para continuar usando o sistema
                  </small>
                </div>
              </CardBody>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default PasswordChange;
