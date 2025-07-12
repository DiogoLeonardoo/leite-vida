import dayjs from 'dayjs';

export interface IPaciente {
  id?: number;
  nome?: string;
  registroHospitalar?: string;
  dataNascimento?: dayjs.Dayjs;
  pesoNascimento?: number | null;
  idadeGestacional?: number | null;
  condicaoClinica?: string;
  nomeResponsavel?: string;
  cpfResponsavel?: string;
  telefoneResponsavel?: string;
  parentescoResponsavel?: string;
  statusAtivo?: boolean;
}

export const defaultValue: Readonly<IPaciente> = {
  statusAtivo: false,
};
