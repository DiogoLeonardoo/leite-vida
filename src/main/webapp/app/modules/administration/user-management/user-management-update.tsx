import React, { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row, Card, CardHeader, CardBody, Container } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isEmail, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { languages, locales } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createUser, getRoles, getUser, reset, updateUser } from './user-management.reducer';
import { maskCPF, removeMask, validateCPF } from 'app/shared/util/validation-utils';
import './user-management-update.scss';
import { toast } from 'react-toastify';

export const UserManagementUpdate = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { login } = useParams<'login'>();
  const isNew = login === undefined;
  const [formData, setFormData] = useState({
    id: '',
    login: '',
    firstName: '',
    lastName: '',
    email: '',
    authorities: '',
  });
  const [cpfValid, setCpfValid] = useState(false);

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getUser(login));
    }
    dispatch(getRoles());
    return () => {
      dispatch(reset());
    };
  }, [login]);

  const handleClose = () => {
    navigate('/admin/user-management');
  };

  const saveUser = async values => {
    // Validate CPF before submission
    if (!validateCPF(formData.login)) {
      toast.error('CPF inválido. Verifique o número digitado.');
      return;
    }

    const finalValues = {
      id: formData.id || values.id,
      login: removeMask(formData.login || values.login),
      firstName: formData.firstName || values.firstName,
      lastName: formData.lastName || values.lastName,
      email: formData.email || values.email,
      authorities: [formData.authorities || values.authorities].filter(Boolean),
      activated: true,
      langKey: 'pt-br',
    };

    try {
      if (isNew) {
        await dispatch(createUser(finalValues)).unwrap();
      } else {
        await dispatch(updateUser(finalValues)).unwrap();
      }
      handleClose();
    } catch (error) {
      if (error?.response?.data?.message === 'error.userexists') {
        toast.error('CPF já cadastrado no sistema.');
      } else if (error?.response?.data?.message === 'error.emailexists') {
        toast.error('Este e-mail já está em uso.');
      } else {
        toast.error('Erro ao salvar o usuário.');
      }
    }
  };

  const handleInputChange = (field, value) => {
    setFormData(prev => ({
      ...prev,
      [field]: value,
    }));

    // Validate CPF when login field changes
    if (field === 'login') {
      setCpfValid(validateCPF(value));
    }
  };

  const isInvalid = false;
  const user = useAppSelector(state => state.userManagement.user);
  const loading = useAppSelector(state => state.userManagement.loading);
  const updating = useAppSelector(state => state.userManagement.updating);
  const authorities = useAppSelector(state => state.userManagement.authorities);

  useEffect(() => {
    if (user) {
      const maskedCPF = maskCPF(user.login || '');
      setFormData({
        id: user.id || '',
        login: maskedCPF,
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        email: user.email || '',
        authorities: user.authorities && user.authorities.length > 0 ? user.authorities[0] : '',
      });
      setCpfValid(validateCPF(maskedCPF));
    } else if (isNew) {
      setFormData({
        id: '',
        login: '',
        firstName: '',
        lastName: '',
        email: '',
        authorities: '',
      });
      setCpfValid(false);
    }
  }, [user, isNew]);

  // Check if form is valid for submission
  const isFormValid = cpfValid && formData.firstName && formData.email && formData.authorities;

  return (
    <div className="user-management-update-page">
      <Container>
        <Row className="justify-content-center">
          <Col md="10" lg="8">
            <h2 className="text-center mb-4">
              <FontAwesomeIcon icon="user-cog" className="me-2" />
              {isNew ? 'Criar Novo Usuário' : 'Editar Usuário'}
            </h2>

            {loading ? (
              <div className="text-center">
                <div className="spinner-border text-primary" role="status">
                  <span className="visually-hidden">Carregando...</span>
                </div>
              </div>
            ) : (
              <Card>
                <CardHeader className="bg-primary">
                  <h5 className="mb-0">
                    <FontAwesomeIcon icon="user-cog" className="me-2" />
                    Informações do Usuário
                  </h5>
                </CardHeader>
                <CardBody>
                  <ValidatedForm onSubmit={saveUser} defaultValues={formData}>
                    <Row>
                      {formData.id ? (
                        <Col md="6">
                          <ValidatedField
                            type="text"
                            name="id"
                            required
                            readOnly
                            label="ID"
                            value={formData.id}
                            validate={{ required: true }}
                          />
                        </Col>
                      ) : null}

                      <Col md={formData.id ? '6' : '12'}>
                        <ValidatedField
                          type="text"
                          name="login"
                          label="CPF"
                          value={formData.login}
                          onChange={e => {
                            const maskedValue = maskCPF(e.target.value);
                            handleInputChange('login', maskedValue);
                          }}
                          validate={{
                            required: {
                              value: true,
                              message: 'CPF é obrigatório',
                            },
                            validate: v => validateCPF(v) || 'CPF inválido',
                          }}
                          placeholder="000.000.000-00"
                          className={cpfValid ? 'is-valid' : formData.login ? 'is-invalid' : ''}
                        />
                        {formData.login && !cpfValid && (
                          <div className="invalid-feedback d-block">CPF inválido. Verifique o número digitado.</div>
                        )}
                        {cpfValid && <div className="valid-feedback d-block">CPF válido</div>}
                      </Col>
                    </Row>

                    <Row>
                      <Col md="6">
                        <ValidatedField
                          type="text"
                          name="firstName"
                          label="Nome"
                          value={formData.firstName}
                          onChange={e => handleInputChange('firstName', e.target.value)}
                          validate={{
                            required: {
                              value: true,
                              message: 'Nome é obrigatório',
                            },
                            maxLength: {
                              value: 50,
                              message: 'Nome deve ter no máximo 50 caracteres',
                            },
                          }}
                        />
                      </Col>

                      <Col md="6">
                        <ValidatedField
                          type="text"
                          name="lastName"
                          label="Sobrenome"
                          value={formData.lastName}
                          onChange={e => handleInputChange('lastName', e.target.value)}
                          validate={{
                            maxLength: {
                              value: 50,
                              message: 'Sobrenome deve ter no máximo 50 caracteres',
                            },
                          }}
                        />
                      </Col>
                    </Row>

                    <Row>
                      <Col md="8">
                        <ValidatedField
                          name="email"
                          label="E-mail"
                          placeholder="Digite o e-mail do usuário"
                          type="email"
                          value={formData.email}
                          onChange={e => handleInputChange('email', e.target.value)}
                          validate={{
                            required: {
                              value: true,
                              message: 'E-mail é obrigatório',
                            },
                            minLength: {
                              value: 5,
                              message: 'E-mail deve ter pelo menos 5 caracteres',
                            },
                            maxLength: {
                              value: 254,
                              message: 'E-mail deve ter no máximo 254 caracteres',
                            },
                            validate: v => isEmail(v) || 'E-mail inválido',
                          }}
                        />
                      </Col>

                      <Col md="4">
                        <ValidatedField
                          type="select"
                          name="authorities"
                          label="Perfil do Usuário"
                          value={formData.authorities}
                          onChange={e => handleInputChange('authorities', e.target.value)}
                          validate={{
                            required: {
                              value: true,
                              message: 'Selecione um perfil para o usuário',
                            },
                          }}
                        >
                          <option value="">Selecione um perfil</option>
                          {authorities
                            .filter(role => role !== 'ROLE_USER')
                            .map(role => (
                              <option value={role} key={role}>
                                {role === 'ROLE_ADMIN'
                                  ? 'Administrador'
                                  : role === 'ROLE_ENF'
                                    ? 'Enfermeira'
                                    : role === 'ROLE_LAB'
                                      ? 'Laboratório'
                                      : role}
                              </option>
                            ))}
                        </ValidatedField>
                      </Col>
                    </Row>

                    <input type="hidden" name="activated" value="true" />
                    <input type="hidden" name="langKey" value="pt-br" />

                    <div className="button-group d-flex justify-content-between">
                      <Button tag={Link} to="/admin/user-management" replace color="info">
                        <FontAwesomeIcon icon="arrow-left" className="me-2" />
                        <Translate contentKey="entity.action.back">Back</Translate>
                      </Button>

                      <Button color="primary" type="submit" disabled={!isFormValid || updating}>
                        {updating ? (
                          <>
                            <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                            Salvando...
                          </>
                        ) : (
                          <>
                            <FontAwesomeIcon icon="save" className="me-2" />
                            <Translate contentKey="entity.action.save">Save</Translate>
                          </>
                        )}
                      </Button>
                    </div>
                  </ValidatedForm>
                </CardBody>
              </Card>
            )}
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default UserManagementUpdate;
