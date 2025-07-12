import dayjs from 'dayjs';
import { TipoDoadora } from 'app/shared/model/enumerations/tipo-doadora.model';
import { LocalPreNatal } from 'app/shared/model/enumerations/local-pre-natal.model';
import { ResultadoExame } from 'app/shared/model/enumerations/resultado-exame.model';

export interface IDoadora {
  id?: number;
  nome?: string;
  cartaoSUS?: string | null;
  cpf?: string;
  dataNascimento?: dayjs.Dayjs;
  cep?: string;
  estado?: string;
  cidade?: string;
  endereco?: string;
  telefone?: string;
  profissao?: string | null;
  tipoDoadora?: keyof typeof TipoDoadora;
  localPreNatal?: keyof typeof LocalPreNatal | null;
  resultadoVDRL?: keyof typeof ResultadoExame | null;
  resultadoHBsAg?: keyof typeof ResultadoExame | null;
  resultadoFTAabs?: keyof typeof ResultadoExame | null;
  resultadoHIV?: keyof typeof ResultadoExame | null;
  transfusaoUltimos5Anos?: boolean | null;
  dataRegistro?: dayjs.Dayjs;
}

export const defaultValue: Readonly<IDoadora> = {
  transfusaoUltimos5Anos: false,
};
