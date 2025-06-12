package com.radik.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: плохо работает, нужно логировать все действия
// это защитный класс логирования
public class RadikCoreDEFEND {
    public static void Logger(String player, String msg, String dimension, int x, int y, int z, String method) {
        String fileName = "RadikLogging.log";
        File logFile = new File(fileName);

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            switch (dimension) {
                case "overworld" -> dimension = "\"Верхний мир\"";
                case "nether" -> dimension = "\"Нижний мир\"";
                case "end" -> dimension = "\"Край\"";
            }

            String dateTime = new SimpleDateFormat("dd.MM.yy; HH.mm.ss").format(new Date());
            String sets = "";
            switch (method) {
                case "open" -> sets = String.format("игрок %s открыл %s в измерении %s на координатах [%d, %d, %d]", player, msg, dimension, x, y, z);
                case "break" -> sets = String.format("игрок %s сломал %s в измерении %s на координатах [%d, %d, %d]", player, msg, dimension, x, y, z);
                case "place" -> sets = String.format("игрок %s поставил %s в измерении %s на координатах [%d, %d, %d]", player, msg, dimension, x, y, z);
                case "use" -> sets = String.format("игрок %s использовал %s в измерении %s на координатах [%d, %d, %d]", player, msg, dimension, x, y, z);
            }
            String message = String.format("[%s]: %s", dateTime, sets);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(message);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
