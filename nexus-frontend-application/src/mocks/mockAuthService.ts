import type { LoginRequest, LoginResponse, User } from '@/types';
import { mockUsers } from './mockData';

// Simulate network delay
const delay = (ms: number) => new Promise(resolve => setTimeout(resolve, ms));

// Mock credentials - Test different roles
const MOCK_CREDENTIALS = {
  'alice@acme.com': 'password123',       // ADMIN
  'bob@acme.com': 'password123',         // SUPER_ADMIN
  'sarah@acme.com': 'password123',       // USER
  'viewer@nexus.io': 'password123',      // VIEWER
  'admin@nexus.io': 'admin123',          // SUPER_ADMIN
};

export const mockAuthService = {
  /**
   * Mock login - validates credentials and returns mock token + user
   */
  login: async (credentials: LoginRequest): Promise<LoginResponse> => {
    await delay(800); // Simulate API call

    const { username, password } = credentials;
    
    // Check if credentials match
    const validPassword = MOCK_CREDENTIALS[username as keyof typeof MOCK_CREDENTIALS];
    
    if (!validPassword || validPassword !== password) {
      throw new Error('Invalid credentials');
    }

    // Find user by email/username
    const user = mockUsers.find(
      (u) => u.email === username || u.username === username
    );

    if (!user) {
      throw new Error('User not found');
    }

    // Return mock response
    return {
      accessToken: `mock_token_${Date.now()}`,
      refreshToken: `mock_refresh_${Date.now()}`,
      user: {
        id: user.id,
        username: user.username,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        role: user.role,
        status: user.status,
        department: user.department,
        jobTitle: user.jobTitle,
        phone: user.phone,
        avatar: user.avatar,
        createdAt: user.createdAt,
        updatedAt: user.updatedAt,
      },
      tokenType: 'Bearer',
      expiresIn: 3600,
    };
  },

  /**
   * Mock refresh token
   */
  refresh: async (): Promise<LoginResponse> => {
    await delay(300);
    
    // Return Alice as default user for refresh
    const user = mockUsers[0];
    
    return {
      accessToken: `mock_token_${Date.now()}`,
      refreshToken: `mock_refresh_${Date.now()}`,
      user,
      tokenType: 'Bearer',
      expiresIn: 3600,
    };
  },

  /**
   * Mock logout
   */
  logout: async (): Promise<void> => {
    await delay(200);
    // Just resolve - in real app would invalidate token
  },

  /**
   * Mock register
   */
  register: async (data: Partial<User>): Promise<User> => {
    await delay(1000);
    
    const newUser: User = {
      id: Math.floor(Math.random() * 10000) + 100,
      username: data.username || data.email?.split('@')[0] || 'new_user',
      email: data.email || '',
      firstName: data.firstName || '',
      lastName: data.lastName || '',
      role: 'USER',
      status: 'ACTIVE',
      department: data.department || '',
      jobTitle: data.jobTitle || '',
      phone: data.phone || '',
      avatar: null,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    return newUser;
  },
};

