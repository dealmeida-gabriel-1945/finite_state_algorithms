package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

public class TesteVerificacaoEstadosInalcancaveis02_UT12 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_01);

            //a cópia terá estados inacessíveis, visualize no jflap
            if(!automato.possui_estados_inacessiveis()){
                System.out.println(ExemploUtil.VERDE + "TesteVerificacaoEstadosInalcancaveis02_UT12 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteVerificacaoEstadosInalcancaveis02_UT12 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
