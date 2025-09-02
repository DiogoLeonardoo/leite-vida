import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IDoadora, defaultValue } from 'app/shared/model/doadora.model';

const initialState: EntityState<IDoadora> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/doadoras';

// Actions

export const getEntities = createAsyncThunk(
  'doadora/fetch_entity_list',
  async (params: Record<string, any>) => {
    const queryParams = new URLSearchParams();

    if (params.page !== undefined) queryParams.append('page', params.page);
    if (params.size !== undefined) queryParams.append('size', params.size);
    if (params.sort) queryParams.append('sort', params.sort);
    if (params.search && params.search.trim()) {
      queryParams.append('search', params.search.trim());
    }

    Object.entries(params).forEach(([key, value]) => {
      if (!['page', 'size', 'sort', 'search'].includes(key) && value != null) {
        queryParams.append(key, value);
      }
    });

    queryParams.append('cacheBuster', new Date().getTime().toString());

    const requestUrl = `${apiUrl}?${queryParams.toString()}`;
    return axios.get<IDoadora[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'doadora/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IDoadora>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'doadora/create_entity',
  async (entity: IDoadora, thunkAPI) => {
    try {
      const result = await axios.post<IDoadora>(apiUrl, cleanEntity(entity));
      thunkAPI.dispatch(getEntities({}));
      return result;
    } catch (error) {
      console.log('Error in createEntity:', error);

      // Check for CPF already exists error - multiple possible formats
      if (error.response?.status === 400) {
        const errorData = error.response.data;
        const errorMessage = errorData?.detail || errorData?.message || errorData?.title || '';

        // Check various possible error formats for CPF conflict
        if (
          errorData?.title === 'CPF já cadastrado' ||
          errorMessage.includes('CPF já cadastrado') ||
          errorMessage.includes('cpfexists') ||
          errorMessage.includes('ux_doadora__cpf') ||
          errorMessage.includes('duplicar valor da chave') ||
          errorMessage.includes('Chave (cpf)') ||
          errorMessage.includes('já existe')
        ) {
          return thunkAPI.rejectWithValue('CPF já existe no sistema');
        }
      }

      // For other errors, use the default serialization
      return thunkAPI.rejectWithValue(error.response?.data?.detail || error.response?.data?.message || 'Erro interno do servidor');
    }
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'doadora/update_entity',
  async (entity: IDoadora, thunkAPI) => {
    const result = await axios.put<IDoadora>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'doadora/partial_update_entity',
  async (entity: IDoadora, thunkAPI) => {
    const result = await axios.patch<IDoadora>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'doadora/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IDoadora>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const DoadoraSlice = createEntitySlice({
  name: 'doadora',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = false;
        state.errorMessage = (action.payload as string) || action.error?.message || 'Erro desconhecido';
      });
  },
});

export const { reset } = DoadoraSlice.actions;

// Reducer
export default DoadoraSlice.reducer;
