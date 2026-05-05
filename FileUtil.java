package util;

import java.io.File;

/**
 * Responsável por criar diretórios e arquivos.
 */
public class FileUtil {

    public static final String OUTPUT_DIR = "Analise";

    /**
     * Garante que a pasta exista
     */
    public static void ensureOutputDir() {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}
