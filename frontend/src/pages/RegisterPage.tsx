import { useState } from "react";
import axios from "axios";
import { Link, useNavigate } from "react-router-dom";
import { registerUser } from "../api/authApi";

export default function RegisterPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setError(null);

    const eMail = email.trim().toLowerCase();
    if (!eMail) return setError("El correo es obligatorio.");
    if (!password.trim()) return setError("La contraseña es obligatoria.");
    if (password.length < 6) return setError("La contraseña debe tener al menos 6 caracteres.");
    if (password !== confirmPassword) return setError("Las contraseñas no coinciden.");

    setLoading(true);
    try {
      await registerUser({ email: eMail, password });
      navigate(`/verify?email=${encodeURIComponent(eMail)}`);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al registrar`;
        setError(msg);
      } else {
        setError("Error inesperado al registrar");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-shell">
      <div className="auth-card card">
        {/* Izquierda */}
        <div className="auth-left">
          <div className="brand">
            <div className="brand-badge">🧾</div>
            <div className="brand-name">ControlDeRecetaMedica</div>
          </div>

          <div>
            <h1 className="hero-title">Crea tu cuenta</h1>
            <p className="hero-sub u-mt-3">
              Regístrate para guardar tus recetas, horarios y controlar tus tomas de medicamentos.
            </p>

            <div className="quote-card">
              <p className="quote-text">
                “Una cuenta verificada te permite recibir recordatorios y mantener tu control al día.”
              </p>
              <div className="quote-footer">
                <span>✅</span>
                <span>Registro seguro</span>
              </div>
            </div>
          </div>
        </div>

        {/* Derecha */}
        <div className="auth-right">
          <h2 className="auth-title">Registro</h2>
          <p className="auth-subtitle">Completa tus datos para continuar.</p>

          {error && <div className="alert alert-error u-mt-4">{error}</div>}

          <form className="form" onSubmit={onSubmit}>
            <label className="label">
              Correo Electrónico
              <input
                className="input"
                type="email"
                placeholder="nombre@ejemplo.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
            </label>

            <label className="label">
              Contraseña
              <input
                className="input"
                type="password"
                placeholder="mínimo 6 caracteres"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </label>

            <label className="label">
              Repetir contraseña
              <input
                className="input"
                type="password"
                placeholder="repite tu contraseña"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
              />
            </label>

            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? "Registrando..." : "Crear cuenta"}
            </button>

            <p className="u-center u-muted">
              ¿Ya tienes cuenta?{" "}
              <Link to="/login"><strong>Inicia sesión</strong></Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}