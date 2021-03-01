package data_shape.enums;

import data_shape.Automato;
import data_shape.Estado;
import data_shape.strategy.OperadorStrategy;
import util.AutomatoUtil;

public enum EnumOperador implements OperadorStrategy {
    UNIAO{
        /**
         * @param automato1 automato para realizar a uniao
         * @param automato2 automato para realizar a uniao
         * @return automato resultante
         * */
        @Override
        public Automato executaOperacao(Automato automato1, Automato automato2) {
            Automato resultante = new Automato();
            resultante.inputs_possiveis = automato1.inputs_possiveis;
            this.multiplicaEstados(automato1, automato2, resultante);
            resultante.transicoes.addAll(AutomatoUtil.MULTIPLICA_TRANSICOES(automato1, automato2, resultante));
            return resultante;
        }

        @Override
        public void multiplicaEstados(Automato automato1, Automato automato2, Automato resultante) {
            automato1.estados.forEach(
                estado1 -> {
                    automato2.estados.forEach(
                        estado2 -> {
                            Estado est = new Estado(
                                AutomatoUtil.GERA_ID_NAO_UTILIZADO(resultante.estados),
                                String.join("_", estado1.nome, estado2.nome),
                                estado1.de_aceitacao || estado2.de_aceitacao,
                                estado1.inicial && estado2.inicial
                            );
                            est.idElder1 = estado1.id;
                            est.idElder2 = estado2.id;
                            resultante.estados.add(est);
                            if(est.inicial) resultante.estados_iniciais.add(est);
                            if(est.de_aceitacao) resultante.estados_de_aceitacao.add(est);
                        }
                    );
                }
            );
        }
    },
    INTERSECCAO{
        @Override
        public Automato executaOperacao(Automato automato1, Automato automato2) {
            Automato resultante = new Automato();
            resultante.inputs_possiveis = automato1.inputs_possiveis;
            this.multiplicaEstados(automato1, automato2, resultante);
            resultante.transicoes.addAll(AutomatoUtil.MULTIPLICA_TRANSICOES(automato1, automato2, resultante));
            return resultante;
        }

        @Override
        public void multiplicaEstados(Automato automato1, Automato automato2, Automato resultante) {
            automato1.estados.forEach(
                estado1 -> {
                    automato2.estados.forEach(
                        estado2 -> {
                            Estado est = new Estado(
                                AutomatoUtil.GERA_ID_NAO_UTILIZADO(resultante.estados),
                                String.join("_", estado1.nome, estado2.nome),
                                estado1.de_aceitacao && estado2.de_aceitacao,
                                estado1.inicial && estado2.inicial
                            );
                            est.idElder1 = estado1.id;
                            est.idElder2 = estado2.id;
                            resultante.estados.add(est);
                            if(est.inicial) resultante.estados_iniciais.add(est);
                            if(est.de_aceitacao) resultante.estados_de_aceitacao.add(est);
                        }
                    );
                }
            );
        }
    },
//TODO: continuar implementação das operações
//
//    DIFERENCA{
//        @Override
//        public Automato executaOperacao(Automato automato1, Automato automato2) {
//            Automato resultante = new Automato();
//            this.multiplicaEstados(automato1, automato2, resultante);
//            return resultante;
//        }
//
//        @Override
//        public void multiplicaEstados(Automato automato1, Automato automato2, Automato resultante) {
//            automato1.estados.forEach(
//                    estado1 -> {
//                        automato2.estados.forEach(
//                                estado2 -> {
//                                    Estado est = new Estado(
//                                            String.join("_", estado1.nome, estado2.nome),
//                                            estado1.de_aceitacao && !estado2.de_aceitacao,
//                                            estado1.inicial && estado2.inicial
//                                    );
//                                    resultante.estados.add(est);
//                                    if(est.inicial) resultante.estado_inicial = est;
//                                    if(est.de_aceitacao) resultante.estados_de_aceitacao.add(est);
//                                }
//                        );
//                    }
//            );
//        }
//    };
//    CONJUNTO_COMPLEMENTAR{
//        @Override
//        public Automato executaOperacao(Automato automato1, Automato automato2) {
//            return null;
//        }
//    };
}
