import type { NetworkClient } from "../../types/api";

type RecentNetworkClientsProps = {
  clients: NetworkClient[];
};

function formatSeenAt(value: string): string {
  return new Date(value).toLocaleString();
}

export function RecentNetworkClients({ clients }: RecentNetworkClientsProps) {
  if (clients.length === 0) {
    return (
      <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-8 text-center text-slate-500">
        No network clients reported yet.
      </div>
    );
  }

  return (
    <div className="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm">
      <table className="min-w-full border-collapse text-sm">
        <thead className="bg-slate-50 text-left text-xs uppercase tracking-wide text-slate-500">
          <tr>
            <th className="px-4 py-3">Hostname</th>
            <th className="px-4 py-3">Vendor</th>
            <th className="px-4 py-3">IP</th>
            <th className="px-4 py-3">MAC</th>
            <th className="px-4 py-3">Router</th>
            <th className="px-4 py-3">Last seen</th>
          </tr>
        </thead>

        <tbody>
          {clients.map((client) => (
            <tr key={client.id} className="border-t border-slate-100">
              <td className="px-4 py-3 font-semibold text-slate-900">
                {client.hostname || "Unknown"}
              </td>
              <td className="px-4 py-3 text-slate-700">
                {client.vendor || "-"}
              </td>
              <td className="px-4 py-3 font-mono text-xs text-slate-700">
                {client.ip}
              </td>
              <td className="px-4 py-3 font-mono text-xs text-slate-500">
                {client.mac}
              </td>
              <td className="px-4 py-3 font-mono text-xs text-slate-500">
                {client.watchdogDeviceId}
              </td>
              <td className="px-4 py-3 text-slate-700">
                {formatSeenAt(client.lastSeenAt)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
