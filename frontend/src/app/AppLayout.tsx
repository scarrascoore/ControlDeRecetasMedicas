import { Link, Route, Routes, useNavigate } from "react-router-dom";

import TodayPage from "../pages/TodayPage";
import SpecialtiesPage from "../pages/SpecialtiesPage";
import PrescriptionsPage from "../pages/PrescriptionsPage";
import PrescriptionCreatePage from "../pages/PrescriptionCreatePage";
import PrescriptionDetailPage from "../pages/PrescriptionDetailPage";
import ScheduleCreatePage from "../pages/ScheduleCreatePage";

export default function AppLayout() {
  const navigate = useNavigate();

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("remember");
    navigate("/login");
  }

  return (
    <div>
      <nav className="nav">
        <div className="nav-left">
          <Link className="nav-link" to="/today">Hoy</Link>
          <Link className="nav-link" to="/prescriptions">Recetas</Link>
          <Link className="nav-link" to="/specialties">Especialidades</Link>
        </div>

        <button className="btn btn-primary nav-logout" onClick={logout} type="button">
          Cerrar sesión
        </button>
      </nav>

      <Routes>
        <Route path="/today" element={<TodayPage />} />
        <Route path="/specialties" element={<SpecialtiesPage />} />
        <Route path="/prescriptions" element={<PrescriptionsPage />} />
        <Route path="/prescriptions/new" element={<PrescriptionCreatePage />} />
        <Route path="/prescriptions/:id" element={<PrescriptionDetailPage />} />
        <Route path="/prescriptions/:prescriptionId/schedules/new" element={<ScheduleCreatePage />} />
      </Routes>
    </div>
  );
}