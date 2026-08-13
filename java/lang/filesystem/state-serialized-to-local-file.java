package com.example.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Properties;

public class StateStore {

  private final ObjectMapper mapper = new ObjectMapper();
  private final XStream xstream = new XStream();
  private final OrderRepository repository;

  public StateStore(OrderRepository repository) {
    this.repository = repository;
  }

  public void savePropertiesState(Properties props) throws IOException {
    try (OutputStream out = new FileOutputStream(new File(rootDir(), "state.properties"))) {
      // ruleid: state-serialized-to-local-file
      props.store(out, "application state");
    }
  }

  public void javaSerialize(Object graph) throws IOException {
    File target = new File(rootDir(), "state.ser");
    // ruleid: state-serialized-to-local-file
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(target))) {
      out.writeObject(graph);
    }
  }

  /** How Jenkins persists its object model. */
  public void xstreamToDisk(Object config) throws IOException {
    // ruleid: state-serialized-to-local-file
    xstream.toXML(config, new FileWriter(new File(rootDir(), "config.xml")));
  }

  public void jacksonToDisk(Object config) throws IOException {
    // ruleid: state-serialized-to-local-file
    mapper.writeValue(new File(rootDir(), "config.json"), config);
  }

  public void saveToDatabase(Object config) {
    // ok: state-serialized-to-local-file
    repository.upsert(config);
  }

  public String serializeToString(Object config) throws IOException {
    // ok: state-serialized-to-local-file
    return mapper.writeValueAsString(config);
  }

  private File rootDir() {
    return new File(System.getenv("APP_HOME"));
  }

  public interface OrderRepository {
    void upsert(Object config);
  }
}
