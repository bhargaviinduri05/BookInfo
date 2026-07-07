import axios from 'axios';

// Base URL of the Spring Boot backend. Override via .env (VITE_API_BASE_URL)
// when deploying to a different host/port.
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

export const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    Accept: 'application/json',
  },
});

export default apiClient;
