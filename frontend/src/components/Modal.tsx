import { useEffect, useState } from "react";
import axios from "axios";
import { login } from "../api/authApi";

type Props = {
  open: boolean;
  emailDefault: string;
  onClose: () => void;
  onLoggedIn: () => void; // lo llamamos cuando el login sale OK
};

export default function LoginModal({ open, emailDefault, onClose, onLoggedIn }: Props) {
  const [email, setEmail] = useState(emailDefault);
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // ✅ Mantener email sincronizado si cambia emailDefault
  useEffect(() => {
    if (open) {
      setEmail(emailDefault);
      setPassword("");
      setError(null);
    }
  }, [open, emailDefault]);

  if (!open) return null;

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    if (!email.trim()) {
      setError("El correo es obligatorio.");
      return;
    }
    if (!password.trim()) {
      setError("La contraseña es obligatoria.");
      return;
    }

    setLoading(true);
    try {
      const res = await login({ email: email.trim().toLowerCase(), password });

      localStorage.setItem("token", res.token);
      onLoggedIn();
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
    <div
      role="dialog"
      aria-modal="true"
      onClick={onClose}
      style={{
        position: "fixed",
        inset: 0,
        background: "rgba(0,0,0,0.6)",
        display: "grid",
        placeItems: "center",
        padding: 16,
        zIndex: 9999,
      }}
    >
      <div
        onClick={(e) => e.stopPropagation()}
        className="card"
        style={{
          width: "min(520px, 100%)",
          padding: 18,
          borderRadius: "var(--radius-xl)",
          background: "var(--color-bg)",
        }}
      >
        <div className="u-between">
          <h3 style={{ margin: 0, fontSize: "var(--text-lg)", fontWeight: 900 }}>
            Contraseña actualizada ✅
          </h3>

          <button
            onClick={onClose}
            className="btn"
            style={{
              width: "auto",
              padding: "8px 10px",
              fontWeight: 900,
              background: "transparent",
            }}
            aria-label="Cerrar"
            type="button"
          >
            ✕
          </button>
        </div>

        <p className="u-muted u-mt-3" style={{ marginBottom: 0 }}>
          Ahora puedes iniciar sesión con tu nueva contraseña.
        </p>

        {error && <div className="alert alert-error u-mt-4">{error}</div>}

        <form className="form" onSubmit={onSubmit}>
          <label className="label">
            Email
            <input
              className="input"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="correo@gmail.com"
            />
          </label>

          <label className="label">
            Contraseña
            <input
              className="input"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
            />
          </label>

          <button className="btn btn-primary" type="submit" disabled={loading}>
            {loading ? "Ingresando..." : "Iniciar sesión"}
          </button>
        </form>
      </div>
    </div>
  );
}