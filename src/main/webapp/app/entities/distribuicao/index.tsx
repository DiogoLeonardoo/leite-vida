import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Distribuicao from './distribuicao';
import DistribuicaoDetail from './distribuicao-detail';
import DistribuicaoUpdate from './distribuicao-update';
import DistribuicaoDeleteDialog from './distribuicao-delete-dialog';

const DistribuicaoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Distribuicao />} />
    <Route path="new" element={<DistribuicaoUpdate />} />
    <Route path=":id">
      <Route index element={<DistribuicaoDetail />} />
      <Route path="edit" element={<DistribuicaoUpdate />} />
      <Route path="delete" element={<DistribuicaoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DistribuicaoRoutes;
