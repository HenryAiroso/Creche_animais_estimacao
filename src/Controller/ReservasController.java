package Controller;

import Bean.ReservasBean;
import opcoes.Opcoes;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.Scanner;

public class ReservasController {
    private boolean animalExists(int id_animal, Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (a:Animal) WHERE id(a) = $id RETURN a";
            Result result = session.run(query, Values.parameters("id", id_animal));

            return result.hasNext();
        }
    }

    private boolean planoExists(int id_plano, Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (p:Plano) WHERE id(p) = $id RETURN p";
            Result result = session.run(query, Values.parameters("id", id_plano));

            return result.hasNext();
        }
    }

    private boolean funcionarioExists(int matricula, Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (f:Funcionario) WHERE id(f) = $matricula RETURN f";
            Result result = session.run(query, Values.parameters("matricula", matricula));

            return result.hasNext();
        }
    }

    public void createReservas(Driver driver) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Insira os seguintes dados para cadastrar uma nova reserva: ");

            System.out.println("Lista de Animais Disponíveis:");
            // Modify the method to list animals from Neo4j
            new AnimaisController().listarAnimais(driver);

            System.out.print("ID do Animal: ");
            String id_animal = input.nextLine();
            if (!animalExists(Integer.parseInt(id_animal), driver)) {
                System.out.println("Animal não encontrado. A reserva não pode ser criada.");
                return;
            }

            System.out.println("Lista de Planos Disponíveis:");
            // Modify the method to list plans from Neo4j
            new PlanosController().listarPlanos(driver);

            System.out.print("ID do Plano: ");
            String id_plano = input.nextLine();

            if (!planoExists(Integer.parseInt(id_plano), driver)) {
                System.out.println("Plano não encontrado. A reserva não pode ser criada.");
                return;
            }

            System.out.println("Lista de Funcionarios Disponíveis:");
            // Modify the method to list employees from Neo4j
            new FuncionariosController().listarFuncionarios(driver);

            System.out.print("Matricula do funcionario: ");
            String matricula = input.nextLine();

            if (!funcionarioExists(Integer.parseInt(matricula), driver)) {
                System.out.println("Funcionario não encontrado. A reserva não pode ser criada.");
                return;
            }

            System.out.print("Data Entrada (xx/xx/xxxx): ");
            String data_entrada = input.nextLine();
            System.out.print("Hora de entrada (xx:xx): ");
            String hora_entrada = input.nextLine();
            System.out.print("Dia e Hora de saída(xx/xx/xxxx xx:xx): ");
            String data_hora_saida = input.nextLine();
            System.out.print("Observações da Reserva: ");
            String observacoes_reserva = input.nextLine();
            System.out.print("Status da Reserva: ");
            String status_reserva = input.nextLine();

            try (Session session = driver.session()) {
                String query = "CREATE (r:Reserva {id_animal: $id_animal, id_plano: $id_plano, matricula: $matricula, " +
                        "data_entrada: $data_entrada, hora_entrada: $hora_entrada, data_hora_saida: $data_hora_saida, " +
                        "observacoes_reserva: $observacoes_reserva, status_reserva: $status_reserva}) " +
                        "RETURN id(r) as idReserva"; // Add RETURN statement to get the node ID
                Result result = session.run(query, Values.parameters(
                        "id_animal", id_animal,
                        "id_plano", id_plano,
                        "matricula", matricula,
                        "data_entrada", data_entrada,
                        "hora_entrada", hora_entrada,
                        "data_hora_saida", data_hora_saida,
                        "observacoes_reserva", observacoes_reserva,
                        "status_reserva", status_reserva
                ));


                if (result.hasNext()) {
                    Record record = result.next();
                    int idReserva = record.get("idReserva").asInt(); // Save the node ID to the variable idReserva
                    System.out.println("Reserva criada com sucesso!! ID da Reserva: " + idReserva);
                    query = "MATCH (r:Reserva) WHERE id(r) = $idReserva SET r.id_animal = $id_animal, r.id_plano = $id_plano, r.matricula = $matricula, r.data_entrada = $data_entrada, r.hora_entrada = $hora_entrada, r.data_hora_saida = $data_hora_saida, r.observacoes_reserva = $observacoes_reserva, r.status_reserva = $status_reserva";
                    session.run(query, Values.parameters(
                            "id_animal", id_animal,
                            "id_plano", id_plano,
                            "matricula", matricula,
                            "data_entrada", data_entrada,
                            "hora_entrada", hora_entrada,
                            "data_hora_saida", data_hora_saida,
                            "observacoes_reserva", observacoes_reserva,
                            "status_reserva", status_reserva,
                            "idReserva", idReserva
                    ));

                    query = "MATCH (f:Funcionario), (r:Reserva) WHERE id(f) = "+matricula+" AND id(r) = "+idReserva+"" +
                            " CREATE (f)-[:trabalha_na_reserva]->(r)";
                    session.run(query, Values.parameters(
                            "matricula", matricula,
                            "idReserva", idReserva
                    ));

                    query = "MATCH (a:Animal), (r:Reserva) WHERE id(a) = "+id_animal+" AND id(r) = "+idReserva +
                            " CREATE (a)-[:Possui_uma_reserva]->(r)";
                    session.run(query, Values.parameters(
                            "id_animal", id_animal,
                            "idReserva", idReserva
                    ));

                    query = "MATCH (p:Plano), (r:Reserva) WHERE id(p) = "+id_plano+" AND id(r) = "+idReserva +
                            " CREATE (p)-[:alocado_a_reserva]->(r)";
                    session.run(query, Values.parameters(
                            "id_plano", id_plano,
                            "idReserva", idReserva
                    ));
                    // Rest of the code...
                } else {
                    System.out.println("Falha ao criar a reserva.");
                }
                System.out.println("Reserva criada com sucesso!!");

            Opcoes.showMenu(driver);
        }
    }}



    public void listarReservas(Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (r:Reserva) RETURN r");
            while (result.hasNext()) {
                Record record = result.next();
                Node reservaNode = record.get("r").asNode();
                ReservasBean reserva = new ReservasBean(
                        reservaNode.get("id_animal").asString(),
                        reservaNode.get("id_plano").asString(),
                        reservaNode.get("matricula").asString(),
                        reservaNode.get("data_entrada").asString(),
                        reservaNode.get("hora_entrada").asString(),
                        reservaNode.get("data_hora_saida").asString(),
                        reservaNode.get("observacoes_reserva").asString(),
                        reservaNode.get("status_reserva").asString()
                );
                System.out.println("ID da Reserva: " + reservaNode.id() + ", " + reserva);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*{ public void updateReservas(Driver driver)
        listarReservas(driver);
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Informe o ID da Reserva que deseja atualizar: ");
            int idReserva = input.nextInt();

            if (!reservaExists(idReserva, driver)) {
                System.out.println("Reserva não encontrada.");
                return;
            }
            System.out.println("Insira os novos dados para atualizar a reserva:");
            // Prompt user for new data (similar to createReservas method)
            System.out.print("ID do Animal: ");
            String id_animal = input.next();
            if (!animalExists(Integer.parseInt(id_animal), driver)) {
                System.out.println("Animal não encontrado. A reserva não pode ser atualizada.");
                return;
            }


            System.out.print("ID do Plano: ");
            String id_plano = input.next();
            if (!planoExists(Integer.parseInt(id_plano), driver)) {
                System.out.println("Plano não encontrado. A reserva não pode ser atualizada.");
                return;
            }

            System.out.print("Matricula do funcionario: ");
            String matricula = input.next();
            if (!funcionarioExists(Integer.parseInt(matricula), driver)) {
                System.out.println("Funcionario não encontrado. A reserva não pode ser atualizada.");
                return;
            }

            System.out.print("Data do dia: ");
            String data_dodia = input.next();

            System.out.print("Hora de entrada: ");
            String hora_entrada = input.next();

            System.out.print("Hora de saída: ");
            String hora_saida = input.next();

            System.out.print("Observações da Reserva: ");
            String observacoes_reserva = input.next();

            System.out.print("Status da Reserva: ");
            String status_reserva = input.next();

            ReservasBean updatedReserva = new ReservasBean(id_animal, id_plano, matricula, data_dodia, hora_entrada, hora_saida, observacoes_reserva, status_reserva);

            updateReservaNode(idReserva, updatedReserva, driver);

            System.out.println("Reserva atualizada com sucesso!");

        }
    }*/

    public void updateReservas(Driver driver) {
        listarReservas(driver);
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Informe o ID da Reserva que deseja atualizar: ");
            int idReserva = Integer.parseInt(input.nextLine());

            if (!reservaExists(idReserva, driver)) {
                System.out.println("Reserva não encontrada.");
                return;
            }
            System.out.println("Insira os novos dados para atualizar a reserva:");
            new AnimaisController().listarAnimais(driver);
            // Prompt user for new data (similar to createReservas method)
            System.out.print("ID do Animal: ");
            String id_animal = input.nextLine();
            if (!animalExists(Integer.parseInt(id_animal), driver)) {
                System.out.println("Animal não encontrado. A reserva não pode ser atualizada.");
                return;
            }
            new PlanosController().listarPlanos(driver);

            System.out.print("ID do Plano: ");
            String id_plano = input.nextLine();
            if (!planoExists(Integer.parseInt(id_plano), driver)) {
                System.out.println("Plano não encontrado. A reserva não pode ser atualizada.");
                return;
            }
            new FuncionariosController().listarFuncionarios(driver);

            System.out.print("Matricula do funcionario: ");
            String matricula = input.nextLine();
            if (!funcionarioExists(Integer.parseInt(matricula), driver)) {
                System.out.println("Funcionario não encontrado. A reserva não pode ser atualizada.");
                return;
            }

            System.out.print("Data Entrada (xx/xx/xxxx): ");
            String data_entrada = input.nextLine();
            System.out.print("Hora de entrada (xx:xx): ");
            String hora_entrada = input.nextLine();
            System.out.print("Dia e Hora de saída(xx/xx/xxxx xx:xx): ");
            String data_hora_saida = input.nextLine();
            System.out.print("Observações da Reserva: ");
            String observacoes_reserva = input.nextLine();
            System.out.print("Status da Reserva: ");
            String status_reserva = input.nextLine();

            try (Session session = driver.session()) {
                String query = "MATCH (r:Reserva) WHERE id(r) = $idReserva " +
                        "SET r.id_animal = $id_animal, r.id_plano = $id_plano, r.matricula = $matricula, " +
                        "r.data_entrada = $data_entrada, r.hora_entrada = $hora_entrada, r.data_hora_saida = $data_hora_saida, " +
                        "r.observacoes_reserva = $observacoes_reserva, r.status_reserva = $status_reserva";
                session.run(query, Values.parameters(
                        "idReserva", idReserva,
                        "id_animal", id_animal,
                        "id_plano", id_plano,
                        "matricula", matricula,
                        "data_entrada", data_entrada,
                        "hora_entrada", hora_entrada,
                        "data_hora_saida", data_hora_saida,
                        "observacoes_reserva", observacoes_reserva,
                        "status_reserva", status_reserva
                ));

                System.out.println("Reserva atualizada com sucesso!!");
            }

            Opcoes.showMenu(driver);
        }
    }

    public void deleteReservas(Driver driver) {
        listarReservas(driver);
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Informe o ID da Reserva que deseja excluir: ");
            int idReserva = input.nextInt();

            if (!reservaExists(idReserva, driver)) {
                System.out.println("Reserva não encontrada.");
                return;
            }

            try (Session session = driver.session()) {

                System.out.print("Deseja excluir a Reserva? (S/N): ");
                String choice = input.next();

                if (choice.equalsIgnoreCase("S")) {
                    String query = "MATCH (r:Reserva)-[rel]-() WHERE id(r) = $idReserva DELETE r, rel";
                    session.run(query, Values.parameters("idReserva", idReserva));
                    System.out.println("Reserva excluída com sucesso!");
                    Opcoes.showMenu(driver);
                }
                System.out.println("Exclusão cancelada, voltar ao Menu!");
                Opcoes.showMenu(driver);



            }
        }
    }

    private boolean reservaExists(int idReserva, Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (r:Reserva) WHERE id(r) = $idReserva RETURN r";
            Result result = session.run(query, Values.parameters("idReserva", idReserva));

            return result.hasNext();
        }
    }




}
