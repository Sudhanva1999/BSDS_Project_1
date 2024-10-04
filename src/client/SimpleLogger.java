package src.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The SimpleLogger class provides a simple logging utility to log messages to
 * the console and to a log file.
 * It supports logging both general messages and error messages, with timestamps
 * for each log entry.
 * Logs are stored in a daily log file located in the specified directory.
 */
public class SimpleLogger {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String logDirPath = "src/client/logs/";

    private static FileWriter logFileWriter = null;
    private static String currentLogFileName = "";

    static {
        try {
            File logDir = new File(logDirPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
        } catch (Exception e) {
            System.err.println("Could not create log directory: " + e.getMessage());
        }
    }

    /**
     * Logs a general message to the console and the log file.
     *
     * @param message The message to be logged.
     */
    public static void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = timestamp + " - " + message;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Logs an error message to the console and the log file, prefixed with
     * "ERROR:".
     *
     * @param message The error message to be logged.
     */
    public static void logError(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = timestamp + " - ERROR: " + message;
        System.err.println(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Writes a log message to the log file. If the log file for the current day has
     * changed,
     * it switches to the new log file.
     *
     * @param message The message to be written to the log file.
     */
    private static void writeToFile(String message) {
        try {
            String logFileName = fileDateFormat.format(new Date()) + ".log";

            if (!logFileName.equals(currentLogFileName)) {
                switchLogFile(logFileName);
            }

            logFileWriter.write(message + "\n");
            logFileWriter.flush();
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }

    /**
     * Switches to a new log file for the specified date.
     *
     * @param newLogFileName The name of the new log file to switch to.
     * @throws IOException If an error occurs while closing the current log file or
     *                     opening the new one.
     */
    private static void switchLogFile(String newLogFileName) throws IOException {
        if (logFileWriter != null) {
            logFileWriter.close();
        }

        currentLogFileName = newLogFileName;
        File logFile = new File(logDirPath + currentLogFileName);
        logFileWriter = new FileWriter(logFile, true);
    }

    /**
     * Closes the log file writer, releasing any resources associated with it.
     */
    public static void close() {
        try {
            if (logFileWriter != null) {
                logFileWriter.close();
            }
        } catch (IOException e) {
            System.err.println("Failed to close log file: " + e.getMessage());
        }
    }
}
