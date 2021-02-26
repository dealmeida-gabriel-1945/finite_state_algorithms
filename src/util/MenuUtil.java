package util;

import java.util.List;
import java.util.Scanner;

public class MenuUtil {
    public static int MOSTRA_MENU(String titulo, List<String> opcoes, Scanner ler){
        int escolha = 0;
        System.out.println("\n");
        while ((escolha < 1) || (opcoes.size() < escolha)){
            System.out.println(titulo);
            opcoes.forEach(opcao -> System.out.println((opcoes.indexOf(opcao) + 1)  + ".) " + opcao));
            System.out.print("Sua escolha: ");
            escolha = ler.nextInt();
        }
        return escolha;
    }
}
