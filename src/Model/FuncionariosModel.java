package Model;

import Bean.FuncionariosBean;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;

import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.neo4j.driver.Values.parameters;

public class FuncionariosModel {

    private static final Logger LOGGER = Logger.getLogger(FuncionariosModel.class.getName());

    public static void create(FuncionariosBean funcionario, Driver driver) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (f:Funcionario {nome: $nome, sobrenome: $sobrenome, dataNascimento: $dataNascimento, " +
                        "cpf: $cpf, telefone: $telefone, email: $email, dataContratacao: $dataContratacao, status: $status, " +
                        "observacoes: $observacoes})", parameters(
                        "nome", funcionario.getNomeFuncionario(),
                        "sobrenome", funcionario.getSobrenomeFuncionario(),
                        "dataNascimento", funcionario.getDataNascimento(),
                        "cpf", funcionario.getCpfFuncionario(),
                        "telefone", funcionario.getTelefone(),
                        "email", funcionario.getEmail(),
                        "dataContratacao", funcionario.getDataContratacao(),
                        "status", funcionario.getStatus(),
                        "observacoes", funcionario.getObservacoes()
                ));
                return null;
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating funcionario", e);
        }
        System.out.println("Funcionário criado com sucesso!");
    }

    public static HashSet<FuncionariosBean> listAll(Driver driver) {
        HashSet<FuncionariosBean> list = new HashSet<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (f:Funcionario) RETURN f");
            while (result.hasNext()) {
                Record record = result.next();
                Node funcionarioNode = record.get("f").asNode();
                FuncionariosBean funcionario = new FuncionariosBean(
                        funcionarioNode.get("nome").asString(),
                        funcionarioNode.get("sobrenome").asString(),
                        funcionarioNode.get("dataNascimento").asString(),
                        funcionarioNode.get("cpf").asString(),
                        funcionarioNode.get("telefone").asString(),
                        funcionarioNode.get("email").asString(),
                        funcionarioNode.get("dataContratacao").asString(),
                        funcionarioNode.get("status").asString(),
                        funcionarioNode.get("observacoes").asString()
                );
                list.add(funcionario);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing funcionarios", e);
        }
        return list;
    }

    public static FuncionariosBean findById(int matricula, Driver driver) {
        FuncionariosBean funcionario = null;
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (f:Funcionario) WHERE id(f) = $matricula RETURN f", parameters("matricula", matricula));
            if (result.hasNext()) {
                Record record = result.next();
                Node funcionarioNode = record.get("f").asNode();
                funcionario = new FuncionariosBean(
                        funcionarioNode.get("nome").asString(),
                        funcionarioNode.get("sobrenome").asString(),
                        funcionarioNode.get("dataNascimento").asString(), // Convert the string to LocalDate
                        funcionarioNode.get("cpf").asString(),
                        funcionarioNode.get("telefone").asString(),
                        funcionarioNode.get("email").asString(),
                        funcionarioNode.get("dataContratacao").asString(), // Convert the string to LocalDate
                        funcionarioNode.get("status").asString(),
                        funcionarioNode.get("observacoes").asString()
                );
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding funcionario", e);
        }
        return funcionario;

    }

    public static void update(FuncionariosBean existingFuncionario, Driver driver) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("MATCH (f:Funcionario) WHERE id(f) = $matricula SET f.nome = $nome, f.sobrenome = $sobrenome, " +
                                "f.dataNascimento = $dataNascimento, f.cpf = $cpf, f.telefone = $telefone, f.email = $email, " +
                                "f.dataContratacao = $dataContratacao, f.status = $status, f.observacoes = $observacoes",
                        parameters(
                                "matricula", existingFuncionario.getMatriculaFuncionario(),
                                "nome", existingFuncionario.getNomeFuncionario(),
                                "sobrenome", existingFuncionario.getSobrenomeFuncionario(),
                                "dataNascimento", existingFuncionario.getDataNascimento(),
                                "cpf", existingFuncionario.getCpfFuncionario(),
                                "telefone", existingFuncionario.getTelefone(),
                                "email", existingFuncionario.getEmail(),
                                "dataContratacao", existingFuncionario.getDataContratacao(),
                                "status", existingFuncionario.getStatus(),
                                "observacoes", existingFuncionario.getObservacoes()
                        ));
                return null;
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating funcionario", e);
        }
    }
}