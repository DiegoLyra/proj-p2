import service.Analyzer;

import java.util.Scanner;

/**
 * Classe principal com menu.
 */
public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o caminho do arquivo de log: ");
        String path = sc.nextLine();

        Analyzer analyzer = new Analyzer(path);

        int op;

        do {
            System.out.println("\n1 - Recursos grandes");
            System.out.println("2 - Não respondidos (Nov 2021)");
            System.out.println("3 - Sistemas operacionais");
            System.out.println("4 - Média POST");
            System.out.println("0 - Sair");

            op = sc.nextInt();

            try {
                switch (op) {
                    case 1:
                        analyzer.analisarRecursosGrandes();
                        break;
                    case 2:
                        analyzer.analisarNaoRespondidas();
                        break;
                    case 3:
                        analyzer.analisarSistemasOperacionais();
                        break;
                    case 4:
                        analyzer.analisarMediaPost();
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } while (op != 0);

        sc.close();
    }
}

// ideia para o menu

import service.analyzer;

import java.util.Scanner;

public class Main {
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        boolean saidaUser = false;

        while (!saidaUser) {
            exibirMenu();
            String entrada = scanner.nextLine();

            if (entrada.equals("1")) {
                analyzer.analisarRecursosGrandes();

            } else if (entrada.equals("2")) {
                analyzer.analisarNaoRespondidas();

            } else if (entrada.equals("3")) {
                analyzer.analisarSistemasOperacionais();

            } else if (entrada.equals("4")) {
                analyzer.analisarMediaPost();

            } else if (entrada.equals("0")) {
                System.out.println("Encerrando o programa...");
                saidaUser = true;

            } else {
                System.out.println("Opção inválida. Tente novamente.");
            }
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- DASHBOARD DE REQUISIÇÕES ---");
        System.out.println("1 - Recursos Grandes Respondidos");
        System.out.println("2 - Não Respondidos");
        System.out.println("3 - % de Requisições por SO");
        System.out.println("4 - Média das Requisições POST");
        System.out.println("0 - Sair\n");
    }
}
