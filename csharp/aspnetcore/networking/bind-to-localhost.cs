using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;

var builder = WebApplication.CreateBuilder(args);

// ruleid: bind-to-localhost
builder.WebHost.UseUrls("http://localhost:5000");

// ruleid: bind-to-localhost
builder.WebHost.ConfigureKestrel(options => options.ListenLocalhost(5001));

var app = builder.Build();

// ruleid: bind-to-localhost
app.Run("http://127.0.0.1:5000");

var good = WebApplication.CreateBuilder(args);

// ok: bind-to-localhost
good.WebHost.UseUrls("http://0.0.0.0:8080");

var goodApp = good.Build();

// ok: bind-to-localhost
goodApp.Run("http://+:8080");

// ok: bind-to-localhost
goodApp.Run();
