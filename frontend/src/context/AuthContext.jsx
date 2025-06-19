import React, { createContext, useState, useEffect, useContext } from "react";
import axios from "../api/axios";
import { useNavigate } from "react-router-dom";

const AuthContext = createContext(null);

// Custom hook to use the AuthContext
export const useAuth = () => useContext(AuthContext);

// AuthProvider component
export const AuthProvider = ({ children }) => {
  // Initialize state from localStorage to persist login across sessions
  const [token, setToken] = useState(localStorage.getItem("jwtToken"));
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(
    !!localStorage.getItem("jwtToken")
  ); // Check if token exists
  const navigate = useNavigate();

  // Update isAuthenticated and potentially decode user when token changes
  useEffect(() => {
    if (token) {
      setIsAuthenticated(true);
      localStorage.setItem("jwtToken", token);
    } else {
      setIsAuthenticated(false);
      setUser(null);
      localStorage.removeItem("jwtToken");
    }
  }, [token]);

  // Function to handle user login
  const login = async (email, password) => {
    try {
      const response = await axios.post("/auth/login", { email, password });
      const newToken = response.data.token;
      setToken(newToken);
      setIsAuthenticated(true);
      navigate("/dashboard");
      return true;
    } catch (error) {
      console.error("Login failed:", error.response?.data || error.message);
      setToken(null);
      setIsAuthenticated(false);
      return false;
    }
  };

  // Function to handle user registration
  const register = async (email, password, firstName, lastName) => {
    try {
      const response = await axios.post("/auth/register", {
        email,
        password,
        firstName,
        lastName,
      });
      console.log("Registration successful:", response.data);
      navigate("/login");
      return true;
    } catch (error) {
      console.error(
        "Registration failed:",
        error.response?.data || error.message
      );
      return false;
    }
  };

  // Function to handle user logout
  const logout = () => {
    setToken(null);
    setUser(null);
    setIsAuthenticated(false);
    localStorage.removeItem("jwtToken");
    navigate("/login");
  };

  // Value to be provided by the context
  const authContextValue = {
    token,
    user,
    isAuthenticated,
    login,
    register,
    logout,
  };

  return (
    <AuthContext.Provider value={authContextValue}>
      {children}
    </AuthContext.Provider>
  );
};
