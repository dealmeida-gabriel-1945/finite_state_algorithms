package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.stream.Stream;

public class TesteVerificacaoAFDN_UT05 {
    public static void main() {
        try{
            Automato automato1 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_01);
            Automato automato2 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_02);
            Automato automato3 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_03);
            Automato automato4 = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_04);


            if(Stream.of(automato1, automato2, automato3).allMatch(Automato::is_deterministico) && !automato4.is_deterministico()){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoAFDN_UT05 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoAFDN_UT05 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
