package Model;

import Bean.ReservasBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.HashSet;

public class ReservasModel {

    public static void create(ReservasBean reserva, Driver driver) {
        try (Session session = driver.session()) {
            String query = "CREATE (r:Reserva {id_animal: $id_animal, id_plano: $id_plano, matricula: $matricula, " +
                    "data: $data, hora_entrada: $hora_entrada, hora_saida: $hora_saida, " +
                    "observacoes_reserva: $observacoes_reserva, status_reserva: $status_reserva})";
            session.run(query,
                    Values.parameters(
                            "id_animal", reserva.getId_animal(),
                            "id_plano", reserva.getId_plano(),
                            "matricula", reserva.getMatricula(),
                            "data", reserva.getData_dodia().toString(),  // Adjust based on your data model
                            "hora_entrada", reserva.getHora_entrada(),
                            "hora_saida", reserva.getHora_saida(),
                            "observacoes_reserva", reserva.getObservacoes_reserva(),
                            "status_reserva", reserva.getStatus_reserva()
                    )
            );
        }
    }

    public static HashSet<ReservasBean> listAll(Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (r:Reserva) RETURN r";
            Result result = session.run(query);
            HashSet<ReservasBean> list = new HashSet<>();

            while (result.hasNext()) {
                Record record = result.next();
                Node node = record.get("r").asNode();

                // Extract properties from the Neo4j node
                String id_animal = node.get("id_animal").asString();
                String id_plano = node.get("id_plano").asString();
                String matricula = node.get("matricula").asString();
                String data_entrada = node.get("data_entrada").asString();  // Adjust based on your data model
                String hora_entrada = node.get("hora_entrada").asString();
                String data_hora_saida = node.get("data_hora_saida").asString();
                String observacoes_reserva = node.get("observacoes_reserva").asString();
                String status_reserva = node.get("status_reserva").asString();


                ReservasBean reserva = new ReservasBean(id_animal, id_plano, matricula, data_entrada, hora_entrada, data_hora_saida, observacoes_reserva, status_reserva);
                reserva.setId_animal(id_animal);
                reserva.setId_plano(id_plano);
                reserva.setMatricula(matricula);
                reserva.setData_dodia(data_entrada);
                reserva.setHora_saida(data_hora_saida);
                reserva.setObservacoes_reserva(observacoes_reserva);
                reserva.setStatus_reserva(status_reserva);

                list.add(reserva);
            }

            return list;
        }
    }

    public static ReservasBean findById(String id_reserva, Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (r:Reserva) WHERE ID(r) = $id_reserva RETURN r";
            Result result = session.run(query, Values.parameters("id_reserva", id_reserva));

            Node node = null;
            if (result.hasNext()) {
                Record record = result.next();
                node = record.get("r").asNode();

                String id_animal = node.get("id_animal").asString();
                String id_plano = node.get("id_plano").asString();
                String matricula = node.get("matricula").asString();
                String data_dodia = node.get("data").asString();
                String hora_entrada = node.get("hora_entrada").asString();
                String hora_saida = node.get("hora_saida").asString();
                String observacoes_reserva = node.get("observacoes_reserva").asString();
                String status_reserva = node.get("status_reserva").asString();

                ReservasBean reserva = new ReservasBean(id_animal, id_plano, matricula, data_dodia, hora_entrada, hora_saida, observacoes_reserva, status_reserva);
                reserva.setId_reserva(id_reserva);
                reserva.setId_animal(id_animal);
                reserva.setId_plano(id_plano);
                reserva.setMatricula(matricula);
                reserva.setData_dodia(data_dodia);
                reserva.setHora_entrada(hora_entrada);
                reserva.setHora_saida(hora_saida);
                reserva.setObservacoes_reserva(observacoes_reserva);
                reserva.setStatus_reserva(status_reserva);
                return reserva;
            }


        }


        return null;
    }


    public static void delete(int id_reserva, Driver driver) {
        try (Session session = driver.session()) {
            String query = "MATCH (r:Reserva) WHERE id(r) = $id_reserva DELETE r";
            session.run(query, Values.parameters("id_reserva", id_reserva));
        }
    }
}