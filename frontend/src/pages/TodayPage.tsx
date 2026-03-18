import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import DoseCard from "../components/DoseCard";
import { getToday, markIntake, type TodayDose } from "../api/intakeApi";
import { Link } from "react-router-dom";

export default function TodayPage() {
  const [loading, setLoading] = useState(true);
  const [items, setItems] = useState<TodayDose[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [busyKey, setBusyKey] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);

    try {
      const data = await getToday();
      setItems(data);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al cargar /today`;
        setError(msg);
      } else {
        setError("Error inesperado al cargar /today");
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void load();
  }, []);

  function toHHmm(time: string): string {
    return time.length >= 5 ? time.slice(0, 5) : time;
  }

  async function onMark(scheduleId: number, plannedTime: string, status: "TAKEN" | "SKIPPED") {
    const key = `${scheduleId}-${plannedTime}-${status}`;
    setBusyKey(key);
    setError(null);
    setSuccessMessage(null);

    try {
      await markIntake(scheduleId, {
        plannedTime: toHHmm(plannedTime),
        status,
        note: null,
      });

      setSuccessMessage(
        status === "TAKEN"
          ? `✅ Dosis marcada como tomada (${toHHmm(plannedTime)})`
          : `⏭️ Dosis marcada como omitida (${toHHmm(plannedTime)})`
      );

      await load();
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al registrar dosis`;
        setError(msg);
      } else {
        setError("Error inesperado al registrar dosis");
      }
    } finally {
      setBusyKey(null);
    }
  }

  const sortedItems = useMemo(() => {
    return [...items].sort((a, b) => a.plannedTime.localeCompare(b.plannedTime));
  }, [items]);

  // Progreso diario
  const total = sortedItems.length;
  const done = sortedItems.filter((d) => d.status === "TAKEN").length;
  const percent = total === 0 ? 0 : Math.round((done / total) * 100);

  const todayText = useMemo(() => {
    const d = new Date();
    return d.toLocaleDateString("es-PE", { weekday: "long", day: "numeric", month: "long" });
  }, []);

  return (
    <div className="page">
      <div className="today-head">
        <div>
          <h2 className="today-title">Mis dosis de hoy</h2>
          <p className="today-subtitle">{todayText}</p>
        </div>

        <div className="today-actions">
          <Link className="btn-ghost" to="/prescriptions">
            Mis Recetas
          </Link>
          <Link className="btn-dark" to="/today">
            Hoy
          </Link>
        </div>
      </div>

      {/* Progreso */}
      <div className="progress-card">
        <div className="progress-left">
          <div className="progress-ring">{percent}%</div>
          <div>
            <p className="progress-meta-title">Progreso Diario</p>
            <p className="progress-meta-sub">
              {done} de {total} dosis completadas
            </p>
          </div>
        </div>
      </div>

      {loading && <p className="u-muted u-mt-4">Cargando...</p>}
      {successMessage && <div className="alert alert-success u-mt-4">{successMessage}</div>}
      {error && <div className="alert alert-error u-mt-4">{error}</div>}

      {!loading && !error && sortedItems.length === 0 && (
        <p className="u-muted u-mt-4">No hay dosis programadas para hoy.</p>
      )}

      <div className="today-list">
        {sortedItems.map((d) => {
          const busyTaken = busyKey === `${d.scheduleId}-${d.plannedTime}-TAKEN`;
          const busySkipped = busyKey === `${d.scheduleId}-${d.plannedTime}-SKIPPED`;

          return (
            <DoseCard
              key={`${d.scheduleId}-${d.plannedTime}`}
              medicineName={d.medicineName}
              dose={d.dose}
              plannedTime={d.plannedTime}
              status={d.status}
              busy={busyTaken || busySkipped}
              onTaken={() => onMark(d.scheduleId, d.plannedTime, "TAKEN")}
              onSkipped={() => onMark(d.scheduleId, d.plannedTime, "SKIPPED")}
            />
          );
        })}
      </div>
    </div>
  );
}