package com.example.orders;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Rollups {

  // ruleid: uncoordinated-scheduled-task
  @Scheduled(cron = "0 0 2 * * *")
  public void nightlyRollup() {
    invoiceCustomers();
  }

  // ruleid: uncoordinated-scheduled-task
  @Scheduled(fixedDelay = 60_000)
  void reconcile() {
    invoiceCustomers();
  }

  // ok: uncoordinated-scheduled-task
  @Scheduled(cron = "0 0 3 * * *")
  @SchedulerLock(name = "lockedRollup", lockAtMostFor = "10m")
  public void lockedRollup() {
    invoiceCustomers();
  }

  private void invoiceCustomers() {}
}
