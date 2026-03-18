import { useEffect, useMemo, useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { getSpecialties } from "../api/specialtyApi";

type Specialty = {
  id: number;
  name: string;
  active: boolean;
};

export default function SpecialtiesPage() {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [items, setItems] = useState<Specialty[]>([]);
  const [error, setError] = useState<string | null>(null);

  async function load() {
    setLoading(true);
    setError(null);

    try {
      const data = await getSpecialties();
      setItems(data);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""} al cargar especialidades`
        );
      } else {
        setError("Error inesperado al cargar especialidades");
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void load();
  }, []);

  const activeCount = useMemo(() => items.filter((s) => s.active).length, [items]);

  return (
    <div className="page">
      <div className="today-head">
        <div>
          <h2 className="today-title">Especialidades</h2>
          <p className="today-subtitle">
            {activeCount} activas • selecciona una para crear una receta
          </p>
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

      {loading && <p className="u-muted u-mt-4">Cargando...</p>}
      {error && <div className="alert alert-error u-mt-4">{error}</div>}

      {!loading && !error && (
        <div className="section-card">
          <div className="u-between">
            <div>
              <p className="progress-meta-title" style={{ margin: 0 }}>
                Lista de especialidades
              </p>
              <p className="progress-meta-sub" style={{ margin: "6px 0 0 0" }}>
                Usa una especialidad para registrar tu receta.
              </p>
            </div>
          </div>

          <div className="list">
            {items.map((s) => (
              <div key={s.id} className="item-card">
                <div className="item-main">
                  <h3 className="item-title">{s.name}</h3>
                  <p className="item-subtitle">Especialidad ID: {s.id}</p>

                  <div className="item-meta">
                    <span className={s.active ? "pill pill-active" : "pill pill-inactive"}>
                      {s.active ? "ACTIVA" : "INACTIVA"}
                    </span>
                  </div>
                </div>

                <div className="item-actions">
                  <button
                    type="button"
                    className="action-btn action-btn-primary"
                    disabled={!s.active}
                    onClick={() =>
                      navigate(
                        `/prescriptions/new?specialtyId=${s.id}&specialtyName=${encodeURIComponent(
                          s.name
                        )}`
                      )
                    }
                    title={
                      s.active
                        ? "Crear receta con esta especialidad"
                        : "Especialidad inactiva"
                    }
                  >
                    Crear receta
                  </button>
                </div>
              </div>
            ))}
          </div>

          {items.length === 0 && (
            <p className="u-muted u-mt-4">Aún no hay especialidades registradas.</p>
          )}
        </div>
      )}
    </div>
  );
}