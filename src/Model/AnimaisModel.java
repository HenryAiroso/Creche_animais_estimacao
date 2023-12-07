package Model;

import Bean.AnimaisBean;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.util.HashSet;

public class AnimaisModel {

    public static void create(AnimaisBean m, Driver driver) {
        try (Session session = driver.session()) {
            session.run("CREATE (a:Animal {nome_animal: $nome, especie_animal: $especie, raca: $raca, idade: $idade, sexo_animal: $sexo, observacao: $observacao, cpf_cliente: $cpf})",
                    Values.parameters("nome", m.getNome_animal(), "especie", m.getEspecie_animal(), "raca", m.getRaca(),
                            "idade", m.getIdade(), "sexo", m.getSexo_animal(), "observacao", m.getObservacao(), "cpf", m.getCpf_cliente()));
        }
    }



    public static AnimaisBean findById(int id_animal, Driver driver) {
        AnimaisBean animaisBean = null;

        try (Session session = driver.session()) {
            Result result = session.run("MATCH (a:Animal) WHERE id(a) = $id RETURN a.nome_animal, a.especie_animal, a.raca, a.idade, a.sexo_animal, a.observacao, a.cpf_cliente",
                    Values.parameters("id", id_animal));
            if (result.hasNext()) {
                Record record = result.next();
                animaisBean = new AnimaisBean(record.get(0).asString(), record.get(1).asString(), record.get(2).asString(),
                        record.get(3).asString(), record.get(4).asString(), record.get(5).asString(), record.get(6).asInt());
            }
        }
        return animaisBean;
    }

    public static void update(AnimaisBean existingAnimais, Driver driver) {
        try (Session session = driver.session()) {
            session.run("MATCH (a:Animal) WHERE id(a) = $id SET a.nome_animal = $nome_animal, a.especie_animal = $especie_animal, a.raca = $raca, a.idade = $idade, a.sexo_animal = $sexo_animal, a.observacao = $observacao, a.cpf_cliente = $cpf_cliente",
                    Values.parameters("id", existingAnimais.getId_animal(), "nome_animal", existingAnimais.getNome_animal(),
                            "especie_animal", existingAnimais.getEspecie_animal(), "raca", existingAnimais.getRaca(),
                            "idade", existingAnimais.getIdade(), "sexo_animal", existingAnimais.getSexo_animal(),
                            "observacao", existingAnimais.getObservacao(), "cpf_cliente", existingAnimais.getCpf_cliente()));
        }
    }

    public static void delete(int id_animal, Driver driver) {
        try (Session session = driver.session()) {
            session.run("MATCH (a:Animal) WHERE id(a) = $id DELETE a", Values.parameters("id", id_animal));
        }
    }

    public static HashSet<AnimaisBean> listAllWithClientes(Driver driver) {
        HashSet<AnimaisBean> list = new HashSet<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (a:Animal)-[:PERTENCE_A]->(c:Cliente) RETURN a.nome_animal, a.especie_animal, a.raca, a.idade, a.sexo_animal, a.observacao, a.cpf_cliente");
            while (result.hasNext()) {
                Record record = result.next();
                list.add(new AnimaisBean(record.get(0).asString(), record.get(1).asString(), record.get(2).asString(),
                        record.get(3).asString(), record.get(4).asString(), record.get(5).asString(), record.get(6).asInt()));
            }
        }
        return list;
    }

    public static boolean isAnimaisInReserva(int id_animal, Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (a:Animal)-[:POSSUI]->(r:Reserva) WHERE id(a) = $id RETURN count(r) > 0", Values.parameters("id", id_animal));
            return result.single().get(0).asBoolean();
        }
    }

}
