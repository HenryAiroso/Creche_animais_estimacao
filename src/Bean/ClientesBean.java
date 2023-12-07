package Bean;

/**
 * @author Ana Clara
 */
public class ClientesBean {
    private int cpf; // Change variable name to match the property key
    private String nome; // Change variable name to match the property key
    private String sobrenome; // Change variable name to match the property key
    private String email;
    private String rua;
    private String cidade;
    private String estado;
    private String pais;
    private String telefone;

    public ClientesBean(int cpf, String nome, String sobrenome, String email, String rua, String cidade, String estado, String pais, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.rua = rua;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
        this.telefone = telefone;
    }

    public int getCpf() {
        return cpf;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return "CPF: " + cpf + " Nome: " + nome + " Sobrenome: " + sobrenome +
                " Email: " + email + " Rua: " + rua + " Cidade: " + cidade +
                " Estado: " + estado + " Pais: " + pais + " Telefone " + telefone;
    }

}
