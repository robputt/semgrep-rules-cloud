using System.IO;
using System.Threading.Tasks;
using Azure.Storage.Blobs;

namespace Example.Orders;

public class ReportStore
{
    private readonly BlobContainerClient container;

    public ReportStore(BlobContainerClient container)
    {
        this.container = container;
    }

    public void SaveReport(string json)
    {
        // ruleid: local-file-persistence
        File.WriteAllText("./reports/latest.json", json);
    }

    public void AppendAudit(string line)
    {
        // ruleid: local-file-persistence
        File.AppendAllText("/tmp/audit.log", line);
    }

    public void EnsureUploadDir()
    {
        // ruleid: local-file-persistence
        Directory.CreateDirectory("uploads/incoming");
    }

    public StreamWriter OpenExport()
    {
        // ruleid: local-file-persistence
        return new StreamWriter("data/export.csv");
    }

    public string ReadConfig()
    {
        // ok: local-file-persistence
        return File.ReadAllText("config/settings.json");
    }

    public string Scratch()
    {
        // ok: local-file-persistence
        return Path.Combine(Path.GetTempPath(), Path.GetRandomFileName());
    }

    public async Task UploadReportAsync(string json)
    {
        // ok: local-file-persistence
        await container.UploadBlobAsync("latest.json", System.BinaryData.FromString(json));
    }
}
