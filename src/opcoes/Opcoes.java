package opcoes;

import Controller.*;
import GerarRelatorios.GerarRelatorio;
import org.neo4j.driver.Driver;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Opcoes {

    static String relatorios;

    public static void showMenu(Driver driver) {
        int option;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println();
            System.out.println("Informe o número da opção que deseja executar: ");
            System.out.println("1 - Inserir um novo Cliente");
            System.out.println("2 - Exibir todos os Clientes");
            System.out.println("3 - Alterar informações de Clientes");
            System.out.println("4 - Excluir informações de Clientes");
            System.out.println("5 - Inserir um novo Animal");
            System.out.println("6 - Exibir todos os Animais");
            System.out.println("7 - Alterar informações de Animais");
            System.out.println("8 - Excluir informação de Animais");
            System.out.println("9 - Inserir um novo Plano");
            System.out.println("10 - Exibir todos os Planos");
            System.out.println("11 - Alterar informações de Planos");
            System.out.println("12 - Excluir informação de Planos");
            System.out.println("13 - Inserir um novo Funcionário");
            System.out.println("14 - Exibir todos os Funcionários");
            System.out.println("15 - Alterar informações de Funcionários");
            System.out.println("16 - Excluir informação de Funcionários");
            System.out.println("17 - Inserir uma nova Reserva");
            System.out.println("18 - Exibir todas as Reservas");
            System.out.println("19 - Alterar informações de Reservas");
            System.out.println("20 - Excluir informação de Reservas");
            System.out.println("21 - Gerar relatório de Reservas");
            System.out.println("22 - Gerar relatório de Reservas por Animal");
            System.out.println("23 - Gerar relatório de Animais por Planos");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");

            try {
                if (scanner.hasNextInt()) {
                    option = scanner.nextInt();
                    scanner.nextLine(); // consume the newline character
                    handleMenuOption(option, driver);
                } else {
                    System.out.println("Opção inválida. Por favor, tente novamente:");
                    scanner.nextLine(); // consume the invalid input
                    option = -1; // set option to an invalid value to continue the loop
                }
            } catch (InputMismatchException e) {
                System.out.println("Opção inválida. Por favor, tente novamente:");
                scanner.nextLine(); // consume the invalid input
                option = -1; // set option to an invalid value to continue the loop
            }
        } while (option != 0);
    }

    private static void handleMenuOption(int option, Driver driver) {
        switch (option) {
            case 1:
                new ClientesController().createClientes(driver);
                break;
            case 2:
                new ClientesController().listarClientes(driver);
                break;
            case 3:
                new ClientesController().updateClientes(driver);
                break;
            case 4:
                new ClientesController().deleteClientes(driver);
                break;
            case 5:
                new AnimaisController().createAnimais(driver);
                break;
            case 6:
                new AnimaisController().listarAnimais(driver);
                break;
            case 7:
                new AnimaisController().updateAnimais(driver);
                break;
            case 8:
                new AnimaisController().deleteAnimais(driver);
                break;
            case 9:
                new PlanosController().createPlanos(driver);
                break;
            case 10:
                new PlanosController().listarPlanos(driver);
                break;
            case 11:
                new PlanosController().updatePlanos(driver);
                break;
            case 12:
                new PlanosController().deletePlano(driver);
                break;
            case 13:
                new FuncionariosController().createFuncionarios(driver);
                break;
            case 14:
                new FuncionariosController().listarFuncionarios(driver);
                break;
            case 15:
                new FuncionariosController().updateFuncionarios(driver);
                break;
            case 16:
                new FuncionariosController().deleteFuncionarios(driver);
                break;
            case 17:
                new ReservasController().createReservas(driver);
                break;
            case 18:
                new ReservasController().listarReservas(driver);
                break;
            case 19:
                new ReservasController().updateReservas(driver);
                break;
            case 20:
                new ReservasController().deleteReservas(driver);
                break;
            case 21:
                GerarRelatorio.gerarRelatorioReservas(driver);
                break;
            case 22:

                GerarRelatorio.gerarRelatorioReservasAnimal(driver);
                break;
            case 23:
                GerarRelatorio.gerarRelatorioAnimaisPorPlanos(driver);
                break;
            case 0:
                System.out.println("Saindo...");
                break;
            default:
                break;
        }
    }

}