// ========== AUTH TYPES ==========
export interface User {
  id: number | string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  role: UserRole;
  status: UserStatus;
  department?: string;
  jobTitle?: string;
  phone?: string;
  avatar?: string | null;
  createdAt: string;
  updatedAt: string;
}

export type UserRole = 'ADMIN' | 'USER' | 'SUPER_ADMIN' | 'VIEWER';

export type UserStatus = 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';

export interface AuthTokens {
  accessToken: string;
  refreshToken?: string;
  expiresIn: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  firstName: string;
  lastName: string;
}

export interface AuthResponse {
  user: User;
  tokens: AuthTokens;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
  tokenType: string;
  expiresIn: number;
}

// ========== API TYPES ==========
export interface ApiResponse<T = any> {
  success: boolean;
  data: T;
  message?: string;
  timestamp: string;
}

export interface ApiError {
  status: number;
  message: string;
  errors?: Record<string, string[]>;
}

export interface PaginatedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

export interface PaginationParams {
  page?: number;
  size?: number;
  sort?: string;
}

// ========== SERVICE TYPES ==========
export interface Service {
  id: string;
  name: string;
  description: string;
  status: ServiceStatus;
  type: ServiceType;
  endpoint: string;
  healthCheckUrl: string;
  createdAt: string;
  updatedAt: string;
}

export enum ServiceStatus {
  UP = 'UP',
  DOWN = 'DOWN',
  DEGRADED = 'DEGRADED',
  MAINTENANCE = 'MAINTENANCE',
}

export enum ServiceType {
  MICROSERVICE = 'MICROSERVICE',
  DATABASE = 'DATABASE',
  CACHE = 'CACHE',
  MESSAGING = 'MESSAGING',
}

// ========== DASHBOARD TYPES ==========
export interface KPICard {
  title: string;
  value: string | number;
  change: number;
  trend: 'up' | 'down';
  icon: string;
}

export interface ChartDataPoint {
  name: string;
  value: number;
  timestamp?: string;
}

export interface ActivityLog {
  id: string;
  userId: string;
  userName: string;
  action: string;
  entity: string;
  entityId: string;
  timestamp: string;
  details?: Record<string, any>;
}

// ========== WEBSOCKET TYPES ==========
export interface WebSocketMessage<T = any> {
  type: WebSocketMessageType;
  payload: T;
  timestamp: string;
}

export enum WebSocketMessageType {
  SERVICE_STATUS = 'SERVICE_STATUS',
  USER_ACTIVITY = 'USER_ACTIVITY',
  NOTIFICATION = 'NOTIFICATION',
  METRIC_UPDATE = 'METRIC_UPDATE',
}

export interface Notification {
  id: string;
  type: NotificationType;
  title: string;
  message: string;
  timestamp: string;
  read: boolean;
}

export enum NotificationType {
  INFO = 'INFO',
  SUCCESS = 'SUCCESS',
  WARNING = 'WARNING',
  ERROR = 'ERROR',
}

