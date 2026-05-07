package com.p2.app;

import com.p2.analyzer.Analyzer;
import com.p2.data.LogEntry;

import java.util.List;
import java.util.Scanner;

//classe que exibe o menu do sistema
public class Menu {
    // Lista com todas as entradas do arquivo de log
    private List<LogEntry> entradas;

    //scanner para ler o que o usuario digitou
    private Scanner scanner;

    public Menu(List<LogEntry> entradas){
        this.entradas = entradas;
        this.scanner = new Scanner(System.in);
    }

    //inicia o menu principal
    public void start() {
        //variavel que armazena o que o usuario digitou
        int opcao = -1;

        while (opcao != 0){
            //opcoes disponiveis
            System.out.println("\n1 - Recursos grandes respondidos");
            System.out.println("2 - Nao respondidos");
            System.out.println("3 - % de requisicoes por SO");
            System.out.println("4 - Media das requisicoes por POST");
            System.out.println("0 - Sair");
            System.out.println("| ");

            try{
                opcao = Integer.parseInt(scanner.nextLine().trim()); //converte a opcao digitada para inteiro
            } catch (NumberFormatException e) {
                System.out.println("Opcao invalida.");
                continue;
            }
            //executa conforme a opcao escolhida pelo usuario
             switch(opcao) {
                case 1 -> Analyzer.recursosGrandes(entradas);
                case 2 -> Analyzer.naoRespondidosNovembro(entradas);
                case 3 -> Analyzer.sistemasOperacionais(entradas);
                case 4 -> Analyzer.mediaPost(entradas);
                case 0 -> System.out.println("Encerrando o programa.");
                default -> System.out.println("Opcao invalida.");
            }
        }
        //fecha o scanner e finaliza o programa
        scanner.close();   
    }
}