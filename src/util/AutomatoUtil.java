package util;

import data_shape.Automato;
import data_shape.Estado;
import data_shape.Transicao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AutomatoUtil {
    public static List<Transicao> MULTIPLICA_TRANSICOES(Automato automato1, Automato automato2, Automato resultante) {
        List<Transicao> resultado = new ArrayList<>();
        automato1.inputs_possiveis.forEach(
            input -> automato1.estados.forEach(
                estado1 -> {
                    automato2.estados.forEach(
                        estado2 ->{
                            Optional<Transicao> opt_trans1 = automato1.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem, estado1) && Objects.equals(transicao.valor, input)).findFirst();
                            Optional<Transicao> opt_trans2 = automato2.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem, estado2) && Objects.equals(transicao.valor, input)).findFirst();
                            if(opt_trans1.isPresent() && opt_trans2.isPresent()){
                                Optional<Estado> opt_estado_origem = resultante.estados.stream().filter(estado -> Objects.equals(estado.nome, String.join("_", opt_trans1.get().origem.nome, opt_trans2.get().origem.nome))).findFirst();
                                Optional<Estado> opt_estado_destino = resultante.estados.stream().filter(estado -> Objects.equals(estado.nome, String.join("_", opt_trans1.get().destino.nome, opt_trans2.get().destino.nome))).findFirst();
                                if(!opt_estado_origem.isPresent() || !opt_estado_destino.isPresent()) return;
                                Transicao trans = new Transicao();
                                trans.valor = input;
                                trans.origem = opt_estado_origem.get();
                                trans.destino = opt_estado_destino.get();
                                resultado.add(trans);
                            }
                        }
                    );
                }
            )
        );
        return resultado;
    }
}
