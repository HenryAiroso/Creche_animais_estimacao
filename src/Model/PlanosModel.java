package Model;

import Bean.PlanosBean;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;

import static org.neo4j.driver.Values.parameters;

public class PlanosModel {

    static void create(PlanosBean m, Driver driver) {
        try (Session session = driver.session()) {
            String cypherQuery = "CREATE (p:Plano {nome_plano: $nome_plano, descricao_plano: $descricao_plano, " +
                    "duracao_plano: $duracao_plano, tipo_acomodacao: $tipo_acomodacao, " +
                    "preco_plano: $preco_plano, restricao_especie: $restricao_especie, disponibilidade: $disponibilidade})";

            session.run(cypherQuery, parameters(
                    "nome_plano", m.getNome_plano(),
                    "descricao_plano", m.getDescricao_plano(),
                    "duracao_plano", m.getDuracao_plano(),
                    "tipo_acomodacao", m.getTipo_acomodacao(),
                    "preco_plano", m.getPreco_plano(),
                    "restricao_especie", m.getRestricao_especie(),
                    "disponibilidade", m.getDisponibilidade()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PlanosBean findById(int id_plano, Driver driver) {
        try (Session session = driver.session()) {
            String cypherQuery = "MATCH (p:Plano) WHERE id(p) = $id_plano RETURN p";
            Result result = session.run(cypherQuery, parameters("id_plano", id_plano));
            if (result.hasNext()) {
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
                return plano;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void listarPlanosDisponiveis(Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (p:Plano) RETURN p.id_plano, p.nome_plano");
            while (result.hasNext()) {
                Record record = result.next();
                int id_plano = record.get("p.id_plano").asInt();
                String nome_plano = record.get("p.nome_plano").asString();
                System.out.println("ID do Plano: " + id_plano + ", Nome do Plano: " + nome_plano);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void update(PlanosBean existingPlano, Driver driver) {

        try (Session session = driver.session()) {
            String cypherQuery = "MATCH (p:Plano) WHERE p.id = $id_plano " + // Update this line
                    "SET p.nome_plano = $nome_plano, p.descricao_plano = $descricao_plano, " +
                    "p.duracao_plano = $duracao_plano, p.tipo_acomodacao = $tipo_acomodacao, " +
                    "p.preco_plano = $preco_plano, p.restricao_especie = $restricao_especie, " +
                    "p.disponibilidade = $disponibilidade";

            session.run(cypherQuery, parameters(
                    "id_plano", existingPlano.getId_plano(),
                    "nome_plano", existingPlano.getNome_plano(),
                    "descricao_plano", existingPlano.getDescricao_plano(),
                    "duracao_plano", existingPlano.getDuracao_plano(),
                    "tipo_acomodacao", existingPlano.getTipo_acomodacao(),
                    "preco_plano", existingPlano.getPreco_plano(),
                    "restricao_especie", existingPlano.getRestricao_especie(),
                    "disponibilidade", existingPlano.getDisponibilidade()
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(int id_plano, Driver driver) {
        try (Session session = driver.session()) {
            String cypherQuery = "MATCH (p:Plano) WHERE p.id = $id_plano DELETE p";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPlanosInReserva(int id_plano, Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (p:Plano)-[:HAS_RESERVA]->() WHERE p.id_plano = $id_plano RETURN count(*) > 0", parameters("id_plano", id_plano));
            return result.single().get(0).asBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}