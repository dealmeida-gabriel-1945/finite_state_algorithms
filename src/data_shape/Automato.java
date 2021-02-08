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
                estado -> this.inputs_possiveis.stream().allMatch(input -> this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.nome, estado.nome) && Objects.equals(transicao.valor, input)).count() == 1)
        );
    }

    /**
     * Verifica se o autômato é completo
     * @return Boolean: TRUE -> é completo : FALSE -> não é completo
     * */
    public Boolean is_completo(){
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

    /**
     * Verifica se o estado passado é um estado morto
     * @return Boolean: TRUE -> é um estado morto : FALSE -> não é um estado morto
     * */
    private Boolean e_estado_morto(Estado estado) {
        return this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.nome, estado.nome))
                .allMatch(transicao -> Objects.equals(transicao.destino.nome, estado.nome));
    }

    /**
     * Função que visa minimizar o autômato. Para isso ele deve:
     *  ser AFD, não pode ter estados inacessíveis e deve ser completo.
     * */
    public void minimiza(){
        //verifica se pode ser ocorrido a minimização
        if(!this.is_completo()) this.completa();
        if(!this.is_deterministico() || this.possui_estados_inacessiveis()) return;
        this.minimiza_parte_1();
    }

    private void minimiza_parte_1() {
        List<Dupla> duplas = new ArrayList<>();
        //1° passo: colocar como não equivalentes os que são de aceitação e os que não são
        for (int i = 1; i < this.estados.size(); i++) {
            for (int j = 0; j < i; j++) {
                duplas.add(new Dupla(this.estados.get(i), this.estados.get(j)));
            }
        }
        this.minimiza_parte_2(duplas);
        System.out.println("");
    }

    private void minimiza_parte_2(List<Dupla> duplas) {
        Integer index = 0;
        while (!duplas.stream().allMatch(item -> Objects.nonNull(item.equivalentes))){
            duplas.forEach(
                dupla -> {
                    if (Objects.nonNull(dupla.equivalentes)){
                        duplas.stream().filter(
                                duplaInterna -> duplaInterna.depende_de.contains(dupla) && Objects.isNull(duplaInterna.equivalentes)
                        ).forEach(duplaInterna -> {
                            if(dupla.equivalentes){
                                duplaInterna.depende_de = duplaInterna.depende_de.stream().filter(dependencia -> !Objects.equals(dependencia, dupla)).collect(Collectors.toList());
                                if(duplaInterna.depende_de.size() == 0){
                                    duplaInterna.equivalentes = Boolean.TRUE;
                                }
                            }else{
                                duplaInterna.depende_de = new ArrayList<>();
                                duplaInterna.equivalentes = Boolean.FALSE;
                            }
                        });
                    }else if(dupla.depende_de.size() == 0){
                        dupla.valida_equivalencia(duplas, this.transicoes, this.inputs_possiveis);
                    }
                }
            );
        }
    }

    /**
     * Função que visa completar as transições que faltam no atomato, levando
     * até um "estado morto"
     * */
    private void completa() {
        Estado estado_morto = new Estado(this.gera_nome_nao_utilizado());

        List<Estado> estados_new = new ArrayList<>(this.estados);
        List<Transicao> transicoes_new = new ArrayList<>(this.transicoes);
        estados_new.add(estado_morto);

        this.inputs_possiveis.forEach(
                input -> transicoes_new.add(new Transicao(estado_morto, estado_morto, input))
        );

        this.estados.stream().filter(
                estado -> this.transicoes.stream().filter(transicao -> (Objects.equals(estado.nome, transicao.origem.nome))).count() < (this.inputs_possiveis.size())
        ).forEach(estado -> {
            List<String> inputs_implementados = this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.nome, estado.nome)).map(transicao -> transicao.valor).collect(Collectors.toList());
            this.inputs_possiveis.stream().filter(
                    input -> !inputs_implementados.contains(input)
            ).forEach(
                    input -> transicoes_new.add(new Transicao(estado, estado_morto, input))
            );
        });
        this.transicoes = transicoes_new;
        this.estados = estados_new;
    }

    /**
     * Função para gerar um nome de estado que não existe no automato
     * */
    private String gera_nome_nao_utilizado() {
        String nome_inicial = "estado_morto";
        List<String> nomes = this.estados.stream().map(estado -> estado.nome).collect(Collectors.toList());
        while(nomes.contains(nome_inicial)){
            nome_inicial+=".";
        }
        return nome_inicial;
    }
}

