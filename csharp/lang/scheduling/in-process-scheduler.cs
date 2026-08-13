using System;
using System.Threading;
using System.Threading.Tasks;
using Hangfire;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

namespace Example.Orders;

public class Rollups
{
    private Timer? timer;

    public void StartReconciler()
    {
        // ruleid: in-process-scheduler
        timer = new Timer(_ => Reconcile(), null, TimeSpan.Zero, TimeSpan.FromMinutes(5));
    }

    public void ConfigureHangfire(IServiceCollection services)
    {
        // ruleid: in-process-scheduler
        services.AddHangfire(config => config.UseMemoryStorage());

        // ruleid: in-process-scheduler
        RecurringJob.AddOrUpdate("nightly", () => Reconcile(), Cron.Daily);
    }

    public void ConfigureShared(IServiceCollection services, string connectionString)
    {
        // ok: in-process-scheduler
        services.AddHangfire(config => config.UsePostgreSqlStorage(connectionString));
    }

    private void Reconcile() { }
}

// ok: in-process-scheduler
public class TriggeredWorker : BackgroundService
{
    protected override Task ExecuteAsync(CancellationToken stoppingToken) => Task.CompletedTask;
}
