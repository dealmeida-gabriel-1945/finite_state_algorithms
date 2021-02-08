package data_shape;

public class Estado {
    public String nome;
    public Boolean de_aceitacao = Boolean.FALSE;
    public Boolean inicial = Boolean.FALSE;

    public Estado() { }

    public Estado(String nome, Boolean de_aceitacao, Boolean inicial) {
        this.nome = nome;
        this.de_aceitacao = de_aceitacao;
        this.inicial = inicial;
    }

    public Estado(String nome) {
        this.nome = nome;
    }

    public String monta_string_show() {
        StringBuilder sb = new StringBuilder();
        sb.append((this.inicial) ? "->" : "");
        sb.append((this.de_aceitacao) ? "[" : "");
        sb.append(this.nome);
        sb.append((this.de_aceitacao) ? "]" : "");
        return sb.toString();
    }
}
