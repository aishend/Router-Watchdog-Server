import type { Command } from "../../types/api";

type CommandTableProps = {
  commands: Command[];
};

function formatDate(value: string | null): string {
  if (!value) {
    return "-";
  }

  return new Date(value).toLocaleString();
}

export function CommandTable({ commands }: CommandTableProps) {
  if (commands.length === 0) {
    return (
      <div className="rounded-2xl border border-dashed border-slate-300 bg-white p-8 text-center text-slate-500">
        No commands yet.
      </div>
    );
  }

  return (
    <div className="overflow-x-auto rounded-2xl border border-slate-200 bg-white shadow-sm">
      <table className="min-w-full border-collapse text-sm">
        <thead className="bg-slate-50 text-left text-xs uppercase tracking-wide text-slate-500">
          <tr>
            <th className="px-4 py-3">Type</th>
            <th className="px-4 py-3">Status</th>
            <th className="px-4 py-3">Command ID</th>
            <th className="px-4 py-3">Created</th>
            <th className="px-4 py-3">Delivered</th>
            <th className="px-4 py-3">Completed</th>
          </tr>
        </thead>
        <tbody>
          {commands.map((command) => (
            <tr key={command.id} className="border-t border-slate-100">
              <td className="px-4 py-3 font-semibold">{command.type}</td>
              <td className="px-4 py-3">{command.status}</td>
              <td className="px-4 py-3 font-mono text-xs text-slate-500">
                {command.id}
              </td>
              <td className="px-4 py-3">{formatDate(command.createdAt)}</td>
              <td className="px-4 py-3">{formatDate(command.deliveredAt)}</td>
              <td className="px-4 py-3">{formatDate(command.completedAt)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}