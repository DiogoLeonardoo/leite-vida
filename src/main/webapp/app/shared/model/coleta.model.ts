import dayjs from 'dayjs';
import { IProcessamento } from 'app/shared/model/processamento.model';
import { IDoadora } from 'app/shared/model/doadora.model';
import { StatusColeta } from 'app/shared/model/enumerations/status-coleta.model';

export interface IColeta {
  id?: number;
  dataColeta?: dayjs.Dayjs;
  volumeMl?: number;
  temperatura?: number;
  localColeta?: string | null;
  observacoes?: string | null;
  statusColeta?: keyof typeof StatusColeta;
  processamento?: IProcessamento | null;
  doadora?: IDoadora;
}

export const defaultValue: Readonly<IColeta> = {};
