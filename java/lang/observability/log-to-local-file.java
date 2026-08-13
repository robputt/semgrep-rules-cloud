package com.example.orders;

import ch.qos.logback.core.rolling.RollingFileAppender;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Logging {

  private static final Logger LOGGER = Logger.getLogger(Logging.class.getName());

  /** How Jenkins attaches its own log file. */
  public static void configureFileLogging() throws IOException {
    // ruleid: log-to-local-file
    FileHandler handler = new FileHandler("/var/log/app/app.log", 10_485_760, 5, true);
    LOGGER.addHandler(handler);
  }

  public static RollingFileAppender<Object> configureRolling() {
    // ruleid: log-to-local-file
    return new RollingFileAppender<>();
  }

  public static void redirectStdout() throws IOException {
    // ruleid: log-to-local-file
    System.setOut(new PrintStream(new File("/var/log/app/stdout.log"), "UTF-8"));
  }

  public static void configureConsole() {
    // ok: log-to-local-file
    LOGGER.addHandler(new ConsoleHandler());
  }
}
