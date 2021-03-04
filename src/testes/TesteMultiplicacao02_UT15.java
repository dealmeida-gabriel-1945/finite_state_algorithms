package testes;

import data_shape.Automato;
import data_shape.enums.EnumOperador;
import util.AutomatoUtil;
import util.ExemploUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TesteMultiplicacao02_UT15 {
    public static void main() {
        try{
            //Cadeias para o A
            //Sucesso
            List<String> cadeiaAS01 = Arrays.asList("a");
            List<String> cadeiaAS02 = Arrays.asList("b", "a");
            List<String> cadeiaAS03 = Arrays.asList("a", "a", "a", "b");
            //Falhas
            List<String> cadeiaAF01 = Arrays.asList("b");
            List<String> cadeiaAF02 = Arrays.asList("b", "b");
            List<String> cadeiaAF03 = Arrays.asList("a", "a");

            //Cadeias para o B
            //Sucesso
            List<String> cadeiaBS01 = Arrays.asList("b");
            List<String> cadeiaBS02 = Arrays.asList("a", "b");
            List<String> cadeiaBS03 = Arrays.asList("b", "a", "b");
            //Falhas
            List<String> cadeiaBF01 = Arrays.asList("a");
            List<String> cadeiaBF02 = Arrays.asList("b", "a");
            List<String> cadeiaBF03 = Arrays.asList("a", "a");

            //Atomatos A e B
            Automato automatoA = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_07_A);
            Automato automatoB = AutomatoUtil.READ_FILE(ExemploUtil.EXEMPLO_07_B);
            //Uniao
            Automato intercessaoAB = EnumOperador.INTERCESSAO.executaOperacao(automatoA, automatoB);
            Automato intercessaoBA = EnumOperador.INTERCESSAO.executaOperacao(automatoB, automatoA);

            //Verificação das cadeias
            //A
            Boolean sucessInA = Stream.of(cadeiaAS01, cadeiaAS02, cadeiaAS03).allMatch(automatoA::pertence_a_linguagem);
            Boolean failInA = Stream.of(cadeiaAF01, cadeiaAF02, cadeiaAF03).noneMatch(automatoA::pertence_a_linguagem);
            //B
            Boolean sucessInB = Stream.of(cadeiaBS01, cadeiaBS02).allMatch(automatoB::pertence_a_linguagem);
            Boolean failInB = Stream.of(cadeiaBF01, cadeiaBF02, cadeiaBF03).noneMatch(automatoB::pertence_a_linguagem);

            AutomatoUtil.WRITE_FILE(intercessaoAB, ExemploUtil.EXEMPLO_07_INTERCESSAO);

            //a cópia terá estados inacessíveis, visualize no jflap
            if(sucessInA && sucessInB && failInA && failInB && intercessaoAB.equivale_a(intercessaoBA)){
                System.out.println(ExemploUtil.VERDE + "TesteMultiplicacao02_UT15 => SUCESSO" + ExemploUtil.RESET);
            }else {
                System.out.println(ExemploUtil.VERMELHO + "TesteMultiplicacao02_UT15 => FALHOU" + ExemploUtil.RESET);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
