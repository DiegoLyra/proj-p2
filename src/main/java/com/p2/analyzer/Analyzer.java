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
            Path path = Paths.get(PASTA);
            Files.createDirectories(path);
        } catch (IOException e) {
            System.out.println("Erro ao criar a pasta: " + e.getMessage());
        }
    }

    // Opcao 1: recursos grandes respondidos com sucesso
    public static void recursosGrandes(List<LogEntry> entradas) {
        criarPasta();
        Path arquivo = Paths.get(PASTA, "recursosGrandes.txt");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()));

            for (LogEntry entrada : entradas) {
                if (entrada.isSuccess() && entrada.getResponseSize() > 2000) {
                    writer.write(entrada.getStatusCode() + " "
                            + entrada.getResponseSize() + " "
                            + entrada.getIp());
                    writer.newLine();
                }
            }

            writer.close();
            System.out.println("Arquivo recursosGrandes.txt gerado com sucesso.");

        } catch (IOException e) {
            System.out.println("Erro ao gravar o arquivo: " + e.getMessage());
        }
    }

    // Opcao 2: requisicoes nao respondidas em novembro de 2021
    public static void naoRespondidosNovembro(List<LogEntry> entradas) {
        criarPasta();
        Path arquivo = Paths.get(PASTA, "naoRespondidosNovembro.txt");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()));

            for (LogEntry entrada : entradas) {
                boolean naoRespondido = entrada.isNotFound();
                boolean novembro2021 = entrada.getDateTime().getMonthValue() == 11
                        && entrada.getDateTime().getYear() == 2021;

                if (naoRespondido && novembro2021) {
                    writer.write(entrada.getStatusCode() + " \""
                            + entrada.getReferer() + "\" Nov/2021");
                    writer.newLine();
                }
            }

            writer.close();
            System.out.println("Arquivo naoRespondidosNovembro.txt gerado com sucesso.");

        } catch (IOException e) {
            System.out.println("Erro ao gravar o arquivo: " + e.getMessage());
        }
    }

    // Opcao 3: percentual de acessos por sistema operacional em 2021
    public static void sistemasOperacionais(List<LogEntry> entradas) {
        criarPasta();
        Path arquivo = Paths.get(PASTA, "sistemasOperacionais.txt");

        Map<String, Integer> contagem = new HashMap<>();
        contagem.put("Windows", 0);
        contagem.put("Macintosh", 0);
        contagem.put("Ubuntu", 0);
        contagem.put("Fedora", 0);
        contagem.put("Mobile", 0);
        contagem.put("Linux, outros", 0);

        int total = 0;

        for (LogEntry entrada : entradas) {
            if (entrada.getDateTime().getYear() != 2021) continue;

            String ua = entrada.getUserAgent();
            String so = detectarSO(ua);
            contagem.put(so, contagem.get(so) + 1);
            total++;
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo.toFile()));

            for (Map.Entry<String, Integer> entry : contagem.entrySet()) {
                double percentual = total > 0 ? (entry.getValue() * 100.0) / total : 0.0;
                writer.write(entry.getKey() + " " + String.format("%.4f", percentual));
                writer.newLine();
            }

            writer.close();
            System.out.println("Arquivo sistemasOperacionais.txt gerado com sucesso.");

        } catch (IOException e) {
            System.out.println("Erro ao gravar o arquivo: " + e.getMessage());
        }
    }

    private static String detectarSO(String userAgent) {
        if (userAgent.contains("Android") || userAgent.contains("Mobile")) {
            return "Mobile";
        }
        if (userAgent.contains("Windows")) {
            return "Windows";
        }
        if (userAgent.contains("Macintosh")) {
            return "Macintosh";
        }
        if (userAgent.contains("Ubuntu")) {
            return "Ubuntu";
        }
        if (userAgent.contains("Fedora")) {
            return "Fedora";
        }
        if (userAgent.contains("X11")) {
            return "Linux, outros";
        }
        return "Linux, outros";
    }

    // Opcao 4: media dos tamanhos das requisicoes POST com sucesso em 2021
    public static void mediaPost(List<LogEntry> entradas) {
        long soma = 0;
        int quantidade = 0;

        for (LogEntry entrada : entradas) {
            boolean isPost = entrada.getMethod().equals("POST");
            boolean sucesso = entrada.isSuccess();
            boolean ano2021 = entrada.getDateTime().getYear() == 2021;

            if (isPost && sucesso && ano2021) {
                soma += entrada.getResponseSize();
                quantidade++;
            }
        }

        if (quantidade == 0) {
            System.out.println("Nenhuma requisicao POST encontrada para o ano de 2021.");
            return;
        }

        double media = (double) soma / quantidade;
        System.out.printf("Media dos tamanhos das requisicoes POST em 2021: %.2f bytes%n", media);
    }
}
