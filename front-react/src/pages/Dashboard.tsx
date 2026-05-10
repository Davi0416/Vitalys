import { Routes, Route, Navigate } from 'react-router-dom';
import Header from '../components/Header';
import Inicio from '../components/Inicio';
import Pacientes from '../components/Pacientes';
import Agendamentos from '../components/Agendamentos';
import Profissionais from '../components/Profissionais';
import Calendario from '../components/Calendario';

export default function Dashboard() {
  return (
    <>
      <Header />
      <main>
        <Routes>
          <Route path="/"              element={<Inicio />} />
          <Route path="/pacientes"     element={<Pacientes />} />
          <Route path="/agendamentos"  element={<Agendamentos />} />
          <Route path="/profissionais" element={<Profissionais />} />
          <Route path="/calendario"    element={<Calendario />} />
          <Route path="*"              element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </>
  );
}
