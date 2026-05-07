package com.p2.app;

import com.p2.data.LogEntry;
import com.p2.parser.LogParser;

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
