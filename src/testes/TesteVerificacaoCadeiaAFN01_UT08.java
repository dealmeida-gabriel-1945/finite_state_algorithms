package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TesteVerificacaoCadeiaAFN01_UT08 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_04);
            List<String> cadeia1 = new ArrayList<>();
            List<String> cadeia2 = Arrays.asList("a", "b", "b");
            List<String> cadeia3 = Arrays.asList("b", "a", "b", "b", "a");

            if(
                automato.pertence_a_linguagem(cadeia1) &&
                automato.pertence_a_linguagem(cadeia2) &&
                !automato.pertence_a_linguagem(cadeia3)
            ){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoCadeiaAFN01_UT08 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoCadeiaAFN01_UT08 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
