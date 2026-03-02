import axios, { AxiosInstance } from "axios";
import { API_BACKEND_URL } from "./api";
import AuthService from "./auth.service";

/**
 * Aplica interceptors comuns
 */
const applyInterceptors = (api) => {
  api.interceptors.request.use((config) => {
    const token = AuthService.getAccessTokenSync();

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  });

  api.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        AuthService.logout();
      }

      return Promise.reject(error);
    }
  );
};

/**
 * Instância API Segurança
 */
export const apiBackend = axios.create({
  baseURL: API_BACKEND_URL,
});

applyInterceptors(apiBackend);
