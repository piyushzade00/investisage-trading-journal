import axiosInstance from "./axios";

const BASE_URL = "/accounts"; // Base path for account endpoints

const accountService = {
  // Fetch all accounts for the authenticated user
  getAccounts: async () => {
    try {
      const response = await axiosInstance.get(BASE_URL);
      return response.data;
    } catch (error) {
      console.error(
        "Error fetching accounts:",
        error.response?.data || error.message
      );
      throw error; // Re-throw to be handled by the calling component
    }
  },

  // Fetch a single account by ID
  getAccountById: async (id) => {
    try {
      const response = await axiosInstance.get(`${BASE_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(
        `Error fetching account with ID ${id}:`,
        error.response?.data || error.message
      );
      throw error;
    }
  },

  // Create a new account
  createAccount: async (accountData) => {
    try {
      const response = await axiosInstance.post(BASE_URL, accountData);
      return response.data;
    } catch (error) {
      console.error(
        "Error creating account:",
        error.response?.data || error.message
      );
      throw error;
    }
  },

  // Update an existing account
  updateAccount: async (id, accountData) => {
    try {
      const response = await axiosInstance.put(
        `${BASE_URL}/${id}`,
        accountData
      );
      return response.data;
    } catch (error) {
      console.error(
        `Error updating account with ID ${id}:`,
        error.response?.data || error.message
      );
      throw error;
    }
  },

  // Delete an account
  deleteAccount: async (id) => {
    try {
      await axiosInstance.delete(`${BASE_URL}/${id}`);
    } catch (error) {
      console.error(
        `Error deleting account with ID ${id}:`,
        error.response?.data || error.message
      );
      throw error;
    }
  },
};

export default accountService;
