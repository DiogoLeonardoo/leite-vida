import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/doadora">
        <Translate contentKey="global.menu.entities.doadora" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/paciente">
        <Translate contentKey="global.menu.entities.paciente" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/coleta">
        <Translate contentKey="global.menu.entities.coleta" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/processamento">
        <Translate contentKey="global.menu.entities.processamento" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/estoque">
        <Translate contentKey="global.menu.entities.estoque" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/distribuicao">
        <Translate contentKey="global.menu.entities.distribuicao" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
