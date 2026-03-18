type DoseCardProps = {
  medicineName: string;
  dose: string;
  plannedTime: string;
  status: "PENDING" | "TAKEN" | "SKIPPED";
  busy?: boolean;
  onTaken: () => void;
  onSkipped: () => void;
};

function toHHmm(time: string): string {
  return time.length >= 5 ? time.slice(0, 5) : time;
}

function statusLabel(status: DoseCardProps["status"]): string {
  switch (status) {
    case "PENDING":
      return "PENDIENTE";
    case "TAKEN":
      return "TOMADA";
    case "SKIPPED":
      return "OMITIDA";
    default:
      return status;
  }
}

function pillClass(status: DoseCardProps["status"]) {
  if (status === "TAKEN") return "pill pill-taken";
  if (status === "SKIPPED") return "pill pill-skipped";
  return "pill pill-pending";
}

function iconFor(status: DoseCardProps["status"]) {
  if (status === "TAKEN") return "✓";
  if (status === "SKIPPED") return "⏭";
  return "🕒";
}

export default function DoseCard({
  medicineName,
  dose,
  plannedTime,
  status,
  busy = false,
  onTaken,
  onSkipped,
}: DoseCardProps) {
  const isFinal = status === "TAKEN" || status === "SKIPPED";
  const time = toHHmm(plannedTime);

  return (
    <div className="dose-item">
      <div className="dose-left">
        <div className="dose-icon">{iconFor(status)}</div>

        <div className="dose-text">
          <div className={pillClass(status)} style={{ display: "inline-block", marginBottom: 8 }}>
            {statusLabel(status)}
          </div>

          <h3 className="dose-name">{medicineName}</h3>
          <p className="dose-desc">{dose}</p>
        </div>
      </div>

      <div className="dose-right">
        <div className="dose-time">{time}</div>

        {/* Acciones solo cuando está PENDING */}
        {!isFinal ? (
          <div className="dose-actions">
            <button
              type="button"
              className="btn-small btn-small-primary"
              onClick={onTaken}
              disabled={busy}
              title="Marcar como tomada"
            >
              {busy ? "..." : "Tomada"}
            </button>

            <button
              type="button"
              className="btn-small"
              onClick={onSkipped}
              disabled={busy}
              title="Marcar como omitida"
            >
              {busy ? "..." : "Omitir"}
            </button>
          </div>
        ) : null}
      </div>
    </div>
  );
}