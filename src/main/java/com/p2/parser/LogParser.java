package com.p2.parser;

import com.p2.data.LogEntry;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class LogParser {

  private static final int NUM_THREADS = 4;
  private static final FORMATTER =
      DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

  private static List<LogEntry> loadFile(String filePath){
      List<LogEntry> result = Collections.synchronizedList(new ArrayList<>());

    try {
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
        long fileSize = raf.length();
        raf.close();

        long chunkSize = fileSize / NUM_THREADS;
        List<Thread> threads = new ArrayList<>();

      for (int i = 0; i < NUM_THREADS; i++){
          long start = i * chunkSize;
          long end = (i == NUM_THREADS) ? fileSize : (i + 1) * chunkSize;

          Thread thread = new Thread(new ChunkReader)
      }
    }
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
    public void run(){
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
  }
}
