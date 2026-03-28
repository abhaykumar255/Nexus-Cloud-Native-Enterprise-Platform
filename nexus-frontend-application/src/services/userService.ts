// TODO: Replace with real API calls once backend is integrated
// import { api } from './api';
import { mockUserService } from '@/mocks/mockUserService';
import { User, PaginatedResponse, PaginationParams, UserRole, UserStatus } from '@/types';

export interface CreateUserRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  department?: string;
  jobTitle?: string;
  phone?: string;
}

export interface UpdateUserRequest {
  email?: string;
  firstName?: string;
  lastName?: string;
  role?: UserRole;
  status?: UserStatus;
  department?: string;
  jobTitle?: string;
  phone?: string;
}

// USING MOCK DATA FOR NOW - Will be replaced with real API calls
export const userService = {
  getUsers: async (params?: PaginationParams): Promise<{ data: PaginatedResponse<User> }> => {
    const response = await mockUserService.getUsers(params);
    return { data: response };
  },

  getUserById: async (id: string): Promise<{ data: User }> => {
    const response = await mockUserService.getUserById(id);
    return { data: response };
  },

  createUser: async (data: CreateUserRequest): Promise<{ data: User }> => {
    const response = await mockUserService.createUser(data);
    return { data: response };
  },

  updateUser: async (id: string, data: UpdateUserRequest): Promise<{ data: User }> => {
    const response = await mockUserService.updateUser(id, data);
    return { data: response };
  },

  deleteUser: async (id: string): Promise<void> => {
    await mockUserService.deleteUser(id);
  },

  searchUsers: async (query: string): Promise<{ data: User[] }> => {
    // Simple mock search - filter by name or email
    const response = await mockUserService.getUsers({ page: 0, size: 100 });
    const filtered = response.content.filter(user =>
      user.firstName.toLowerCase().includes(query.toLowerCase()) ||
      user.lastName.toLowerCase().includes(query.toLowerCase()) ||
      user.email.toLowerCase().includes(query.toLowerCase()) ||
      user.username.toLowerCase().includes(query.toLowerCase())
    );
    return { data: filtered };
  },
};

/*
 * REAL API IMPLEMENTATION - Uncomment when backend is ready:
 *
 * export const userService = {
 *   getUsers: (params?: PaginationParams) =>
 *     api.get<PaginatedResponse<User>>('/users', params),
 *
 *   getUserById: (id: string) =>
 *     api.get<User>(`/users/${id}`),
 *
 *   createUser: (data: CreateUserRequest) =>
 *     api.post<User>('/users', data),
 *
 *   updateUser: (id: string, data: UpdateUserRequest) =>
 *     api.put<User>(`/users/${id}`, data),
 *
 *   deleteUser: (id: string) =>
 *     api.delete(`/users/${id}`),
 *
 *   searchUsers: (query: string) =>
 *     api.get<User[]>('/users/search', { q: query }),
 * };
 */

