package GerarRelatorios;

import Controller.AnimaisController;
import Controller.FuncionariosController;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class GerarRelatorio {

    public static void gerarRelatorioReservas(Driver driver) {
        try (Session session = driver.session()) {
            Scanner scanner = new Scanner(System.in);
            new FuncionariosController().listarFuncionarios(driver);

            System.out.print("Digite o matricula do funcionário: ");
            String matricula = scanner.nextLine();

            String cypher = "MATCH (f:Funcionario)-[:trabalha_na_reserva]->(r:Reserva)<-[:alocado_a_reserva]-(p:Plano) " +
                    "MATCH (r:Reserva)<-[:Possui_uma_reserva]-(a:Animal) " +
                    "WHERE ID(f) = " + matricula + " " +
                    "RETURN r, f, a, p";

            Result result = session.run(cypher);

            LocalDateTime dataHoraAtual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String dataHoraFormatada = dataHoraAtual.format(formatter);
            String arquivoSaida = "relatorio_reservas_" + dataHoraFormatada + ".txt";

            // Get the source path
            String sourcePath = System.getProperty("user.dir");

            // Construct the relative path
            String relativePath = sourcePath + File.separator;

            try (PrintWriter writer = new PrintWriter(new FileWriter(relativePath + File.separator + arquivoSaida))) {
                writer.println("+-----------------------------------------------------------------------------------");
                writer.println("|                         Relatório Reservas por Funcionário                       |");
                writer.println("+----------------------------------------------------------------------------------+");
                writer.println("ID \t\t\tNome do Animal\t\t\tNome do Plano\t\t\tData da Reserva\t\t\tCPF\t\t\t\t\tFuncionario");

                while (result.hasNext()) {
                    Record record = result.next();
                    Node reserva = record.get("r").asNode();
                    Node funcionario = record.get("f").asNode();
                    Node animal = record.get("a").asNode();
                    Node plano = record.get("p").asNode();

                    String formattedIdReserva = String.valueOf(reserva.id());
                    String formattedNomeAnimal = animal.get("nome_animal").asString();
                    String formattedNomePlano = plano.get("nome_plano").asString();
                    String formattedData = reserva.get("data_entrada").asString();
                    String cpfFuncionario = funcionario.get("cpf").asString();
                    String nomeFuncionario = funcionario.get("nome").asString();

                    String formattedCpf = String.format("%-25s", cpfFuncionario);
                    String formattedNomeFuncionario = String.format("%-25s", nomeFuncionario);

                    writer.println(formattedIdReserva + "\t\t\t\t" + formattedNomeAnimal + "\t\t\t" + formattedNomePlano +
                            "\t\t\t" + formattedData + "\t\t\t" + formattedCpf + "\t\t\t" + formattedNomeFuncionario);
                }

                System.out.println("Relatório gerado com sucesso em " + relativePath + File.separator + arquivoSaida);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void gerarRelatorioReservasAnimal(Driver driver) {
        try (Session session = driver.session()) {
            Scanner scanner = new Scanner(System.in);
            new AnimaisController().listarAnimais(driver);
            System.out.print("Digite o ID do animal: ");
            int id_animal = scanner.nextInt();

            // Consulta Cypher para obter todas as reservas feitas para aquele animal
            String cypher = "MATCH (r:Reserva)<-[:Possui_uma_reserva]-(a:Animal) WHERE ID(a) = "+id_animal+" "+
                    " RETURN r, a.nome_animal ";

            Result result = session.run(cypher, Values.parameters("id_animal", id_animal));

            LocalDateTime dataHoraAtual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String dataHoraFormatada = dataHoraAtual.format(formatter);
            String arquivoSaida = "relatorio_reservas_animal_" + dataHoraFormatada + ".txt";

            // Get the source path
            String sourcePath = System.getProperty("user.dir");

            // Construct the relative path
            String relativePath = sourcePath + File.separator;

            try (PrintWriter writer = new PrintWriter(new FileWriter(relativePath + File.separator + arquivoSaida))) {
                writer.println("+---------------------------------------------+");
                writer.println("|  Relatório Reservas por Animal Selecionado  |");
                writer.println("+---------------------------------------------+");
                writer.println("ID Reserva\t\tNome do Animal\t\tMatricula");

                while (result.hasNext()) {
                    Record record = result.next();
                    Node reserva = record.get("r").asNode();
                    String nomeAnimal = record.get("a.nome_animal").asString();
                    String matriculaStr = reserva.get("matricula").asString();
                    int matricula = Integer.parseInt(matriculaStr);

                    String formattedIdReserva = String.format("%-25s", reserva.id());
                    String formattedNomeAnimal = String.format("%-25s", nomeAnimal);
                    String formattedMatricula = String.format("%-25s", matricula);

                    writer.println(formattedIdReserva + formattedNomeAnimal + formattedMatricula);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void gerarRelatorioAnimaisPorPlanos(Driver driver) {
        try (Session session = driver.session()) {
            String cypher = "MATCH (a:Animal)-[:Possui_uma_reserva]->(r:Reserva)<-[:alocado_a_reserva]-(p:Plano) " +
                    "MATCH ((f:Funcionario)-[:trabalha_na_reserva]->(r:Reserva)) " +
                    "RETURN a, p";

            Result result = session.run(cypher);

            LocalDateTime dataHoraAtual = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String dataHoraFormatada = dataHoraAtual.format(formatter);
            String arquivoSaida = "relatorio_animais_por_planos_" + dataHoraFormatada + ".txt";

            // Get the source path
            String sourcePath = System.getProperty("user.dir");

            // Construct the relative path
            String relativePath = sourcePath + File.separator;

            try (PrintWriter writer = new PrintWriter(new FileWriter(relativePath + File.separator + arquivoSaida))) {
                writer.println("+------------------------------------------------+");
                writer.println("|         Relatório Animais por Planos           |");
                writer.println("+------------------------------------------------+");
                writer.println("Nome do Animal\t\tNome do Plano");

                while (result.hasNext()) {
                    Record record = result.next();
                    Node animal = record.get("a").asNode();
                    Node plano = record.get("p").asNode();

                    String nomeAnimal = animal.get("nome_animal").asString();
                    String nomePlano = plano.get("nome_plano").asString();

                    String formattedNomeAnimal = String.format("%-25s", nomeAnimal);
                    String formattedNomePlano = String.format("%-25s", nomePlano);

                    writer.println(formattedNomeAnimal + formattedNomePlano);
                }

                System.out.println("Relatório gerado com sucesso em " + relativePath + File.separator + arquivoSaida);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

