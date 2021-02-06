package data_shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Automato {
    public List<Estado> estados = new ArrayList<>(); //Q
    public List<Transicao> transicoes = new ArrayList<>();
    public Estado estado_inicial = new Estado();
    public List<Estado> estados_de_aceitacao = new ArrayList<>(); //Q

    public Boolean submeteInput(List<String> input, Integer index, Estado estado_atual){
        if(index >= input.size()) return estado_atual.de_aceitacao;
        Optional<Transicao> opt_trans = this.transicoes.stream()
                .filter(transicao -> (Objects.equals(estado_atual.nome, transicao.origem.nome)) && (Objects.equals(input.get(index), transicao.valor))).findFirst();
        if(!opt_trans.isPresent()) return Boolean.FALSE;
        return this.submeteInput(input, (index + 1), opt_trans.get().destino);
    }
}
