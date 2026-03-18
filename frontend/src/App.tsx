import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import VerifyPage from "./pages/VerifyPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";

import ProtectedRoute from "./components/ProtectedRoute";
import AppLayout from "./app/AppLayout";

function HomeRedirect() {
  const token = localStorage.getItem("token");
  return <Navigate to={token ? "/today" : "/login"} replace />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomeRedirect />} />

        {/* públicas */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/verify" element={<VerifyPage />} />
        <Route path="/reset-password" element={<ResetPasswordPage />} />

        {/* privadas */}
        <Route
          path="/*"
          element={
            <ProtectedRoute>
              <AppLayout />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}