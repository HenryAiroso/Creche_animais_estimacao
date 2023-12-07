package Bean;

public class FuncionariosBean {
    private String nome_funcionario;
    private String sobrenome_funcionario;
    private String data_nascimento;
    private String cpf_funcionario;
    private String telefone;
    private String email;
    private String data_contratacao;
    private String status;
    private String observacoes;
    private int matricula;  // This seems to be a unique identifier, you can use it as the node identifier in Neo4j

    public FuncionariosBean(String nome_funcionario, String sobrenome_funcionario, String data_nascimento,
                            String cpf_funcionario, String telefone, String email, String data_contratacao,
                            String status, String observacoes) {
        this.nome_funcionario = nome_funcionario;
        this.sobrenome_funcionario = sobrenome_funcionario;
        this.data_nascimento = data_nascimento;
        this.cpf_funcionario = cpf_funcionario;
        this.telefone = telefone;
        this.email = email;
        this.data_contratacao = data_contratacao;
        this.status = status;
        this.observacoes = observacoes;
    }



    public int getMatriculaFuncionario() {
        return matricula;
    }

    public void setMatriculaFuncionario(int matricula) {
        this.matricula = matricula;
    }

    public String getNomeFuncionario() {
        return nome_funcionario;
    }

    public void setNomeFuncionario(String nome_funcionario) {
        this.nome_funcionario = nome_funcionario;
    }

    public String getSobrenomeFuncionario() {
        return sobrenome_funcionario;
    }

    public void setSobrenomeFuncionario(String sobrenome_funcionario) {
        this.sobrenome_funcionario = sobrenome_funcionario;
    }

    public String getDataNascimento() {
        return data_nascimento;
    }

    public void setDataNascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getCpfFuncionario() {
        return cpf_funcionario;
    }

    public void setCpfFuncionario(String cpf_funcionario) {
        this.cpf_funcionario = cpf_funcionario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataContratacao() {
        return data_contratacao;
    }

    public void setDataContratacao(String data_contratacao) {
        this.data_contratacao = data_contratacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String toString() {
        return "Nome funcionario: " + nome_funcionario + " Sobrenome Funcionario: " +
                sobrenome_funcionario + " Data Nascimento: " + data_nascimento + " CPF: " + cpf_funcionario +
                " Telefone: " + telefone + " Email: " + email + " Data Contratacao: " + data_contratacao +
                " Status: " + status + " Observacoes: " + observacoes;
    }
}
