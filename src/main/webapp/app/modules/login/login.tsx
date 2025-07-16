import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { Translate, ValidatedField } from 'react-jhipster';
import { Alert, Button, Form } from 'reactstrap';
import { type FieldError, useForm } from 'react-hook-form';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { login } from 'app/shared/reducers/authentication';
import './login.scss';

export const Login = () => {
  const dispatch = useAppDispatch();
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);
  const loginError = useAppSelector(state => state.authentication.loginError);
  const pageLocation = useLocation();

  const {
    handleSubmit,
    register,
    formState: { errors, touchedFields },
  } = useForm({ mode: 'onTouched' });

  const formatCPF = (value: string) => {
    const numericValue = value.replace(/\D/g, '').slice(0, 11);
    return numericValue
      .replace(/(\d{3})(\d)/, '$1.$2')
      .replace(/(\d{3})(\d)/, '$1.$2')
      .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
  };

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const onSubmit = ({ username, password, rememberMe }) => {
    const cleanUsername = username.replace(/\D/g, '');
    handleLogin(cleanUsername, password, rememberMe);
  };

  const { from } = pageLocation.state || { from: { pathname: '/home', search: pageLocation.search } };

  if (isAuthenticated) {
    return <Navigate to={from} replace />;
  }

  return (
    <div className="login-page">
      <div className="login-container">
        <div className="logo">
          <img src="content/images/leite-vida.svg" alt="Leite Vida Logo" />
        </div>
        <Form className="login-form" onSubmit={handleSubmit(onSubmit)}>
          {loginError ? (
            <Alert color="danger" data-cy="loginError">
              <Translate contentKey="login.messages.error.authentication">
                <strong>Failed to sign in!</strong> Please check your credentials and try again.
              </Translate>
            </Alert>
          ) : null}
          <div className="form-group">
            <ValidatedField
              name="username"
              placeholder={'CPF'}
              required
              autoFocus
              data-cy="username"
              maxLength={14}
              validate={{
                required: 'CPF não pode ser vazio!',
                pattern: {
                  value: /^\d{3}\.\d{3}\.\d{3}-\d{2}$/,
                  message: 'CPF deve ter 11 dígitos',
                },
              }}
              register={register}
              error={errors.username as FieldError}
              isTouched={touchedFields.username}
              onChange={e => {
                const formatted = formatCPF(e.target.value);
                e.target.value = formatted;
              }}
            />
          </div>
          <div className="form-group">
            <ValidatedField
              name="password"
              type="password"
              placeholder={'Senha'}
              required
              data-cy="password"
              validate={{ required: 'Senha não pode ser vazia!' }}
              register={register}
              error={errors.password as FieldError}
              isTouched={touchedFields.password}
            />
          </div>
          <Button className="btn-login" type="submit" data-cy="submit" block>
            <Translate contentKey="login.form.button">Sign in</Translate>
          </Button>
        </Form>
      </div>
    </div>
  );
};

export default Login;
