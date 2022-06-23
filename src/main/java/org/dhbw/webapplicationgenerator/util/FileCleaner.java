package org.dhbw.webapplicationgenerator.util;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;

@Service
public class FileCleaner {

    private static final String TMP_PATH = ".tmp";
    private static final String TMP2_PATH = ".tmp2";

    /**
     * Cleans up the temporary paths, mainly .tmp
     */
    public void performCleanup() {
        try {
            FileSystemUtils.deleteRecursively(Path.of(TMP_PATH));
            FileSystemUtils.deleteRecursively(Path.of(TMP2_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
