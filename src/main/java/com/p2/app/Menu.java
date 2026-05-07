package com.p2.app;

import br.upe.analyzer.Analyzer;
import br.upe.data.LogEntry;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private List<LogEntry> entradas;
    private Scanner scanner;

    public Menu(List<LogEntry> etradas){
        this.entradas = entradas;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int opcao = -1;

        while (opcao != 0){
            System.out.println("\n1 - Recursos grandes respondidos");
            System.out.println("2 - Nao respondidos");
            System.out.println("3 - % de requisicoes por SO");
            System.out.println("4 - Media das requisicoes por POST");
            System.out.println("0 - Sair");
            System.out.println("| ");

            try{
                opcao = Intereger.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida.");
                continue;
            }

             switch(opcao) {
                case 1 -> Analyzer.recursosGrandes(entradas);
                case 2 -> Analyzer.naoRespondidosNovembro(entradas);
                case 3 -> Analyzer.sistemasOperacionais(entradas);
                case 4 -> Analyzer.mediaPost(entradas);
                case 0 -> System.out.println("Encerrando o programa.");
                default -> System.out.println("Opcao invalida.");
            }
        }
        scanner.close();   
    }
}