import './header.scss';

import React, { useState } from 'react';
import { Storage, Translate } from 'react-jhipster';
import { Collapse, Nav, Navbar, NavbarToggler } from 'reactstrap';
import LoadingBar from 'react-redux-loading-bar';
import { useLocation } from 'react-router-dom';

import { useAppDispatch } from 'app/config/store';
import { setLocale } from 'app/shared/reducers/locale';
import { AccountMenu, AdminMenu, EntitiesMenu, LocaleMenu } from '../menus';
import { Brand, Coleta, Distribuicao, Doadora, Estoque, Pacientes, Processamentos, Users } from './header-components';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { useAppSelector } from 'app/config/store';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const location = useLocation();
  const account = useAppSelector(state => state.authentication.account);
  const isLab = hasAnyAuthority(account.authorities, ['ROLE_LAB']);
  const isEnf = hasAnyAuthority(account.authorities, ['ROLE_LAB']);

  const dispatch = useAppDispatch();

  const handleLocaleChange = event => {
    const langKey = event.target.value;
    Storage.session.set('locale', langKey);
    dispatch(setLocale(langKey));
  };

  const toggleMenu = () => setMenuOpen(!menuOpen);

  if (location.pathname === '/account/password-change') {
    return null;
  }

  return (
    <>
      {props.isAuthenticated && (
        <div id="app-header">
          <LoadingBar className="loading-bar" />
          <Navbar data-cy="navbar" dark expand="md" fixed="top" className="jh-navbar">
            <NavbarToggler aria-label="Menu" onClick={toggleMenu} />
            <Brand />
            <Collapse isOpen={menuOpen} navbar>
              <Nav id="header-tabs" className="justify-content-center flex-grow-1" navbar>
                <LocaleMenu currentLocale={props.currentLocale} onClick={handleLocaleChange} />
                <>
                  {!isLab && <Doadora />}
                  {!isLab && <Pacientes />}
                  {props.isAuthenticated && <Coleta />}
                  {!isLab && <Distribuicao />}
                  {!isLab && <Estoque />}
                  {isLab && <Processamentos />}
                  {props.isAdmin && <Users />}
                  {/* {props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />} */}
                </>
              </Nav>
              <Nav className="ms-auto" navbar>
                <AccountMenu isAuthenticated={props.isAuthenticated} />
              </Nav>
            </Collapse>
          </Navbar>
        </div>
      )}
    </>
  );
};

export default Header;
