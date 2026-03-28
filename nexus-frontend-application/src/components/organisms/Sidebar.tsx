import React, { useMemo } from 'react';
import { NavLink } from 'react-router-dom';
import { cn } from '@/utils/cn';
import { useUIStore } from '@/store/uiStore';
import { useAuthStore } from '@/store/authStore';
import { permissions } from '@/utils/permissions';
import { UserRole } from '@/types';
import {
  LayoutDashboard,
  Users,
  BarChart3,
  GitBranch,
  Bell,
  Settings,
  Activity,
  ChevronLeft,
  ChevronRight,
} from 'lucide-react';

interface NavItem {
  label: string;
  path: string;
  icon: React.ReactNode;
  allowedRoles?: UserRole[]; // If not specified, visible to all authenticated users
  checkPermission?: (role: UserRole) => boolean; // Custom permission check
}

const allNavItems: NavItem[] = [
  {
    label: 'Dashboard',
    path: '/dashboard',
    icon: <LayoutDashboard size={20} />,
    // Visible to all authenticated users
  },
  {
    label: 'Users',
    path: '/users',
    icon: <Users size={20} />,
    checkPermission: permissions.canViewUsers,
  },
  {
    label: 'Analytics',
    path: '/analytics',
    icon: <BarChart3 size={20} />,
    checkPermission: permissions.canViewAnalytics,
  },
  {
    label: 'Graph Explorer',
    path: '/graph',
    icon: <GitBranch size={20} />,
    checkPermission: permissions.canViewGraph,
  },
  {
    label: 'Notifications',
    path: '/notifications',
    icon: <Bell size={20} />,
    checkPermission: permissions.canViewNotifications,
  },
  {
    label: 'Monitoring',
    path: '/admin/monitoring',
    icon: <Activity size={20} />,
    checkPermission: permissions.canViewMonitoring,
  },
  {
    label: 'Settings',
    path: '/settings',
    icon: <Settings size={20} />,
    checkPermission: permissions.canViewSettings,
  },
];

const Sidebar: React.FC = () => {
  const { sidebarCollapsed, toggleSidebar } = useUIStore();
  const { user } = useAuthStore();

  // Filter navigation items based on user role
  const navItems = useMemo(() => {
    if (!user) return [];

    return allNavItems.filter(item => {
      // If no permission check specified, show to all authenticated users
      if (!item.checkPermission) return true;

      // Check custom permission
      return item.checkPermission(user.role);
    });
  }, [user]);

  return (
    <aside
      className={cn(
        'fixed left-0 top-0 h-screen bg-navy border-r border-navy-light transition-all duration-300 z-40',
        sidebarCollapsed ? 'w-16' : 'w-64'
      )}
    >
      {/* Logo */}
      <div className="h-16 flex items-center justify-center border-b border-navy-light">
        <h1 className={cn(
          'font-bold text-white transition-opacity',
          sidebarCollapsed ? 'text-heading-3' : 'text-heading-2'
        )}>
          {sidebarCollapsed ? 'N' : 'NEXUS'}
        </h1>
      </div>

      {/* Navigation */}
      <nav className="py-4 px-2">
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }) =>
              cn(
                'flex items-center gap-3 px-3 py-3 mb-1 rounded-medium text-white/70 hover:bg-navy-light hover:text-white transition-smooth',
                isActive && 'bg-blue text-white',
                sidebarCollapsed && 'justify-center'
              )
            }
            title={sidebarCollapsed ? item.label : undefined}
          >
            {item.icon}
            {!sidebarCollapsed && (
              <span className="text-body font-medium">{item.label}</span>
            )}
          </NavLink>
        ))}
      </nav>

      {/* Toggle Button */}
      <button
        onClick={toggleSidebar}
        className="absolute -right-3 top-20 w-6 h-6 bg-navy border border-navy-light rounded-full flex items-center justify-center text-white hover:bg-navy-light transition-smooth"
      >
        {sidebarCollapsed ? <ChevronRight size={14} /> : <ChevronLeft size={14} />}
      </button>
    </aside>
  );
};

export default Sidebar;

