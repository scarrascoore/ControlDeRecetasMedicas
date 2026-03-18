import { useMemo, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import axios from "axios";
import { createSchedule } from "../api/scheduleApi";

export default function ScheduleCreatePage() {
  const navigate = useNavigate();
  const params = useParams();

  const prescriptionId = useMemo(() => {
    const raw = params.prescriptionId;
    return raw ? Number(raw) : NaN;
  }, [params.prescriptionId]);

  const invalidPrescriptionId = Number.isNaN(prescriptionId);

  const [medicineName, setMedicineName] = useState("");
  const [dose, setDose] = useState("");
  const [timesPerDay, setTimesPerDay] = useState("3");

  const [startDate, setStartDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [endDate, setEndDate] = useState(() => new Date().toISOString().slice(0, 10));
  const [startTime, setStartTime] = useState("08:00");
  const [active, setActive] = useState(true);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function validate(): string | null {
    if (invalidPrescriptionId) return "Prescription ID inválido.";
    if (!medicineName.trim()) return "El nombre del medicamento es obligatorio.";
    if (!dose.trim()) return "La dosis es obligatoria.";
    if (!startDate) return "La fecha de inicio es obligatoria.";
    if (!endDate) return "La fecha de fin es obligatoria.";
    if (!startTime) return "La hora inicial es obligatoria.";

    if (endDate < startDate) return "La fecha fin no puede ser menor que la fecha inicio.";

    const n = Number(timesPerDay);
    if (!Number.isInteger(n) || n <= 0) {
      return "timesPerDay debe ser un número entero mayor a 0.";
    }

    return null;
  }

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    const validationError = validate();
    if (validationError) {
      setError(validationError);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await createSchedule(prescriptionId, {
        medicineName: medicineName.trim(),
        dose: dose.trim(),
        frequencyType: "DAILY",
        timesPerDay: Number(timesPerDay),
        intervalHours: null,
        startDate,
        endDate,
        active,
        startTime,
      });

      navigate("/today");
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al crear horario`;
        setError(msg);
      } else {
        setError("Error inesperado al crear horario");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="page">
      <div className="today-head">
        <div>
          <h2 className="today-title">Crear horario</h2>
          <p className="today-subtitle">Configura el medicamento y su frecuencia diaria.</p>
        </div>

        <div className="today-actions">
          <Link className="btn-ghost" to="/prescriptions">Volver</Link>
          <Link className="btn-dark" to="/today">Hoy</Link>
        </div>
      </div>

      <div className="helper-card">
        <p className="helper-title">Receta seleccionada</p>
        <p className="helper-sub">
          {invalidPrescriptionId ? "ID inválida" : `ID ${prescriptionId}`}
        </p>
      </div>

      {error && <div className="alert alert-error u-mt-4">{error}</div>}

      <div className="form-card">
        <form className="form-grid" onSubmit={onSubmit}>
          <label className="label">
            Medicamento *
            <input
              className="input"
              type="text"
              value={medicineName}
              onChange={(e) => setMedicineName(e.target.value)}
              placeholder="Ej: Ibuprofeno"
            />
          </label>

          <label className="label">
            Dosis *
            <input
              className="input"
              type="text"
              value={dose}
              onChange={(e) => setDose(e.target.value)}
              placeholder="Ej: 1 tableta"
            />
          </label>

          <label className="label">
            Tipo de frecuencia
            <input
              className="input"
              type="text"
              value="DAILY"
              disabled
            />
          </label>

          <label className="label">
            Veces por día *
            <input
              className="input"
              type="number"
              min={1}
              step={1}
              value={timesPerDay}
              onChange={(e) => setTimesPerDay(e.target.value)}
            />
          </label>

          <div className="form-two">
            <label className="label">
              Fecha inicio *
              <input
                className="input"
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
              />
            </label>

            <label className="label">
              Fecha fin *
              <input
                className="input"
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
              />
            </label>
          </div>

          <label className="label">
            Hora inicial *
            <input
              className="input"
              type="time"
              value={startTime}
              onChange={(e) => setStartTime(e.target.value)}
            />
          </label>

          <label className="checkbox-row">
            <input
              type="checkbox"
              checked={active}
              onChange={(e) => setActive(e.target.checked)}
            />
            Horario activo
          </label>

          <div className="btn-row">
            <button
              className="btn btn-primary btn-wide"
              type="submit"
              disabled={loading || invalidPrescriptionId}
            >
              {loading ? "Guardando..." : "Guardar horario"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}