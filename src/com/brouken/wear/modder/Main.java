package com.brouken.wear.modder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) throws Exception {

        for (String arg : args) {
            System.out.println(arg);
            processPath(arg);
        }
    }

    private static void processPath(String path) throws IOException {
        final File file = new File(path);

        String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8);

        content = processRange(content, "dip", 2, 720);
        content = processRange(content, "sp", 2, 240);

        content = content.replace(">?android:actionBarSize", ">28.0dip");

        FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
    }

    private static String processRange(String content, final String units, final int min, final int max) {
        for (int i = min; i < max; i++) {
            content = content.replace(">" + i + ".0" + units, ">" + i/2 + ".0" + units);
            content = content.replace("\"" + i + ".0" + units, "\"" + i/2 + ".0" + units);
        }
        return content;
    }


}
