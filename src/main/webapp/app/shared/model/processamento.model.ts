import dayjs from 'dayjs';
import { IEstoque } from 'app/shared/model/estoque.model';
import { ResultadoAnalise } from 'app/shared/model/enumerations/resultado-analise.model';
import { StatusProcessamento } from 'app/shared/model/enumerations/status-processamento.model';

export interface IProcessamento {
  id?: number;
  dataProcessamento?: dayjs.Dayjs;
  tecnicoResponsavel?: string;
  valorAcidezDornic?: number | null;
  valorCaloricoKcal?: number | null;
  resultadoAnalise?: keyof typeof ResultadoAnalise;
  statusProcessamento?: keyof typeof StatusProcessamento;
  estoque?: IEstoque | null;
}

export const defaultValue: Readonly<IProcessamento> = {};
