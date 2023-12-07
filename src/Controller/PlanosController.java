package Controller;

import Bean.PlanosBean;
import Model.PlanosModel;
import opcoes.Opcoes;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;

import java.util.Scanner;

import static org.neo4j.driver.Values.parameters;

public class PlanosController {

    public void createPlanos(Driver driver) {
        try (Session session = driver.session()) {
            Scanner input = new Scanner(System.in);
            System.out.println("Insira os seguintes dados para cadastrar um novo Plano: ");
            System.out.print("Nome Plano: ");
            String nome_plano = input.nextLine();
            System.out.print("Descricao Plano: ");
            String descricao_plano = input.nextLine();
            System.out.print("Duracao Plano: ");
            int duracao_plano = Integer.parseInt(input.nextLine());
            System.out.print("Tipo Acomodacao: ");
            String tipo_acomodacao = input.nextLine();
            System.out.print("Preco Plano: ");
            double preco_plano = Double.parseDouble(input.nextLine());
            System.out.print("Restricao Especie: ");
            String restricao_especie = input.nextLine();
            System.out.print("Disponibilidade: ");
            String disponibilidade = input.nextLine();

            // Cypher query to create a new Plano node
            String cypherQuery = "CREATE (p:Plano {nome_plano: $nome_plano, descricao_plano: $descricao_plano, " +
                    "duracao_plano: $duracao_plano, tipo_acomodacao: $tipo_acomodacao, " +
                    "preco_plano: $preco_plano, restricao_especie: $restricao_especie, disponibilidade: $disponibilidade})";

            // Set parameters
            session.run(cypherQuery, parameters(
                    "nome_plano", nome_plano,
                    "descricao_plano", descricao_plano,
                    "duracao_plano", duracao_plano,
                    "tipo_acomodacao", tipo_acomodacao,
                    "preco_plano", preco_plano,
                    "restricao_especie", restricao_especie,
                    "disponibilidade", disponibilidade
            ));

            System.out.println("Plano criado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listarPlanos(Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (p:Plano) RETURN p");
            while (result.hasNext()) {
                Record record = result.next();
                Node planoNode = record.get("p").asNode();
                PlanosBean plano = new PlanosBean(
                        planoNode.get("id_plano").asString(),
                        planoNode.get("nome_plano").asString(),
                        planoNode.get("descricao_plano").asString(),
                        planoNode.get("duracao_plano").asInt(),
                        planoNode.get("tipo_acomodacao").asString(),
                        planoNode.get("preco_plano").asDouble(),
                        planoNode.get("restricao_especie").asString(),
                        planoNode.get("disponibilidade").asString()
                );
                System.out.println("Plano ID: " + planoNode.id() + ", " + plano);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePlanos(Driver driver) {
        listarPlanos(driver);
        Scanner input = new Scanner(System.in);
        System.out.print("Digite o ID do Plano que deseja atualizar: ");
        int id_plano = Integer.parseInt(input.nextLine());

        PlanosBean existingPlano;
        existingPlano = PlanosModel.findById(id_plano, driver);
        System.out.println("Parâmetros atuais: " + existingPlano);
        if(existingPlano == null){
            System.out.println("\n Plano Inválido, tente novamente");

            Opcoes.showMenu(driver);

        }

        existingPlano.setId_plano(String.valueOf(id_plano)); // Defina o id_plano no objeto existingPlano

        System.out.print("Novo Nome do Plano: ");
        existingPlano.setNome_plano(input.nextLine());
        System.out.print("Nova Descrição do Plano: ");
        existingPlano.setDescricao_plano(input.nextLine());
        System.out.print("Nova Duração do Plano: ");
        existingPlano.setDuracao_plano(Integer.parseInt(input.nextLine()));
        System.out.print("Novo Tipo de Acomodação: ");
        existingPlano.setTipo_acomodacao(input.nextLine());
        System.out.print("Novo Preço do Plano: ");
        existingPlano.setPreco_plano(Double.parseDouble(input.nextLine()));
        System.out.print("Nova Restrição de Espécie: ");
        existingPlano.setRestricao_especie(input.nextLine());
        System.out.print("Nova Disponibilidade: ");
        existingPlano.setDisponibilidade(input.nextLine());

        try (Session session = driver.session()) {
        //Cypher query para atualizar o nó
            String updateQuery = "MATCH (p:Plano) WHERE id(p) = $id_plano " +
                    "SET p.nome_plano = $nome_plano, " +
                    "p.descricao_plano = $descricao_plano, " +
                    "p.duracao_plano = $duracao_plano, " +
                    "p.tipo_acomodacao = $tipo_acomodacao, " +
                    "p.preco_plano = $preco_plano, " +
                    "p.restricao_especie = $restricao_especie, " +
                    "p.disponibilidade = $disponibilidade";

            session.run(updateQuery, parameters(
                    "id_plano", id_plano,
                    "nome_plano", existingPlano.getNome_plano(),
                    "descricao_plano", existingPlano.getDescricao_plano(),
                    "duracao_plano", existingPlano.getDuracao_plano(),
                    "tipo_acomodacao", existingPlano.getTipo_acomodacao(),
                    "preco_plano", existingPlano.getPreco_plano(),
                    "restricao_especie", existingPlano.getRestricao_especie(),
                    "disponibilidade", existingPlano.getDisponibilidade()

            ));

            PlanosModel.update(existingPlano, driver);
            System.out.println("Plano atualizado com sucesso!");
            Opcoes.showMenu(driver);

        }
    }

    public void deletePlano(Driver driver) {
        listarPlanos(driver);
        try (Session session = driver.session()) {
            Scanner input = new Scanner(System.in);
            System.out.print("Digite o ID do Plano que deseja excluir: ");
            int id_plano = input.nextInt();

            // Cypher query plano existe?
            String checkQuery = "MATCH (p:Plano) WHERE id(p) = $id_plano RETURN p";
            Result checkResult = session.run(checkQuery, parameters("id_plano", id_plano));
            if (!checkResult.hasNext()) {
                System.out.println("Plano não encontrado.");
                return;
            }

            // Plano associado a reserva?
            String reservaQuery = "MATCH (p:Plano)-[:alocado_a_reserva]->(r:Reserva) WHERE id(p) = $id_plano RETURN r";
            Result reservaResult = session.run(reservaQuery, parameters("id_plano", id_plano));
            if (reservaResult.hasNext()) {
                System.out.println("Não é possível excluir o plano, pois ele está associado a uma reserva.");
                return;
            }
            System.out.print("Deseja excluir o Plano? (S/N): ");
            String choice = input.next();
            if (choice.equalsIgnoreCase("S")) {
                String deleteQuery = "MATCH (p:Plano) WHERE id(p) = $id_plano DELETE p";
                session.run(deleteQuery, parameters("id_plano", id_plano));
                System.out.println("Plano excluído com sucesso!");
                Opcoes.showMenu(driver);
            }
            System.out.println("Exclusão cancelada, voltar ao Menu!");
            Opcoes.showMenu(driver);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}