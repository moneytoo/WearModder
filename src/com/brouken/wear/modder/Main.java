package com.brouken.wear.modder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    public static void main(String[] args) throws Exception {

        for (String arg : args) {
            final Path path = Paths.get(arg);

            if (Files.isDirectory(path))
                Files.walk(Paths.get(arg))
                        .filter(Files::isRegularFile)
                        .forEach(Main::processFile);
            else
                processFile(Paths.get(arg));

        }
    }

    private static void processFile(Path path) {
        if (!path.toString().toLowerCase().endsWith(".xml"))
            return;

        System.out.println(path.toString());

        try {
            String content = new String(Files.readAllBytes(path));

            content = processRange(content, "dip", 2, 720);
            content = processRange(content, "sp", 2, 240);

            content = content.replace(">?android:actionBarSize", ">28.0dip");

            Files.write(path, content.getBytes(), StandardOpenOption.WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRange(String content, final String units, final int min, final int max) {
        for (int i = min; i < max; i++) {
            content = content.replace(">" + i + ".0" + units, ">" + i/2 + ".0" + units);
            content = content.replace("\"" + i + ".0" + units, "\"" + i/2 + ".0" + units);
        }
        return content;
    }


}
