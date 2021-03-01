package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;

public class TesteCadeiaAFN_UT02 {
    public static void main() {
        List<String> cadeia_01 = Arrays.asList("a", "b", "b");
        List<String> cadeia_02 = Arrays.asList("a", "b", "c");
        List<String> cadeia_03 = Arrays.asList("2", "2", "3", "3", "2");
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_04);
            if (
                automato.pertence_a_linguagem(cadeia_01) &&
                !automato.pertence_a_linguagem(cadeia_02) &&
                !automato.pertence_a_linguagem(cadeia_03)
            ){
                System.out.println(ExemploUtil.VERDE + "TesteCadeiaAFN_UT02 => SUCESSO" + ExemploUtil.RESET);
            }else{
                System.out.println(ExemploUtil.VERMELHO + "TesteCadeiaAFN_UT02 => FALHOU" + ExemploUtil.RESET + "\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
