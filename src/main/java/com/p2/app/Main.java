package com.p2;

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
                recursosRespondidos();
            } else if (entrada.equals("2")) {
                naoRespondidos();
            } else if (entrada.equals("3")) {
                percentualRequisicoes();
            } else if (entrada.equals("4")) {
                mediaPOST();
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