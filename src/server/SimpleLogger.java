package src.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleLogger class for logging messages and errors to the console and log files.
 * Logs are organized by date, creating a new log file for each day.
 */
public class SimpleLogger {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final String logDirPath = "src/server/logs/";
    
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
     * Logs a message to the console and the log file.
     *
     * @param message The message to log.
     */
    public static void log(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = timestamp + " - " + message;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Logs an error message to the console and the log file.
     *
     * @param message The error message to log.
     */
    public static void logError(String message) {
        String timestamp = dateFormat.format(new Date());
        String logMessage = timestamp + " - ERROR: " + message;
        System.err.println(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Writes the log message to the appropriate log file.
     *
     * @param message The message to write to the log file.
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
     * Switches to a new log file for the given file name.
     *
     * @param newLogFileName The name of the new log file to switch to.
     * @throws IOException If an I/O error occurs while closing or opening the log file.
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
     * Closes the log file writer.
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