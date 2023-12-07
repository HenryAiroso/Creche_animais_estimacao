package Bean;

public class ReservasBean {
    private String id_reserva;
    private String id_animal;
    private String id_plano;
    private String matricula;
    private String data_entrada; // Change the data type to java.util.Date
    private String hora_entrada;
    private String data_hora_saida;
    private String observacoes_reserva;
    private String status_reserva;

    public ReservasBean(String idAnimal, String idPlano, String matricula, String data_entrada, String horaEntrada, String data_hora_saida, String observacoesReserva, String statusReserva) {
    this.id_animal = idAnimal;
    this.id_plano = idPlano;
    this.matricula = matricula;
    this.data_entrada = data_entrada;
    this.hora_entrada = horaEntrada;
    this.data_hora_saida = data_hora_saida;
    this.observacoes_reserva = observacoesReserva;
    this.status_reserva = statusReserva;

    }

    public String getId_reserva() {
        return id_reserva;
    }

    public void setId_reserva(String id_reserva) {
        this.id_reserva = id_reserva;
    }

    public String getId_animal() {
        return id_animal;
    }

    public void setId_animal(String id_animal) {
        this.id_animal = id_animal;
    }

    public String getId_plano() {
        return id_plano;
    }

    public void setId_plano(String id_plano) {
        this.id_plano = id_plano;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getData_dodia() {
        return data_entrada;
    }

    public void setData_dodia(String data_entrada) {
        this.data_entrada = data_entrada;
    }

    public String getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(String hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public String getHora_saida() {
        return data_hora_saida;
    }

    public void setHora_saida(String hora_saida) {
        this.data_hora_saida = hora_saida;
    }

    public String getObservacoes_reserva() {
        return observacoes_reserva;
    }

    public void setObservacoes_reserva(String observacoes_reserva) {
        this.observacoes_reserva = observacoes_reserva;
    }

    public String getStatus_reserva() {
        return status_reserva;
    }

    public void setStatus_reserva(String status_reserva) {
        this.status_reserva = status_reserva;
    }

    public String toString() {
        return "ID Animal: " + id_animal + " Id Plano: " + id_plano + " Matricula Funcionario: " + matricula + " Data entrada: " + data_entrada + " Hora Entrada: " + hora_entrada + " Data/Hora Saida: " + data_hora_saida + " Observacoes Reserva: " + observacoes_reserva + " Status Reserva: " + status_reserva;
    }
}