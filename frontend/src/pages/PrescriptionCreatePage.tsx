import { useMemo, useState } from "react";
import { useNavigate, useSearchParams, Link } from "react-router-dom";
import axios from "axios";
import { createPrescription } from "../api/prescriptionApi";

export default function PrescriptionCreatePage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const specialtyId = useMemo(() => {
    const raw = searchParams.get("specialtyId");
    return raw ? Number(raw) : NaN;
  }, [searchParams]);

  const specialtyName = searchParams.get("specialtyName") ?? "";

  const [title, setTitle] = useState("");
  const [notes, setNotes] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const invalidSpecialty = Number.isNaN(specialtyId);

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    if (invalidSpecialty) {
      setError("Especialidad inválida. Regresa y selecciona una especialidad.");
      return;
    }

    if (!title.trim()) {
      setError("El título es obligatorio.");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const created = await createPrescription({
        specialtyId,
        title: title.trim(),
        notes: notes.trim() ? notes.trim() : null,
      });

      navigate(`/prescriptions/${created.id}/schedules/new`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al crear receta`;
        setError(msg);
      } else {
        setError("Error inesperado al crear receta");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="page">
      <div className="today-head">
        <div>
          <h2 className="today-title">Crear receta</h2>
          <p className="today-subtitle">Registra el título y notas de tu receta.</p>
        </div>

        <div className="today-actions">
          <Link className="btn-ghost" to="/specialties">Volver</Link>
          <Link className="btn-dark" to="/today">Hoy</Link>
        </div>
      </div>

      <div className="helper-card">
        <p className="helper-title">Especialidad seleccionada</p>
        <p className="helper-sub">
          {specialtyName || "(sin nombre)"}{" "}
          {!invalidSpecialty ? `— ID ${specialtyId}` : ""}
        </p>

        {invalidSpecialty && (
          <div className="alert alert-error u-mt-4">
            No se recibió specialtyId. Debes entrar desde la lista de especialidades.
          </div>
        )}
      </div>

      {error && <div className="alert alert-error u-mt-4">{error}</div>}

      <div className="form-card">
        <form className="form-grid" onSubmit={onSubmit}>
          <label className="label">
            Título de la receta *
            <input
              className="input"
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Ej: Receta febrero - dolor muscular"
              maxLength={120}
            />
          </label>

          <label className="label">
            Notas (opcional)
            <textarea
              className="textarea"
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="Indicaciones médicas, observaciones..."
              rows={4}
              maxLength={500}
            />
          </label>

          <button
            className="btn btn-primary btn-wide"
            type="submit"
            disabled={loading || invalidSpecialty}
          >
            {loading ? "Creando..." : "Crear receta"}
          </button>
        </form>
      </div>
    </div>
  );
}