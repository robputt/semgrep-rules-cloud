package com.example.orders

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Rollups {

    // ruleid: uncoordinated-scheduled-task
    @Scheduled(cron = "0 0 2 * * *")
    fun nightlyRollup() {
        invoiceCustomers()
    }

    // ruleid: uncoordinated-scheduled-task
    @Scheduled(fixedDelay = 60_000)
    fun reconcile() {
        invoiceCustomers()
    }

    // ok: uncoordinated-scheduled-task
    @Scheduled(cron = "0 0 3 * * *")
    @SchedulerLock(name = "lockedRollup", lockAtMostFor = "10m")
    fun lockedRollup() {
        invoiceCustomers()
    }

    private fun invoiceCustomers() {}
}
