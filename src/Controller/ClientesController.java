package Controller;

import Bean.ClientesBean;
import opcoes.Opcoes;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashSet;
import java.util.Scanner;

public class ClientesController {
    static void create(ClientesBean m, Driver driver) {
        try (Session session = driver.session()) {
            session.run("CREATE (c:Cliente {cpf: $cpf, nome: $nome, sobrenome: $sobrenome, email: $email, rua: $rua, cidade: $cidade, estado: $estado, pais: $pais, telefone: $telefone})",
                    Values.parameters("cpf", m.getCpf(), "nome", m.getNome(), "sobrenome", m.getSobrenome(),
                            "email", m.getEmail(), "rua", m.getRua(), "cidade", m.getCidade(), "estado", m.getEstado(),
                            "pais", m.getPais(), "telefone", m.getTelefone()));
        }
    }

    static HashSet<ClientesBean> listAll(Driver driver) {
        HashSet<ClientesBean> list = new HashSet<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente) RETURN c.cpf, c.nome, c.sobrenome, c.email, c.rua, c.cidade, c.estado, c.pais, c.telefone");
            while (result.hasNext()) {
                Record record = result.next();
                list.add(new ClientesBean(record.get(0).asInt(), record.get(1).asString(), record.get(2).asString(),
                        record.get(3).asString(), record.get(4).asString(), record.get(5).asString(),
                        record.get(6).asString(), record.get(7).asString(), record.get(8).asString()));
            }
        }
        return list;
    }

    public static ClientesBean findById(int cpf, Driver driver) {
        ClientesBean cb = null;

        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente) WHERE c.cpf = $cpf RETURN c.cpf, c.nome, c.sobrenome, c.email, c.rua, c.cidade, c.estado, c.pais, c.telefone",
                    Values.parameters("cpf", cpf));
            if (result.hasNext()) {
                Record record = result.next();
                cb = new ClientesBean(record.get(0).asInt(), record.get(1).asString(), record.get(2).asString(),
                        record.get(3).asString(), record.get(4).asString(), record.get(5).asString(),
                        record.get(6).asString(), record.get(7).asString(), record.get(8).asString());
            }
        }
        return cb;
    }

    static void update(ClientesBean existingClientes, Driver driver) {
        try (Session session = driver.session()) {
            session.run("MATCH (c:Cliente) WHERE c.cpf = $cpf SET c.nome = $nome, c.sobrenome = $sobrenome, c.email = $email, c.rua = $rua, c.cidade = $cidade, c.estado = $estado, c.pais = $pais, c.telefone = $telefone",
                    Values.parameters("cpf", existingClientes.getCpf(), "nome", existingClientes.getNome(),
                            "sobrenome", existingClientes.getSobrenome(), "email", existingClientes.getEmail(),
                            "rua", existingClientes.getRua(), "cidade", existingClientes.getCidade(),
                            "estado", existingClientes.getEstado(), "pais", existingClientes.getPais(),
                            "telefone", existingClientes.getTelefone()));
        }
    }

    public static void delete(int cpf, Driver driver) {
        try (Session session = driver.session()) {
            session.run("MATCH (c:Cliente) WHERE c.cpf = $cpf DELETE c", Values.parameters("cpf", cpf));
        }
    }

    static HashSet<ClientesBean> listAllWithAnimais(Driver driver) {
        HashSet<ClientesBean> list = new HashSet<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente)-[:POSSUI]->(a:Animal) RETURN c.cpf, c.nome, c.sobrenome, c.email, c.rua, c.cidade, c.estado, c.pais, c.telefone");
            while (result.hasNext()) {
                Record record = result.next();
                list.add(new ClientesBean(record.get(0).asInt(), record.get(1).asString(), record.get(2).asString(),
                        record.get(3).asString(), record.get(4).asString(), record.get(5).asString(),
                        record.get(6).asString(), record.get(7).asString(), record.get(8).asString()));
            }
        }
        return list;
    }

    public static boolean isClientesInAnimais(int cpf, Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente)-[:POSSUI]->(a:Animal) WHERE c.cpf = $cpf RETURN count(a) > 0", Values.parameters("cpf", cpf));
            return result.single().get(0).asBoolean();
        }


    }

    public void createClientes(Driver driver) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Enter the following data to create a new client: ");

            int cpf;
            while (true) {
                System.out.print("CPF: ");
                if (input.hasNextInt()) {
                    cpf = Integer.parseInt(input.nextLine());

                    // Check if the client already exists
                    if (findById(cpf, driver) != null) {
                        System.out.println("Client with CPF " + cpf + " already exists. Cannot create again.");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Invalid input. Please enter a valid CPF.");
                    input.nextLine(); // consume the invalid input
                }
            }

            System.out.print("Nome: ");
            String nome = input.nextLine();
            System.out.print("Sobrenome: ");
            String sobrenome = input.nextLine();
            System.out.print("Email: ");
            String email = input.nextLine();
            System.out.print("Rua: ");
            String rua = input.nextLine();
            System.out.print("Cidade: ");
            String cidade = input.nextLine();
            System.out.print("Estado: ");
            String estado = input.nextLine();
            System.out.print("Pais: ");
            String pais = input.nextLine();
            System.out.print("Telefone: ");
            String telefone = input.nextLine();

            ClientesBean cliente = new ClientesBean(cpf, nome, sobrenome, email, rua, cidade, estado, pais, telefone);

            // Create the client in Neo4j
            create(cliente, driver);
            // Ask the user for the next action
            System.out.print("Criado com sucesso!");

            Opcoes opcoes = new Opcoes();

            opcoes.showMenu(driver);

        }
    }

    public void listarClientes(Driver driver) {
        HashSet<ClientesBean> clientesList = listAll(driver);
        System.out.println("Lista de Clientes:");
        for (ClientesBean cliente : clientesList) {
            System.out.println(cliente);
        }
    }

    public void updateClientes(Driver driver) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Insira o CPF para o cliente que gostaria de atualizar: ");
            int cpf = Integer.parseInt(input.nextLine());

            // Check if the client exists
            ClientesBean existingCliente = findById(cpf, driver);
            if (existingCliente == null) {
                System.out.println("Cliente com  CPF " + cpf + " não encontrado.");
                return;
            }

            System.out.println("Atualize os dados para o cliente com CPF - " + cpf + ":");
            System.out.print("Nome: ");
            String nome = input.nextLine();
            System.out.print("Sobrenome: ");
            String sobrenome = input.nextLine();
            System.out.print("Email: ");
            String email = input.nextLine();
            System.out.print("Rua: ");
            String rua = input.nextLine();
            System.out.print("Cidade: ");
            String cidade = input.nextLine();
            System.out.print("Estado: ");
            String estado = input.nextLine();
            System.out.print("Pais: ");
            String pais = input.nextLine();
            System.out.print("Telefone: ");
            String telefone = input.nextLine();

            // Update the client in Neo4j
            existingCliente.setNome(nome);
            existingCliente.setSobrenome(sobrenome);
            existingCliente.setEmail(email);
            existingCliente.setRua(rua);
            existingCliente.setCidade(cidade);
            existingCliente.setEstado(estado);
            existingCliente.setPais(pais);
            existingCliente.setTelefone(telefone);

            update(existingCliente, driver);
            System.out.println("Client updated successfully!");
            Opcoes opcoes = new Opcoes();

            opcoes.showMenu(driver);

        }
    }

    public void deleteClientes(Driver driver) {

        listarClientes(driver);
        System.out.print("\n");

        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Insira o Cpf do Cliente que gostaria de excluir: ");
            int cpf = input.nextInt();

            // Check if the client exists
            ClientesBean existingCliente = findById(cpf, driver);
            if (existingCliente == null) {
                System.out.println("Cliente com CPF " + cpf + " não encontrado.");
                return;
            }

            // Check if the client has associated animals
            if (isClientesInAnimais(cpf, driver)) {
                System.out.println("Cliente com CPF " + cpf + " possui um animal associado. Por favor, excluir o animal antes de deletar cliente.");
                return;
            }

            Opcoes opcoes = new Opcoes();


            System.out.print("Deseja excluir o Cliente? (S/N): ");
            String choice = input.next();
            if (choice.equalsIgnoreCase("S")) {
                delete(cpf, driver);
                System.out.println("Cadastro Cliente excluído com sucesso!");
                opcoes.showMenu(driver);
            }
            System.out.println("Exclusão cancelada, voltar ao Menu!");
            opcoes.showMenu(driver);
        }
    }


}
