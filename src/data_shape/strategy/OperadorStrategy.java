package data_shape.strategy;

import data_shape.Automato;

public interface OperadorStrategy {
    Automato executaOperacao(Automato automatoA, Automato automatoB);
    void multiplicaEstados(Automato automatoA, Automato automatoB, Automato resultante);
}
