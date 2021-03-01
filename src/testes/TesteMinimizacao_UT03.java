package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;

public class TesteMinimizacao_UT03 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_02);
            Automato copia = new Automato(automato);
            copia.minimiza();

            if(automato.equivale_a(copia)){
                System.out.println(ExemploUtil.VERDE + "TesteMinimizacao_UT03 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteMinimizacao_UT03 => FALHOU" + ExemploUtil.RESET);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
