import React, { useState } from 'react';
import Card from '@/components/atoms/Card';
import Button from '@/components/atoms/Button';
import Badge from '@/components/atoms/Badge';
import {
  LineChart, Line, PieChart, Pie, Cell,
  FunnelChart, Funnel, XAxis, YAxis, CartesianGrid,
  Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import { Calendar, TrendingUp, Users, Activity, Download, Filter } from 'lucide-react';
import { 
  mockAnalyticsEvents, 
  mockFunnelData, 
  mockConversionData, 
  mockEventDistribution,
  mockRealtimeEvents 
} from '@/mocks/mockData';

const AnalyticsPage: React.FC = () => {
  const [dateRange, setDateRange] = useState('30d');
  const [eventFilter, setEventFilter] = useState('all');
  const [realtimeEvents, setRealtimeEvents] = useState(mockRealtimeEvents);
  const [showFilters, setShowFilters] = useState(false);

  // Simulate real-time updates
  React.useEffect(() => {
    const interval = setInterval(() => {
      const newEvent = {
        id: Date.now(),
        user: ['alice.johnson', 'bob.smith', 'sarah.chen'][Math.floor(Math.random() * 3)],
        action: ['viewed dashboard', 'created task', 'uploaded file'][Math.floor(Math.random() * 3)],
        timestamp: new Date().toISOString(),
        type: ['page_view', 'task_create', 'file_upload'][Math.floor(Math.random() * 3)],
      };
      setRealtimeEvents(prev => [newEvent, ...prev.slice(0, 9)]);
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  const filteredEvents = eventFilter === 'all' 
    ? mockAnalyticsEvents 
    : mockAnalyticsEvents.filter(e => e.event === eventFilter);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-heading-1 text-text-primary font-bold">Analytics Dashboard</h1>
          <p className="text-body text-text-muted mt-1">
            Track user behavior, conversions, and system events
          </p>
        </div>
        <div className="flex gap-3">
          <div className="relative">
            <Button
              variant="outline"
              size="sm"
              onClick={() => setShowFilters(!showFilters)}
            >
              <Filter className="w-4 h-4 mr-2" />
              Event Filter
            </Button>
            {showFilters && (
              <div className="absolute right-0 top-12 w-48 bg-surface border border-border rounded-medium shadow-dropdown py-2 z-10">
                <button
                  onClick={() => { setEventFilter('all'); setShowFilters(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${eventFilter === 'all' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  All Events
                </button>
                <button
                  onClick={() => { setEventFilter('page_view'); setShowFilters(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${eventFilter === 'page_view' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  Page Views
                </button>
                <button
                  onClick={() => { setEventFilter('task_create'); setShowFilters(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${eventFilter === 'task_create' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  Task Created
                </button>
                <button
                  onClick={() => { setEventFilter('file_upload'); setShowFilters(false); }}
                  className={`w-full text-left px-4 py-2 text-body hover:bg-background ${eventFilter === 'file_upload' ? 'text-blue font-semibold' : 'text-text-primary'}`}
                >
                  File Uploads
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
          <Button variant="primary" size="sm">
            <Download className="w-4 h-4 mr-2" />
            Export
          </Button>
        </div>
      </div>

      {/* KPI Summary */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Card className="p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-caption text-text-muted">Total Events</p>
              <p className="text-heading-2 font-bold text-text-primary mt-1">83,024</p>
              <p className="text-caption text-green mt-1">↑ 12.5%</p>
            </div>
            <Activity className="w-10 h-10 text-blue" />
          </div>
        </Card>
        <Card className="p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-caption text-text-muted">Unique Users</p>
              <p className="text-heading-2 font-bold text-text-primary mt-1">12,847</p>
              <p className="text-caption text-green mt-1">↑ 8.2%</p>
            </div>
            <Users className="w-10 h-10 text-teal" />
          </div>
        </Card>
        <Card className="p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-caption text-text-muted">Conversion Rate</p>
              <p className="text-heading-2 font-bold text-text-primary mt-1">22.5%</p>
              <p className="text-caption text-red mt-1">↓ 2.1%</p>
            </div>
            <TrendingUp className="w-10 h-10 text-amber" />
          </div>
        </Card>
        <Card className="p-4">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-caption text-text-muted">Avg Session</p>
              <p className="text-heading-2 font-bold text-text-primary mt-1">8m 42s</p>
              <p className="text-caption text-green mt-1">↑ 15.3%</p>
            </div>
            <Calendar className="w-10 h-10 text-purple" />
          </div>
        </Card>
      </div>

      {/* Charts Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Conversion Funnel */}
        <Card className="p-6">
          <h3 className="text-heading-3 font-semibold mb-4">User Conversion Funnel</h3>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <FunnelChart>
                <Tooltip />
                <Funnel
                  dataKey="users"
                  data={mockFunnelData}
                  isAnimationActive
                >
                  {mockFunnelData.map((_entry, index) => (
                    <Cell key={`cell-${index}`} fill={['#003D82', '#0066CC', '#3399FF', '#66B2FF', '#99CCFF', '#CCE5FF'][index]} />
                  ))}
                </Funnel>
              </FunnelChart>
            </ResponsiveContainer>
          </div>
        </Card>

        {/* Event Distribution */}
        <Card className="p-6">
          <h3 className="text-heading-3 font-semibold mb-4">Event Distribution</h3>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={mockEventDistribution}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {mockEventDistribution.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={entry.color} />
                  ))}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </Card>

        {/* Conversion Over Time */}
        <Card className="p-6 lg:col-span-2">
          <h3 className="text-heading-3 font-semibold mb-4">Conversion Trend (30 Days)</h3>
          <div className="h-80">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={mockConversionData}>
                <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
                <XAxis dataKey="date" tick={{ fill: '#6B7280', fontSize: 12 }} />
                <YAxis tick={{ fill: '#6B7280', fontSize: 12 }} />
                <Tooltip />
                <Legend />
                <Line type="monotone" dataKey="signups" stroke="#0066CC" strokeWidth={2} name="Sign-ups" />
                <Line type="monotone" dataKey="conversions" stroke="#14B8A6" strokeWidth={2} name="Conversions" />
                <Line type="monotone" dataKey="rate" stroke="#FFA726" strokeWidth={2} name="Rate (%)" />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>

        {/* Event Breakdown Table */}
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-heading-3 font-semibold">Event Breakdown</h3>
            <select
              className="px-3 py-1.5 border border-border rounded-lg text-body"
              value={eventFilter}
              onChange={(e) => setEventFilter(e.target.value)}
            >
              <option value="all">All Events</option>
              <option value="page_view">Page Views</option>
              <option value="user_login">User Login</option>
              <option value="task_create">Task Create</option>
              <option value="api_call">API Calls</option>
            </select>
          </div>
          <div className="space-y-3 max-h-80 overflow-y-auto">
            {filteredEvents.map((event) => (
              <div key={event.id} className="flex items-center justify-between py-2 border-b border-border">
                <div>
                  <p className="text-body font-medium text-text-primary">{event.event}</p>
                  <p className="text-caption text-text-muted">{event.page}</p>
                </div>
                <div className="text-right">
                  <p className="text-body font-semibold text-text-primary">{event.count.toLocaleString()}</p>
                  <p className="text-caption text-text-muted">{event.users.toLocaleString()} users</p>
                </div>
              </div>
            ))}
          </div>
        </Card>

        {/* Real-time Events */}
        <Card className="p-6">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-heading-3 font-semibold">Real-time Events</h3>
            <Badge variant="success">
              <span className="inline-block w-2 h-2 bg-green rounded-full mr-2 animate-pulse"></span>
              Live
            </Badge>
          </div>
          <div className="space-y-2 max-h-80 overflow-y-auto">
            {realtimeEvents.map((event) => (
              <div
                key={event.id}
                className="flex items-start gap-3 py-2 border-b border-border last:border-0"
              >
                <div className="w-8 h-8 rounded-full bg-blue-pale text-blue flex items-center justify-center text-caption font-semibold">
                  {event.user.charAt(0).toUpperCase()}
                </div>
                <div className="flex-1">
                  <p className="text-body text-text-primary">
                    <span className="font-semibold">{event.user}</span> {event.action}
                  </p>
                  <p className="text-caption text-text-muted">
                    {new Date(event.timestamp).toLocaleTimeString()}
                  </p>
                </div>
                <Badge
                  variant={
                    event.type === 'page_view' ? 'info' :
                    event.type === 'task_create' ? 'success' : 'warning'
                  }
                  size="sm"
                >
                  {event.type}
                </Badge>
              </div>
            ))}
          </div>
        </Card>
      </div>
    </div>
  );
};

export default AnalyticsPage;
