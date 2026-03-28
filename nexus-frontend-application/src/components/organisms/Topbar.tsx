import React from 'react';
import { useAuth } from '@/hooks/useAuth';
import { useUIStore } from '@/store/uiStore';
import { getRoleDisplayName } from '@/utils/permissions';
import { cn } from '@/utils/cn';
import Badge from '@/components/atoms/Badge';
import { Bell, Sun, Moon, LogOut, User as UserIcon, Shield } from 'lucide-react';

const Topbar: React.FC = () => {
  const { user, logout } = useAuth();
  const { theme, toggleTheme, sidebarCollapsed, setNotificationsOpen } = useUIStore();
  const [showUserMenu, setShowUserMenu] = React.useState(false);

  return (
    <header
      className={cn(
        'fixed top-0 right-0 h-16 bg-surface border-b border-border z-30 transition-all duration-300',
        sidebarCollapsed ? 'left-16' : 'left-64'
      )}
    >
      <div className="h-full px-6 flex items-center justify-between">
        {/* Search / Breadcrumb Area */}
        <div className="flex-1">
          <h2 className="text-heading-3 text-text-primary font-semibold">
            Welcome back, {user?.firstName || 'User'}
          </h2>
        </div>

        {/* Actions */}
        <div className="flex items-center gap-2">
          {/* Theme Toggle */}
          <button
            onClick={toggleTheme}
            className="p-2 rounded-medium hover:bg-background transition-smooth text-text-muted hover:text-text-primary"
            title="Toggle theme"
          >
            {theme === 'light' ? <Moon size={20} /> : <Sun size={20} />}
          </button>

          {/* Notifications */}
          <button
            onClick={() => setNotificationsOpen(true)}
            className="p-2 rounded-medium hover:bg-background transition-smooth text-text-muted hover:text-text-primary relative"
            title="Notifications"
          >
            <Bell size={20} />
            <span className="absolute top-1 right-1 w-2 h-2 bg-danger rounded-full" />
          </button>

          {/* User Menu */}
          <div className="relative">
            <button
              onClick={() => setShowUserMenu(!showUserMenu)}
              className="flex items-center gap-3 px-3 py-2 rounded-medium hover:bg-background transition-smooth"
            >
              <div className="w-8 h-8 rounded-full bg-blue text-white flex items-center justify-center text-caption font-semibold">
                {user?.firstName?.[0]}{user?.lastName?.[0]}
              </div>
              <div className="text-left">
                <div className="text-body text-text-primary font-medium">
                  {user?.username}
                </div>
                {user?.role && (
                  <div className="text-caption text-text-muted flex items-center gap-1">
                    <Shield size={10} />
                    {getRoleDisplayName(user.role)}
                  </div>
                )}
              </div>
            </button>

            {showUserMenu && (
              <div className="absolute right-0 top-12 w-64 bg-surface border border-border rounded-medium shadow-dropdown py-2">
                {user && (
                  <div className="px-4 py-3 border-b border-border">
                    <p className="text-caption text-text-muted">Signed in as</p>
                    <p className="text-body text-text-primary font-medium">{user.email}</p>
                    <Badge className="mt-2 text-xs" variant="info">
                      {getRoleDisplayName(user.role)}
                    </Badge>
                  </div>
                )}
                <button
                  onClick={() => {
                    setShowUserMenu(false);
                    // Navigate to profile
                  }}
                  className="w-full flex items-center gap-2 px-4 py-2 text-body text-text-primary hover:bg-background transition-smooth"
                >
                  <UserIcon size={16} />
                  Profile
                </button>
                <hr className="my-2 border-border" />
                <button
                  onClick={() => {
                    setShowUserMenu(false);
                    logout();
                  }}
                  className="w-full flex items-center gap-2 px-4 py-2 text-body text-danger hover:bg-danger-light transition-smooth"
                >
                  <LogOut size={16} />
                  Logout
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </header>
  );
};

export default Topbar;

