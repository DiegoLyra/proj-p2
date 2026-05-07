package com.p2.app;

import com.p2.data.LogEntry;
import com.p2.parser.LogParser;

import java.util.List;

//classe principal que carrega arquivo log e inicia o menu
public class Main {
    //processa o caminho do arquivo log
    private static final String CAMINHO_LOG = "access.log";

    public static void main(String[] args){
        System.out.println("Carregando arquivo de log...");
        //carrega e converte arquivos log em uma lista de objetos LogEntry
        List<LogEntry> entradas = LogParser.loadFile(CAMINHO_LOG);
        System.out.println(entradas.size() + " registros carregados.");

        //cria o menu passando as entradas
        Menu menu = new Menu(entradas);
        menu.start();
    }
}
