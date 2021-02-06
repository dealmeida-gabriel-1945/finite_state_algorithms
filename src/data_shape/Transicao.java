package data_shape;

public class Transicao {
    public Estado origem = new Estado();
    public Estado destino = new Estado();
    public String valor;

    public Transicao() { }

    public Transicao(Estado origem, Estado destino, String valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }
}
