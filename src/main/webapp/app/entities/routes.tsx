import React from 'react';
import { Route, Navigate } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import PrivateRoute from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import { useAppSelector } from 'app/config/store';
import { hasAnyAuthority } from 'app/shared/auth/private-route';

import Doadora from './doadora';
import Paciente from './paciente';
import Coleta from './coleta';
import Processamento from './processamento';
import Estoque from './estoque';
import Distribuicao from './distribuicao';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const EntitiesRoutesWrapper = () => {
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  if (!isAuthenticated) {
    return <Navigate to="/" replace />;
  }

  const isLabUser = hasAnyAuthority(account.authorities, ['ROLE_LAB']);
  const isEnfUser = hasAnyAuthority(account.authorities, ['ROLE_ENF']);
  const isAdmin = hasAnyAuthority(account.authorities, [AUTHORITIES.ADMIN]);

  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="doadora/*" element={
          (isAdmin || isEnfUser) ? <Doadora /> : <Navigate to="/unauthorized" replace />
        } />
        <Route path="paciente/*" element={isAdmin || isEnfUser ? <Paciente /> : <Navigate to="/unauthorized" replace />} />
        <Route path="coleta/*" element={isAdmin || isEnfUser || isLabUser ? <Coleta /> : <Navigate to="/unauthorized" replace />} />
        <Route path="processamento/*" element={isAdmin || isLabUser ? <Processamento /> : <Navigate to="/unauthorized" replace />} />
        <Route path="estoque/*" element={isAdmin || isEnfUser ? <Estoque /> : <Navigate to="/unauthorized" replace />} />
        <Route path="distribuicao/*" element={isAdmin || isEnfUser ? <Distribuicao /> : <Navigate to="/unauthorized" replace />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

export default EntitiesRoutesWrapper;
