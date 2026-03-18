import { useEffect, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { getPrescriptions } from "../api/prescriptionApi";
import { getSpecialties } from "../api/specialtyApi";

type Prescription = {
  id: number;
  specialtyId: number;
  title: string;
  notes: string | null;
  createdAt: string;
};

type Specialty = {
  id: number;
  name: string;
  active: boolean;
};

function formatDate(iso: string): string {
  try {
    return new Date(iso).toLocaleString("es-PE", {
      dateStyle: "medium",
      timeStyle: "short",
    });
  } catch {
    return iso;
  }
}

export default function PrescriptionsPage() {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [items, setItems] = useState<Prescription[]>([]);
  const [error, setError] = useState<string | null>(null);

  const [showSpecialtyModal, setShowSpecialtyModal] = useState(false);
  const [specialtiesLoading, setSpecialtiesLoading] = useState(false);
  const [specialtiesError, setSpecialtiesError] = useState<string | null>(null);
  const [specialties, setSpecialties] = useState<Specialty[]>([]);

  async function load() {
    setLoading(true);
    setError(null);

    try {
      const data = await getPrescriptions();
      setItems(data);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""} al cargar recetas`
        );
      } else {
        setError("Error inesperado al cargar recetas");
      }
    } finally {
      setLoading(false);
    }
  }

  async function openSpecialtyModal() {
    setShowSpecialtyModal(true);
    setSpecialtiesLoading(true);
    setSpecialtiesError(null);

    try {
      const data = await getSpecialties();
      setSpecialties(data.filter((s) => s.active));
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setSpecialtiesError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""} al cargar especialidades`
        );
      } else {
        setSpecialtiesError("Error inesperado al cargar especialidades");
      }
    } finally {
      setSpecialtiesLoading(false);
    }
  }

  function closeSpecialtyModal() {
    setShowSpecialtyModal(false);
    setSpecialtiesError(null);
  }

  function goToCreatePrescription(specialty: Specialty) {
    navigate(
      `/prescriptions/new?specialtyId=${specialty.id}&specialtyName=${encodeURIComponent(
        specialty.name
      )}`
    );
  }

  useEffect(() => {
    void load();
  }, []);

  return (
    <div className="page">
      <div className="today-head">
        <div>
          <h2 className="today-title">Mis recetas</h2>
          <p className="today-subtitle">
            Gestiona tus recetas y agrega horarios de medicación.
          </p>
        </div>

        <div className="today-actions">
          <Link className="btn-ghost" to="/specialties">
            Especialidades
          </Link>
          <Link className="btn-dark" to="/today">
            Hoy
          </Link>
        </div>
      </div>

      {loading && <p className="u-muted u-mt-4">Cargando...</p>}
      {error && <div className="alert alert-error u-mt-4">{error}</div>}

      {!loading && !error && (
        <div className="section-card">
          <div className="u-between">
            <div>
              <p className="progress-meta-title" style={{ margin: 0 }}>
                Tus recetas
              </p>
              <p className="progress-meta-sub" style={{ margin: "6px 0 0 0" }}>
                Selecciona una receta para ver el detalle y sus horarios.
              </p>
            </div>

            <button
              type="button"
              className="btn-ghost"
              onClick={() => void openSpecialtyModal()}
            >
              + Nueva receta
            </button>
          </div>

          {items.length === 0 ? (
            <p className="u-muted u-mt-4">Aún no tienes recetas registradas.</p>
          ) : (
            <div className="list">
              {items.map((p) => (
                <div key={p.id} className="item-card">
                  <div className="item-main">
                    <h3 className="item-title">{p.title}</h3>
                    <p className="item-subtitle">
                      Especialidad ID: {p.specialtyId}
                      {p.notes?.trim() ? ` • ${p.notes}` : ""}
                    </p>

                    <div className="item-meta">
                      <span>Receta ID: {p.id}</span>
                      <span>Creada: {formatDate(p.createdAt)}</span>
                    </div>
                  </div>

                  <div className="item-actions">
                    <Link className="action-btn" to={`/prescriptions/${p.id}`}>
                      Ver detalle
                    </Link>

                    <Link
                      className="action-btn action-btn-primary"
                      to={`/prescriptions/${p.id}/schedules/new`}
                    >
                      Agregar horario
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {showSpecialtyModal && (
        <div
          style={{
            position: "fixed",
            inset: 0,
            background: "rgba(0,0,0,0.35)",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            zIndex: 999,
            padding: "16px",
          }}
          onClick={closeSpecialtyModal}
        >
          <div
            style={{
              width: "100%",
              maxWidth: "520px",
              background: "#fff",
              borderRadius: "18px",
              padding: "20px",
              boxShadow: "0 20px 50px rgba(0,0,0,0.2)",
            }}
            onClick={(e) => e.stopPropagation()}
          >
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                gap: "12px",
                marginBottom: "12px",
              }}
            >
              <div>
                <h3 style={{ margin: 0 }}>Selecciona una especialidad</h3>
                <p style={{ margin: "6px 0 0 0", color: "#666" }}>
                  Elige una especialidad para crear la receta.
                </p>
              </div>

              <button
                type="button"
                className="btn-ghost"
                onClick={closeSpecialtyModal}
              >
                Cerrar
              </button>
            </div>

            {specialtiesLoading && (
              <p className="u-muted u-mt-4">Cargando especialidades...</p>
            )}

            {specialtiesError && (
              <div className="alert alert-error u-mt-4">{specialtiesError}</div>
            )}

            {!specialtiesLoading && !specialtiesError && specialties.length === 0 && (
              <p className="u-muted u-mt-4">
                No hay especialidades activas disponibles.
              </p>
            )}

            {!specialtiesLoading && !specialtiesError && specialties.length > 0 && (
              <div style={{ display: "grid", gap: "10px", marginTop: "16px" }}>
                {specialties.map((s) => (
                  <button
                    key={s.id}
                    type="button"
                    className="action-btn action-btn-primary"
                    style={{ width: "100%", justifyContent: "center" }}
                    onClick={() => goToCreatePrescription(s)}
                  >
                    {s.name}
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}