import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Doadora from './doadora';
import Paciente from './paciente';
import Coleta from './coleta';
import Processamento from './processamento';
import Estoque from './estoque';
import Distribuicao from './distribuicao';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="doadora/*" element={<Doadora />} />
        <Route path="paciente/*" element={<Paciente />} />
        <Route path="coleta/*" element={<Coleta />} />
        <Route path="processamento/*" element={<Processamento />} />
        <Route path="estoque/*" element={<Estoque />} />
        <Route path="distribuicao/*" element={<Distribuicao />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
