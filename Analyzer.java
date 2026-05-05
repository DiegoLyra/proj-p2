package service;

import model.LogEntry;
import parser.LogParser;
import util.FileUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsável por todas as análises.
 * NÃO guarda tudo na memória.
 * Processa linha por linha.
 */
public class Analyzer {

    private String filePath;

    public Analyzer(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Opção 1 - Recursos grandes
     */
    public void analisarRecursosGrandes() throws IOException {

        FileUtil.ensureOutputDir();

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        BufferedWriter bw = new BufferedWriter(
                new FileWriter(FileUtil.OUTPUT_DIR + "/recursosGrandes.txt")
        );

        String line;

        while ((line = br.readLine()) != null) {

            LogEntry entry = LogParser.parse(line);
            if (entry == null) continue;

            if (entry.statusCode >= 200 && entry.statusCode <= 299
                    && entry.responseSize > 2000) {

                bw.write(entry.statusCode + " "
                        + entry.responseSize + " "
                        + entry.ip);
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        System.out.println("Arquivo recursosGrandes.txt gerado.");
    }

    /**
     * Opção 2 - Não respondidos Novembro 2021
     */
    public void analisarNaoRespondidas() throws IOException {

        FileUtil.ensureOutputDir();

        BufferedReader br = new BufferedReader(new FileReader(filePath));
        BufferedWriter bw = new BufferedWriter(
                new FileWriter(FileUtil.OUTPUT_DIR + "/naoRespondidosNovembro.txt")
        );

        String line;

        while ((line = br.readLine()) != null) {

            LogEntry entry = LogParser.parse(line);
            if (entry == null) continue;

            if (entry.statusCode >= 400 && entry.statusCode <= 499
                    && entry.timestamp.contains("Nov/2021")) {

                bw.write(entry.statusCode + " \""
                        + entry.endpoint + "\" Nov/2021");
                bw.newLine();
            }
        }

        br.close();
        bw.close();

        System.out.println("Arquivo naoRespondidosNovembro.txt gerado.");
    }

    /**
     * Opção 3 - Sistemas operacionais
     */
    public void analisarSistemasOperacionais() throws IOException {

        FileUtil.ensureOutputDir();

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        Map<String, Integer> contagem = new HashMap<>();
        int total = 0;

        String line;

        while ((line = br.readLine()) != null) {

            LogEntry entry = LogParser.parse(line);
            if (entry == null) continue;

            if (!entry.timestamp.contains("2021")) continue;

            total++;

            String ua = entry.userAgent;

            String os = identificarSO(ua);

            contagem.put(os, contagem.getOrDefault(os, 0) + 1);
        }

        br.close();

        BufferedWriter bw = new BufferedWriter(
                new FileWriter(FileUtil.OUTPUT_DIR + "/sistemasOperacionais.txt")
        );

        for (String os : contagem.keySet()) {
            double perc = (contagem.get(os) * 100.0) / total;
            bw.write(os + " " + perc);
            bw.newLine();
        }

        bw.close();

        System.out.println("Arquivo sistemasOperacionais.txt gerado.");
    }

    /**
     * Identifica o sistema operacional pelo user agent
     */
    private String identificarSO(String ua) {

        ua = ua.toLowerCase();

        if (ua.contains("android") || ua.contains("mobile"))
            return "Mobile";
        if (ua.contains("windows"))
            return "Windows";
        if (ua.contains("mac"))
            return "Macintosh";
        if (ua.contains("ubuntu"))
            return "Ubuntu";
        if (ua.contains("fedora"))
            return "Fedora";
        if (ua.contains("x11"))
            return "Linux, outros";

        return "Outros";
    }

    /**
     * Opção 4 - Média POST
     */
    public void analisarMediaPost() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;

        long soma = 0;
        int count = 0;

        while ((line = br.readLine()) != null) {

            LogEntry entry = LogParser.parse(line);
            if (entry == null) continue;

            if (entry.method.equals("POST")
                    && entry.statusCode >= 200 && entry.statusCode <= 299
                    && entry.timestamp.contains("2021")) {

                soma += entry.responseSize;
                count++;
            }
        }

        br.close();

        double media = (count == 0) ? 0 : (double) soma / count;

        System.out.println("Média tamanho POST (2021): " + media);
    }
}
