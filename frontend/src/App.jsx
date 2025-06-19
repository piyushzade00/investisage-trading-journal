import { Routes, Route, Link } from "react-router-dom"; // Import Routes, Route, Link
import { useAuth } from "./context/AuthContext"; // Import useAuth hook
import "./App.css"; // You can remove this if not using default styling

import Register from "./pages/Register";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Home from "./pages/Home";
import PrivateRoute from "./components/PrivateRoute";

function App() {
  const { isAuthenticated, logout } = useAuth(); // Get auth state and logout function

  return (
    <>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />

        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
      </Routes>
    </>
  );
}

export default App;
