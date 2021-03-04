package testes;

import data_shape.Automato;
import data_shape.enums.EnumOperador;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TesteMultiplicacao03_UT16 {
    public static void main() {
        try{
            //Cadeias para o A
            //Sucesso
            List<String> cadeiaAS01 = Arrays.asList("a", "b");

            //Cadeias para o B
            //Sucesso
            List<String> cadeiaBS01 = Arrays.asList("a", "b");

            //Atomatos A e B
            Automato automatoA = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_07_A);
            Automato automatoB = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_07_B);
            //Uniao
            Automato diferencaAB = EnumOperador.DIFERENCA.executaOperacao(automatoA, automatoB);
            Automato diferencaBA = EnumOperador.DIFERENCA.executaOperacao(automatoB, automatoA);

            //Verificação das cadeias
            //A
            Boolean sucessInA = Stream.of(cadeiaAS01).allMatch(automatoA::pertence_a_linguagem);
            //B
            Boolean sucessInB = Stream.of(cadeiaBS01).allMatch(automatoB::pertence_a_linguagem);

            Boolean failInAB = Stream.of(cadeiaAS01).noneMatch(diferencaAB::pertence_a_linguagem);

            AutomatoUtil.WRITE_FILE(diferencaAB, ExemploUtil.EXEMPLO_07_DIFERENCA);

            //a cópia terá estados inacessíveis, visualize no jflap
            if(sucessInA && sucessInB && !diferencaAB.equivale_a(diferencaBA) && failInAB){
                System.out.println(ExemploUtil.VERDE + "TesteMultiplicacao03_UT16 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteMultiplicacao03_UT16 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
