package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.stream.Stream;

public class TesteVerificacaoCompletude02_UT10 {
    public static void main() {
        try{
            Automato automatoIncompleto = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_03);
            Automato automatoCompleto = new Automato(automatoIncompleto);
            automatoCompleto.completa();

            if(!automatoIncompleto.is_completo() && automatoCompleto.is_completo()){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoCompletude02_UT10 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoCompletude02_UT10 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
