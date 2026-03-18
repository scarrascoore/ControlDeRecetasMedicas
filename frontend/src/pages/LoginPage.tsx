import { useMemo, useState } from "react";
import axios from "axios";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { login } from "../api/authApi";

export default function LoginPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const welcome = useMemo(() => searchParams.get("welcome") === "1", [searchParams]);
  const emailFromQuery = useMemo(() => searchParams.get("email") ?? "", [searchParams]);

  const [email, setEmail] = useState(emailFromQuery);
  const [password, setPassword] = useState("");
  const [remember, setRemember] = useState(false);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setError(null);

    if (!email.trim()) return setError("El correo es obligatorio.");
    if (!password.trim()) return setError("La contraseña es obligatoria.");

    setLoading(true);
    try {
      const res = await login({ email: email.trim().toLowerCase(), password });

      // token
      localStorage.setItem("token", res.token);

      localStorage.setItem("remember", remember ? "1" : "0");

      navigate("/today");
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al iniciar sesión`;
        setError(msg);
      } else {
        setError("Error inesperado al iniciar sesión");
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
            <div className="brand-badge">💊</div>
            <div className="brand-name">ControlDeRecetaMedica</div>
          </div>

          <div>
            <h1 className="hero-title">Tu salud, nuestra prioridad.</h1>
            <p className="hero-sub u-mt-3">
              Controla tus recetas médicas, horarios y seguimiento diario de tomas en un solo lugar.
            </p>

            <div className="quote-card">
              <p className="quote-text">
                “Un recordatorio a tiempo puede marcar la diferencia. Mantén tu tratamiento al día.”
              </p>
              <div className="quote-footer">
                <span>🏥</span>
                <span>Seguimiento inteligente</span>
              </div>
            </div>
          </div>
        </div>

        {/* Derecha */}
        <div className="auth-right">
          <h2 className="auth-title">Bienvenido de nuevo</h2>
          <p className="auth-subtitle">Ingresa tus credenciales para continuar.</p>

          {welcome && (
            <div className="alert alert-success u-mt-4">
              🎉 Tu cuenta ya está verificada. Ya puedes iniciar sesión.
            </div>
          )}

          {error && (
            <div className="alert alert-error u-mt-4">
              {error}
            </div>
          )}

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
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </label>

            <div className="form-row">
              <label className="checkbox">
                <input
                  type="checkbox"
                  checked={remember}
                  onChange={(e) => setRemember(e.target.checked)}
                />
                Recordarme
              </label>

              <Link to={`/reset-password?email=${encodeURIComponent(email)}`}>
                ¿Olvidaste tu contraseña?
              </Link>
            </div>

            <button className="btn btn-primary" type="submit" disabled={loading}>
              {loading ? "Ingresando..." : "Iniciar Sesión"}
            </button>

            <p className="u-center u-muted">
              ¿No tienes una cuenta?{" "}
              <Link to="/register"><strong>Regístrate ahora</strong></Link>
            </p>
          </form>
        </div>
      </div>
    </div>
  );
}