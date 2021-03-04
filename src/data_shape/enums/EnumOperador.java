package data_shape.enums;

import data_shape.Automato;
import data_shape.Estado;
import data_shape.strategy.OperadorStrategy;
import util.AutomatoUtil;

import java.util.stream.Collectors;

public enum EnumOperador implements OperadorStrategy {
    UNIAO{
        /**
         * @param automatoA automato para realizar a uniao
         * @param automatoB automato para realizar a uniao
         * @return automato resultante
         * */
        @Override
        public Automato executaOperacao(Automato automatoA, Automato automatoB) {
            Automato resultante = new Automato();
            resultante.inputs_possiveis = automatoA.inputs_possiveis;
            resultante.inputs_possiveis.addAll(automatoB.inputs_possiveis.stream().filter(item -> !resultante.inputs_possiveis.contains(item)).collect(Collectors.toList()));
            this.multiplicaEstados(automatoA, automatoB, resultante);
            resultante.transicoes.addAll(AutomatoUtil.MULTIPLICA_TRANSICOES(automatoA, automatoB, resultante));
            return resultante;
        }

        @Override
        public void multiplicaEstados(Automato automatoA, Automato automatoB, Automato resultante) {
            automatoA.estados.forEach(
                estado1 -> {
                    automatoB.estados.forEach(
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
    INTERCESSAO{
        @Override
        public Automato executaOperacao(Automato automatoA, Automato automatoB) {
            Automato resultante = new Automato();
            resultante.inputs_possiveis = automatoA.inputs_possiveis;
            this.multiplicaEstados(automatoA, automatoB, resultante);
            resultante.transicoes.addAll(AutomatoUtil.MULTIPLICA_TRANSICOES(automatoA, automatoB, resultante));
            return resultante;
        }

        @Override
        public void multiplicaEstados(Automato automatoA, Automato automatoB, Automato resultante) {
            automatoA.estados.forEach(
                estado1 -> {
                    automatoB.estados.forEach(
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
    DIFERENCA{
        @Override
        public Automato executaOperacao(Automato automatoA, Automato automatoB) {
            Automato resultante = new Automato();
            resultante.inputs_possiveis = automatoA.inputs_possiveis;
            this.multiplicaEstados(automatoA, automatoB, resultante);
            resultante.transicoes.addAll(AutomatoUtil.MULTIPLICA_TRANSICOES(automatoA, automatoB, resultante));
            return resultante;
        }

        @Override
        public void multiplicaEstados(Automato automatoA, Automato automatoB, Automato resultante) {
            automatoA.estados.forEach(
                estado1 -> {
                    automatoB.estados.forEach(
                        estado2 -> {
                            Estado est = new Estado(
                                AutomatoUtil.GERA_ID_NAO_UTILIZADO(resultante.estados),
                                String.join("_", estado1.nome, estado2.nome),
                                estado1.de_aceitacao && !estado2.de_aceitacao,
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
    COMPLEMENTO{
        @Override
        public Automato executaOperacao(Automato automatoA, Automato automatoB) {
            Automato resultante = new Automato();
            resultante.inputs_possiveis = automatoA.inputs_possiveis;
            this.multiplicaEstados(automatoA, automatoB, resultante);
            resultante.transicoes.addAll(AutomatoUtil.MULTIPLICA_TRANSICOES(automatoA, automatoB, resultante));
            return resultante;
        }

        @Override
        public void multiplicaEstados(Automato automatoA, Automato automatoB, Automato resultante) {
            automatoA.estados.forEach(
                estado1 -> {
                    automatoB.estados.forEach(
                        estado2 -> {
                            Estado est = new Estado(
                                AutomatoUtil.GERA_ID_NAO_UTILIZADO(resultante.estados),
                                String.join("_", estado1.nome, estado2.nome),
                                !estado1.de_aceitacao,
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
    };
}
