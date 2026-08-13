using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Collections.Immutable;

namespace Example.Orders;

public class OrderRegistry
{
    // ruleid: static-mutable-collection
    private static readonly ConcurrentDictionary<string, Order> SessionCache = new();

    // ruleid: static-mutable-collection
    private static Dictionary<string, Order> sessions = new Dictionary<string, Order>();

    // ruleid: static-mutable-collection
    internal static List<string> PendingIds = new List<string>();

    // ruleid: static-mutable-collection
    private static HashSet<string> seen = new HashSet<string>();

    // ok: static-mutable-collection
    private static readonly ImmutableDictionary<string, string> StatusLabels =
        ImmutableDictionary<string, string>.Empty;

    // ok: static-mutable-collection
    private readonly Dictionary<string, Order> perInstance = new();

    // ok: static-mutable-collection
    private const int MaxRetries = 3;

    public void Register(Order order)
    {
        // ok: static-mutable-collection
        var local = new Dictionary<string, Order> { [order.Id] = order };
        foreach (var kv in local)
        {
            SessionCache[kv.Key] = kv.Value;
        }
    }

    public record Order(string Id);
}
