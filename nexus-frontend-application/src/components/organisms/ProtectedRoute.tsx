import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '@/store/authStore';
import { hasAnyRole } from '@/utils/permissions';
import { UserRole } from '@/types';
import Spinner from '../atoms/Spinner';

interface ProtectedRouteProps {
  children: React.ReactNode;
  allowedRoles?: UserRole[]; // If not specified, any authenticated user can access
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, allowedRoles }) => {
  const { isAuthenticated, isLoading, user } = useAuthStore();
  const location = useLocation();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Check role-based access if allowedRoles is specified
  if (allowedRoles && allowedRoles.length > 0 && user) {
    const hasAccess = hasAnyRole(user.role, allowedRoles);

    if (!hasAccess) {
      // Redirect to dashboard with access denied message
      return <Navigate to="/dashboard" state={{ accessDenied: true }} replace />;
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute;

