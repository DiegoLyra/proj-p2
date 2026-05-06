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
    
    // opcao 2
    public static void naoRespondidosNovembro(List<LogEntry> entradas) {
        criarPasta();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Paths.get(PASTA, "naoRespondidosNovembro.txt").toFile()));

            for (LogEntry e : entradas) {
                if (e.isNotFound() && e.getDateTime().getMonthValue() == 11 && e.getDateTime().getYear() == 2021) {
                    bw.write(e.getStatusCode() + " \"" + e.getReferer() + "\" Nov/2021");
                    bw.newLine();
                }
            }

            bw.close();
            System.out.println("naoRespondidosNovembro.txt gerado.");

        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    // opcao 3
    public static void sistemasOperacionais(List<LogEntry> entradas) {
        criarPasta();

        Map<String, Integer> contagem = new HashMap<>();
        contagem.put("Windows", 0);
        contagem.put("Macintosh", 0);
        contagem.put("Ubuntu", 0);
        contagem.put("Fedora", 0);
        contagem.put("Mobile", 0);
        contagem.put("Linux, outros", 0);

        int total = 0;

        for (LogEntry e : entradas) {
            if (e.getDateTime().getYear() != 2021) continue;
            String so = detectarSO(e.getUserAgent());
            contagem.put(so, contagem.get(so) + 1);
            total++;
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Paths.get(PASTA, "sistemasOperacionais.txt").toFile()));

            for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
                double pct = total > 0 ? (entry.getValue() * 100.0) / total : 0;
                bw.write(entry.getKey() + " " + String.format("%.4f", pct));
                bw.newLine();
            }

            bw.close();
            System.out.println("sistemasOperacionais.txt gerado.");

        } catch (IOException ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
    }

    private static String detectarSO(String ua) {
        if (ua.contains("Android") || ua.contains("Mobile")) return "Mobile";
        if (ua.contains("Windows")) return "Windows";
        if (ua.contains("Macintosh")) return "Macintosh";
        if (ua.contains("Ubuntu")) return "Ubuntu";
        if (ua.contains("Fedora")) return "Fedora";
        return "Linux, outros";
    }

// opcao 4
    public static void mediaPost(List<LogEntry> entradas) {
        long soma = 0;
        int qtd = 0;

        for (LogEntry e : entradas) {
            if (e.getMethod().equals("POST") && e.isSuccess() && e.getDateTime().getYear() == 2021) {
                soma += e.getResponseSize();
                qtd++;
            }
        }

        if (qtd == 0) {
            System.out.println("Nenhum POST encontrado em 2021.");
            return;
        }

        System.out.printf("Media POST 2021: %.2f bytes%n", (double) soma / qtd);
    }
}
