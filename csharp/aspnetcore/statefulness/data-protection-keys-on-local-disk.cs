using System.IO;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.DataProtection;
using Microsoft.Extensions.DependencyInjection;

var builder = WebApplication.CreateBuilder(args);

// ruleid: data-protection-keys-on-local-disk
builder.Services.AddDataProtection()
    .PersistKeysToFileSystem(new DirectoryInfo("/var/keys"));

// ruleid: data-protection-keys-on-local-disk
builder.Services.AddSession(options => options.IdleTimeout = System.TimeSpan.FromMinutes(20));

var shared = WebApplication.CreateBuilder(args);

// ok: data-protection-keys-on-local-disk
shared.Services.AddStackExchangeRedisCache(o =>
    o.Configuration = shared.Configuration.GetConnectionString("Redis"));

// ok: data-protection-keys-on-local-disk
shared.Services.AddSession(options => options.IdleTimeout = System.TimeSpan.FromMinutes(20));

// ok: data-protection-keys-on-local-disk
shared.Services.AddDataProtection()
    .PersistKeysToAzureBlobStorage(shared.Configuration["KeyBlobUri"])
    .ProtectKeysWithAzureKeyVault(new System.Uri(shared.Configuration["KeyVaultKeyId"]), credential);

var app = builder.Build();
app.Run();
