using System.Net.Http;
using System.Threading.Tasks;

namespace Example.Orders;

public class InventoryClient
{
    private readonly IHttpClientFactory factory;

    public InventoryClient(IHttpClientFactory factory)
    {
        this.factory = factory;
    }

    public async Task<string> LeakySocketsAsync()
    {
        // ruleid: httpclient-instantiated-directly
        using var client = new HttpClient();
        return await client.GetStringAsync("https://inventory.internal/stock");
    }

    public async Task<string> AlsoLeakyAsync()
    {
        // ruleid: httpclient-instantiated-directly
        var client = new System.Net.Http.HttpClient { Timeout = System.TimeSpan.FromSeconds(5) };
        return await client.GetStringAsync("https://inventory.internal/stock");
    }

    public async Task<string> PooledAsync()
    {
        // ok: httpclient-instantiated-directly
        var client = factory.CreateClient("inventory");
        return await client.GetStringAsync("/stock");
    }
}
