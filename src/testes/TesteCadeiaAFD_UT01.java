package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;

public class TesteCadeiaAFD_UT01 {
    public static void main() {
        List<String> cadeia_01 = Arrays.asList("1", "2", "3");
        List<String> cadeia_02 = Arrays.asList("2", "1", "3");
        List<String> cadeia_03 = Arrays.asList("2", "2", "3");
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_01);
            if (
                !automato.pertence_a_linguagem(cadeia_01) &&
                automato.pertence_a_linguagem(cadeia_02) &&
                !automato.pertence_a_linguagem(cadeia_03)
            ){
                System.out.println(ExemploUtil.VERDE + "TesteCadeiaAFD_UT01 => SUCESSO" + ExemploUtil.RESET);
            }else{
                System.out.println(ExemploUtil.VERMELHO + "TesteCadeiaAFD_UT01 => FALHOU" + ExemploUtil.RESET + "\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
