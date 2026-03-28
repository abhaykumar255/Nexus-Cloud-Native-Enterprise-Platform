import { UserRole } from '@/types';

/**
 * Role hierarchy for NEXUS platform
 * SUPER_ADMIN > ADMIN > USER > VIEWER
 */

// Define permission levels
export const ROLE_HIERARCHY: Record<UserRole, number> = {
  SUPER_ADMIN: 4,
  ADMIN: 3,
  USER: 2,
  VIEWER: 1,
};

/**
 * Check if user has required role or higher
 */
export const hasRole = (userRole: UserRole, requiredRole: UserRole): boolean => {
  return ROLE_HIERARCHY[userRole] >= ROLE_HIERARCHY[requiredRole];
};

/**
 * Check if user has any of the allowed roles
 */
export const hasAnyRole = (userRole: UserRole, allowedRoles: UserRole[]): boolean => {
  return allowedRoles.some(role => hasRole(userRole, role));
};

/**
 * Check if user is Super Admin
 */
export const isSuperAdmin = (userRole: UserRole): boolean => {
  return userRole === 'SUPER_ADMIN';
};

/**
 * Check if user is Admin or higher
 */
export const isAdmin = (userRole: UserRole): boolean => {
  return hasRole(userRole, 'ADMIN');
};

/**
 * Check if user is regular User or higher
 */
export const isUser = (userRole: UserRole): boolean => {
  return hasRole(userRole, 'USER');
};

/**
 * Check if user is Viewer only
 */
export const isViewer = (userRole: UserRole): boolean => {
  return userRole === 'VIEWER';
};

/**
 * Permission checks for specific actions
 */
export const permissions = {
  // User Management
  canViewUsers: (role: UserRole) => hasRole(role, 'ADMIN'),
  canCreateUser: (role: UserRole) => hasRole(role, 'ADMIN'),
  canEditUser: (role: UserRole) => hasRole(role, 'ADMIN'),
  canDeleteUser: (role: UserRole) => isSuperAdmin(role),
  canChangeUserRole: (role: UserRole) => isSuperAdmin(role),

  // Monitoring & Admin
  canViewMonitoring: (role: UserRole) => hasRole(role, 'ADMIN'),
  canViewSystemLogs: (role: UserRole) => hasRole(role, 'ADMIN'),
  canManageSystem: (role: UserRole) => isSuperAdmin(role),

  // Analytics
  canViewAnalytics: (role: UserRole) => hasRole(role, 'USER'),
  canExportAnalytics: (role: UserRole) => hasRole(role, 'ADMIN'),

  // Graph Explorer
  canViewGraph: (role: UserRole) => hasRole(role, 'USER'),
  canEditGraph: (role: UserRole) => hasRole(role, 'ADMIN'),

  // Settings
  canViewSettings: (role: UserRole) => hasRole(role, 'USER'),
  canEditGlobalSettings: (role: UserRole) => isSuperAdmin(role),

  // Notifications
  canViewNotifications: (role: UserRole) => hasRole(role, 'USER'),
  canManageNotifications: (role: UserRole) => hasRole(role, 'ADMIN'),

  // Dashboard
  canViewDashboard: () => true, // All authenticated users
  canViewFullDashboard: (role: UserRole) => hasRole(role, 'USER'),
};

/**
 * Get allowed routes for a user role
 */
export const getAllowedRoutes = (role: UserRole): string[] => {
  const routes: string[] = ['/dashboard'];

  if (permissions.canViewUsers(role)) {
    routes.push('/users');
  }

  if (permissions.canViewAnalytics(role)) {
    routes.push('/analytics');
  }

  if (permissions.canViewGraph(role)) {
    routes.push('/graph');
  }

  if (permissions.canViewNotifications(role)) {
    routes.push('/notifications');
  }

  if (permissions.canViewSettings(role)) {
    routes.push('/settings');
  }

  if (permissions.canViewMonitoring(role)) {
    routes.push('/admin/monitoring');
  }

  return routes;
};

/**
 * Get user role display name
 */
export const getRoleDisplayName = (role: UserRole): string => {
  const roleNames: Record<UserRole, string> = {
    SUPER_ADMIN: 'Super Administrator',
    ADMIN: 'Administrator',
    USER: 'User',
    VIEWER: 'Viewer',
  };
  return roleNames[role];
};

/**
 * Get user role badge color
 */
export const getRoleBadgeColor = (role: UserRole): string => {
  const colors: Record<UserRole, string> = {
    SUPER_ADMIN: 'bg-purple text-white',
    ADMIN: 'bg-blue text-white',
    USER: 'bg-green text-white',
    VIEWER: 'bg-gray-500 text-white',
  };
  return colors[role];
};

