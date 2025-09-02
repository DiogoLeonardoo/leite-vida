import React from 'react';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row, Button } from 'reactstrap';
import { Link } from 'react-router-dom';

const Unauthorized = () => (
  <div className="mt-5 pt-5">
    <Row className="justify-content-center">
      <Col md="8">
        <Alert color="danger">
          <h4>Acesso Negado</h4>
          <p>Você não tem autoridade para acessar esta página.</p>
          <Link to="/home">
            <Button color="primary">Voltar ao ínicio</Button>
          </Link>
        </Alert>
      </Col>
    </Row>
  </div>
);

export default Unauthorized;
