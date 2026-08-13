using NLog.Targets;
using Serilog;
using Serilog.Formatting.Compact;

namespace Example.Orders;

public static class Logging
{
    public static ILogger FileLogger()
    {
        // ruleid: log-to-local-file
        return new LoggerConfiguration()
            .WriteTo.File("logs/app.log", rollingInterval: RollingInterval.Day)
            .CreateLogger();
    }

    public static FileTarget NLogFileTarget()
    {
        // ruleid: log-to-local-file
        return new FileTarget("file") { FileName = "/var/log/app/app.log" };
    }

    public static ILogger ConsoleLogger()
    {
        // ok: log-to-local-file
        return new LoggerConfiguration()
            .WriteTo.Console(new CompactJsonFormatter())
            .CreateLogger();
    }
}
