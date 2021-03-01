package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

public class TesteVerificacaoEstadosInalcancaveis01_UT11 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_05);
            Automato copia = new Automato(automato);
            copia.completa();
            AutomatoUtil.WRITE_FILE(copia, ExemploUtil.EXEMPLO_05_COMPLETO);

            //a cópia terá estados inacessíveis, visualize no jflap
            if(automato.possui_estados_inacessiveis() && copia.possui_estados_inacessiveis()){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoEstadosInalcancaveis01_UT11 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoEstadosInalcancaveis01_UT11 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
