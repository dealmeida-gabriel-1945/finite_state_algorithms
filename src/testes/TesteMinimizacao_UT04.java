package testes;

import data_shape.Automato;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Objects;
import java.util.stream.Collectors;

public class TesteMinimizacao_UT04 {
    public static void main() {
        try{
            Automato automato = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_02);
            Automato copia = new Automato(automato);
            copia.transicoes.clear();
            if(!automato.equivale_a(copia)){
                System.out.println(ExemploUtil.VERDE + "TesteMinimizacao_UT04 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteMinimizacao_UT04 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
