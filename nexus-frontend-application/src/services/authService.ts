// TODO: Replace with real API calls once backend is integrated
// import { api } from './api';
import { mockAuthService } from '@/mocks/mockAuthService';
import { LoginRequest, RegisterRequest, AuthResponse, User } from '@/types';

// USING MOCK DATA FOR NOW - Will be replaced with real API calls
export const authService = {
  login: async (credentials: LoginRequest): Promise<{ data: AuthResponse }> => {
    const response = await mockAuthService.login(credentials);
    return {
      data: {
        user: response.user,
        tokens: {
          accessToken: response.accessToken,
          refreshToken: response.refreshToken,
          expiresIn: response.expiresIn,
        },
      },
    };
  },

  register: async (data: RegisterRequest): Promise<{ data: AuthResponse }> => {
    const user = await mockAuthService.register(data);
    return {
      data: {
        user,
        tokens: {
          accessToken: `mock_token_${Date.now()}`,
          refreshToken: `mock_refresh_${Date.now()}`,
          expiresIn: 3600,
        },
      },
    };
  },

  logout: async () => {
    await mockAuthService.logout();
    return { data: null };
  },

  getCurrentUser: async (): Promise<{ data: User }> => {
    // Return the first mock user (Alice) as the current user
    const response = await mockAuthService.refresh();
    return { data: response.user };
  },

  refreshToken: async (): Promise<{ data: { accessToken: string } }> => {
    const response = await mockAuthService.refresh();
    return { data: { accessToken: response.accessToken } };
  },
};

/*
 * REAL API IMPLEMENTATION - Uncomment when backend is ready:
 *
 * export const authService = {
 *   login: (credentials: LoginRequest) =>
 *     api.post<AuthResponse>('/auth/login', credentials),
 *
 *   register: (data: RegisterRequest) =>
 *     api.post<AuthResponse>('/auth/register', data),
 *
 *   logout: () =>
 *     api.post('/auth/logout'),
 *
 *   getCurrentUser: () =>
 *     api.get<User>('/auth/me'),
 *
 *   refreshToken: () =>
 *     api.post<{ accessToken: string }>('/auth/refresh'),
 * };
 */

