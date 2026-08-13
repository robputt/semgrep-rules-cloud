const fs = require('fs');
const { S3Client, PutObjectCommand } = require('@aws-sdk/client-s3');

function saveReport(report) {
  // ruleid: local-file-persistence
  fs.writeFileSync('./reports/latest.json', JSON.stringify(report));
}

function appendAudit(line) {
  // ruleid: local-file-persistence
  fs.appendFileSync('/tmp/audit.log', line);
}

function ensureUploadDir() {
  // ruleid: local-file-persistence
  fs.mkdirSync('uploads/incoming', { recursive: true });
}

function streamExport() {
  // ruleid: local-file-persistence
  return fs.createWriteStream('./data/export.csv');
}

function readConfig() {
  // ok: local-file-persistence
  return fs.readFileSync('./config/settings.json', 'utf8');
}

const s3 = new S3Client({});

async function uploadReport(report) {
  // ok: local-file-persistence
  await s3.send(
    new PutObjectCommand({
      Bucket: process.env.REPORT_BUCKET,
      Key: 'latest.json',
      Body: JSON.stringify(report),
    })
  );
}

module.exports = { saveReport, appendAudit, ensureUploadDir, streamExport, readConfig, uploadReport };
