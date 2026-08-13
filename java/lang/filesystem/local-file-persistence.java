package com.example.orders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.apache.commons.io.FileUtils;

public class ReportStore {

  private final BlobClient blobs;

  public ReportStore(BlobClient blobs) {
    this.blobs = blobs;
  }

  /** Computed path, exactly how Jenkins persists config into JENKINS_HOME. */
  public void saveConfig(String xml) throws IOException {
    File target = new File(rootDir(), "config.xml");
    // ruleid: local-file-persistence
    try (Writer writer = new FileWriter(target)) {
      writer.write(xml);
    }
  }

  public void saveBuildRecord(byte[] body) throws IOException {
    // ruleid: local-file-persistence
    try (FileOutputStream out = new FileOutputStream(new File(rootDir(), "build.dat"))) {
      out.write(body);
    }
  }

  public void saveJson(String json) throws IOException {
    // ruleid: local-file-persistence
    Files.writeString(rootDir().toPath().resolve("build.json"), json);
  }

  public void ensureWorkspace() throws IOException {
    // ruleid: local-file-persistence
    Files.createDirectories(rootDir().toPath().resolve("workspace"));
  }

  /** Channel-level write, as used inside buffered-writer wrapper classes. */
  public FileChannel openChannel() throws IOException {
    // ruleid: local-file-persistence
    return FileChannel.open(
        rootDir().toPath().resolve("state.bin"),
        StandardOpenOption.WRITE,
        StandardOpenOption.CREATE);
  }

  public void legacyWrite(String body) throws IOException {
    // ruleid: local-file-persistence
    FileUtils.writeStringToFile(new File(rootDir(), "notes.txt"), body, "UTF-8");
  }

  public boolean ensureDir() {
    // ruleid: local-file-persistence
    return new File(rootDir(), "jobs").mkdirs();
  }

  public String readConfig() throws IOException {
    // ok: local-file-persistence
    return Files.readString(new File(rootDir(), "config.xml").toPath());
  }

  public Path scratch() throws IOException {
    // ok: local-file-persistence
    return Files.createTempDirectory("render-");
  }

  public void stageViaTempFile(byte[] body) throws IOException {
    // ok: local-file-persistence
    try (FileOutputStream out = new FileOutputStream(File.createTempFile("stage", ".tmp"))) {
      out.write(body);
    }
  }

  public void uploadReport(String json) {
    // ok: local-file-persistence
    blobs.put("latest.json", json.getBytes());
  }

  private File rootDir() {
    return new File(System.getenv("APP_HOME"));
  }

  public interface BlobClient {
    void put(String key, byte[] body);
  }
}
