import dayjs from 'dayjs';
import { IEstoque } from 'app/shared/model/estoque.model';
import { IPaciente } from 'app/shared/model/paciente.model';

export interface IDistribuicao {
  id?: number;
  dataDistribuicao?: dayjs.Dayjs;
  volumeDistribuidoMl?: number;
  responsavelEntrega?: string;
  responsavelRecebimento?: string;
  observacoes?: string | null;
  estoque?: IEstoque;
  paciente?: IPaciente;
}

export const defaultValue: Readonly<IDistribuicao> = {};
