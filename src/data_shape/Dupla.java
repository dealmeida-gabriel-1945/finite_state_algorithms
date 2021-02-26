package data_shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Dupla {
    public Estado estado_1;
    public Estado estado_2;
    public Boolean equivalentes;
    public List<Dupla> depende_de = new ArrayList<>();

    public Dupla(Estado estado_1, Estado estado_2) {
        this.estado_1 = estado_1;
        this.estado_2 = estado_2;
        this.equivalentes = (!Objects.equals(estado_1.de_aceitacao, estado_2.de_aceitacao)) ? Boolean.FALSE : null;
    }

    public void valida_equivalencia(List<Dupla> duplas, List<Transicao> transicoes, List<String> inputs_possiveis){

        for (String input : inputs_possiveis) {
            Optional<Transicao> opt_estado_destino_1 = transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.id, this.estado_1.id) && Objects.equals(input, transicao.valor)).findFirst();
            Optional<Transicao> opt_estado_destino_2 = transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.id, this.estado_2.id) && Objects.equals(input, transicao.valor)).findFirst();

            if(opt_estado_destino_1.isPresent() && opt_estado_destino_1.isPresent()){
                Estado estado_destino_1 = opt_estado_destino_1.get().destino;
                Estado estado_destino_2 = opt_estado_destino_2.get().destino;
                Optional<Dupla> dupla_12_opt = duplas.stream().filter(
                        dupla -> (Objects.equals(dupla.estado_1.id, estado_destino_1.id) && Objects.equals(dupla.estado_2.id, estado_destino_2.id))
                                || (Objects.equals(dupla.estado_1.id, estado_destino_2.id) && Objects.equals(dupla.estado_2.id, estado_destino_1.id))
                ).findFirst();
                if(dupla_12_opt.isPresent()){//verifica se existe a combiação do destino 1 e 2
                    Dupla dupla_12 = dupla_12_opt.get();
                    if (Objects.nonNull(dupla_12.equivalentes)){
                        if(!dupla_12.equivalentes){
                            this.depende_de = new ArrayList<>();
                            this.equivalentes = Boolean.FALSE;
                            return;
                        }
                    }else{
                        this.depende_de.add(dupla_12);
                    }
                }
            }
        }
        //caso todos as combinações não existirem, quer dizer que
        // os estados fazem a mesma coisa, portanto são equivalentes
        this.equivalentes = Boolean.TRUE;
    }
}
