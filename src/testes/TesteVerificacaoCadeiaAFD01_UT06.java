package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;

public class TesteVerificacaoCadeiaAFD01_UT06 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_01);
            List<String> cadeia1 = Arrays.asList("1", "2", "3", "3", "3", "2");
            List<String> cadeia2 = Arrays.asList("3", "2", "3", "3", "3", "2");
            List<String> cadeia3 = Arrays.asList("2", "1", "3", "3", "2", "2");

            if(
                !automato.pertence_a_linguagem(cadeia1, 0, automato.estado_inicial) &&
                !automato.pertence_a_linguagem(cadeia2, 0, automato.estado_inicial) &&
                automato.pertence_a_linguagem(cadeia3, 0, automato.estado_inicial)
            ){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoCadeiaAFD01_UT06 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoCadeiaAFD01_UT06 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
