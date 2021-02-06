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

    /**
     * Verifica-se se uma lista de caracteres fazem parte da linguagem do automato
     * @param input lista de caracteres
     * @param index index de leitura que se encontra a lista de caracteres
     * @param estado_atual o estado atual que se encontra
     * @return Boolean: TRUE -> pertence : FALSE -> não pertence
     * */
    public Boolean pertence_a_linguagem(List<String> input, Integer index, Estado estado_atual){
        if(index >= input.size()) return estado_atual.de_aceitacao;
        if(!this.inputs_possiveis.contains(input.get(index))) return Boolean.FALSE;
        Optional<Transicao> opt_trans = this.transicoes.stream()
                .filter(transicao -> (Objects.equals(estado_atual.nome, transicao.origem.nome)) && (Objects.equals(input.get(index), transicao.valor))).findFirst();
        if(!opt_trans.isPresent()) return Boolean.FALSE;
        return this.pertence_a_linguagem(input, (index + 1), opt_trans.get().destino);
    }

    /**
     * Verifica se o autômato é determinístico
     * @return Boolean: TRUE -> é determinístico : FALSE -> não é determinístico
     * */
    public Boolean is_deterministico(){
        return this.estados.stream().allMatch(
                estado -> this.transicoes.stream().filter(transicao -> (Objects.equals(estado.nome, transicao.origem.nome))).count() == (this.inputs_possiveis.size())
        );
    }

    /**
     * Verifica se o autômato possui estados inacessíveis
     * @return Boolean: TRUE -> possui estados inacessíveis : FALSE -> não possui estados inacessíveis
     * */
    public Boolean possui_estados_inacessiveis(){
        List<Estado> estados_visitados = new ArrayList<>();
        this.visite(estados_visitados, this.estado_inicial);
        return estados_visitados.size() < this.estados.size();
    }
    private void visite(List<Estado> estados_visitados, Estado estado_atual){
        estados_visitados.add(estado_atual);
        if(this.e_estado_morto(estado_atual)) return;

        this.transicoes.stream()
            .filter(transicao -> Objects.equals(transicao.origem.nome, estado_atual.nome) && !estados_visitados.stream().map(estado -> estado.nome).collect(Collectors.toList()).contains(transicao.destino.nome)).map(transicao -> transicao.destino)
                .forEach(estado -> {
                    this.visite(estados_visitados, estado);
                });
    }

    private Boolean e_estado_morto(Estado estado) {
        return this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.nome, estado.nome))
                    .allMatch(transicao -> Objects.equals(transicao.destino.nome, estado.nome));
    }
}
