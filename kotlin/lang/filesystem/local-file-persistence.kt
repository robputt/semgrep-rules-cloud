package com.example.orders

import java.io.File
import java.nio.file.Files

class ReportStore(private val blobs: BlobClient) {

    fun saveReport(json: String) {
        // ruleid: local-file-persistence
        File("./reports/latest.json").writeText(json)
    }

    fun appendAudit(line: String) {
        // ruleid: local-file-persistence
        File("/tmp/audit.log").appendText(line)
    }

    fun ensureUploadDir(): Boolean {
        // ruleid: local-file-persistence
        return File("uploads/incoming").mkdirs()
    }

    fun readConfig(): String {
        // ok: local-file-persistence
        return File("config/settings.yaml").readText()
    }

    fun scratch(): java.nio.file.Path {
        // ok: local-file-persistence
        return Files.createTempDirectory("render-")
    }

    fun uploadReport(json: String) {
        // ok: local-file-persistence
        blobs.upload("latest.json", json.toByteArray())
    }

    interface BlobClient {
        fun upload(key: String, body: ByteArray)
    }
}
