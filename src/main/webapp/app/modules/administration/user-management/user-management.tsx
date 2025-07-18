import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import {
  Badge,
  Button,
  Table,
  Input,
  InputGroup,
  InputGroupText,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Form,
  FormGroup,
  Label,
} from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faLock, faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import axios from 'axios';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsersAsAdmin, updateUser } from './user-management.reducer';
import './user-management.scss';
import { toast } from 'react-toastify';

export const UserManagement = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [pagination, setPagination] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );
  const [searchTerm, setSearchTerm] = useState('');
  const [debouncedSearchTerm, setDebouncedSearchTerm] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [resetLoading, setResetLoading] = useState(false);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearchTerm(searchTerm);
      setPagination(prev => ({
        ...prev,
        activePage: 1,
      }));
    }, 500);
    return () => clearTimeout(handler);
  }, [searchTerm]);

  const getUsersFromProps = () => {
    dispatch(
      getUsersAsAdmin({
        page: pagination.activePage - 1,
        size: pagination.itemsPerPage,
        sort: `${pagination.sort},${pagination.order}`,
      }),
    );
    const endURL = `?page=${pagination.activePage}&sort=${pagination.sort},${pagination.order}${debouncedSearchTerm ? `&search=${encodeURIComponent(debouncedSearchTerm)}` : ''}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    getUsersFromProps();
  }, [pagination.activePage, pagination.order, pagination.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sortParam = params.get(SORT);
    const search = params.get('search');

    if (page && sortParam) {
      const sortSplit = sortParam.split(',');
      setPagination({
        ...pagination,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }

    if (search && search !== searchTerm) {
      setSearchTerm(search);
    }
  }, [pageLocation.search]);

  const sort = p => () =>
    setPagination({
      ...pagination,
      order: pagination.order === ASC ? DESC : ASC,
      sort: p,
    });

  const handlePagination = currentPage =>
    setPagination({
      ...pagination,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    getUsersFromProps();
  };

  const toggleActive = user => () => {
    dispatch(
      updateUser({
        ...user,
        activated: !user.activated,
      }),
    );
  };

  const handleSearch = event => {
    setSearchTerm(event.target.value);
  };

  const handleResetPassword = user => {
    setSelectedUser(user);
    setModalOpen(true);
  };

  const toggleModal = () => {
    setModalOpen(!modalOpen);
    if (!modalOpen) {
      setSelectedUser(null);
    }
  };

  const handlePasswordSubmit = async e => {
    e.preventDefault();
    if (!selectedUser) return;

    setResetLoading(true);
    try {
      await axios.post(`/api/admin/users/${selectedUser.login}/reset-password`);

      toast.info('Senha resetada com sucesso! Nova senha: leitevida123');
      toggleModal();
      handleSyncList();
    } catch (error) {
      console.error('Erro ao resetar senha:', error);
      if (error.response?.status === 401) {
        toast.info('Erro de autorização. Verifique se você tem permissões de administrador.');
      } else {
        toast.info('Erro ao resetar a senha. Tente novamente.');
      }
    } finally {
      setResetLoading(false);
    }
  };

  const account = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const totalItems = useAppSelector(state => state.userManagement.totalItems);
  const loading = useAppSelector(state => state.userManagement.loading);

  const filteredUsers = users.filter(
    user =>
      !debouncedSearchTerm ||
      user.login?.toLowerCase().includes(debouncedSearchTerm.toLowerCase()) ||
      user.email?.toLowerCase().includes(debouncedSearchTerm.toLowerCase()) ||
      user.firstName?.toLowerCase().includes(debouncedSearchTerm.toLowerCase()) ||
      user.lastName?.toLowerCase().includes(debouncedSearchTerm.toLowerCase()),
  );

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = pagination.sort;
    const order = pagination.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div className="user-management-page">
      <h2 className="user-management-title" data-cy="userManagementPageHeading">
        <Translate contentKey="userManagement.home.title">Users</Translate>
        <div className="d-flex align-items-center gap-3">
          <InputGroup className="search-input">
            <InputGroupText>
              <FontAwesomeIcon icon="search" />
            </InputGroupText>
            <Input type="text" placeholder="Pesquisar usuários..." value={searchTerm} onChange={handleSearch} />
          </InputGroup>
          <Link to="new" className="btn btn-primary jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            <span className="d-none d-md-inline">
              <Translate contentKey="userManagement.home.createLabel">Create a new user</Translate>
            </span>
            <span className="d-md-none">Novo</span>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <Table responsive striped>
          <thead>
            <tr>
              <th>ID</th>
              <th>Login</th>
              <th>Email</th>
              <th />
              <th>Autoridade</th>
              <th>Data da Criação</th>
              <th>Criador</th>
              <th>Data modificação</th>
              <th />
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map((user, i) => (
              <tr id={user.login} key={`user-${i}`}>
                <td>
                  <Button tag={Link} to={user.login} color="link" size="sm">
                    {user.id}
                  </Button>
                </td>
                <td>{user.login}</td>
                <td>{user.email}</td>
                <td>
                  {user.activated ? (
                    <Button color="success" onClick={toggleActive(user)} size="sm">
                      <Translate contentKey="userManagement.activated">Activated</Translate>
                    </Button>
                  ) : (
                    <Button color="danger" onClick={toggleActive(user)} size="sm">
                      <Translate contentKey="userManagement.deactivated">Deactivated</Translate>
                    </Button>
                  )}
                </td>
                <td>
                  {user.authorities
                    ? user.authorities.map((authority, j) => (
                        <div key={`user-auth-${i}-${j}`}>
                          <Badge color="info">{authority}</Badge>
                        </div>
                      ))
                    : null}
                </td>
                <td>
                  {user.createdDate ? <TextFormat value={user.createdDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid /> : null}
                </td>
                <td>{user.lastModifiedBy}</td>
                <td>
                  {user.lastModifiedDate ? (
                    <TextFormat value={user.lastModifiedDate} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                  ) : null}
                </td>
                <td className="text-end">
                  <div className="btn-group flex-btn-group-container">
                    <Button onClick={() => handleResetPassword(user)} color="info" size="sm" title="Resetar Senha">
                      <FontAwesomeIcon icon={faLock} />
                      <span className="d-none d-md-inline"></span>
                    </Button>
                    <Button tag={Link} to={`${user.login}/edit`} color="primary" size="sm">
                      <FontAwesomeIcon icon="pencil-alt" />{' '}
                    </Button>
                    <Button tag={Link} to={`${user.login}/delete`} color="danger" size="sm" disabled={account.login === user.login}>
                      <FontAwesomeIcon icon="trash" />{' '}
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
        {filteredUsers.length === 0 && !loading && (
          <div className="alert alert-warning">
            {debouncedSearchTerm ? 'Nenhum usuário encontrado com os critérios de busca.' : 'Nenhum usuário encontrado.'}
          </div>
        )}
      </div>
      {totalItems ? (
        <div className={users?.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={pagination.activePage} total={totalItems} itemsPerPage={pagination.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={pagination.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={pagination.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}

      <Modal isOpen={modalOpen} toggle={toggleModal} centered>
        <ModalHeader toggle={toggleModal} className="bg-primary text-white">
          <FontAwesomeIcon icon={faLock} className="me-2" />
          Resetar Senha
        </ModalHeader>
        <Form onSubmit={handlePasswordSubmit}>
          <ModalBody>
            <div className="mb-3">
              <strong>Usuário:</strong> {selectedUser?.login} ({selectedUser?.email})
            </div>
            <div className="alert alert-info">
              <strong>Nova senha padrão:</strong> leitevida123
              <br />
              <small>O usuário deverá alterar esta senha no primeiro acesso.</small>
            </div>
          </ModalBody>
          <ModalFooter>
            <Button type="button" color="secondary" onClick={toggleModal} disabled={resetLoading}>
              Cancelar
            </Button>
            <Button type="submit" color="primary" disabled={resetLoading}>
              {resetLoading ? (
                <>
                  <span className="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                  Resetando...
                </>
              ) : (
                <>
                  <FontAwesomeIcon icon={faLock} className="me-1" />
                  Resetar Senha
                </>
              )}
            </Button>
          </ModalFooter>
        </Form>
      </Modal>
    </div>
  );
};

export default UserManagement;
