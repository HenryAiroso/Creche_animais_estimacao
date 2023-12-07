package Bean;

public class PlanosBean {
    private String nome_plano;
    private String descricao_plano;
    private int duracao_plano;
    private String tipo_acomodacao;
    private double preco_plano;
    private String restricao_especie;
    private String disponibilidade;
    private String id_plano;

    public PlanosBean(String idPlano, String nome_plano, String descricao_plano, int duracao_plano, String tipo_acomodacao, double preco_plano, String restricao_especie, String disponibilidade) {
        this.id_plano = idPlano;
        this.nome_plano = nome_plano;
        this.descricao_plano = descricao_plano;
        this.duracao_plano = duracao_plano;
        this.tipo_acomodacao = tipo_acomodacao;
        this.preco_plano = preco_plano;
        this.restricao_especie = restricao_especie;
        this.disponibilidade = disponibilidade;
    }

    // Getters and Setters
    public String getId_plano() {
        return id_plano;
    }

    public void setId_plano(String id_plano) {
        this.id_plano = id_plano;
    }


    public String getNome_plano() {
        return nome_plano;
    }

    public void setNome_plano(String nome_plano) {
        this.nome_plano = nome_plano;
    }

    public String getDescricao_plano() {
        return descricao_plano;
    }

    public void setDescricao_plano(String descricao_plano) {
        this.descricao_plano = descricao_plano;
    }

    public int getDuracao_plano() {
        return duracao_plano;
    }

    public void setDuracao_plano(int duracao_plano) {
        this.duracao_plano = duracao_plano;
    }

    public String getTipo_acomodacao() {
        return tipo_acomodacao;
    }

    public void setTipo_acomodacao(String tipo_acomodacao) {
        this.tipo_acomodacao = tipo_acomodacao;
    }

    public double getPreco_plano() {
        return preco_plano;
    }

    public void setPreco_plano(double preco_plano) {
        this.preco_plano = preco_plano;
    }

    public String getRestricao_especie() {
        return restricao_especie;
    }

    public void setRestricao_especie(String restricao_especie) {
        this.restricao_especie = restricao_especie;
    }

    public String getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(String disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    @Override
    public String toString() {
        return "Nome plano: " + nome_plano + " Descricao plano: " + descricao_plano + " Duracao plano: " + duracao_plano +
                " Tipo acomodacao: " + tipo_acomodacao + " Preco plano: " + preco_plano + " Restricao Especie: " + restricao_especie + " Disponibilidade: " + disponibilidade;
    }
}
