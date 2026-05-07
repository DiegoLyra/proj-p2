package com.p2.data;

import java.time.LocalDateTime;

public class LogEntry {

    // dados de quem fez a requisição
    private String ip;
    private String userId;
    private LocalDateTime dateTime; // LocalDateTime permite filtrar por dia, mês e ano facilmente

    // dados da requisição em si
    private String method;
    private String resource;
    private int statusCode;
    private long responseSize; // long porque o tamanho pode ser um número grande

    // dados extras do acesso
    private String referer;
    private String userAgent; // identifica o navegador/SO de quem acessou

    public LogEntry(String ip, String userId, LocalDateTime dateTime,
                    String method, String resource, int statusCode,
                    long responseSize, String referer, String userAgent) {
        this.ip           = ip;
        this.userId       = userId;
        this.dateTime     = dateTime;
        this.method       = method;
        this.resource     = resource;
        this.statusCode   = statusCode;
        this.responseSize = responseSize;
        this.referer      = referer;
        this.userAgent    = userAgent; // this. diferencia o atributo da classe do parametro de mesmo nome
    }

    public boolean isSuccess()  { return statusCode >= 200 && statusCode <= 299; }
    public boolean isNotFound() { return statusCode >= 400 && statusCode <= 499; }

    public String getIp()           { return ip; }
    public String getUserId()       { return userId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getMethod()       { return method; }
    public String getResource()     { return resource; }
    public int getStatusCode()      { return statusCode; }
    public long getResponseSize()   { return responseSize; }
    public String getReferer()      { return referer; }
    public String getUserAgent()    { return userAgent; }
}
