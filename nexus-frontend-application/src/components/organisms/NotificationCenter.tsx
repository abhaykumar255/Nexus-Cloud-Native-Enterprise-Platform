import React, { useState, useEffect } from 'react';
import Card from '@/components/atoms/Card';
import Badge from '@/components/atoms/Badge';
import Button from '@/components/atoms/Button';
import {
  Bell, Check, CheckCheck, Trash2,
  UserPlus, AlertTriangle, AlertOctagon, Activity, CheckCircle
} from 'lucide-react';
import { mockNotifications } from '@/mocks/mockData';
import { cn } from '@/utils/cn';

interface Notification {
  id: number;
  type: string;
  title: string;
  description: string;
  timestamp: string;
  read: boolean;
  icon: string;
  color: string;
}

const NotificationCenter: React.FC = () => {
  const [notifications, setNotifications] = useState<Notification[]>(mockNotifications);
  const [filter, setFilter] = useState<'all' | 'unread' | 'read'>('all');
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    const count = notifications.filter(n => !n.read).length;
    setUnreadCount(count);
  }, [notifications]);

  // Simulate real-time notifications
  useEffect(() => {
    const interval = setInterval(() => {
      const newNotification: Notification = {
        id: Date.now(),
        type: ['user', 'alert', 'system', 'success'][Math.floor(Math.random() * 4)],
        title: [
          'New user registered',
          'API threshold warning',
          'System alert',
          'Task completed'
        ][Math.floor(Math.random() * 4)],
        description: 'Real-time notification from system',
        timestamp: 'Just now',
        read: false,
        icon: ['UserPlus', 'AlertTriangle', 'Activity', 'CheckCircle'][Math.floor(Math.random() * 4)],
        color: ['blue', 'amber', 'teal', 'green'][Math.floor(Math.random() * 4)],
      };
      setNotifications(prev => [newNotification, ...prev.slice(0, 19)]);
    }, 15000);

    return () => clearInterval(interval);
  }, []);

  const filteredNotifications = notifications.filter(n => {
    if (filter === 'unread') return !n.read;
    if (filter === 'read') return n.read;
    return true;
  });

  const markAsRead = (id: number) => {
    setNotifications(prev =>
      prev.map(n => n.id === id ? { ...n, read: true } : n)
    );
  };

  const markAllAsRead = () => {
    setNotifications(prev => prev.map(n => ({ ...n, read: true })));
  };

  const deleteNotification = (id: number) => {
    setNotifications(prev => prev.filter(n => n.id !== id));
  };

  const getIcon = (iconName: string) => {
    const icons: Record<string, any> = {
      UserPlus, AlertTriangle, AlertOctagon, Activity, CheckCircle
    };
    const IconComponent = icons[iconName] || Bell;
    return IconComponent;
  };

  const getColorClasses = (color: string) => {
    const colors: Record<string, string> = {
      blue: 'bg-blue-pale text-blue',
      amber: 'bg-amber-pale text-amber',
      red: 'bg-red-pale text-red',
      teal: 'bg-teal-pale text-teal',
      green: 'bg-green-pale text-green',
    };
    return colors[color] || 'bg-gray-100 text-gray-600';
  };

  return (
    <Card className="p-6">
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <div className="flex items-center gap-3">
          <Bell className="w-6 h-6 text-text-primary" />
          <h2 className="text-heading-2 font-bold text-text-primary">Notifications</h2>
          {unreadCount > 0 && (
            <Badge variant="danger" size="sm">{unreadCount}</Badge>
          )}
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm" onClick={markAllAsRead} disabled={unreadCount === 0}>
            <CheckCheck className="w-4 h-4 mr-2" />
            Mark all read
          </Button>
        </div>
      </div>

      {/* Filters */}
      <div className="flex gap-2 mb-4">
        <Button
          variant={filter === 'all' ? 'primary' : 'outline'}
          size="sm"
          onClick={() => setFilter('all')}
        >
          All ({notifications.length})
        </Button>
        <Button
          variant={filter === 'unread' ? 'primary' : 'outline'}
          size="sm"
          onClick={() => setFilter('unread')}
        >
          Unread ({unreadCount})
        </Button>
        <Button
          variant={filter === 'read' ? 'primary' : 'outline'}
          size="sm"
          onClick={() => setFilter('read')}
        >
          Read ({notifications.length - unreadCount})
        </Button>
      </div>

      {/* Notification List */}
      <div className="space-y-3 max-h-96 overflow-y-auto">
        {filteredNotifications.map((notification) => {
          const Icon = getIcon(notification.icon);
          return (
            <div
              key={notification.id}
              className={cn(
                'flex items-start gap-4 p-4 rounded-lg border transition-colors',
                notification.read 
                  ? 'bg-white border-border' 
                  : 'bg-blue-50 border-blue-200'
              )}
            >
              <div className={cn('w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0', getColorClasses(notification.color))}>
                <Icon className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <h4 className="text-body font-semibold text-text-primary">{notification.title}</h4>
                <p className="text-caption text-text-muted mt-1">{notification.description}</p>
                <p className="text-caption text-text-muted mt-2">{notification.timestamp}</p>
              </div>
              <div className="flex gap-2">
                {!notification.read && (
                  <button
                    onClick={() => markAsRead(notification.id)}
                    className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
                    title="Mark as read"
                  >
                    <Check className="w-4 h-4 text-text-muted" />
                  </button>
                )}
                <button
                  onClick={() => deleteNotification(notification.id)}
                  className="p-2 hover:bg-red-50 rounded-lg transition-colors"
                  title="Delete"
                >
                  <Trash2 className="w-4 h-4 text-red" />
                </button>
              </div>
            </div>
          );
        })}

        {filteredNotifications.length === 0 && (
          <div className="text-center py-12">
            <Bell className="w-12 h-12 text-text-muted mx-auto mb-3" />
            <p className="text-body text-text-muted">No notifications</p>
          </div>
        )}
      </div>
    </Card>
  );
};

export default NotificationCenter;

