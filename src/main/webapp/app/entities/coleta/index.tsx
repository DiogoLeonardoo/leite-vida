import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Coleta from './coleta';
import ColetaDetail from './coleta-detail';
import ColetaUpdate from './coleta-update';
import ColetaDeleteDialog from './coleta-delete-dialog';

const ColetaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Coleta />} />
    <Route path="new" element={<ColetaUpdate />} />
    <Route path=":id">
      <Route index element={<ColetaDetail />} />
      <Route path="edit" element={<ColetaUpdate />} />
      <Route path="delete" element={<ColetaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ColetaRoutes;
