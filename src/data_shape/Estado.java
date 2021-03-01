package data_shape;

public class Estado {
    public Long id;
    public String nome;
    public Boolean de_aceitacao = Boolean.FALSE;
    public Boolean inicial = Boolean.FALSE;

    //Variaveis para recuperação nas multiplicações
    public Long idElder1;
    public Long idElder2;

    public Estado() { }

    public Estado(Estado toCopy) {
        this.id = toCopy.id;
        this.nome = toCopy.nome;
        this.de_aceitacao = toCopy.de_aceitacao;
        this.inicial = toCopy.inicial;
    }

    public Estado(String nome, Boolean de_aceitacao, Boolean inicial) {
        this.nome = nome;
        this.de_aceitacao = de_aceitacao;
        this.inicial = inicial;
    }

    public Estado(Long id, String nome, Boolean de_aceitacao, Boolean inicial) {
        this.id = id;
        this.nome = nome;
        this.de_aceitacao = de_aceitacao;
        this.inicial = inicial;
    }

    public Estado(String nome) {
        this.nome = nome;
    }
    public Estado(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String monta_string_show() {
        StringBuilder sb = new StringBuilder();
        sb.append((this.inicial) ? "->" : "");
        sb.append((this.de_aceitacao) ? "[" : "");
        sb.append(this.nome);

        sb.append("(id: ");
        sb.append(this.id);
        sb.append(")");

        sb.append((this.de_aceitacao) ? "]" : "");
        return sb.toString();
    }
}
