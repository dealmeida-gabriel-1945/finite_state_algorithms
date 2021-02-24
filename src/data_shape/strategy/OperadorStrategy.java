package data_shape.strategy;

import data_shape.Automato;

public interface OperadorStrategy {
    Automato executaOperacao(Automato automato1, Automato automato2);
    void multiplicaEstados(Automato automato1, Automato automato2, Automato resultante);
}
