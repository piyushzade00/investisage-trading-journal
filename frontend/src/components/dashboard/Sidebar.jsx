import React from "react";
import { Link } from "react-router-dom";

const Sidebar = ({
  selectedAccount,
  accounts,
  balance,
  onAccountChange,
  onAddAccount,
  onEditAccount,
}) => {
  const handleSelectChange = (e) => {
    onAccountChange(e.target.value);
  };

  return (
    <div className="sidebar">
      <div className="account-section">
        <div style={{ display: "flex", alignItems: "center", gap: "0.5rem" }}>
          <select
            className="account-select"
            value={selectedAccount?.id || ""}
            onChange={handleSelectChange}
          >
            {accounts?.map((account) => (
              <option key={account.id} value={account.id}>
                {account.name} ({account.broker})
              </option>
            ))}
          </select>
          <button
            className="account-action-btn"
            title="Add Account"
            onClick={onAddAccount}
            style={{
              padding: "0.3rem 0.6rem",
              borderRadius: "4px",
              background: "#3b82f6",
              color: "#fff",
              border: "none",
              cursor: "pointer",
            }}
          >
            +
          </button>
          <button
            className="account-action-btn"
            title="Edit Account"
            onClick={() => onEditAccount(selectedAccount)}
            style={{
              padding: "0.3rem 0.6rem",
              borderRadius: "4px",
              background: "#f59e42",
              color: "#fff",
              border: "none",
              cursor: "pointer",
            }}
            disabled={!selectedAccount}
          >
            ✎
          </button>
        </div>

        <div className="balance-info">
          <div className="total-balance">
            <span>Total Balance</span>
            <h3>₹{balance?.total || "0.00"}</h3>
          </div>
          <div className="balance-details">
            <div className="cash">
              <span>Cash</span>
              <p>₹{balance?.cash || "0.00"}</p>
            </div>
            <div className="active">
              <span>Active</span>
              <p>₹{balance?.active || "0.00"}</p>
            </div>
          </div>
        </div>
      </div>

      <nav className="sidebar-nav">
        <Link to="/dashboard" className="nav-item active">
          <i className="icon-dashboard"></i>
          Dashboard
        </Link>
        <Link to="/stats" className="nav-item">
          <i className="icon-stats"></i>
          Stats
        </Link>
        <Link to="/calendar" className="nav-item">
          <i className="icon-calendar"></i>
          Calendar
        </Link>
        <Link to="/settings" className="nav-item">
          <i className="icon-settings"></i>
          Settings
        </Link>
        <Link to="/help" className="nav-item">
          <i className="icon-help"></i>
          Help
        </Link>
      </nav>

      <div className="action-buttons">
        <button className="btn-new-trade">New Trade</button>
        <button className="btn-new-setup">New Setup</button>
        <button className="btn-new-note">New Note</button>
      </div>
    </div>
  );
};

export default Sidebar;
