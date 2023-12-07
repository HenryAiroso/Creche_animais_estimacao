package Controller;

import Bean.AnimaisBean;
import Model.AnimaisModel;
import opcoes.Opcoes;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;

import java.util.Scanner;

public class AnimaisController {

    private boolean clienteExists(int cpf_cliente, Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (c:Cliente) WHERE c.cpf = $cpf RETURN c",
                    Values.parameters("cpf", cpf_cliente));
            return !result.hasNext();
        }
    }

    public void createAnimais(Driver driver) {
        Scanner input = new Scanner(System.in);
        System.out.println("Insira os seguintes dados para cadastrar um novo Animal: ");
        System.out.print("Nome Animal: ");
        String nome_animal = input.nextLine();
        System.out.print("Especie Animal: ");
        String especie_animal = input.nextLine();
        System.out.print("Raca: ");
        String raca = input.nextLine();
        System.out.print("Idade: ");
        String idade = input.nextLine();
        System.out.print("Sexo Animal: ");
        String sexo_animal = input.nextLine();
        System.out.print("Observacao: ");
        String observacao = input.nextLine(); // Use nextLine() Paara permitir multiplas palavras


        System.out.println("Clientes disponíveis:");
        new ClientesController().listarClientes(driver);
        System.out.print("Insira o CPF do cliente ao vincular um animal: ");
        int cpf_cliente = input.nextInt();


        if (clienteExists(cpf_cliente, driver)) {
            System.out.println("CPF do cliente não encontrado. Tente novamente.");
            return; // Encerre a função se o CPF do cliente não for encontrado
        }

        if (clienteExists(cpf_cliente, driver)) {
            System.out.println("CPF do cliente não encontrado. Tente novamente.");
            return; // Encerre a função se o CPF do cliente não for encontrado
        }

        AnimaisBean animaisBean = new AnimaisBean(nome_animal, especie_animal, raca, idade, sexo_animal, observacao, cpf_cliente);
        AnimaisModel.create(animaisBean, driver);

        try (Session session = driver.session()) {
            session.run("MATCH (a:Animal), (c:Cliente) WHERE a.nome_animal = $nome_animal AND c.cpf = $cpf_cliente CREATE (a)-[:Animal_de]->(c)",
                    Values.parameters("nome_animal", nome_animal, "cpf_cliente", cpf_cliente));
        }
    }



    public void listarAnimais(Driver driver) {
        try (Session session = driver.session()) {
            Result result = session.run("MATCH (a:Animal) RETURN a");
            while (result.hasNext()) {
                Record record = result.next();
                Node animalNode = record.get("a").asNode();
                int id_animal = (int) animalNode.id();
                String nome_animal = animalNode.get("nome_animal").asString();
                String especie_animal = animalNode.get("especie_animal").asString();
                String raca = animalNode.get("raca").asString();
                String idade = animalNode.get("idade").asString();
                String sexo_animal = animalNode.get("sexo_animal").asString();
                String observacao = animalNode.get("observacao").asString();
                int cpf_cliente = animalNode.get("cpf_cliente").asInt();

                AnimaisBean animaisBean = new AnimaisBean(nome_animal, especie_animal, raca, idade, sexo_animal, observacao, cpf_cliente);
                animaisBean.setId_animal(id_animal);
                System.out.println("id_animal: " + id_animal + ", " + animaisBean.toString());
            }
        }
    }

    public void updateAnimais(Driver driver) {
        listarAnimais(driver);
        Scanner input = new Scanner(System.in);
        System.out.print("Digite o ID do Animal que deseja atualizar: ");
        int id_animal = Integer.parseInt(input.nextLine());

        AnimaisBean existingAnimais;
        existingAnimais = AnimaisModel.findById(id_animal, driver);
        System.out.println("Parâmetros atuais: " + existingAnimais);
        if (existingAnimais == null) {
            System.out.println("Animal não encontrado.");
            return;
        }
        existingAnimais.setId_animal(id_animal); // Defina o id_animal no objeto existingAnimais

        System.out.print("Novo Nome Animal: ");
        existingAnimais.setNome_animal(input.nextLine());
        System.out.print("Nova Especie Animal: ");
        existingAnimais.setEspecie_animal(input.nextLine());
        System.out.print("Nova Raca Animal: ");
        existingAnimais.setRaca(input.nextLine());
        System.out.print("Nova Idade do Animal: ");
        existingAnimais.setIdade(input.nextLine());
        System.out.print("Novo Sexo Animal: ");
        existingAnimais.setSexo_animal(input.nextLine());
        System.out.print("Nova Observacao: ");
        existingAnimais.setObservacao(input.nextLine());

        AnimaisModel.update(existingAnimais, driver);
        System.out.println("Animal atualizado com sucesso!");

    }

    public void deleteAnimais(Driver driver) {
        listarAnimais(driver);
        Scanner input = new Scanner(System.in);
        System.out.print("Digite o ID do Animal que deseja excluir: ");
        int id_animal = input.nextInt();

        // Delete relationships first
        try (Session session = driver.session()) {
            session.run("MATCH (a:Animal)-[r]-() WHERE ID(a) = $id DELETE r", Values.parameters("id", id_animal));
            session.run("MATCH (a:Animal) WHERE ID(a) = $id DELETE a", Values.parameters("id", id_animal));
        }

        // Verifica se o animal está associado a alguma reserva
        if (AnimaisModel.isAnimaisInReserva(id_animal, driver)) {
            System.out.println("Não é possível excluir o animal, pois ele está associado a uma reserva.");
            return;
        }


        // Validação retorno menu
        System.out.print("Deseja excluir o ANIMAL? (S/N): ");
        String choice = input.next();
        if (choice.equalsIgnoreCase("S")) {
            AnimaisModel.delete(id_animal, driver);
            System.out.println("Cadastro Animal excluído com sucesso!");
            Opcoes.showMenu(driver);
        }
        System.out.println("Exclusão cancelada, voltar ao Menu!");
        Opcoes.showMenu(driver);


    }

}