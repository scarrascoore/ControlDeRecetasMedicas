import { useMemo, useState } from "react";
import axios from "axios";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import {
  requestPasswordReset,
  verifyPasswordResetOtp,
  confirmPasswordReset,
} from "../api/authApi";
import LoginModal from "../components/Modal"; 

export default function ResetPasswordPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const emailQuery = useMemo(() => searchParams.get("email") ?? "", [searchParams]);

  const [email, setEmail] = useState(emailQuery);
  const [otp, setOtp] = useState("");
  const [resetToken, setResetToken] = useState<string | null>(null);

  const [newPassword, setNewPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");

  const [step, setStep] = useState<1 | 2 | 3>(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // modal login
  const [showLoginModal, setShowLoginModal] = useState(false);

  async function onSendOtp() {
    setError(null);

    if (!email.trim()) {
      setError("El correo es obligatorio.");
      return;
    }

    setLoading(true);
    try {
      await requestPasswordReset({ email: email.trim().toLowerCase() });
      setStep(2);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""}`
        );
      } else setError("Error inesperado");
    } finally {
      setLoading(false);
    }
  }

  async function onVerifyOtp() {
    setError(null);

    if (!/^\d{6}$/.test(otp.trim())) {
      setError("El OTP debe tener exactamente 6 dígitos.");
      return;
    }

    setLoading(true);
    try {
      const res = await verifyPasswordResetOtp({
        email: email.trim().toLowerCase(),
        otp: otp.trim(),
      });

      setResetToken(res.resetToken);
      setStep(3);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""}`
        );
      } else setError("Error inesperado");
    } finally {
      setLoading(false);
    }
  }

  async function onChangePassword() {
    setError(null);

    if (!resetToken) {
      setError("No hay token de reseteo. Verifica el OTP primero.");
      return;
    }

    if (newPassword.length < 6) {
      setError("La nueva contraseña debe tener al menos 6 caracteres.");
      return;
    }

    if (newPassword !== repeatPassword) {
      setError("Las contraseñas no coinciden.");
      return;
    }

    setLoading(true);
    try {
      await confirmPasswordReset({
        email: email.trim().toLowerCase(),
        resetToken,
        newPassword,
      });

      // Mostrar modal con login
      setShowLoginModal(true);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string } | undefined)?.message ??
            `Error ${err.response?.status ?? ""}`
        );
      } else setError("Error inesperado");
    } finally {
      setLoading(false);
    }
  }

  const passwordFieldsEnabled = step === 3;

  return (
    <div className="auth-shell">
      <div className="auth-card card">
        {/* Izquierda */}
        <div className="auth-left">
          <div className="brand">
            <div className="brand-badge">🔁</div>
            <div className="brand-name">ControlDeRecetaMedica</div>
          </div>

          <div>
            <h1 className="hero-title">Restablecer contraseña</h1>
            <p className="hero-sub u-mt-3">
              Te enviaremos un código OTP a tu correo. Luego podrás crear tu nueva contraseña.
            </p>

            <div className="quote-card">
              <p className="quote-text">
                “Si cambias tu contraseña, te enviaremos un correo de confirmación por seguridad.”
              </p>
              <div className="quote-footer">
                <span>🛡️</span>
                <span>Recuperación segura</span>
              </div>
            </div>
          </div>
        </div>

        {/* Derecha */}
        <div className="auth-right">
          <h2 className="auth-title">Recuperación</h2>
          <p className="auth-subtitle">Completa los pasos para recuperar tu cuenta.</p>

          {error && <div className="alert alert-error u-mt-4">{error}</div>}

          <div className="form">
            <label className="label">
              Correo *
              <input
                className="input"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="correo@gmail.com"
                disabled={step !== 1}
              />
            </label>

            {step >= 2 && (
              <label className="label">
                OTP (6 dígitos)
                <input
                  className="input"
                  value={otp}
                  onChange={(e) => setOtp(e.target.value.replace(/\D/g, "").slice(0, 6))}
                  placeholder="123456"
                  disabled={step !== 2}
                  style={{ letterSpacing: "0.25em", textAlign: "center", fontSize: 18 }}
                />
              </label>
            )}

            <div className="u-row" style={{ flexWrap: "wrap" }}>
              {step === 1 && (
                <button className="btn btn-primary" onClick={onSendOtp} disabled={loading}>
                  {loading ? "Enviando..." : "Enviar OTP"}
                </button>
              )}

              {step === 2 && (
                <button className="btn btn-primary" onClick={onVerifyOtp} disabled={loading}>
                  {loading ? "Verificando..." : "Verificar OTP"}
                </button>
              )}
            </div>

            <div style={{ height: 1, background: "var(--color-border)" }} />

            <label className="label">
              Nueva contraseña
              <input
                className="input"
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                placeholder="mínimo 6 caracteres"
                disabled={!passwordFieldsEnabled}
              />
            </label>

            <label className="label">
              Repetir nueva contraseña
              <input
                className="input"
                type="password"
                value={repeatPassword}
                onChange={(e) => setRepeatPassword(e.target.value)}
                placeholder="repite la contraseña"
                disabled={!passwordFieldsEnabled}
              />
            </label>

            <button
              className="btn btn-primary"
              onClick={onChangePassword}
              disabled={loading || !passwordFieldsEnabled}
            >
              {loading ? "Guardando..." : "Cambiar contraseña"}
            </button>

            <p className="u-center u-muted">
              <Link to="/login"><strong>Volver al login</strong></Link>
            </p>
          </div>
        </div>
      </div>

      {/* Modal login (misma lógica) */}
      <LoginModal
        open={showLoginModal}
        emailDefault={email.trim().toLowerCase()}
        onClose={() => {
          setShowLoginModal(false);
          navigate("/login");
        }}
        onLoggedIn={() => {
          setShowLoginModal(false);
          navigate("/today");
        }}
      />
    </div>
  );
}