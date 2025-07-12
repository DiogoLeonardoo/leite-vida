import doadora from 'app/entities/doadora/doadora.reducer';
import paciente from 'app/entities/paciente/paciente.reducer';
import coleta from 'app/entities/coleta/coleta.reducer';
import processamento from 'app/entities/processamento/processamento.reducer';
import estoque from 'app/entities/estoque/estoque.reducer';
import distribuicao from 'app/entities/distribuicao/distribuicao.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  doadora,
  paciente,
  coleta,
  processamento,
  estoque,
  distribuicao,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
