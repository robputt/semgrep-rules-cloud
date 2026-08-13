import { InjectQueue } from '@nestjs/bullmq';
import { Injectable } from '@nestjs/common';
import { Cron, CronExpression, Interval } from '@nestjs/schedule';
import type { Queue } from 'bullmq';

@Injectable()
export class Rollups {
  constructor(@InjectQueue('rollups') private readonly queue: Queue) {}

  // ruleid: uncoordinated-cron
  @Cron(CronExpression.EVERY_DAY_AT_2AM)
  async nightlyRollup(): Promise<void> {
    await this.invoiceCustomers();
  }

  // ruleid: uncoordinated-cron
  @Interval(60_000)
  poll(): void {
    void this.invoiceCustomers();
  }

  // ok: uncoordinated-cron
  async enqueueRollup(): Promise<void> {
    await this.queue.add('daily', {}, { jobId: 'daily-rollup' });
  }

  private async invoiceCustomers(): Promise<void> {}
}
