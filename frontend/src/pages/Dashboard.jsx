import React, { useState, useEffect, useRef } from "react";
import { useAuth } from "../context/AuthContext";
import Sidebar from "../components/dashboard/Sidebar";
import TradeMetrics from "../components/dashboard/TradeMetrics";
import accountService from "../api/accountService";
import AccountModal from "../components/dashboard/AccountModal";
import "../styles/Dashboard.css";

const Dashboard = () => {
  const [trades, setTrades] = useState([]);
  const [metrics, setMetrics] = useState({});
  const [timeFilter, setTimeFilter] = useState("today");
  const [accounts, setAccounts] = useState([]);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [isUserMenuOpen, setIsUserMenuOpen] = useState(false);
  const { user, logout } = useAuth();
  const userMenuRef = useRef();
  const [accountModalOpen, setAccountModalOpen] = useState(false);
  const [modalAccount, setModalAccount] = useState(null);
  const [isEdit, setIsEdit] = useState(false);

  // Fetch accounts when component mounts
  useEffect(() => {
    const fetchAccounts = async () => {
      try {
        setLoading(true);
        const accountsData = await accountService.getAccounts();
        setAccounts(accountsData);
        // If there are accounts, select the first one by default
        if (accountsData.length > 0) {
          // Try to find the primary account
          const primaryAccount = accountsData.find((acc) => acc.primary);
          setSelectedAccount(primaryAccount || accountsData[0]);
        }
      } catch (err) {
        setError("Failed to fetch accounts");
        console.error("Error fetching accounts:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchAccounts();
  }, []);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (userMenuRef.current && !userMenuRef.current.contains(event.target)) {
        setIsUserMenuOpen(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleAccountChange = async (accountId) => {
    const selected = accounts.find(
      (account) => account.id === parseInt(accountId)
    );
    setSelectedAccount(selected);
  };

  // Handler for Add Account
  const handleAddAccount = () => {
    setModalAccount({ name: "", primary: false, balance: 0, transactions: [] });
    setIsEdit(false);
    setAccountModalOpen(true);
  };

  // Handler for Edit Account
  const handleEditAccount = (account) => {
    setModalAccount(account);
    setIsEdit(true);
    setAccountModalOpen(true);
  };

  // Handler for Save
  const handleSaveAccount = (savedAccount) => {
    // If adding a new account (not editing)
    if (!isEdit) {
      // Add the new account to the accounts list
      setAccounts((prev) => [...prev, savedAccount]);
      // Select the new account
      setSelectedAccount(savedAccount);
    } else {
      // If editing, update the account in the list
      setAccounts((prev) =>
        prev.map((acc) => (acc.id === savedAccount.id ? savedAccount : acc))
      );

      setSelectedAccount(savedAccount);
    }
    setAccountModalOpen(false);
  };

  // Handler for Delete
  const handleDeleteAccount = async (account) => {
    if (
      !window.confirm(
        `Are you sure you want to delete account "${account.name}"?`
      )
    )
      return;
    try {
      await accountService.deleteAccount(account.id);
      // Remove from accounts list
      setAccounts((prev) => {
        const updated = prev.filter((acc) => acc.id !== account.id);
        // If no accounts left, clear selection
        if (updated.length === 0) {
          setSelectedAccount(null);
        } else {
          // If deleted account was selected, select the first remaining
          setSelectedAccount((sel) =>
            sel?.id === account.id ? updated[0] : sel
          );
        }
        return updated;
      });
      setAccountModalOpen(false);
    } catch (error) {
      alert("Failed to delete account. Please try again.");
    }
  };

  // Handler for New Account from modal
  const handleNewAccountFromModal = () => {
    setModalAccount({ name: "", primary: false, balance: 0, transactions: [] });
    setIsEdit(false);
  };

  const handleLogout = () => {
    logout();
    setIsUserMenuOpen(false);
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <div className="logo">
          <h1>Investisage</h1>
        </div>
        <div className="time-filters">
          <button className={timeFilter === "today" ? "active" : ""}>
            Today
          </button>
          <button className={timeFilter === "yesterday" ? "active" : ""}>
            Yesterday
          </button>
          <button className={timeFilter === "thisWeek" ? "active" : ""}>
            This wk.
          </button>
          <button className={timeFilter === "lastWeek" ? "active" : ""}>
            Last wk.
          </button>
          <button className={timeFilter === "thisMonth" ? "active" : ""}>
            This mo.
          </button>
        </div>
        <div className="user-menu" ref={userMenuRef}>
          <div
            className="user-menu-trigger"
            onClick={() => setIsUserMenuOpen(!isUserMenuOpen)}
          >
            <span>{user?.email}</span>
            <img src={user?.avatar || "/default-avatar.png"} alt="User" />
          </div>
          {isUserMenuOpen && (
            <div className="user-menu-dropdown">
              <div className="user-info">
                <img src={user?.avatar || "/default-avatar.png"} alt="User" />
                <div>
                  <p className="user-name">
                    {user?.firstName} {user?.lastName}
                  </p>
                  <p className="user-email">{user?.email}</p>
                </div>
              </div>
              <div className="menu-divider"></div>
              <button onClick={handleLogout} className="logout-button">
                <i className="icon-logout"></i>
                Logout
              </button>
            </div>
          )}
        </div>
      </header>

      <div className="dashboard-content">
        <Sidebar
          accounts={accounts}
          selectedAccount={selectedAccount}
          onAccountChange={handleAccountChange}
          balance={selectedAccount?.balance || {}}
          onAddAccount={handleAddAccount}
          onEditAccount={handleEditAccount}
        />
        <AccountModal
          open={accountModalOpen}
          onClose={() => setAccountModalOpen(false)}
          onSave={handleSaveAccount}
          onDelete={handleDeleteAccount}
          onNewAccount={handleNewAccountFromModal}
          account={modalAccount}
          isEdit={isEdit}
        />

        <main className="main-content">
          <div className="equity-chart">
            {/* Chart component will go here */}
          </div>

          <TradeMetrics metrics={metrics} />

          <div className="trades-list">
            <table>
              <thead>
                <tr>
                  <th>Date</th>
                  <th>Symbol</th>
                  <th>Status</th>
                  <th>Side</th>
                  <th>Qty</th>
                  <th>Entry</th>
                  <th>Exit</th>
                  <th>P&L</th>
                  <th>Duration</th>
                  <th>Return %</th>
                </tr>
              </thead>
              <tbody>
                {trades.map((trade) => (
                  <tr key={trade.id} className={trade.status.toLowerCase()}>
                    <td>{trade.date}</td>
                    <td>{trade.symbol}</td>
                    <td>{trade.status}</td>
                    <td>{trade.side}</td>
                    <td>{trade.quantity}</td>
                    <td>₹{trade.entry}</td>
                    <td>₹{trade.exit}</td>
                    <td className={trade.pnl >= 0 ? "positive" : "negative"}>
                      ₹{trade.pnl}
                    </td>
                    <td>{trade.duration}</td>
                    <td
                      className={
                        trade.returnPercentage >= 0 ? "positive" : "negative"
                      }
                    >
                      {trade.returnPercentage}%
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </main>
      </div>
    </div>
  );
};

export default Dashboard;
