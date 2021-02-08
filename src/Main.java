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
        List<String> in_errado1 = Arrays.asList("a", "a", "a", "b", "b");
        List<String> in_errado2 = Arrays.asList("a");
        List<String> in_correto = Arrays.asList("a", "a", "a", "a");

        //adicionando 6 estados
        Estado estado0 = new Estado("q0", Boolean.TRUE, Boolean.TRUE);
        Estado estado1 = new Estado("q1");
        Estado estado2 = new Estado("q2");
        Estado estado3 = new Estado("q3");
        Estado estado4 = new Estado("q4", Boolean.TRUE, Boolean.FALSE);
        Estado estado5 = new Estado("q5", Boolean.TRUE, Boolean.FALSE);

        //adicionando transicoes
        //q0
        Transicao trans1 = new Transicao(estado0, estado1, "b");
        Transicao trans2 = new Transicao(estado0, estado2, "a");
        //q1
        Transicao trans3 = new Transicao(estado1, estado0, "b");
        Transicao trans4 = new Transicao(estado1, estado1, "a");
        //q2
        Transicao trans5 = new Transicao(estado2, estado4, "a");
        Transicao trans6 = new Transicao(estado2, estado5, "b");
        //q3
        Transicao trans7 = new Transicao(estado3, estado4, "b");
        Transicao trans8 = new Transicao(estado3, estado5, "a");
        //q4
        Transicao trans9 = new Transicao(estado4, estado2, "b");
        Transicao trans10 = new Transicao(estado4, estado3, "a");
        //q5
        Transicao trans11 = new Transicao(estado5, estado3, "b");
        Transicao trans12 = new Transicao(estado5, estado2, "a");

        aut.estados = Arrays.asList(estado0, estado1, estado2, estado3, estado4, estado5);
        aut.inputs_possiveis = Arrays.asList("a", "b");
        aut.transicoes = Arrays.asList(trans1, trans2, trans3, trans4, trans5, trans6, trans7, trans8, trans9, trans10, trans11, trans12);
        aut.estado_inicial = estado0;
        aut.estados_de_aceitacao = Arrays.asList(estado4, estado5);

        System.out.println("É deterministico? " + aut.is_deterministico());
        aut.minimiza();
        System.out.println("É deterministico? " + aut.is_deterministico());
        System.out.println(aut.pertence_a_linguagem(in_errado1, 0, aut.estado_inicial));
        System.out.println(aut.pertence_a_linguagem(in_errado2, 0, aut.estado_inicial));
        System.out.println(aut.pertence_a_linguagem(in_correto, 0, aut.estado_inicial));
        System.out.println("Possui estados inacessíveis? " + aut.possui_estados_inacessiveis());
    }
}
