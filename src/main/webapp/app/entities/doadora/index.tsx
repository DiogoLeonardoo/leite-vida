import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Doadora from './doadora';
import DoadoraDetail from './doadora-detail';
import DoadoraUpdate from './doadora-update';
import DoadoraDeleteDialog from './doadora-delete-dialog';

const DoadoraRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Doadora />} />
    <Route path="new" element={<DoadoraUpdate />} />
    <Route path=":id">
      <Route index element={<DoadoraDetail />} />
      <Route path="edit" element={<DoadoraUpdate />} />
      <Route path="delete" element={<DoadoraDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DoadoraRoutes;
