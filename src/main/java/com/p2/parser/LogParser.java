package com.p2.parser;

import com.p2.data.LogEntry;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser {

    private static final int NUM_THREADS = 4;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    // regex que captura todos os campos de uma linha do log
    private static final Pattern LOG_PATTERN = Pattern.compile(
        "(\\S+) \\S+ (\\S+) \\[(.*?)] \"(\\S+) (\\S+)[^\"]*\" (\\d{3}) (\\S+) \"(.*?)\" \"(.*?)\""
    );

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
            this.start    = start;
            this.end      = end;
            this.result   = result;
        }

        @Override
        public void run() {
            try {
                RandomAccessFile raf = new RandomAccessFile(filePath, "r");

                if (start != 0) {
                    raf.seek(start - 1);
                    raf.readLine(); // descarta linha incompleta
                }

                long currentPos = raf.getFilePointer();

                while (currentPos < end) {
                    String line = raf.readLine();
                    if (line == null) break;

                    LogEntry entry = parseLine(line);
                    if (entry != null) result.add(entry);

                    currentPos = raf.getFilePointer();
                }

                // garante que a ultima linha do chunk nao seja perdida
                if (raf.getFilePointer() >= end && raf.getFilePointer() < raf.length()) {
                    String lastLine = raf.readLine();
                    if (lastLine != null) {
                        LogEntry entry = parseLine(lastLine);
                        if (entry != null) result.add(entry);
                    }
                }

                raf.close();

            } catch (Exception e) {
                System.out.println("Erro na thread: " + e.getMessage());
            }
        }

        private LogEntry parseLine(String line) {
            try {
                Matcher m = LOG_PATTERN.matcher(line);
                if (!m.find()) return null;

                String ip           = m.group(1);
                String userId       = m.group(2);
                LocalDateTime dt    = LocalDateTime.parse(m.group(3), FORMATTER);
                String method       = m.group(4);
                String resource     = m.group(5);
                int statusCode      = Integer.parseInt(m.group(6));
                long responseSize   = m.group(7).equals("-") ? 0 : Long.parseLong(m.group(7));
                String referer      = m.group(8);
                String userAgent    = m.group(9);

                return new LogEntry(ip, userId, dt, method, resource,
                        statusCode, responseSize, referer, userAgent);

            } catch (Exception e) {
                return null;
            }
        }
    }
}
