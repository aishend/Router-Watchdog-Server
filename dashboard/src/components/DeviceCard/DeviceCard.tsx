import type { Device } from "../../types/api";

type DeviceCardProps = {
  device: Device;
  onRebootRouter: (deviceId: string) => void;
};

function formatUptime(seconds: number): string {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);

  if (hours > 0) {
    return `${hours}h ${minutes}m`;
  }

  return `${minutes}m`;
}

export function DeviceCard({ device, onRebootRouter }: DeviceCardProps) {
  const isUp = device.deviceStatus === "UP";

  return (
    <article className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
      <header className="mb-5 flex items-start justify-between gap-4">
        <div>
          <h3 className="text-lg font-bold text-slate-900">{device.deviceId}</h3>
          <p className="font-mono text-sm text-slate-500">{device.ip}</p>
        </div>

        <span
          className={
            isUp
              ? "rounded-full bg-emerald-100 px-3 py-1 text-xs font-bold text-emerald-800"
              : "rounded-full bg-red-100 px-3 py-1 text-xs font-bold text-red-800"
          }
        >
          {device.deviceStatus}
        </span>
      </header>

      <dl className="mb-5 grid grid-cols-2 gap-3">
        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Firmware
          </dt>
          <dd className="mt-1 font-bold text-slate-900">{device.firmwareVersion}</dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            RSSI
          </dt>
          <dd className="mt-1 font-bold text-slate-900">{device.rssi} dBm</dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Heap
          </dt>
          <dd className="mt-1 font-bold text-slate-900">
            {Math.round(device.freeHeap / 1024)} KB
          </dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Uptime
          </dt>
          <dd className="mt-1 font-bold text-slate-900">{formatUptime(device.uptime)}</dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Failures
          </dt>
          <dd className="mt-1 font-bold text-slate-900">{device.failures}</dd>
        </div>

        <div className="rounded-xl bg-slate-50 p-3">
          <dt className="text-xs font-semibold uppercase tracking-wide text-slate-500">
            Last heartbeat
          </dt>
          <dd className="mt-1 font-bold text-slate-900">
            {device.secondsSinceLastHeartbeat}s ago
          </dd>
        </div>
      </dl>

      <button
        className="w-full rounded-xl bg-slate-900 px-4 py-2.5 font-bold text-white hover:bg-slate-700"
        type="button"
        onClick={() => onRebootRouter(device.deviceId)}
      >
        Reboot router
      </button>
    </article>
  );
}