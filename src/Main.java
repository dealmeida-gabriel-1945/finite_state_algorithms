import data_shape.Automato;
import data_shape.Dupla;
import data_shape.Estado;
import data_shape.Transicao;
import data_shape.enums.EnumOperador;
import util.AutomatoUtil;
import util.MenuUtil;
import util.MessageUtil;

import javax.rmi.CORBA.Util;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static List<Automato> automatos = new ArrayList<>();
    public static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {
        gerenciarAutomatos();
    }
/*============menus=============*/
    public static void menuAutomatos(){
        System.out.println("\n\n====================MENU AUTOMATOS======================");
        System.out.println("\t" + automatos.size() + " automato(s) carregados.");
        System.out.println("\t1. Ver automato carregado;");
        System.out.println("\t2. Salvar automato carregado em um arquivo;");
        System.out.println("\t3. Carregar automato de um arquivo;");
        System.out.println("\t4. Copiar um automato carregado;");
        System.out.println("\t5. Minimizar um automato carregado;");
        System.out.println("\t6. Excluir um automato carregado;");
        System.out.println("\t7. Calcular estados equivalentes de um automato AFD carregado;");
        System.out.println("\t8. Calcular equivalencia entre dois automatos AFD carregados;");
        System.out.println("\t9. Submeter palavra;");
        System.out.println("\t10. Multiplicar automatos e aplicar operações;");
        System.out.println("\t11. Remover Movimentos vazios;");
        System.out.println("\t12. Transformar AFN em AFD;");
        System.out.println("\t13. Sair;");
        System.out.print("\tSua escolha: ");
    }
/*==========^^menus^^===========*/

    public static void gerenciarAutomatos(){
        int escolha = 0;
        while (escolha != 13){
            menuAutomatos();
            escolha = ler.nextInt();
            switch (escolha){
                case 1:
                    mostrarAutomato();
                    break;
                case 2:
                    salvarAutomatoEmArquivo();
                    break;
                case 3:
                    criarAutomato();
                    break;
                case 4:
                    copiarAutomato();
                    break;
                case 5:
                    minimizarAutomato();
                    break;
                case 6:
                    excluirAutomato();
                    break;
                case 7:
                    calculaEstadosEquivalentesAFD();
                    break;
                case 8:
                    calculaEquivalenciaEntreAFD();
                    break;
                case 9:
                    submetePalavra();
                    break;
                case 10:
                    mutiplicaAutomatos();
                    break;
                case 11:
                    retiraMovimentosVazios();
                    break;
                case 12:
                    transformaAfnEmAfd();
                    break;
                case 13:break;
                default:
                    MessageUtil.ERRO_ESCOLHA_INVALIDA();
                    break;
            }
        }
    }

    private static void mutiplicaAutomatos() {
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        System.out.println("Selecione o automato A:");
        int indexA = selecionarAutomato() - 1;
        System.out.println("Selecione o automato B:");
        int indexB = selecionarAutomato() - 1;

        int indexOperador = MenuUtil.MOSTRA_MENU("Selecione a operação:", Arrays.stream(EnumOperador.values()).map(Enum::name).collect(Collectors.toList()), ler) - 1;
        Automato resultado = EnumOperador.values()[indexOperador].executaOperacao(automatos.get(indexA), automatos.get(indexB));
        resultado.show();

        if(MenuUtil.MOSTRA_MENU("Deseja salvar o automato resultante?", Arrays.asList("Sim", "Não"), ler) == 1){
            automatos.add(resultado);
        }
    }

    private static void submetePalavra(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }

        int index = selecionarAutomato() - 1;

        System.out.print("Digite a quantidade de letras: ");
        Long qtd = ler.nextLong();

        List<String> palavra = new ArrayList<>();

        for (Long i = 0L; i < qtd; i++) {
            System.out.print("(" + (i+1) + ") input: ");
            palavra.add(ler.next());
        }
        System.out.println(
            "A palavra " +
            (automatos.get(index).pertence_a_linguagem(palavra) ? "" : " não ") +
            "pertence à linguagem do autômato."
        );
    }

    private static void calculaEquivalenciaEntreAFD(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        if(automatos.stream().noneMatch(Automato::is_deterministico)){
            MessageUtil.ERRO_AUTOMATOS_AFD_INSUFICIENTES();
            return;
        }

        for (int i = 0; i < automatos.size(); i++) {
            System.out.println((i + 1) + ".) " + (automatos.get(i).is_deterministico() ? "AFD" : "AFN"));
        }

        Boolean selected1 = Boolean.FALSE, selected2 = Boolean.FALSE;
        Automato afd1 = new Automato(), afd2 = new Automato();
        //Seleciona AFD 01
        while (!selected1){
            int automatoIndex = selecionarAutomato();
            if(automatos.get(automatoIndex - 1).is_deterministico()){
                selected1 = Boolean.TRUE;
                afd1 = automatos.get(automatoIndex - 1);
            }else{
                MessageUtil.ERRO_AUTOMATO_NAO_E_AFD();
            }
        }
        //Seleciona AFD 02
        while (!selected2){
            int automatoIndex = selecionarAutomato();
            if(automatos.get(automatoIndex - 1).is_deterministico()){
                selected2 = Boolean.TRUE;
                afd2 = automatos.get(automatoIndex - 1);
            }else{
                MessageUtil.ERRO_AUTOMATO_NAO_E_AFD();
            }
        }
        System.out.println((afd1.equivale_a(afd2) ? "São " : "Não são ") + "equivalentes!");
    }

    private static void calculaEstadosEquivalentesAFD(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        if(automatos.stream().noneMatch(Automato::is_deterministico)){
            MessageUtil.ERRO_AUTOMATOS_AFD_INSUFICIENTES();
            return;
        }
        int automatoIndex = selecionarAutomato();
        List<Dupla> duplas = automatos.get(automatoIndex - 1).calcula_estados_equivalentes();
        if (duplas.size() == 0){
            System.out.println("\tNenhum estado equivalente");
        }else{
            duplas.forEach(dupla -> System.out.println(dupla.estado_1.monta_string_show() + " x " + dupla.estado_2.monta_string_show()));
        }
    }

    private static void salvarAutomatoEmArquivo(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        System.out.print("Digite o path do arquivo de saída: ");
        String path = ler.next();
        try {
            AutomatoUtil.WRITE_FILE(automatos.get(selecionarAutomato() - 1), path);
        }catch (Exception e){
            MessageUtil.ERRO_PADRAO();
            e.printStackTrace();
        }
    }
    private static void excluirAutomato(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        int index = selecionarAutomato() - 1;
        if(MenuUtil.MOSTRA_MENU("Tem certeza que deseja excluir este automato?", Arrays.asList("Sim", "Não"), ler) == 1){
            automatos.remove(index);
        }
    }
    private static void minimizarAutomato(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        int index = selecionarAutomato() - 1;
        Automato copy = new Automato(automatos.get(index));
        copy.minimiza();
        copy.show();
        if(MenuUtil.MOSTRA_MENU("Deseja salvar o automato minimizado?", Arrays.asList("Sim", "Não"), ler) == 1){
            if(MenuUtil.MOSTRA_MENU("Deseja sobrescrever o automato original?", Arrays.asList("Sim", "Não"), ler) == 1){
                automatos.set(index, copy);
            }else{
                automatos.add(copy);
            }
        }
    }

    private static void copiarAutomato(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        automatos.add(new Automato(automatos.get(selecionarAutomato() - 1)));
    }

    private static void criarAutomato(){
        System.out.print("Path do arquivo: ");
        String path = ler.next();
        try {
            automatos.add(AutomatoUtil.READ_FILE(path));
        }catch (Exception e){
            MessageUtil.ERRO_PADRAO();
            e.printStackTrace();
        }
    }

    private static int selecionarAutomato() {
        System.out.println("Há " + automatos.size() + " automatos, qual você deseja selecionar? ");
        int toReturn = -1;
        while ((toReturn < 0) || (automatos.size() < toReturn)) {
            System.out.print("Resposta: ");
            toReturn = ler.nextInt();
            if((toReturn < 0) || (automatos.size() < toReturn)) {
                MessageUtil.ERRO_ESCOLHA_INVALIDA();
            }
        }
        return toReturn;
    }

    private static void mostrarAutomato() {
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        int automatoIndex = selecionarAutomato();
        automatos.get(automatoIndex - 1).show();
    }

    private static void retiraMovimentosVazios(){
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        int index = selecionarAutomato() - 1;
        Automato toWork = new Automato(automatos.get(index));
        if(!toWork.possui_movimentos_vazios()) {
            MessageUtil.ERRO_AUTOMATO_NAO_POSSUI_MOVIMENTO_VAZIO();
            return;
        }
        toWork.remove_movimentos_vazios();

        if(MenuUtil.MOSTRA_MENU("Deseja salvar o automato sem os movimentos vazios?", Arrays.asList("Sim", "Não"), ler) == 1){
            if(MenuUtil.MOSTRA_MENU("Deseja sobrescrever o automato sem os movimentos vazios?", Arrays.asList("Sim", "Não"), ler) == 1){
                automatos.set(index, toWork);
            }else{
                automatos.add(toWork);
            }
        }
    }

    private static void transformaAfnEmAfd() {
        if(automatos.size() == 0){
            MessageUtil.ERRO_NENHUM_AUTOMATO();
            return;
        }
        int index = selecionarAutomato() - 1;
        Automato toWork = new Automato(automatos.get(index));
        if(toWork.is_deterministico()) {
            MessageUtil.ERRO_AUTOMATO_NAO_E_AFN();
            return;
        }
        toWork.to_afd();

        if(MenuUtil.MOSTRA_MENU("Deseja salvar o automato tranformado?", Arrays.asList("Sim", "Não"), ler) == 1){
            if(MenuUtil.MOSTRA_MENU("Deseja sobrescrever o automato tranformado?", Arrays.asList("Sim", "Não"), ler) == 1){
                automatos.set(index, toWork);
            }else{
                automatos.add(toWork);
            }
        }
    }
}
