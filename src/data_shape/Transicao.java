package data_shape;

public class Transicao {
    public Estado origem = new Estado();
    public Estado destino = new Estado();
    public String valor;

    public Transicao() { }

    public Transicao(Transicao transicao) {
        this.origem = new Estado(transicao.origem);
        this.destino = new Estado(transicao.destino);
        this.valor = transicao.valor;
    }

    public Transicao(Estado origem, Estado destino, String valor) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
    }

    public String monta_string_show() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.origem.nome);
        sb.append("->");
        sb.append(this.valor);
        sb.append("->");
        sb.append(this.destino.nome);
        return sb.toString();
    }
}
