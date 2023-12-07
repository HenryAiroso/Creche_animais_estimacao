package Model;

import Bean.ClientesBean;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import java.util.HashSet;

public class ClientesModel {

    static void create(ClientesBean a, Driver driver) {
        try (Session session = driver.session()) {
            session.run("CREATE (c:Cliente {cpf_cliente: $cpf, nome_cliente: $nome, sobrenome_cliente: $sobrenome, email: $email, rua: $rua, cidade: $cidade, estado: $estado, pais: $pais, telefone: $telefone})",
                    Values.parameters("cpf", a.getCpf(), "nome", a.getNome(), "sobrenome", a.getSobrenome(),
                            "email", a.getEmail(), "rua", a.getRua(), "cidade", a.getCidade(), "estado", a.getEstado(),
                            "pais", a.getPais(), "telefone", a.getTelefone()));
        }
    }

    static HashSet<ClientesBean> listAll(Driver driver) {
        HashSet<ClientesBean> list = new HashSet<>();
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente) RETURN c.cpf_cliente, c.nome_cliente, c.sobrenome_cliente, c.email, c.rua, c.cidade, c.estado, c.pais, c.telefone");
            while (result.hasNext()) {
                Record record = result.next();
                list.add(new ClientesBean(record.get(0).asInt(), record.get(1).asString(), record.get(2).asString(),
                        record.get(3).asString(), record.get(4).asString(), record.get(5).asString(),
                        record.get(6).asString(), record.get(7).asString(), record.get(8).asString()));
            }
        }
        return list;
    }



    static void update(ClientesBean existingClientes, Driver driver) {
        try (Session session = driver.session()) {
            session.run("MATCH (c:Cliente) WHERE c.cpf_cliente = $cpf SET c.nome_cliente = $nome, c.sobrenome_cliente = $sobrenome, c.email = $email, c.rua = $rua, c.cidade = $cidade, c.estado = $estado, c.pais = $pais, c.telefone = $telefone",
                    Values.parameters("cpf", existingClientes.getCpf(), "nome", existingClientes.getNome(),
                            "sobrenome", existingClientes.getSobrenome(), "email", existingClientes.getEmail(),
                            "rua", existingClientes.getRua(), "cidade", existingClientes.getCidade(),
                            "estado", existingClientes.getEstado(), "pais", existingClientes.getPais(),
                            "telefone", existingClientes.getTelefone()));
        }
    }

    public static void delete(int cpf_cliente, Driver driver) {
        try (Session session = driver.session()) {
            session.run("MATCH (c:Cliente) WHERE c.cpf_cliente = $cpf DELETE c", Values.parameters("cpf", cpf_cliente));
        }
    }

    static boolean isClientesInAnimais(int cpf_cliente, Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente)-[:POSSUI]->(a:Animal) WHERE c.cpf_cliente = $cpf RETURN count(a) > 0",
                    Values.parameters("cpf", cpf_cliente));
            return result.single().get(0).asBoolean();
        }
    }
}
