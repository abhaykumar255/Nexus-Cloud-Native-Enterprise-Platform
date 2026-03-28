import React from 'react';
import { Outlet } from 'react-router-dom';
import { useUIStore } from '@/store/uiStore';
import { cn } from '@/utils/cn';
import { X } from 'lucide-react';
import Sidebar from '../organisms/Sidebar';
import Topbar from '../organisms/Topbar';
import NotificationCenter from '../organisms/NotificationCenter';

const AppLayout: React.FC = () => {
  const { sidebarCollapsed, notificationsOpen, setNotificationsOpen } = useUIStore();

  return (
    <div className="min-h-screen bg-background">
      <Sidebar />
      <Topbar />

      {/* Main Content */}
      <main
        className={cn(
          'pt-16 transition-all duration-300 min-h-screen',
          sidebarCollapsed ? 'ml-16' : 'ml-64'
        )}
      >
        <div className="p-6">
          <Outlet />
        </div>
      </main>

      {/* Notifications Drawer */}
      {notificationsOpen && (
        <>
          {/* Backdrop */}
          <div
            className="fixed inset-0 bg-black/50 z-40"
            onClick={() => setNotificationsOpen(false)}
          />

          {/* Drawer */}
          <div className="fixed top-0 right-0 h-full w-96 bg-surface shadow-xl z-50 overflow-y-auto">
            <div className="p-6">
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-heading-2 font-bold text-text-primary">Notifications</h2>
                <button
                  onClick={() => setNotificationsOpen(false)}
                  className="p-2 hover:bg-background rounded-medium transition-smooth"
                >
                  <X size={20} className="text-text-muted" />
                </button>
              </div>

              <NotificationCenter />
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default AppLayout;

