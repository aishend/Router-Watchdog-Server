import { useEffect, useState } from "react";

import {
  getCommandsByDevice,
  getNetworkClientsByDevice,
} from "../../api/routerWatchdogApi";
import { CommandTable } from "../../components/CommandTable";
import { RecentNetworkClients } from "../../components/RecentNetworkClients";
import type { Command, Device, NetworkClient } from "../../types/api";
import { formatDuration } from "../../utils/formatDuration";

type DeviceDetailPageProps = {
  device: Device;
  onBack: () => void;
};

export function DeviceDetailPage({ device, onBack }: DeviceDetailPageProps) {
  const [commands, setCommands] = useState<Command[]>([]);
  const [networkClients, setNetworkClients] = useState<NetworkClient[]>([]);
  const [error, setError] = useState<string | null>(null);

  async function loadDeviceDetails() {
    try {
      const [commandsResponse, networkClientsResponse] = await Promise.all([
        getCommandsByDevice(device.deviceId),
        getNetworkClientsByDevice(device.deviceId),
      ]);

      setCommands(commandsResponse);
      setNetworkClients(networkClientsResponse.clients);
      setError(null);
    } catch {
      setError("Failed to load device details");
    }
  }

  useEffect(() => {
    loadDeviceDetails();

    const intervalId = window.setInterval(loadDeviceDetails, 3000);

    return () => {
      window.clearInterval(intervalId);
    };
  }, [device.deviceId]);

  return (
    <main className="min-h-screen bg-slate-100 px-4 py-10 text-slate-900">
      <div className="mx-auto max-w-6xl">
        <button
          type="button"
          onClick={onBack}
          className="mb-6 rounded-xl border border-slate-300 bg-white px-4 py-2 font-semibold text-slate-700 hover:bg-slate-50"
        >
          Back to dashboard
        </button>

        <header className="mb-8 rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
          <div className="flex flex-wrap items-start justify-between gap-4">
            <div>
              <p className="text-sm font-semibold uppercase tracking-wide text-slate-500">
                Router detail
              </p>

              <h1 className="mt-1 text-3xl font-bold">
                {device.displayName || device.deviceId}
              </h1>

              <p className="mt-1 font-mono text-sm text-slate-500">
                {device.deviceId}
              </p>

              {device.location && (
                <p className="mt-2 text-slate-600">{device.location}</p>
              )}

              {device.notes && (
                <p className="mt-2 text-sm text-slate-500">{device.notes}</p>
              )}
            </div>

            <span
              className={
                device.deviceStatus === "UP"
                  ? "rounded-full bg-emerald-100 px-3 py-1 text-xs font-bold text-emerald-800"
                  : "rounded-full bg-red-100 px-3 py-1 text-xs font-bold text-red-800"
              }
            >
              {device.deviceStatus}
            </span>
          </div>

          <dl className="mt-6 grid gap-3 md:grid-cols-4">
            <div className="rounded-xl bg-slate-50 p-3">
              <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                IP
              </dt>
              <dd className="mt-1 font-mono text-sm font-bold">{device.ip}</dd>
            </div>

            <div className="rounded-xl bg-slate-50 p-3">
              <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                Gateway
              </dt>
              <dd className="mt-1 font-mono text-sm font-bold">{device.gateway}</dd>
            </div>

            <div className="rounded-xl bg-slate-50 p-3">
              <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                RSSI
              </dt>
              <dd className="mt-1 font-bold">{device.rssi} dBm</dd>
            </div>

            <div className="rounded-xl bg-slate-50 p-3">
              <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
                Last heartbeat
              </dt>
              <dd className="mt-1 font-bold">
                {formatDuration(device.secondsSinceLastHeartbeat)} ago
              </dd>
            </div>
          </dl>
        </header>

        {error && (
          <div className="mb-6 rounded-xl border border-red-200 bg-red-50 p-4 text-red-700">
            {error}
          </div>
        )}

        <section className="mb-10">
          <h2 className="mb-4 text-xl font-bold">
            Recent Network Clients
          </h2>
          <RecentNetworkClients clients={networkClients} />
        </section>

        <section>
          <h2 className="mb-4 text-xl font-bold">Commands</h2>
          <CommandTable commands={commands} />
        </section>
      </div>
    </main>
  );
}
