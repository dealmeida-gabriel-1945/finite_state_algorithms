package data_shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Automato {
    public List<Estado> estados = new ArrayList<>(); //Q
    public List<Transicao> transicoes = new ArrayList<>();
    public List<String> inputs_possiveis = new ArrayList<>();
    public Estado estado_inicial = new Estado();
    public List<Estado> estados_de_aceitacao = new ArrayList<>(); //Q

    public Boolean pertence_a_linguagem(List<String> input, Integer index, Estado estado_atual){
        if(index >= input.size()) return estado_atual.de_aceitacao;
        if(!this.inputs_possiveis.contains(input.get(index))) return Boolean.FALSE;
        Optional<Transicao> opt_trans = this.transicoes.stream()
                .filter(transicao -> (Objects.equals(estado_atual.nome, transicao.origem.nome)) && (Objects.equals(input.get(index), transicao.valor))).findFirst();
        if(!opt_trans.isPresent()) return Boolean.FALSE;
        return this.pertence_a_linguagem(input, (index + 1), opt_trans.get().destino);
    }

    public Boolean is_deterministico(){
        return this.estados.stream().allMatch(
                estado -> this.transicoes.stream().filter(transicao -> (Objects.equals(estado.nome, transicao.origem.nome))).count() == (this.inputs_possiveis.size())
        );
    }
}
