// ruleid: in-process-cron
const cron = require('node-cron');
const { CronJob } = require('cron');
const { Queue } = require('bullmq');

// ruleid: in-process-cron
cron.schedule('0 2 * * *', () => rollupDaily());

// ruleid: in-process-cron
const job = new CronJob('0 * * * *', () => reconcile());
job.start();

const queue = new Queue('rollups', { connection: { url: process.env.REDIS_URL } });

// ok: in-process-cron
async function enqueueRollup() {
  await queue.add('daily', {}, { jobId: 'daily-rollup' });
}

async function rollupDaily() {}
async function reconcile() {}

module.exports = { enqueueRollup };
