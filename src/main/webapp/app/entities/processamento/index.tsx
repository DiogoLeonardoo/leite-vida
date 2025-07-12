import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Processamento from './processamento';
import ProcessamentoDetail from './processamento-detail';
import ProcessamentoUpdate from './processamento-update';
import ProcessamentoDeleteDialog from './processamento-delete-dialog';

const ProcessamentoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Processamento />} />
    <Route path="new" element={<ProcessamentoUpdate />} />
    <Route path=":id">
      <Route index element={<ProcessamentoDetail />} />
      <Route path="edit" element={<ProcessamentoUpdate />} />
      <Route path="delete" element={<ProcessamentoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ProcessamentoRoutes;
