import type { User, PaginatedResponse, PaginationParams } from '@/types';
import { mockUsers } from './mockData';

// Simulate network delay
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

export const mockUserService = {
  /**
   * Get paginated users
   */
  getUsers: async (params: PaginationParams = {}): Promise<PaginatedResponse<User>> => {
    await delay(500);

    const { page = 0, size = 10 } = params;
    const start = page * size;
    const end = start + size;
    const paginatedUsers = mockUsers.slice(start, end);

    return {
      content: paginatedUsers,
      page,
      size,
      totalElements: mockUsers.length,
      totalPages: Math.ceil(mockUsers.length / size),
      last: end >= mockUsers.length,
    };
  },

  /**
   * Get a single user by ID
   */
  getUserById: async (id: number | string): Promise<User> => {
    await delay(300);

    const user = mockUsers.find(u => u.id === id || u.id === Number(id));
    if (!user) {
      throw new Error('User not found');
    }

    return user;
  },

  /**
   * Create a new user
   */
  createUser: async (data: Partial<User>): Promise<User> => {
    await delay(800);

    const newUser: User = {
      id: Math.floor(Math.random() * 10000) + 100,
      username: data.username || data.email?.split('@')[0] || 'new_user',
      email: data.email || '',
      firstName: data.firstName || '',
      lastName: data.lastName || '',
      role: data.role || 'USER',
      status: data.status || 'ACTIVE',
      department: data.department || '',
      jobTitle: data.jobTitle || '',
      phone: data.phone || '',
      avatar: null,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    // In a real app, this would update the backend
    mockUsers.push(newUser);

    return newUser;
  },

  /**
   * Update an existing user
   */
  updateUser: async (id: number | string, data: Partial<User>): Promise<User> => {
    await delay(600);

    const userIndex = mockUsers.findIndex(u => u.id === id || u.id === Number(id));
    if (userIndex === -1) {
      throw new Error('User not found');
    }

    const updatedUser = {
      ...mockUsers[userIndex],
      ...data,
      updatedAt: new Date().toISOString(),
    };

    mockUsers[userIndex] = updatedUser;

    return updatedUser;
  },

  /**
   * Delete a user
   */
  deleteUser: async (id: number | string): Promise<void> => {
    await delay(400);

    const userIndex = mockUsers.findIndex(u => u.id === id || u.id === Number(id));
    if (userIndex === -1) {
      throw new Error('User not found');
    }

    mockUsers.splice(userIndex, 1);
  },
};

