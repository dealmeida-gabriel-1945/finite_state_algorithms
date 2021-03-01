package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.stream.Stream;

public class TesteVerificacaoCompletude01_UT04 {
    public static void main() {
        try{
            Automato automato1 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_01);
            Automato automato2 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_02);
            Automato automato3 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_03);
            Automato automato4 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_04);


            if(Stream.of(automato1, automato2, automato4).allMatch(Automato::is_completo) && !automato3.is_completo()){
                System.out.println(ExemploUtil.VERDE + "TesteMinimizacao_UT04 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteMinimizacao_UT04 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
