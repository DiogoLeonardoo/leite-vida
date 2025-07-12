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

  const handleLogin = (username, password, rememberMe = false) => dispatch(login(username, password, rememberMe));

  const onSubmit = ({ username, password, rememberMe }) => {
    handleLogin(username, password, rememberMe);
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
              placeholder={'Login'}
              required
              autoFocus
              data-cy="username"
              validate={{ required: 'Login não pode ser vazio!' }}
              register={register}
              error={errors.username as FieldError}
              isTouched={touchedFields.username}
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
