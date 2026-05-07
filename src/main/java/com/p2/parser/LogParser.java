package com.p2.parser;

import com.p2.data.LogEntry;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LogParser {

    private static final int NUM_THREADS = 4;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    public static List<LogEntry> loadFile(String filePath) {
        List<LogEntry> result = Collections.synchronizedList(new ArrayList<>());

        try {
            RandomAccessFile raf = new RandomAccessFile(filePath, "r");
            long fileSize = raf.length();
            raf.close();

            long chunkSize = fileSize / NUM_THREADS;
            List<Thread> threads = new ArrayList<>();

            for (int i = 0; i < NUM_THREADS; i++) {
                long start = i * chunkSize;
                long end = (i == NUM_THREADS - 1) ? fileSize : (i + 1) * chunkSize;

                Thread thread = new Thread(new ChunkReader(filePath, start, end, result));
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    System.out.println("Thread interrompida: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao abrir o arquivo: " + e.getMessage());
        }

        return result;
    }

    private static class ChunkReader implements Runnable {

        private String filePath;
        private long start;
        private long end;
        private List<LogEntry> result;

        public ChunkReader(String filePath, long start, long end, List<LogEntry> result) {
            this.filePath = filePath;
            this.start = start;
            this.end = end;
            this.result = result;
        }

        @Override
        public void run() {
            try {
                RandomAccessFile raf = new RandomAccessFile(filePath, "r");

                // Se nao for o inicio do arquivo, avanca ate o proximo \n
                // para evitar ler uma linha incompleta
                if (start != 0) {
                    raf.seek(start - 1);
                    String skip = raf.readLine();
                }

                long currentPos = raf.getFilePointer();

                while (currentPos < end) {
                    String line = raf.readLine();
                    if (line == null) break;

                    LogEntry entry = parseLine(line);
                    if (entry != null) {
                        result.add(entry);
                    }

                    currentPos = raf.getFilePointer();
                }

                // Se a thread nao for a ultima, termina na proxima \n
                // apos o end para nao perder a ultima linha do chunk
                if (raf.getFilePointer() >= end && raf.getFilePointer() < raf.length()) {
                    String lastLine = raf.readLine();
                    if (lastLine != null) {
                        LogEntry entry = parseLine(lastLine);
                        if (entry != null) {
                            result.add(entry);
                        }
                    }
                }

                raf.close();

            } catch (Exception e) {
                System.out.println("Erro na thread: " + e.getMessage());
            }
        }

        private LogEntry parseLine(String line) {
            try {
                // IP: tudo antes do primeiro espaco
                int pos = line.indexOf(' ');
                String ip = line.substring(0, pos);

                // Pula " - "
                pos = line.indexOf(' ', pos + 1);

                // userId: entre o terceiro e quarto espaco
                int userStart = pos + 1;
                pos = line.indexOf(' ', userStart);
                String userId = line.substring(userStart, pos);

                // Data: entre '[' e ']'
                int dateStart = line.indexOf('[', pos) + 1;
                int dateEnd = line.indexOf(']', dateStart);
                String dateStr = line.substring(dateStart, dateEnd);
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, FORMATTER);

                // Requisicao: entre as primeiras aspas apos a data
                int reqStart = line.indexOf('"', dateEnd) + 1;
                int reqEnd = line.indexOf('"', reqStart);
                String request = line.substring(reqStart, reqEnd);

                // method e resource da requisicao
                String method = "";
                String resource = "";
                if (!request.isEmpty() && request.contains(" ")) {
                    String[] reqParts = request.split(" ");
                    method = reqParts[0];
                    resource = reqParts.length > 1 ? reqParts[1] : "";
                }

                // Status e tamanho: apos as aspas da requisicao
                String rest = line.substring(reqEnd + 2).trim();
                String[] parts = rest.split(" ");
                int statusCode = Integer.parseInt(parts[0]);
                long responseSize = 0;
                if (!parts[1].equals("-")) {
                    responseSize = Long.parseLong(parts[1]);
                }

                // Referer: entre as proximas aspas
                int refStart = line.indexOf('"', reqEnd + 1) + 1;
                int refEnd = line.indexOf('"', refStart);
                String referer = line.substring(refStart, refEnd);

                // UserAgent: entre as ultimas aspas
                int uaStart = line.indexOf('"', refEnd + 1) + 1;
                int uaEnd = line.lastIndexOf('"');
                String userAgent = "";
                if (uaStart < uaEnd) {
                    userAgent = line.substring(uaStart, uaEnd);
                }

                return new LogEntry(ip, userId, dateTime, method, resource,
                        statusCode, responseSize, referer, userAgent);

            } catch (Exception e) {
                return null;
            }
        }
    }
}


