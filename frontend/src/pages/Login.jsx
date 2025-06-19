import React, { useState } from "react";
import { useAuth } from "../context/AuthContext";
import { Link } from "react-router-dom";
import tradingDashboard from "../assets/trading-dashboard.png";
import "../styles/Login.css";

const Login = () => {
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.id]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const success = await login(formData.email, formData.password);
      if (!success) {
        setError("Invalid email or password");
      }
    } catch (err) {
      setError("An error occurred during login");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="left-section">
        <h2>Hi, Welcome Back!</h2>
        <p className="login-subtitle">
          Enter your credentials to access your account.
        </p>

        <button className="google-login">
          <img src="/google-icon.svg" alt="Google" />
          Log in with Google
        </button>

        <div className="divider">
          <span>Or</span>
        </div>

        {error && <p className="error-message">{error}</p>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              id="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>
              Password
              <Link to="/forgot-password" className="forgot-password">
                Forgot password?
              </Link>
            </label>
            <input
              type="password"
              id="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="login-btn" disabled={loading}>
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        <p className="signup-text">
          Don't have an account? <Link to="/register">Sign Up</Link>
        </p>
      </div>

      <div className="right-section">
        <h1>
          Trading Journal for the <span className="highlight">people</span>.
        </h1>
        <p>
          A free and ad free trading journal that's fast, easy to use and
          comprehensive enough to integrate into your daily routine.
        </p>
        <img
          src={tradingDashboard}
          alt="Trading Dashboard"
          className="dashboard-preview"
        />
      </div>
    </div>
  );
};

export default Login;
