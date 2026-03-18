import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import { Link, useParams } from "react-router-dom";

import {
  getPrescriptionById,
  type PrescriptionResponse,
} from "../api/prescriptionApi";

import {
  getSchedulesByPrescription,
  type ScheduleResponse,
} from "../api/scheduleApi";

function formatDate(iso: string): string {
  try {
    return new Date(iso).toLocaleString("es-PE", { dateStyle: "medium", timeStyle: "short" });
  } catch {
    return iso;
  }
}

function formatFrequency(s: ScheduleResponse): string {
  if (s.frequencyType === "DAILY") {
    return `DAILY (${s.timesPerDay ?? "-"} veces/día)`;
  }
  if (s.frequencyType === "INTERVAL_HOURS") {
    return `INTERVAL_HOURS (cada ${s.intervalHours ?? "-"} horas)`;
  }
  return s.frequencyType;
}

export default function PrescriptionDetailPage() {
  const params = useParams<{ id: string }>();


  const id = useMemo(() => {
    const raw = params.id;
    if (!raw) return null;
    const n = Number(raw);
    return Number.isFinite(n) ? n : null;
  }, [params.id]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [prescription, setPrescription] = useState<PrescriptionResponse | null>(null);
  const [schedules, setSchedules] = useState<ScheduleResponse[]>([]);

  async function load(prescriptionId: number) {
    setLoading(true);
    setError(null);

    try {
      const [p, sch] = await Promise.all([
        getPrescriptionById(prescriptionId),
        getSchedulesByPrescription(prescriptionId),
      ]);

      setPrescription(p);

      const ordered = [...sch].sort((a, b) =>
        (a.startTime ?? "").localeCompare(b.startTime ?? "")
      );
      setSchedules(ordered);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""} al cargar detalle`
        );
      } else {
        setError("Error inesperado al cargar detalle");
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (id === null) {
      setLoading(false);
      setError("ID de receta inválido.");
      return;
    }
    void load(id);
  }, [id]);

  return (
    <div className="page">
      <div className="today-head">
        <div>
          <h2 className="today-title">Detalle de receta</h2>
          <p className="today-subtitle">Revisa la receta y administra sus horarios.</p>
        </div>

        <div className="today-actions">
          <Link className="btn-ghost" to="/prescriptions">
            Volver
          </Link>
          <Link className="btn-dark" to="/today">
            Hoy
          </Link>
        </div>
      </div>

      {loading && <p className="u-muted u-mt-4">Cargando...</p>}
      {error && <div className="alert alert-error u-mt-4">{error}</div>}

      {!loading && !error && prescription && (
        <>
          <div className="section-card">
            <div className="detail-header">
              <div>
                <h3 className="detail-title">{prescription.title}</h3>
                <p className="detail-note">
                  {prescription.notes?.trim() ? prescription.notes : "Sin notas"}
                </p>

                <div className="item-meta">
                  <span>Receta ID: {prescription.id}</span>
                  <span>Especialidad ID: {prescription.specialtyId}</span>
                  <span>Creada: {formatDate(prescription.createdAt)}</span>
                </div>
              </div>

              <div className="item-actions">
                <Link
                  className="action-btn action-btn-primary"
                  to={`/prescriptions/${prescription.id}/schedules/new`}
                >
                  + Agregar horario
                </Link>
              </div>
            </div>
          </div>

          <div className="section-card">
            <div className="u-between">
              <div>
                <p className="progress-meta-title" style={{ margin: 0 }}>
                  Horarios
                </p>
                <p className="progress-meta-sub" style={{ margin: "6px 0 0 0" }}>
                  Medicamentos y su configuración.
                </p>
              </div>
            </div>

            {schedules.length === 0 ? (
              <p className="u-muted u-mt-4">Esta receta aún no tiene horarios.</p>
            ) : (
              <div className="list">
                {schedules.map((s) => (
                  <div key={s.id} className="item-card">
                    <div className="item-main">
                      <h3 className="item-title">{s.medicineName}</h3>
                      <p className="item-subtitle">
                        {s.dose} • {formatFrequency(s)}
                      </p>

                      <div className="item-meta">
                        <span>Inicio: {s.startDate} {s.startTime}</span>
                        <span>Fin: {s.endDate}</span>
                        <span className={s.active ? "pill pill-active" : "pill pill-inactive"}>
                          {s.active ? "ACTIVO" : "INACTIVO"}
                        </span>
                      </div>
                    </div>

                    <div className="item-actions">
                      <span className="u-muted" style={{ fontWeight: 800 }}>
                        ID {s.id}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}