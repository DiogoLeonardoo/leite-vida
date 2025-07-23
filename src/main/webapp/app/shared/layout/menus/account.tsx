import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import { useAppSelector } from 'app/config/store';

const accountMenuItemsAuthenticated = () => (
  <>
    <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
      <Translate contentKey="global.menu.account.logout">Sign out</Translate>
    </MenuItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false }) => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <NavDropdown className="account-menu" name={isAuthenticated ? account.firstName : 'Conta'} id="account-menu" data-cy="accountMenu">
      {isAuthenticated && accountMenuItemsAuthenticated()}
    </NavDropdown>
  );
};

export default AccountMenu;
