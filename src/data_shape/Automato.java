package data_shape;
import util.AutomatoUtil;
import util.MessageUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Automato {
    public List<Estado> estados = new ArrayList<>(); //Q
    public List<Transicao> transicoes = new ArrayList<>();
    public List<String> inputs_possiveis = new ArrayList<>();
    public List<Estado> estados_iniciais = new ArrayList<>();
    public List<Estado> estados_de_aceitacao = new ArrayList<>(); //Q

    public Automato(){ }

    public Automato(Automato toCopy){
        this.estados = toCopy.estados.stream().map(Estado::new).collect(Collectors.toList());
        this.transicoes = toCopy.transicoes.stream().map(
            transicao -> new Transicao(
                this.estados.stream().filter(origem -> Objects.equals(transicao.origem.id, origem.id)).findFirst().get(),
                this.estados.stream().filter(destino -> Objects.equals(transicao.destino.id, destino.id)).findFirst().get(),
                transicao.valor
            )
        ).collect(Collectors.toList());
        this.inputs_possiveis = new ArrayList<>(toCopy.inputs_possiveis);
        toCopy.estados_iniciais.forEach(estado -> this.estados_iniciais.add(new Estado(estado)));
        this.estados_de_aceitacao = toCopy.estados_de_aceitacao.stream().map(Estado::new).collect(Collectors.toList());
    }

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
        System.out.println(estados_iniciais.stream().map(Estado::monta_string_show).collect(Collectors.joining(", ")));
        System.out.println("Estados de aceitação: {" + this.estados_de_aceitacao.size() + "}");
        System.out.println(estados_de_aceitacao.stream().map(Estado::monta_string_show).collect(Collectors.joining(", ")));
    }

    /**
     * Verifica-se se uma lista de caracteres fazem parte da linguagem do automato
     * @param input lista de caracteres
     * @return Boolean: TRUE -> pertence : FALSE -> não pertence
     * */
    public Boolean pertence_a_linguagem(List<String> input){
        if (this.estados_iniciais.isEmpty()) return  Boolean.FALSE;
        return this.estados_iniciais.stream().anyMatch(
            estado -> this.pertence_a_linguagem(input, 0, estado)
        );
    }
    /**
     * Verifica-se se uma lista de caracteres fazem parte da linguagem do automato
     * @param input lista de caracteres
     * @param index index de leitura que se encontra a lista de caracteres
     * @param estado_atual o estado atual que se encontra
     * @return Boolean: TRUE -> pertence : FALSE -> não pertence
     * */
    private Boolean pertence_a_linguagem(List<String> input, Integer index, Estado estado_atual){
        if(index >= input.size()) return estado_atual.de_aceitacao;
        if(!this.inputs_possiveis.contains(input.get(index))) return Boolean.FALSE;
        List<Transicao> transicoes = this.transicoes.stream()
                .filter(transicao -> (Objects.equals(estado_atual.id, transicao.origem.id)) && (Objects.equals(input.get(index), transicao.valor))).collect(Collectors.toList());
        if(transicoes.isEmpty()) return Boolean.FALSE;
        return transicoes.stream().anyMatch(transicao -> this.pertence_a_linguagem(input, (index + 1), transicao.destino));
    }

    /**
     * Verifica se o autômato é determinístico
     * @return Boolean: TRUE -> é determinístico : FALSE -> não é determinístico
     * */
    public Boolean is_deterministico(){
        if((this.estados_iniciais.size() != 1) || this.possui_movimentos_vazios()) return Boolean.FALSE;
        return this.estados.stream().allMatch(
            estado ->
                this.inputs_possiveis.stream().allMatch(
                    input -> {
                        Long cont = this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.id, estado.id) && Objects.equals(transicao.valor, input)).count();
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
            estado ->
                this.inputs_possiveis.stream().allMatch(
                    input -> this.transicoes.stream().filter(transicao -> (transicao.origem.id == estado.id) && (Objects.equals(input, transicao.valor))).count() >= 1
                )
        );
    }

    /**
     * Verifica se o autômato possui estados inacessíveis
     * @return Boolean: TRUE -> possui estados inacessíveis : FALSE -> não possui estados inacessíveis
     * */
    public Boolean possui_estados_inacessiveis(){
        List<Estado> estados_visitados = new ArrayList<>();
        this.estados_iniciais.forEach(
            estadoInicial -> this.visite(estados_visitados, estadoInicial)
        );
        return !estados_visitados.stream().map(item -> item.id).collect(Collectors.toList())
            .containsAll(this.estados.stream().map(item -> item.id).collect(Collectors.toList()));
    }
    private void visite(List<Estado> estados_visitados, Estado estado_atual){
        estados_visitados.add(estado_atual);
        if(this.e_estado_morto(estado_atual)) return;

        this.transicoes.stream()
            .filter(
                transicao -> (transicao.destino.id != estado_atual.id) && (transicao.origem.id == estado_atual.id) && !estados_visitados.stream().map(estado -> estado.id).collect(Collectors.toList()).contains(transicao.destino.id)
            ).map(transicao -> transicao.destino)
            .forEach(estado -> this.visite(estados_visitados, estado));
    }

    /**
     * Verifica se o estado passado é um estado morto
     * @return Boolean: TRUE -> é um estado morto : FALSE -> não é um estado morto
     * */
    private Boolean e_estado_morto(Estado estado) {
        return this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.id, estado.id))
                .allMatch(transicao -> Objects.equals(transicao.destino.id, estado.id));
    }

    /**
     * Função que visa minimizar o autômato. Para isso ele deve:
     *  ser AFD, não pode ter estados inacessíveis e deve ser completo.
     * */
    public void minimiza(){
        //verifica se pode ser ocorrido a minimização
        if(!this.is_completo()) this.completa();
        if(!this.is_deterministico() || this.possui_estados_inacessiveis() || (this.estados.size() == 1)){
            MessageUtil.ERRO_IMPOSSIVEL_MINIMIZAR();
            return;
        }
        this.minimiza_parte_1();
    }

    private void minimiza_parte_1() {
        List<Dupla> duplas = new ArrayList<>();
        //1° passo: colocar como não equivalentes os que são de aceitação e os que não são
        this.monta_duplas(duplas, this);
        this.equivalencia_entre_estados(duplas, this);
        this.minimiza_parte_3(duplas.stream().filter(dupla -> dupla.equivalentes).collect(Collectors.toList()));
        this.minimiza_parte_4();
    }
    /**
     * Realiza o processo de atualização dos campos do autômato, como estado inicial, estados de a ceitação, etc...
     * */
    private void minimiza_parte_4() {
        this.estados_iniciais = Collections.singletonList(this.estados.stream().filter(estado -> estado.inicial).findFirst().get());//atualiza o estado inicial
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
                    AutomatoUtil.GERA_ID_NAO_UTILIZADO(this.estados),
                    String.join("_", dupla.estado_1.nome, dupla.estado_2.nome),
                    (dupla.estado_1.de_aceitacao || dupla.estado_2.de_aceitacao),
                    (dupla.estado_1.inicial || dupla.estado_2.inicial)
                );

                //retira os estados da dupla e adiciona o novo equivalete
                this.estados = this.estados.stream().filter(estado -> (!Objects.equals(estado.id, dupla.estado_1.id) && !Objects.equals(estado.id, dupla.estado_2.id))).collect(Collectors.toList());

                //herdar as transicoes onde os estados da dupla são destino
                this.transicoes.stream().filter(
                    transicao -> (Objects.equals(transicao.destino.id, dupla.estado_1.id) || Objects.equals(transicao.destino.id, dupla.estado_2.id))
                ).forEach(
                    transicao -> transicao.destino = novo_estado
                );

                //herdar as transicoes onde os estados da dupla são origem
                this.transicoes.stream().filter(
                    transicao -> (Objects.equals(transicao.origem.id, dupla.estado_1.id) || Objects.equals(transicao.origem.id, dupla.estado_2.id))
                ).forEach(
                    transicao -> transicao.origem = novo_estado
                );

                this.estados.add(novo_estado);
            }
        );
    }

    /**
     * Função que visa completar as transições que faltam no atomato, levando
     * até um "estado morto"
     * */
    public void completa() {
        Estado estado_morto = new Estado(AutomatoUtil.GERA_ID_NAO_UTILIZADO(this.estados), AutomatoUtil.GERA_NOME_DO_ESTADO_MORTO(this.estados));

        List<Estado> estados_new = new ArrayList<>(this.estados);
        List<Transicao> transicoes_new = new ArrayList<>(this.transicoes);
        estados_new.add(estado_morto);

        this.inputs_possiveis.forEach(
                input -> transicoes_new.add(new Transicao(estado_morto, estado_morto, input))
        );

        this.estados.stream().filter(
                estado -> this.transicoes.stream().filter(transicao -> (Objects.equals(estado.id, transicao.origem.id))).count() < (this.inputs_possiveis.size())
        ).forEach(estado -> {
            List<String> inputs_implementados = this.transicoes.stream().filter(transicao -> Objects.equals(transicao.origem.id, estado.id)).map(transicao -> transicao.valor).collect(Collectors.toList());
            this.inputs_possiveis.stream().filter(
                    input -> !inputs_implementados.contains(input)
            ).forEach(
                    input -> transicoes_new.add(new Transicao(estado, estado_morto, input))
            );
        });
        this.transicoes = transicoes_new;
        this.estados = estados_new;
    }

    public Boolean equivale_a(Automato automato) {
        Automato intemediario = new Automato();
        this.equivalencia_pt1(intemediario, automato);

        List<Dupla> duplas = new ArrayList<>();
        this.monta_duplas(duplas, intemediario);
        this.equivalencia_entre_estados(duplas, intemediario);

        return duplas.stream().anyMatch(item -> item.estado_1.inicial && item.estado_2.inicial && item.equivalentes);
    }

    /**
     * Popula o automato intermediario com os valores dos dois automatos a serem comparados
     * */
    private void equivalencia_pt1(Automato intermediario, Automato automato){
        this.estados.forEach(estado -> {
            Estado est = new Estado(estado);
            est.idElder1 = estado.id;
            est.id = AutomatoUtil.GERA_ID_NAO_UTILIZADO(intermediario.estados);
            intermediario.estados.add(est);
        });
        this.transicoes.forEach(transicao -> {
            Transicao trans = new Transicao(transicao);
            trans.origem = intermediario.estados.stream().filter(estado -> Objects.equals(estado.idElder1, trans.origem.id)).findFirst().get();
            trans.destino = intermediario.estados.stream().filter(estado -> Objects.equals(estado.idElder1, trans.destino.id)).findFirst().get();
            intermediario.transicoes.add(trans);
        });

        intermediario.estados.forEach(estado -> estado.idElder1 = null);

        automato.estados.forEach(estado -> {
            Estado est = new Estado(estado);
            est.idElder1 = estado.id;
            est.id = AutomatoUtil.GERA_ID_NAO_UTILIZADO(intermediario.estados);
            intermediario.estados.add(est);
        });
        automato.transicoes.forEach(transicao -> {
            Transicao trans = new Transicao(transicao);
            trans.origem = intermediario.estados.stream().filter(estado -> Objects.equals(estado.idElder1, trans.origem.id)).findFirst().get();
            trans.destino = intermediario.estados.stream().filter(estado -> Objects.equals(estado.idElder1, trans.destino.id)).findFirst().get();
            intermediario.transicoes.add(trans);
        });

        intermediario.estados.forEach(estado -> estado.idElder1 = null);

        intermediario.inputs_possiveis = this.inputs_possiveis;

//        intermediario.estados_iniciais = this.estados_iniciais;
//
//        intermediario.estados_de_aceitacao.addAll(intermediario.estados.stream().filter(estado -> estado.de_aceitacao).collect(Collectors.toList()));
    }

    /**
     * Realiza a verificação de equivalencia entre os estados
     * */
    private void equivalencia_entre_estados(List<Dupla> duplas, Automato automato){
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
                                    if (dupla.estado_1.inicial && dupla.estado_2.inicial){
                                        int i = 0;
                                    }
                                    duplaInterna.depende_de.clear();
                                    duplaInterna.equivalentes = Boolean.FALSE;
                                }
                            });
                        }else if(dupla.depende_de.size() == 0){
                            dupla.valida_equivalencia(duplas, automato.transicoes, automato.inputs_possiveis);
                        }
                    }
            );
        }
    }

    /**
     * Monta as duplas de um autômato
     * */
    private void monta_duplas(List<Dupla> duplas, Automato automato){
        for (int i = 1; i < automato.estados.size(); i++) {
            for (int j = 0; j < i; j++) {
                duplas.add(new Dupla(automato.estados.get(i), automato.estados.get(j)));
            }
        }
    }

    /**
     * Retorna as duplas de estados que são equivalentes
     * */
    public List<Dupla> calcula_estados_equivalentes(){
        List<Dupla> duplas = new ArrayList<>();
        //monta as duplas de estados dos automatos
        this.monta_duplas(duplas, this);
        //verifica a equivalencia entre eles
        this.equivalencia_entre_estados(duplas, this);
        //retorna as duplas equivalentes
        return duplas.stream().filter(dupla -> dupla.equivalentes).collect(Collectors.toList());
    }

    public Boolean possui_movimentos_vazios(){
        return this.transicoes.stream().anyMatch(item -> Objects.equals(AutomatoUtil.STRING_EMPTY, item.valor));
    }

    /**
     * Esta função altera o próprio objeto, tirando os movimentos vazios
     * */
    public void remove_movimentos_vazios(){
        //nesta parte teremos que montar as tabelas e adicionar as novas transições
        this.inputs_possiveis.forEach(//faz as tabelas (letra por letra)
            input -> this.estados.forEach(//faz a linha das tabelas (estado por estado)
                estado -> this.transicoes.addAll(this.gera_nova_transicao_nao_vazia(input, estado))
            )
        );

        //remove os movimentos
        this.transicoes = this.transicoes.stream().filter(transicao -> !Objects.equals(AutomatoUtil.STRING_EMPTY, transicao.valor)).collect(Collectors.toList());

        //remove o input vazio
        this.inputs_possiveis = this.inputs_possiveis.stream().filter(item -> !Objects.equals(AutomatoUtil.STRING_EMPTY, item)).collect(Collectors.toList());
    }

    /**
     * Gera as transições não vazias do input e estado dado
     * @param input - letra do alfabeto a ser usada
     * @param origem - estado, de onde vai partir a transição
     * */
    private List<Transicao> gera_nova_transicao_nao_vazia(String input, Estado origem) {
        //cria vetor para retornar
        List<Transicao> toReturn = new ArrayList<>();

        //inicia o primeiro fecho (començando com a origem)
        List<Estado> primeiroFecho = new ArrayList<>(Collections.singletonList(origem));
        //busca os estados alcançáveis sem gastar nada a partir do origem
        primeiroFecho.addAll(
            this.transicoes.stream()
                .filter(transicao -> (Objects.equals(transicao.origem.id, origem.id)) && (Objects.equals(AutomatoUtil.STRING_EMPTY, transicao.valor)))
                    .map(transicao -> transicao.destino)
                        .collect(Collectors.toList())
        );
        //retira as repetições
        primeiroFecho = AutomatoUtil.LIST_ESTADOS_SEM_REPETICAO(primeiroFecho);
        //cria vetor com os ids dos estados do fecho
        List<Long> idsPrimeiroFecho = primeiroFecho.stream().map(item -> item.id).collect(Collectors.toList());

        //busca os estados alcançáveis gastando o input do primeiro fecho
        List<Estado> estadosGastando = this.transicoes.stream()
            .filter(transicao -> idsPrimeiroFecho.contains(transicao.origem.id) && (Objects.equals(transicao.valor, input)))
                .map(transicao -> transicao.destino)
                    .collect(Collectors.toList());
        //retira as repetições
        estadosGastando = AutomatoUtil.LIST_ESTADOS_SEM_REPETICAO(estadosGastando);
        List<Long> idsEstadosGastando = estadosGastando.stream().map(item -> item.id).collect(Collectors.toList());

        //busca o segundo fecho à partir dos estados alcançáveis gastando o input sem gastar nada
        List<Estado> segundoFecho = this.transicoes.stream()
            .filter(transicao -> idsEstadosGastando.contains(transicao.origem.id) && (Objects.equals(AutomatoUtil.STRING_EMPTY, transicao.valor)))
                .map(transicao -> transicao.destino)
                    .collect(Collectors.toList());
        //adicionando os estados que gastam
        segundoFecho.addAll(estadosGastando);
        //retira as repetições
        segundoFecho = AutomatoUtil.LIST_ESTADOS_SEM_REPETICAO(segundoFecho);

        //cria as transições
        segundoFecho.forEach(estado -> toReturn.add(new Transicao(origem, estado, input)));
        return toReturn;
    }

    public void to_afd(){
        //se o autonomo possuir mais de um estado inicial, devemos tratar
        if(this.estados_iniciais.size() > 1) this.trata_varios_estados_iniciais();
        //a nova leva de estados começa com o (único) estado incial
        this.estados_iniciais.get(0).idsElders = Collections.singletonList(this.estados_iniciais.get(0).id);

        //os novos estados criados
        List<Estado> novosEstados = new ArrayList<>();
        novosEstados.add(this.estados_iniciais.get(0));
        //as novas transições
        List<Transicao> novasTransicoes = new ArrayList<>();

        //vai percorrendo os novos estados
        for (int i = 0; i < novosEstados.size(); i++) {
            Estado estado = novosEstados.get(i);
            //percorre o alfabeto
            inputs_possiveis.forEach(
                    input -> {
                        //procura os estados que você chega gastando o input (sem repetição)
                        List<Estado> elders = AutomatoUtil.LIST_ESTADOS_SEM_REPETICAO(
                            this.transicoes.stream().filter(
                                transicao -> estado.idsElders.contains(transicao.origem.id) && Objects.equals(input, transicao.valor)
                            ).map(transicao -> transicao.destino).collect(Collectors.toList())
                        );

                        //verifica se há um estado gerado pela a cobinação dos estados anteriores
                        if (AutomatoUtil.POSSUI_ESTADO_GERADO_POR(novosEstados, elders.stream().map(item -> item.id).collect(Collectors.toList()))) {
                            //se houver, apenas o busca e cria e adiciona uma nova transição partino do estado para o buscado
                            novasTransicoes.add(new Transicao(estado, AutomatoUtil.PEGA_ESTADO_GERADO_POR(novosEstados, elders.stream().map(item -> item.id).collect(Collectors.toList())), input));
                        } else {
                            //se não houver, cria o novo estado
                            Estado novoEstado = new Estado(
                                    AutomatoUtil.GERA_ID_NAO_UTILIZADO(novosEstados),
                                    elders.stream().map(item -> item.nome).collect(Collectors.joining("_")),
                                    elders.stream().anyMatch(item -> item.de_aceitacao),
                                    Boolean.FALSE,
                                    elders.stream().map(item -> item.id).collect(Collectors.toList())
                            );
                            //adiciona o novo estado na lista de novos estados
                            novosEstados.add(novoEstado);
                            //cria e adiciona a nova transição
                            novasTransicoes.add(new Transicao(estado, novoEstado, input));
                        }
                    }
            );
        }

        //substitui os dados
        this.estados = novosEstados;
        this.estados_de_aceitacao = this.estados.stream().filter(item -> item.de_aceitacao).collect(Collectors.toList());
        this.transicoes = novasTransicoes;
        //completa o automato
        if(!this.is_completo()) this.completa();
    }

    private void trata_varios_estados_iniciais(){
        //cria um novo estado para ser o único estado inicial
        Estado novoInicio = new Estado(
            AutomatoUtil.GERA_ID_NAO_UTILIZADO(this.estados),
            AutomatoUtil.GERA_NOME_NAO_UTILIZADO(this.estados),
            Boolean.FALSE,
            Boolean.TRUE
        );

        //seta o único estado inicial
        this.estados_iniciais = Collections.singletonList(novoInicio);
        //cria as transições vazias
        this.estados.stream().filter(estado -> estado.inicial).forEach(
            estado -> this.transicoes.add(new Transicao(novoInicio, estado, AutomatoUtil.STRING_EMPTY))
        );
        //seta todos os estados como não iniciais
        this.estados.forEach(estado -> estado.inicial = Boolean.FALSE);
        //adiciona o novo estado inicial
        this.estados.add(novoInicio);
        //remove os estados vazios
        this.remove_movimentos_vazios();
    }
}

