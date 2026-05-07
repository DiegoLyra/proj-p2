package br.upe.app;

import br.upe.data.LogEntry;
import br.upe.parser.LogParser;

import java.util.List;

public class Main {
    private static final String CAMINHO_LOG = "access.log";

    public static void main(String[] args){
        System.out.println("Carregando arquivo de log...");
        List<LogEntry> entradas = LogParser.loadFile(CAMINHO_LOG);
        System.out.println(entradas.size() + " registros carregados.");

        Menu menu = new Menu(entradas);
        menu.start();
    }
}