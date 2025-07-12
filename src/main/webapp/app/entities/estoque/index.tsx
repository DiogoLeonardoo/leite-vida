import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estoque from './estoque';
import EstoqueDetail from './estoque-detail';
import EstoqueUpdate from './estoque-update';
import EstoqueDeleteDialog from './estoque-delete-dialog';

const EstoqueRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Estoque />} />
    <Route path="new" element={<EstoqueUpdate />} />
    <Route path=":id">
      <Route index element={<EstoqueDetail />} />
      <Route path="edit" element={<EstoqueUpdate />} />
      <Route path="delete" element={<EstoqueDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EstoqueRoutes;
