using Microsoft.Data.SqlClient;
using Microsoft.Extensions.Configuration;
using StackExchange.Redis;

namespace Example.Orders;

public class DataAccess
{
    private readonly IConfiguration configuration;

    public DataAccess(IConfiguration configuration)
    {
        this.configuration = configuration;
    }

    // ruleid: hardcoded-connection-string
    private const string Sql =
        "Server=prod-db.internal;Database=orders;User Id=app;Password=s3cret;";

    public SqlConnection OpenHardcoded()
    {
        // ruleid: hardcoded-connection-string
        return new SqlConnection("Server=prod-db.internal;Database=orders;Password=s3cret;");
    }

    public string RedisHardcoded()
    {
        // ruleid: hardcoded-connection-string
        return "redis://prod-cache.internal:6379/1";
    }

    public ConnectionMultiplexer StorageHardcoded()
    {
        // ruleid: hardcoded-connection-string
        return ConnectionMultiplexer.Connect("DefaultEndpointsProtocol=https;AccountKey=abc123==");
    }

    public SqlConnection OpenFromConfig()
    {
        // ok: hardcoded-connection-string
        return new SqlConnection(configuration.GetConnectionString("Orders"));
    }

    // ok: hardcoded-connection-string
    private const string DocsUrl = "https://docs.example.com/database";
}
