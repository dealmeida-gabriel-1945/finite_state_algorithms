package testes;

import data_shape.Automato;
import data_shape.Estado;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;

public class TesteVerificacaoCadeiaAFN02_UT12 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_05);

            Estado novoInicial = automato.estados.stream().filter(estado -> !estado.inicial).findFirst().get();
            novoInicial.inicial = Boolean.TRUE;
            automato.estados_iniciais.add(novoInicial);

            List<String> cadeia1 = Arrays.asList("a", "a", "a");
            List<String> cadeia2 = Arrays.asList("a");
            List<String> cadeia3 = Arrays.asList("a", "b", "a");

            //a cópia terá estados inacessíveis, visualize no jflap
            if(
                automato.pertence_a_linguagem(cadeia1) &&
                automato.pertence_a_linguagem(cadeia2) &&
                !automato.pertence_a_linguagem(cadeia3)
            ){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoCadeiaAFN02_UT12 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoCadeiaAFN02_UT12 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
