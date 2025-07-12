import dayjs from 'dayjs';
import { TipoLeite } from 'app/shared/model/enumerations/tipo-leite.model';
import { ClassificacaoLeite } from 'app/shared/model/enumerations/classificacao-leite.model';
import { StatusLote } from 'app/shared/model/enumerations/status-lote.model';

export interface IEstoque {
  id?: number;
  dataProducao?: dayjs.Dayjs;
  dataValidade?: dayjs.Dayjs;
  tipoLeite?: keyof typeof TipoLeite;
  classificacao?: keyof typeof ClassificacaoLeite;
  volumeTotalMl?: number;
  volumeDisponivelMl?: number;
  localArmazenamento?: string;
  temperaturaArmazenamento?: number;
  statusLote?: keyof typeof StatusLote;
}

export const defaultValue: Readonly<IEstoque> = {};
