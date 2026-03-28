import axios, { AxiosInstance, AxiosError, InternalAxiosRequestConfig } from 'axios';
import { ENV } from '@/config/env';
import { useAuthStore } from '@/store/authStore';
import { ApiError, ApiResponse } from '@/types';

// Create axios instance
const apiClient: AxiosInstance = axios.create({
  baseURL: ENV.API_BASE_URL,
  timeout: ENV.REQUEST_TIMEOUT,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // For httpOnly cookies
});

// Request interceptor - Add JWT token
apiClient.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = useAuthStore.getState().accessToken;
    
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    return config;
  },
  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

// Response interceptor - Handle token refresh
let isRefreshing = false;
let failedQueue: Array<{
  resolve: (value?: any) => void;
  reject: (reason?: any) => void;
}> = [];

const processQueue = (error: AxiosError | null, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });

  failedQueue = [];
};

apiClient.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

    // Handle 401 Unauthorized - Token expired
    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        // Queue failed requests while refresh is in progress
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            if (originalRequest.headers) {
              originalRequest.headers.Authorization = `Bearer ${token}`;
            }
            return apiClient(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Attempt to refresh token using httpOnly cookie
        const response = await axios.post(
          `${ENV.API_BASE_URL}/auth/refresh`,
          {},
          { withCredentials: true }
        );

        const { accessToken } = response.data.data;
        useAuthStore.getState().setTokens(accessToken);

        processQueue(null, accessToken);

        if (originalRequest.headers) {
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        }

        return apiClient(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError as AxiosError, null);
        useAuthStore.getState().logout();
        window.location.href = '/login';
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    // Transform error to ApiError format
    const apiError: ApiError = {
      status: error.response?.status || 500,
      message: (error.response?.data as any)?.message || error.message || 'An error occurred',
      errors: (error.response?.data as any)?.errors,
    };

    return Promise.reject(apiError);
  }
);

// Generic request wrapper
export const api = {
  get: <T = any>(url: string, params?: any): Promise<ApiResponse<T>> =>
    apiClient.get(url, { params }).then((res) => res.data),

  post: <T = any>(url: string, data?: any): Promise<ApiResponse<T>> =>
    apiClient.post(url, data).then((res) => res.data),

  put: <T = any>(url: string, data?: any): Promise<ApiResponse<T>> =>
    apiClient.put(url, data).then((res) => res.data),

  patch: <T = any>(url: string, data?: any): Promise<ApiResponse<T>> =>
    apiClient.patch(url, data).then((res) => res.data),

  delete: <T = any>(url: string): Promise<ApiResponse<T>> =>
    apiClient.delete(url).then((res) => res.data),
};

export default apiClient;

