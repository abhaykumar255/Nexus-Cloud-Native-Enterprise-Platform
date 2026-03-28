import React, { useEffect } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Toaster } from 'react-hot-toast';
import { useUIStore } from './store/uiStore';

// Layouts
import AppLayout from './components/templates/AppLayout';
import ProtectedRoute from './components/organisms/ProtectedRoute';

// Auth Pages
import LoginPage from './features/auth/LoginPage';
import RegisterPage from './features/auth/RegisterPage';

// Feature Pages
import DashboardPage from './features/dashboard/DashboardPage';
import UsersPage from './features/users/UsersPage';
import AnalyticsPage from './features/analytics/AnalyticsPage';
import GraphExplorerPage from './features/graph/GraphExplorerPage';
import NotificationsPage from './features/notifications/NotificationsPage';
import SettingsPage from './features/settings/SettingsPage';
import AdminMonitoringPage from './features/admin/AdminMonitoringPage';

// Query Client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
      staleTime: 30000, // 30 seconds
    },
  },
});

const AppContent: React.FC = () => {
  const { theme } = useUIStore();

  // Apply theme to document root
  useEffect(() => {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [theme]);

  return null;
};

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <AppContent />
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          {/* Protected Routes */}
          <Route
            path="/"
            element={
              <ProtectedRoute>
                <AppLayout />
              </ProtectedRoute>
            }
          >
            <Route index element={<Navigate to="/dashboard" replace />} />

            {/* Dashboard - All authenticated users */}
            <Route path="dashboard" element={<DashboardPage />} />

            {/* User Management - ADMIN and SUPER_ADMIN only */}
            <Route
              path="users"
              element={
                <ProtectedRoute allowedRoles={['ADMIN', 'SUPER_ADMIN']}>
                  <UsersPage />
                </ProtectedRoute>
              }
            />

            {/* Analytics - USER, ADMIN, SUPER_ADMIN */}
            <Route
              path="analytics"
              element={
                <ProtectedRoute allowedRoles={['USER', 'ADMIN', 'SUPER_ADMIN']}>
                  <AnalyticsPage />
                </ProtectedRoute>
              }
            />

            {/* Graph Explorer - USER, ADMIN, SUPER_ADMIN */}
            <Route
              path="graph"
              element={
                <ProtectedRoute allowedRoles={['USER', 'ADMIN', 'SUPER_ADMIN']}>
                  <GraphExplorerPage />
                </ProtectedRoute>
              }
            />

            {/* Notifications - USER, ADMIN, SUPER_ADMIN */}
            <Route
              path="notifications"
              element={
                <ProtectedRoute allowedRoles={['USER', 'ADMIN', 'SUPER_ADMIN']}>
                  <NotificationsPage />
                </ProtectedRoute>
              }
            />

            {/* Settings - All authenticated users */}
            <Route path="settings" element={<SettingsPage />} />

            {/* Admin Monitoring - ADMIN and SUPER_ADMIN only */}
            <Route
              path="admin/monitoring"
              element={
                <ProtectedRoute allowedRoles={['ADMIN', 'SUPER_ADMIN']}>
                  <AdminMonitoringPage />
                </ProtectedRoute>
              }
            />
          </Route>

          {/* 404 */}
          <Route path="*" element={<Navigate to="/dashboard" replace />} />
        </Routes>
      </BrowserRouter>

      {/* Toast Notifications */}
      <Toaster
        position="top-right"
        toastOptions={{
          duration: 3000,
          style: {
            background: '#FFFFFF',
            color: '#1A1F2E',
            border: '1px solid #C8D0DA',
            borderRadius: '10px',
            padding: '12px 16px',
          },
          success: {
            iconTheme: {
              primary: '#1A6B3C',
              secondary: '#FFFFFF',
            },
          },
          error: {
            iconTheme: {
              primary: '#C0392B',
              secondary: '#FFFFFF',
            },
          },
        }}
      />
    </QueryClientProvider>
  );
};

export default App;

