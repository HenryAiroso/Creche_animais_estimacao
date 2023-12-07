package Controller;

import Bean.FuncionariosBean;
import Model.FuncionariosModel;
import opcoes.Opcoes;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;

import java.util.Scanner;

import static org.neo4j.driver.Values.parameters;


public class FuncionariosController {

    public void createFuncionarios(Driver driver) {
        try (Session session = driver.session()) {
            Scanner input = new Scanner(System.in);
            System.out.println("Insira os seguintes dados para cadastrar um novo Funcionário:");

            System.out.print("Nome: ");
            String nome_funcionario = input.nextLine();
            System.out.print("Sobrenome: ");
            String sobrenome_funcionario = input.nextLine();
            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
            String dataNascimentoStr = input.nextLine();
            System.out.print("CPF: ");
            String cpf_funcionario = (input.nextLine());
            System.out.print("Telefone: ");
            String telefone = input.nextLine();
            System.out.print("E-mail: ");
            String email = input.nextLine();
            System.out.print("Data de Contratação (dd/MM/yyyy): ");
            String dataContratacaoStr = input.nextLine();
            System.out.print("Status: ");
            String status = input.nextLine();
            System.out.print("Observacoes: ");
            String observacoes = input.nextLine();



            // Cria o nó funcionarios
            String cypherQuery = "CREATE (f:Funcionario {nome: $nome, sobrenome: $sobrenome, dataNascimento: $dataNascimento, " +
                    "cpf: $cpf, telefone: $telefone, email: $email, dataContratacao: $dataContratacao, status: $status, " +
                    "observacoes: $observacoes})";

            // Set dos parametros
            session.run(cypherQuery, parameters(
                    "nome", nome_funcionario,
                    "sobrenome", sobrenome_funcionario,
                    "dataNascimento", dataNascimentoStr,
                    "cpf", cpf_funcionario,
                    "telefone", telefone,
                    "email", email,
                    "dataContratacao", dataContratacaoStr,
                    "status", status,
                    "observacoes", observacoes
            ));

            System.out.println("Funcionário criado com sucesso!");
        }
    }

    public void listarFuncionarios(Driver driver) {
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
                System.out.println("Matricula: " + funcionarioNode.id() + ", " + funcionario);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFuncionarios(Driver driver) {
        try (Session session = driver.session()) {
            Scanner input = new Scanner(System.in);
            System.out.println("Lista de Funcionarios Disponíveis:");
            listarFuncionarios(driver);
            System.out.print("Digite a Matricula do Funcionário que deseja atualizar: ");
            int matricula = Integer.parseInt(input.nextLine());

            FuncionariosBean existingFuncionario = FuncionariosModel.findById(matricula, driver);

            if (existingFuncionario == null) {
                System.out.println("Funcionário não encontrado.");
                Opcoes.showMenu(driver);
                return;
            }

            System.out.println("Parâmetros atuais: " + existingFuncionario);

            System.out.print("Novo Nome: ");
            existingFuncionario.setNomeFuncionario(input.nextLine());
            System.out.print("Novo Sobrenome: ");
            existingFuncionario.setSobrenomeFuncionario(input.nextLine());
            System.out.print("Nova Data de Nascimento (dd/MM/yyyy): ");
            existingFuncionario.setDataNascimento(input.nextLine());
            // Repita o processo para os outros campos que deseja atualizar
            System.out.print("Novo CPF: ");
            existingFuncionario.setCpfFuncionario(input.nextLine());
            System.out.print("Novo Telefone: ");
            existingFuncionario.setTelefone(input.nextLine());
            System.out.print("Novo E-mail: ");
            existingFuncionario.setEmail(input.nextLine());
            System.out.print("Nova Data de Contratação (dd/MM/yyyy): ");
            existingFuncionario.setDataContratacao(input.nextLine());
            System.out.print("Novo Status: ");
            existingFuncionario.setStatus(input.nextLine());
            System.out.print("Novas Observações: ");
            existingFuncionario.setObservacoes(input.nextLine());

            // Cypher query to update Funcionario node
            String cypherQuery = "MATCH (f:Funcionario) WHERE id(f) = $matricula " +
                    "SET f.nome = $nome, f.sobrenome = $sobrenome, f.dataNascimento = $dataNascimento, " +
                    "f.cpf = $cpf, f.telefone = $telefone, f.email = $email, f.dataContratacao = $dataContratacao, " +
                    "f.status = $status, f.observacoes = $observacoes";

            // Set parameters
            session.run(cypherQuery, parameters(
                    "matricula", matricula,
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

            System.out.println("Funcionário atualizado com sucesso!");
            Opcoes.showMenu(driver);

        }
    }

    public void deleteFuncionarios(Driver driver) {
        listarFuncionarios(driver);
        try (Session session = driver.session()) {
            Scanner input = new Scanner(System.in);
            System.out.print("Digite a Matricula do Funcionário que deseja excluir: ");
            int matricula = Integer.parseInt(input.nextLine());

            FuncionariosBean existingFuncionario = FuncionariosModel.findById(matricula, driver);

            if (existingFuncionario == null) {
                System.out.println("Funcionário não encontrado.");
                return;
            }

            System.out.print("Deseja excluir o Funcionario? (S/N): ");
            String choice = input.next();

            if (choice.equalsIgnoreCase("S")) {
                // Cypher query para deletar Funcionario node
                String cypherQuery = "MATCH (f:Funcionario) WHERE id(f) = $matricula DELETE f";
                session.run(cypherQuery, parameters("matricula", matricula));
                System.out.println("Funcionario excluído com sucesso!");
                Opcoes.showMenu(driver);
            }
            System.out.println("Exclusão cancelada, voltar ao Menu!");
            Opcoes.showMenu(driver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
