import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-header-png.png" style={{ width: '2em', marginBottom: '18px' }} />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Doadora = () => (
  <NavItem>
    <NavLink tag={Link} to="/doadora" className="d-flex align-items-center">
      <img src="content/images/pregnant.svg" style={{ width: '28px', marginRight: '5px' }} />
      <span>Doadoras</span>
    </NavLink>
  </NavItem>
);

export const Pacientes = () => (
  <NavItem>
    <NavLink tag={Link} to="/paciente" className="d-flex align-items-center">
      <img src="content/images/pessoas.svg" style={{ width: '23px', marginRight: '10px' }} />
      <span>Pacientes</span>
    </NavLink>
  </NavItem>
);

export const Coleta = () => (
  <NavItem>
    <NavLink tag={Link} to="/coleta" className="d-flex align-items-center">
      <img src="content/images/mamadeira.svg" style={{ width: '15px', marginRight: '10px' }} />
      <span>Coletas</span>
    </NavLink>
  </NavItem>
);

export const Distribuicao = () => (
  <NavItem>
    <NavLink tag={Link} to="/distribuicao" className="d-flex align-items-center">
      <img src="content/images/criancanobraco.svg" style={{ width: '15px', marginRight: '10px' }} />
      <span>Distribuição</span>
    </NavLink>
  </NavItem>
);

export const Estoque = () => (
  <NavItem>
    <NavLink tag={Link} to="/estoque" className="d-flex align-items-center">
      <img src="content/images/frasco.svg" style={{ width: '15px', marginRight: '10px' }} />
      <span>Estoque</span>
    </NavLink>
  </NavItem>
);

export const Users = () => (
  <NavItem>
    <NavLink tag={Link} to="/admin/user-management" className="d-flex align-items-center">
      <img src="content/images/pessoas.svg" style={{ width: '20px', marginRight: '10px' }} />
      <span>Usuários</span>
    </NavLink>
  </NavItem>
);

export const Processamentos = () => (
  <NavItem>
    <NavLink tag={Link} to="/processamento" className="d-flex align-items-center">
      <img src="content/images/frascolab.svg" style={{ width: '20px', marginRight: '10px' }} />
      <span>Processamentos</span>
    </NavLink>
  </NavItem>
);

export const Relatorios = () => (
  <NavItem>
    <NavLink tag={Link} to="/relatorios" className="d-flex align-items-center">
      <img src="content/images/relatorios.svg" style={{ width: '20px', marginRight: '10px' }} />
      <span>Relatórios</span>
    </NavLink>
  </NavItem>
);
