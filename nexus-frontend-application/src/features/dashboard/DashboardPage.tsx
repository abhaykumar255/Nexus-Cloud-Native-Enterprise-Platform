import React, { useState } from 'react';
import { useAuthStore } from '@/store/authStore';
import { getRoleDisplayName, getRoleBadgeColor } from '@/utils/permissions';
import Card from '@/components/atoms/Card';
import Badge from '@/components/atoms/Badge';
import Button from '@/components/atoms/Button';
import { Users, Activity, TrendingUp, TrendingDown, DollarSign, ShoppingCart, Filter, Shield } from 'lucide-react';
import { cn } from '@/utils/cn';
import { mockDashboardKPIs, mockRecentActivity, mockUserActivityData } from '@/mocks/mockData';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

interface KPICardProps {
  title: string;
  value: string | number;
  change: number;
  icon: React.ReactNode;
}

const KPICard: React.FC<KPICardProps> = ({ title, value, change, icon }) => {
  const isPositive = change >= 0;

  return (
    <Card hover>
      <div className="flex items-start justify-between">
        <div>
          <p className="text-caption text-text-muted font-medium mb-1">{title}</p>
          <h3 className="text-heading-2 text-text-primary font-bold mb-2">
            {typeof value === 'number' ? value.toLocaleString() : value}
          </h3>
          <div className="flex items-center gap-1">
            {isPositive ? (
              <TrendingUp size={14} className="text-success" />
            ) : (
              <TrendingDown size={14} className="text-danger" />
            )}
            <span
              className={cn(
                'text-caption font-medium',
                isPositive ? 'text-success' : 'text-danger'
              )}
            >
              {isPositive ? '+' : ''}{change}%
            </span>
            <span className="text-caption text-text-muted">vs last month</span>
          </div>
        </div>
        <div className="w-12 h-12 rounded-medium bg-blue-pale flex items-center justify-center text-blue">
          {icon}
        </div>
      </div>
    </Card>
  );
};

const DashboardPage: React.FC = () => {
  const [dateRange, setDateRange] = useState('30d');
  const [showCategoryFilter, setShowCategoryFilter] = useState(false);
  const [categoryFilter, setCategoryFilter] = useState('all');
  const { user } = useAuthStore();

  // Use mock data
  const kpis = [
    {
      title: 'Total Users',
      value: mockDashboardKPIs.totalUsers.value,
      change: mockDashboardKPIs.totalUsers.change,
      icon: <Users size={24} />
    },
    {
      title: 'Active Sessions',
      value: mockDashboardKPIs.activeSessions.value,
      change: mockDashboardKPIs.activeSessions.change,
      icon: <Activity size={24} />
    },
    {
      title: 'Products Listed',
      value: mockDashboardKPIs.productsListed.value,
      change: mockDashboardKPIs.productsListed.change,
      icon: <ShoppingCart size={24} />
    },
    {
      title: 'Revenue',
      value: `$${(mockDashboardKPIs.revenue.value / 1000).toFixed(1)}K`,
      change: mockDashboardKPIs.revenue.change,
      icon: <DollarSign size={24} />
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <div className="flex items-center gap-3 mb-2">
            <h1 className="text-heading-1 text-text-primary font-bold">Dashboard</h1>
            {user && (
              <Badge className={getRoleBadgeColor(user.role)}>
                <Shield size={12} className="mr-1" />
                {getRoleDisplayName(user.role)}
              </Badge>
            )}
          </div>
          <p className="text-body text-text-secondary">
            Welcome to your NEXUS platform overview{user ? `, ${user.firstName}` : ''}
          </p>
        </div>
        <div className="flex gap-3">
          <div className="relative">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setShowCategoryFilter(!showCategoryFilter)}
            >
              <Filter className="w-4 h-4 mr-2" />
              {categoryFilter === 'all' ? 'All Categories' : categoryFilter === 'users' ? 'Users' : categoryFilter === 'activity' ? 'Activity' : 'Revenue'}
            </Button>
            {showCategoryFilter && (
              <div className="absolute right-0 top-12 w-48 bg-surface border border-border rounded-medium shadow-dropdown py-2 z-10">
                <button
                  onClick={() => { setCategoryFilter('all'); setShowCategoryFilter(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${categoryFilter === 'all' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  All Categories
                </button>
                <button
                  onClick={() => { setCategoryFilter('users'); setShowCategoryFilter(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${categoryFilter === 'users' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  Users
                </button>
                <button
                  onClick={() => { setCategoryFilter('activity'); setShowCategoryFilter(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${categoryFilter === 'activity' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  Activity
                </button>
                <button
                  onClick={() => { setCategoryFilter('revenue'); setShowCategoryFilter(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${categoryFilter === 'revenue' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  Revenue
                </button>
              </div>
            )}
          </div>
          <select
            className="px-4 py-2 border border-border rounded-lg text-body bg-surface hover:border-blue transition-smooth"
            value={dateRange}
            onChange={(e) => setDateRange(e.target.value)}
          >
            <option value="7d">Last 7 Days</option>
            <option value="30d">Last 30 Days</option>
            <option value="90d">Last 90 Days</option>
          </select>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {kpis.map((kpi, index) => (
          <KPICard key={index} {...kpi} />
        ))}
      </div>

      {/* Charts Section */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* User Activity Chart */}
        <Card>
          <h3 className="text-heading-3 text-text-primary font-semibold mb-4">
            User Activity Trend
          </h3>
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={mockUserActivityData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
                <XAxis
                  dataKey="date"
                  tick={{ fill: '#6B7280', fontSize: 12 }}
                  tickLine={{ stroke: '#E5E7EB' }}
                />
                <YAxis
                  tick={{ fill: '#6B7280', fontSize: 12 }}
                  tickLine={{ stroke: '#E5E7EB' }}
                />
                <Tooltip
                  contentStyle={{
                    backgroundColor: '#FFFFFF',
                    border: '1px solid #E5E7EB',
                    borderRadius: '8px',
                    boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1)'
                  }}
                />
                <Line
                  type="monotone"
                  dataKey="users"
                  stroke="#003D82"
                  strokeWidth={2}
                  dot={{ fill: '#003D82', r: 3 }}
                  activeDot={{ r: 5 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>

        {/* Service Status */}
        <Card>
          <h3 className="text-heading-3 text-text-primary font-semibold mb-4">
            Service Status
          </h3>
          <div className="space-y-3">
            {[
              { name: 'User Service', status: 'UP' },
              { name: 'Payment Service', status: 'UP' },
              { name: 'Notification Service', status: 'UP' },
              { name: 'Analytics Service', status: 'DEGRADED' },
              { name: 'Neo4j Database', status: 'UP' },
              { name: 'Redis Cache', status: 'UP' },
            ].map((service, index) => (
              <div key={index} className="flex items-center justify-between py-2 border-b border-border last:border-0">
                <div className="flex items-center gap-3">
                  <div className={cn(
                    "w-2 h-2 rounded-full",
                    service.status === 'UP' ? 'bg-success' : 'bg-amber'
                  )} />
                  <span className="text-body text-text-primary">{service.name}</span>
                </div>
                <Badge
                  variant={service.status === 'UP' ? 'success' : 'warning'}
                  size="sm"
                >
                  {service.status}
                </Badge>
              </div>
            ))}
          </div>
        </Card>
      </div>

      {/* Activity Feed */}
      <Card>
        <h3 className="text-heading-3 text-text-primary font-semibold mb-4">
          Recent Activity
        </h3>
        <div className="space-y-3">
          {mockRecentActivity.map((activity) => (
            <div key={activity.id} className="flex items-start gap-3 py-3 border-b border-border last:border-0">
              <div className="w-8 h-8 rounded-full bg-blue-pale text-blue flex items-center justify-center text-caption font-semibold">
                {activity.avatar}
              </div>
              <div className="flex-1">
                <p className="text-body text-text-primary">
                  <span className="font-semibold">{activity.user}</span> {activity.action}
                </p>
                <p className="text-caption text-text-muted">{activity.timestamp}</p>
              </div>
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
};

export default DashboardPage;

