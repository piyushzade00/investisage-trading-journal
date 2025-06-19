import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import tradingDashboard from "../assets/trading-dashboard.png"; // Add your dashboard image
import "../styles/Register.css"; // Import your CSS styles

const Register = () => {
  const { register } = useAuth();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    confirmPassword: "",
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

    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);
    try {
      const success = await register(
        formData.email,
        formData.password,
        formData.firstName,
        formData.lastName
      );
      if (success) {
        navigate("/login");
      } else {
        setError("Registration failed. Please check your details.");
      }
    } catch (err) {
      setError("An unexpected error occurred during registration.");
      console.error("Registration error:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="register-container">
      <div className="left-section">
        <h2>Get Started</h2>
        <p className="login-text">
          Already have an account? <Link to="/login">Log in</Link>
        </p>

        <button className="google-signup">
          <img src="/google-icon.svg" alt="Google" />
          Sign up with Google
        </button>

        <div className="divider">
          <span>or</span>
        </div>

        {error && <p className="error-message">{error}</p>}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              id="firstName"
              placeholder="First Name"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              id="lastName"
              placeholder="Last Name"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="email"
              id="email"
              placeholder="Email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              id="password"
              placeholder="Password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              id="confirmPassword"
              placeholder="Confirm Password"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
          </div>
          <button type="submit" className="signup-btn" disabled={loading}>
            {loading ? "Creating Account..." : "Sign Up"}
          </button>
        </form>
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

export default Register;
