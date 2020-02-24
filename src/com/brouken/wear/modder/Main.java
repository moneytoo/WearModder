package com.brouken.wear.modder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final int COEF = 2;

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
        if (!path.toString().toLowerCase().endsWith(".xml") || path.toString().toLowerCase().endsWith("strings.xml") || path.toString().toLowerCase().endsWith("plurals.xml"))
            return;

        System.out.println(path.toString());

        try {
            String content = new String(Files.readAllBytes(path));

            content = processRange(content, "dip", 2, 720);
            content = processRange(content, "sp", 2, 240);

            content = content.replace(">?android:actionBarSize<", ">28.0dip<");
            content = content.replace("\"?actionBarSize\"", "\"28.0dip\"");

            Files.write(path, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String processRange(String content, final String units, final int min, final int max) {
        for (int i = min; i < max; i++) {
            content = content.replace(">" + i + ".0" + units, ">" + i/COEF + ".0" + units);
            content = content.replace("\"" + i + ".0" + units, "\"" + i/COEF + ".0" + units);
        }
        return content;
    }


}
