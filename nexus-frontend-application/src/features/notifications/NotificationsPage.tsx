import React from 'react';
import NotificationCenter from '@/components/organisms/NotificationCenter';

const NotificationsPage: React.FC = () => {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-heading-1 text-text-primary font-bold">Notifications</h1>
        <p className="text-body text-text-muted mt-1">
          Stay updated with real-time system notifications and alerts
        </p>
      </div>
      
      <NotificationCenter />
    </div>
  );
};

export default NotificationsPage;

