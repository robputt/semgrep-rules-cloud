package com.example.orders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderRegistry {

  // ruleid: static-mutable-collection
  private static final Map<String, Order> sessionCache = new ConcurrentHashMap<>();

  // ruleid: static-mutable-collection
  static List<String> pendingIds = new ArrayList<>();

  // ruleid: static-mutable-collection
  private static AtomicLong requestCounter = new AtomicLong();

  // ok: static-mutable-collection
  private static final Map<String, String> STATUS_LABELS = new HashMap<>();

  // ok: static-mutable-collection
  private final Map<String, Order> perInstance = new ConcurrentHashMap<>();

  private static final int MAX_RETRIES = 3;

  public void register(Order order) {
    // ok: static-mutable-collection
    Map<String, Order> local = new HashMap<>();
    local.put(order.id(), order);
    sessionCache.putAll(local);
  }

  public record Order(String id) {}
}
