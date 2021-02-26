import data_shape.Automato;
import data_shape.Estado;
import data_shape.Transicao;
import util.AutomatoUtil;
import util.MenuUtil;
import util.MessageUtil;

import javax.rmi.CORBA.Util;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static List<Automato> automatos = new ArrayList<>();
    public static Scanner ler = new Scanner(System.in);

    public static void main(String[] args) {
        gerenciarAutomatos();
    }
/*============menus=============*/
    public static void menu(){
        System.out.println("====================MENU======================");
        System.out.println("\t0. Manipular automatos salvos;");
        System.out.print("\tSua escolha: ");
    }
    public static void menuAutomatos(){
        System.out.println("\n\n====================MENU AUTOMATOS======================");
        System.out.println("\t" + automatos.size() + " automato(s) cadastrados.");
        System.out.println("\t1. Ver automato;");
        System.out.println("\t2. Criar automato;");
        System.out.println("\t3. Copiar automato;");
        System.out.println("\t4. Minimizar automato;");
        System.out.println("\t5. Excluir automato;");
        System.out.println("\t~~~~~~~~~~~obre arquivos...");
        System.out.println("\t6. Salvar em um arquivo;");
        System.out.println("\t5. Sair;");
        System.out.print("\tSua escolha: ");
    }
/*==========^^menus^^===========*/

    public static void gerenciarAutomatos(){
        int escolha = 0;
        while (escolha != 10){
            menuAutomatos();
            escolha = ler.nextInt();
            switch (escolha){
                case 1:
                    mostrarAutomato();
                    break;
                case 2:
                    criarAutomato();
                    break;
                case 3:
                    copiarAutomato();
                    break;
                case 4:
                    minimizarAutomato();
                    break;
                case 5:
                    excluirAutomato();
                    break;
                case 6:
                    salvarAutomatoEmArquivo();
                    break;
                default:
                    MessageUtil.ERRO_ESCOLHA_INVALIDA();
                    break;
            }
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
}
