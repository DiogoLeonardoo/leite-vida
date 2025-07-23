import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppSelector } from 'app/config/store';

export const LoginRedirect = () => {
  const navigate = useNavigate();
  const account = useAppSelector(state => state.authentication.account);
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  useEffect(() => {
    if (isAuthenticated && account) {
      if (account.mustChangePassword) {
        navigate('/account/password-change');
      } else {
        navigate('/');
      }
    }
  }, [isAuthenticated, account, navigate]);

  return (
    <div className="d-flex justify-content-center">
      <div className="spinner-border" role="status">
        <span className="visually-hidden">Carregando...</span>
      </div>
    </div>
  );
};

export default LoginRedirect;
