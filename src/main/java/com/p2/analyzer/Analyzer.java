package com.p2.analyzer;
import com.p2.data.LogEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyzer {

    private static final String PASTA = "analise";

    private static void criarPasta() {
        try {
            Files.createDirectories(Paths.get(PASTA));
        } catch (IOException e) {
            System.out.println("Erro ao criar pasta: " + e.getMessage());
        }
    }

    // opcao 1
    public static void recursosGrandes(List<LogEntry> entradas) {
        criarPasta();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Paths.get(PASTA, "recursosGrandes.txt").toFile()));

            for (LogEntry e : entradas) {
                if (e.isSuccess() && e.getResponseSize() > 2000) {
                    bw.write(e.getStatusCode() + " " + e.getResponseSize() + " " + e.getIp());
                    bw.newLine();
                }
            }

            bw.close();
            System.out.println("recursosGrandes.txt gerado.");

        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }
