import data_shape.Automato;
import data_shape.Estado;
import data_shape.Transicao;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Automato aut = new Automato();
        List<String> in_errado1 = Arrays.asList("1", "2", "2", "2", "1");
        List<String> in_errado2 = Arrays.asList("2", "2", "2", "1", "1", "2");
        List<String> in_correto = Arrays.asList("2", "2", "2", "1", "1");

        //adicionando 3 estados
        Estado estado1 = new Estado("q1", Boolean.FALSE, Boolean.TRUE);
        Estado estado2 = new Estado("q2");
        Estado estado3 = new Estado("3", Boolean.TRUE, Boolean.FALSE);

        //adicionando transicoes
        Transicao trans1 = new Transicao(estado1, estado2, "1");
        Transicao trans2 = new Transicao(estado2, estado2, "1");
        Transicao trans3 = new Transicao(estado2, estado2, "2");
        Transicao trans4 = new Transicao(estado1, estado3, "2");
        Transicao trans5 = new Transicao(estado3, estado1, "2");
        Transicao trans6 = new Transicao(estado3, estado3, "1");

        aut.estados = Arrays.asList(estado1, estado2, estado3);
        aut.transicoes = Arrays.asList(trans1, trans2, trans3, trans4, trans5, trans6);
        aut.estado_inicial = estado1;
        aut.estados_de_aceitacao = Collections.singletonList(estado3);

        System.out.println(aut.submeteInput(in_errado1, 0, aut.estado_inicial));
        System.out.println(aut.submeteInput(in_errado2, 0, aut.estado_inicial));
        System.out.println(aut.submeteInput(in_correto, 0, aut.estado_inicial));
    }
}
