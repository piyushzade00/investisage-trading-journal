import React, { useState, useEffect } from "react";
import "../../styles/AccountModal.css";
import accountService from "../../api/accountService";

const AccountModal = ({
  open,
  onClose,
  onSave,
  onDelete,
  onNewAccount,
  account,
  isEdit,
}) => {
  const [name, setName] = useState(account?.name || "");
  const [primary, setPrimary] = useState(account?.primary || false);
  const [balance, setBalance] = useState(account?.balance || 0);
  const [transactions, setTransactions] = useState(account?.transactions || []);
  const [broker, setBroker] = useState(account?.broker || "");

  useEffect(() => {
    setName(account?.name || "");
    setPrimary(account?.primary || false);
    setBalance(account?.balance || 0);
    setTransactions(
      (account?.transactions || []).map((tx) => ({
        ...tx,
        date: tx.transactionDateTime
          ? tx.transactionDateTime.slice(0, 10) // for input[type="date"]
          : "",
      }))
    );
    setBroker(account?.broker || "");
  }, [account]);

  // Validation for add transaction
  const canAddTransaction =
    transactions.length === 0 ||
    (transactions[transactions.length - 1].date &&
      transactions[transactions.length - 1].amount);

  // Add new transaction row
  const handleAddTransaction = () => {
    if (!canAddTransaction) return;
    setTransactions([
      ...transactions,
      { type: "DEPOSIT", date: "", amount: "", note: "" },
    ]);
  };

  // Remove transaction row
  const handleRemoveTransaction = (idx) => {
    setTransactions(transactions.filter((_, i) => i !== idx));
  };

  // Update transaction field
  const handleTransactionChange = (idx, field, value) => {
    setTransactions(
      transactions.map((t, i) => (i === idx ? { ...t, [field]: value } : t))
    );
  };

  const handleSave = async () => {
    if (!name.trim()) {
      alert("Account name cannot be empty.");
      return;
    }

    // Prepare transactions for API
    const transactionsForApi = transactions.map((tx) => ({
      id: tx.id || null,
      accountId: account?.id || null,
      type: tx.type,
      amount: Number(tx.amount),
      transactionDateTime: tx.date ? new Date(tx.date).toISOString() : null,
      notes: tx.note,
    }));

    // Prepare account data for API
    const accountData = {
      name,
      broker,
      isPrimary: primary,
      transactions: transactionsForApi,
    };

    try {
      let savedAccount;
      if (isEdit && account?.id) {
        savedAccount = await accountService.updateAccount(
          account.id,
          accountData
        );
      } else {
        savedAccount = await accountService.createAccount(accountData);
      }
      onSave(savedAccount);
    } catch (error) {
      alert("Failed to save account. Please try again.");
    }
  };

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
      setAccounts((prev) => prev.filter((acc) => acc.id !== account.id));

      setSelectedAccount((prev) => {
        if (prev?.id === account.id) {
          return accounts.length > 1
            ? accounts.find((acc) => acc.id !== account.id)
            : null;
        }
        return prev;
      });
      setAccountModalOpen(false);
    } catch (error) {
      alert("Failed to delete account. Please try again.");
    }
  };

  if (!open) return null;

  return (
    <div className="modal-overlay">
      <div className="account-modal">
        <div className="modal-header">
          <span>Account & Transactions</span>
          <button className="modal-close" onClick={onClose}>
            ×
          </button>
        </div>
        <div className="modal-body">
          <div className="modal-row">
            <div className="modal-row-name" style={{ flex: 2 }}>
              <label>
                Name<sup>*</sup>
              </label>
              <input
                className="modal-input"
                value={name}
                onChange={(e) => setName(e.target.value)}
                disabled={isEdit}
              />
            </div>
            <div className="modal-row-broker" style={{ flex: 2 }}>
              <label>Broker</label>
              <input
                className="modal-input"
                value={broker}
                onChange={(e) => setBroker(e.target.value)}
                disabled={isEdit}
              />
            </div>
            <div
              className="modal-row-primary"
              style={{ flex: 1, textAlign: "center" }}
            >
              <label>Primary Account?</label>
              <div className="primary-toggle">
                <input
                  type="checkbox"
                  checked={primary}
                  onChange={(e) => setPrimary(e.target.checked)}
                  id="primary-toggle"
                />
                {/* <label htmlFor="primary-toggle" className="toggle-label">
                  {primary ? "✓" : ""}
                </label> */}
              </div>
            </div>
            <div
              className="modal-row-balance"
              style={{ flex: 2, textAlign: "right" }}
            >
              <label>Account Balance</label>
              <div className="modal-balance">
                ₹
                {(account?.accountBalance ?? 0).toLocaleString("en-IN", {
                  minimumFractionDigits: 2,
                })}
              </div>
            </div>
          </div>
          <div className="transactions-section">
            <div className="transactions-header">Transactions</div>
            <table className="transactions-table">
              <thead>
                <tr>
                  <th></th>
                  <th>Type</th>
                  <th>Date</th>
                  <th>Amount</th>
                  <th>Note</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((t, idx) => (
                  <tr key={idx}>
                    <td>
                      <button
                        className="tx-remove"
                        onClick={() => handleRemoveTransaction(idx)}
                        title="Remove"
                      >
                        ✖
                      </button>
                    </td>
                    <td>
                      <select
                        value={t.type}
                        onChange={(e) =>
                          handleTransactionChange(idx, "type", e.target.value)
                        }
                        className="tx-type"
                      >
                        <option value="DEPOSIT">DEPOSIT</option>
                        <option value="WITHDRAWAL">WITHDRAWAL</option>
                      </select>
                    </td>
                    <td>
                      <input
                        type="date"
                        value={t.date}
                        onChange={(e) =>
                          handleTransactionChange(idx, "date", e.target.value)
                        }
                        className="tx-date"
                      />
                    </td>
                    <td>
                      <input
                        type="number"
                        value={t.amount}
                        onChange={(e) =>
                          handleTransactionChange(idx, "amount", e.target.value)
                        }
                        className="tx-amount"
                      />
                    </td>
                    <td>
                      <input
                        value={t.notes}
                        onChange={(e) =>
                          handleTransactionChange(idx, "note", e.target.value)
                        }
                        className="tx-note"
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div style={{ textAlign: "center", margin: "1rem 0" }}>
              <button
                className="tx-add"
                onClick={handleAddTransaction}
                disabled={!canAddTransaction}
                title={
                  !canAddTransaction
                    ? "Fill Date and Amount before adding another transaction"
                    : "Add Transaction"
                }
              >
                +
              </button>
            </div>
          </div>
        </div>
        <div className="modal-footer">
          {isEdit && (
            <button className="modal-delete" onClick={() => onDelete(account)}>
              Delete Account
            </button>
          )}
          <button className="modal-new-account" onClick={onNewAccount}>
            New Account
          </button>
          <button className="modal-save" onClick={handleSave}>
            Save
          </button>
        </div>
      </div>
    </div>
  );
};

export default AccountModal;
