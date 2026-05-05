package model;

/**
 * Representa uma linha do log já tratada.
 * Classe simples (POJO), apenas atributos.
 */
public class LogEntry {

    public String ip;
    public String timestamp;
    public String method;
    public String endpoint;
    public int statusCode;
    public int responseSize;
    public String userAgent;

    public LogEntry(String ip, String timestamp, String method,
                    String endpoint, int statusCode,
                    int responseSize, String userAgent) {
        this.ip = ip;
        this.timestamp = timestamp;
        this.method = method;
        this.endpoint = endpoint;
        this.statusCode = statusCode;
        this.responseSize = responseSize;
        this.userAgent = userAgent;
    }
}
