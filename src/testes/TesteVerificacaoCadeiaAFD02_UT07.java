package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;

public class TesteVerificacaoCadeiaAFD02_UT07 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_03);
            List<String> cadeia1 = Arrays.asList("a", "b", "b");
            List<String> cadeia2 = Arrays.asList("b", "a", "a");
            List<String> cadeia3 = Arrays.asList("a", "b", "b", "a", "b", "2");

            if(
                !automato.pertence_a_linguagem(cadeia1) &&
                automato.pertence_a_linguagem(cadeia2) &&
                !automato.pertence_a_linguagem(cadeia3)
            ){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoCadeiaAFD02_UT07 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoCadeiaAFD02_UT07 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
