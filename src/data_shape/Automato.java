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
     * Função que visa mostrar os dados do autômato
     * */
    public void show(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Estados: {" + this.estados.size() + "}");
        System.out.println(estados.stream().map(Estado::monta_string_show).collect(Collectors.joining(", ")));
        System.out.println("Transições: {" + this.transicoes.size() + "}");
        System.out.println(transicoes.stream().map(Transicao::monta_string_show).collect(Collectors.joining(", ")));
        System.out.println("Alfabeto: {" + this.inputs_possiveis.size() + "}");
        System.out.println(this.inputs_possiveis);
        System.out.println("Estado inicial:");
        System.out.println(this.estado_inicial.monta_string_show());
        System.out.println("Estados de aceitação: {" + this.estados_de_aceitacao.size() + "}");
        System.out.println(estados_de_aceitacao.stream().map(Estado::monta_string_show).collect(Collectors.joining(", ")));
    }

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
            estado ->
                this.inputs_possiveis.stream().allMatch(
                    input -> {
                        Long cont = this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.nome, estado.nome) && Objects.equals(transicao.valor, input)).count();
                        return (cont == 1) || (cont == 0);
                    })
        );
    }

    /**
     * Verifica se o autômato é completo
     * @return Boolean: TRUE -> é completo : FALSE -> não é completo
     * */
    public Boolean is_completo(){
        return this.estados.stream().allMatch(
            estado -> this.inputs_possiveis.stream().allMatch(input -> this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.nome, estado.nome) && Objects.equals(transicao.valor, input)).count() == 1)
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
        this.minimiza_parte_3(duplas.stream().filter(dupla -> dupla.equivalentes).collect(Collectors.toList()));
        this.minimiza_parte_4();
    }
    /**
     * Realiza o processo de atualização dos campos do autômato, como estado inicial, estados de a ceitação, etc...
     * */
    private void minimiza_parte_4() {
        this.estado_inicial = this.estados.stream().filter(estado -> estado.inicial).findFirst().get();//atualiza o estado inicial
        //atualiza as transicoes
        List<Transicao> trans = new ArrayList<>();
        this.transicoes.forEach(transicao -> {
            if(trans.stream().filter(
                transicao_nova -> Objects.equals(transicao.monta_string_show(), transicao_nova.monta_string_show())
            ).count() == 0){
                trans.add(transicao);
            }
        });
        this.transicoes = trans;
        //atualiza estados de aceitação
        this.estados_de_aceitacao = this.estados.stream().filter(estado -> estado.de_aceitacao).collect(Collectors.toList());
    }

    /**
     * Realiza a mesclagem dos automatos equivalentes
     * */
    private void minimiza_parte_3(List<Dupla> duplas_equivalentes) {
        duplas_equivalentes.forEach(
            dupla -> {
                Estado novo_estado = new Estado(
                    String.join("_", dupla.estado_1.nome, dupla.estado_2.nome),
                    (dupla.estado_1.de_aceitacao || dupla.estado_2.de_aceitacao),
                    (dupla.estado_1.inicial || dupla.estado_2.inicial)
                );

                //retira os estados da dupla e adiciona o novo equivalete
                this.estados = this.estados.stream().filter(estado -> (!Objects.equals(estado.nome, dupla.estado_1.nome) && !Objects.equals(estado.nome, dupla.estado_2.nome))).collect(Collectors.toList());

                //herdar as transicoes onde os estados da dupla são destino
                this.transicoes.stream().filter(
                    transicao -> (Objects.equals(transicao.destino.nome, dupla.estado_1.nome) || Objects.equals(transicao.destino.nome, dupla.estado_2.nome))
                ).forEach(
                    transicao -> transicao.destino = novo_estado
                );

                //herdar as transicoes onde os estados da dupla são origem
                this.transicoes.stream().filter(
                    transicao -> (Objects.equals(transicao.origem.nome, dupla.estado_1.nome) || Objects.equals(transicao.origem.nome, dupla.estado_2.nome))
                ).forEach(
                    transicao -> transicao.origem = novo_estado
                );

                this.estados.add(novo_estado);
            }
        );
    }

    /**
     * Realiza a verificação de equivalencia entre os estados
     * */
    private void minimiza_parte_2(List<Dupla> duplas) {
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

