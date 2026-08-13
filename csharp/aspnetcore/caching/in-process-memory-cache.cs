using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;

var builder = WebApplication.CreateBuilder(args);

// ruleid: in-process-memory-cache
builder.Services.AddMemoryCache();

// ruleid: in-process-memory-cache
builder.Services.AddDistributedMemoryCache();

// ok: in-process-memory-cache
builder.Services.AddStackExchangeRedisCache(options =>
{
    options.Configuration = builder.Configuration.GetConnectionString("Redis");
    options.InstanceName = "orders:";
});

// ok: in-process-memory-cache
builder.Services.AddSqlServerCache(options =>
{
    options.ConnectionString = builder.Configuration.GetConnectionString("Orders");
    options.SchemaName = "dbo";
    options.TableName = "CacheEntries";
});

var app = builder.Build();
app.Run();
