import { useMemo, useState } from "react";
import axios from "axios";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { verifyUser } from "../api/authApi";

export default function VerifyPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const email = useMemo(() => searchParams.get("email") ?? "", [searchParams]);

  const [otp, setOtp] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  async function onSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setError(null);
    setSuccessMessage(null);

    if (!email.trim()) {
      setError("No se encontró el correo. Regresa al registro.");
      return;
    }

    if (!/^\d{6}$/.test(otp.trim())) {
      setError("El código debe tener exactamente 6 dígitos.");
      return;
    }

    setLoading(true);

    try {
      await verifyUser({
        email: email.trim().toLowerCase(),
        otp: otp.trim(),
      });

      setSuccessMessage(`🎉 Bienvenido, ${email}. Tu cuenta ha sido verificada correctamente.`);

      setTimeout(() => {
        navigate(`/login?welcome=1&email=${encodeURIComponent(email.trim().toLowerCase())}`);
      }, 1800);
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        const msg =
          (err.response?.data as { message?: string } | undefined)?.message ??
          `Error ${err.response?.status ?? ""} al verificar código`;
        setError(msg);
      } else {
        setError("Error inesperado al verificar");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ maxWidth: 460, margin: "40px auto", fontFamily: "system-ui" }}>
      <h2>Verificar cuenta</h2>

      <div style={{ marginBottom: 12 }}>
        <Link to="/register">← Volver al registro</Link>
      </div>

      <div
        style={{
          border: "1px solid #333",
          borderRadius: 12,
          padding: 14,
          background: "#1a1a1a",
          marginBottom: 14,
        }}
      >
        <strong>Correo:</strong> {email || "No disponible"}
        <p style={{ marginBottom: 0, opacity: 0.85 }}>
          Ingresa el código de 6 dígitos que llegó a tu correo.
        </p>
      </div>

      <form
        onSubmit={onSubmit}
        style={{
          display: "grid",
          gap: 12,
          border: "1px solid #333",
          borderRadius: 12,
          padding: 16,
          background: "#1a1a1a",
        }}
      >
        <label style={{ display: "grid", gap: 6 }}>
          <span>Código de verificación *</span>
          <input
            type="text"
            inputMode="numeric"
            maxLength={6}
            value={otp}
            onChange={(e) => setOtp(e.target.value.replace(/\D/g, ""))}
            placeholder="123456"
            style={{
              padding: 10,
              borderRadius: 8,
              border: "1px solid #555",
              letterSpacing: "0.25em",
              fontSize: 18,
              textAlign: "center",
            }}
          />
        </label>

        {successMessage && (
          <p
            style={{
              color: "#b6f0c2",
              background: "#102317",
              border: "1px solid #244c30",
              padding: "10px 12px",
              borderRadius: 8,
              margin: 0,
            }}
          >
            {successMessage}
          </p>
        )}

        {error && (
          <p
            style={{
              color: "tomato",
              background: "#2a1414",
              border: "1px solid #5a2b2b",
              padding: "10px 12px",
              borderRadius: 8,
              margin: 0,
            }}
          >
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={loading}
          style={{ padding: "10px 14px", cursor: "pointer" }}
        >
          {loading ? "Verificando..." : "Verificar cuenta"}
        </button>
      </form>
    </div>
  );
}