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

}
