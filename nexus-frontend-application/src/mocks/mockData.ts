import type { User } from '@/types';

// Mock Users Data
export const mockUsers: User[] = [
  {
    id: 1,
    username: 'alice.johnson',
    email: 'alice@acme.com',
    firstName: 'Alice',
    lastName: 'Johnson',
    role: 'ADMIN',
    status: 'ACTIVE',
    department: 'Engineering',
    jobTitle: 'Senior Engineer',
    phone: '+1 (555) 123-4567',
    avatar: null,
    createdAt: '2024-03-15T10:30:00Z',
    updatedAt: '2024-03-28T08:15:00Z',
  },
  {
    id: 2,
    username: 'bob.smith',
    email: 'bob@acme.com',
    firstName: 'Bob',
    lastName: 'Smith',
    role: 'SUPER_ADMIN',
    status: 'ACTIVE',
    department: 'Engineering',
    jobTitle: 'CTO',
    phone: '+1 (555) 234-5678',
    avatar: null,
    createdAt: '2024-01-10T09:00:00Z',
    updatedAt: '2024-03-27T16:45:00Z',
  },
  {
    id: 3,
    username: 'sarah.chen',
    email: 'sarah@acme.com',
    firstName: 'Sarah',
    lastName: 'Chen',
    role: 'USER',
    status: 'ACTIVE',
    department: 'Product',
    jobTitle: 'Product Manager',
    phone: '+1 (555) 345-6789',
    avatar: null,
    createdAt: '2024-02-20T14:20:00Z',
    updatedAt: '2024-03-28T09:30:00Z',
  },
  {
    id: 4,
    username: 'viewer.demo',
    email: 'viewer@nexus.io',
    firstName: 'Demo',
    lastName: 'Viewer',
    role: 'VIEWER',
    status: 'ACTIVE',
    department: 'Guest',
    jobTitle: 'Viewer Account',
    phone: '+1 (555) 000-0000',
    avatar: null,
    createdAt: '2024-03-01T12:00:00Z',
    updatedAt: '2024-03-28T10:00:00Z',
  },
  {
    id: 5,
    username: 'john.doe',
    email: 'john@acme.com',
    firstName: 'John',
    lastName: 'Doe',
    role: 'USER',
    status: 'ACTIVE',
    department: 'Sales',
    jobTitle: 'Sales Manager',
    phone: '+1 (555) 456-7890',
    avatar: null,
    createdAt: '2024-03-01T11:00:00Z',
    updatedAt: '2024-03-26T15:20:00Z',
  },
  {
    id: 5,
    username: 'emma.wilson',
    email: 'emma@acme.com',
    firstName: 'Emma',
    lastName: 'Wilson',
    role: 'VIEWER',
    status: 'INACTIVE',
    department: 'Marketing',
    jobTitle: 'Content Writer',
    phone: '+1 (555) 567-8901',
    avatar: null,
    createdAt: '2024-01-25T13:45:00Z',
    updatedAt: '2024-03-20T10:00:00Z',
  },
  {
    id: 6,
    username: 'michael.brown',
    email: 'michael@acme.com',
    firstName: 'Michael',
    lastName: 'Brown',
    role: 'ADMIN',
    status: 'ACTIVE',
    department: 'Engineering',
    jobTitle: 'Tech Lead',
    phone: '+1 (555) 678-9012',
    avatar: null,
    createdAt: '2024-02-05T08:30:00Z',
    updatedAt: '2024-03-28T07:45:00Z',
  },
  {
    id: 7,
    username: 'lisa.anderson',
    email: 'lisa@acme.com',
    firstName: 'Lisa',
    lastName: 'Anderson',
    role: 'USER',
    status: 'ACTIVE',
    department: 'Design',
    jobTitle: 'UX Designer',
    phone: '+1 (555) 789-0123',
    avatar: null,
    createdAt: '2024-03-10T12:15:00Z',
    updatedAt: '2024-03-27T14:30:00Z',
  },
  {
    id: 8,
    username: 'david.lee',
    email: 'david@acme.com',
    firstName: 'David',
    lastName: 'Lee',
    role: 'USER',
    status: 'ACTIVE',
    department: 'Engineering',
    jobTitle: 'Backend Developer',
    phone: '+1 (555) 890-1234',
    avatar: null,
    createdAt: '2024-02-28T09:45:00Z',
    updatedAt: '2024-03-28T08:00:00Z',
  },
  {
    id: 9,
    username: 'maria.garcia',
    email: 'maria@acme.com',
    firstName: 'Maria',
    lastName: 'Garcia',
    role: 'USER',
    status: 'ACTIVE',
    department: 'Operations',
    jobTitle: 'Operations Manager',
    phone: '+1 (555) 901-2345',
    avatar: null,
    createdAt: '2024-01-15T10:00:00Z',
    updatedAt: '2024-03-25T16:00:00Z',
  },
  {
    id: 10,
    username: 'james.taylor',
    email: 'james@acme.com',
    firstName: 'James',
    lastName: 'Taylor',
    role: 'VIEWER',
    status: 'SUSPENDED',
    department: 'Sales',
    jobTitle: 'Sales Rep',
    phone: '+1 (555) 012-3456',
    avatar: null,
    createdAt: '2024-03-05T15:30:00Z',
    updatedAt: '2024-03-22T11:00:00Z',
  },
];

// Mock Dashboard KPIs
export const mockDashboardKPIs = {
  totalUsers: {
    value: 12847,
    change: 8.2,
    trend: 'up' as const,
  },
  activeSessions: {
    value: 3291,
    change: 12.5,
    trend: 'up' as const,
  },
  productsListed: {
    value: 8420,
    change: 2.1,
    trend: 'up' as const,
  },
  revenue: {
    value: 284500,
    change: 15.3,
    trend: 'up' as const,
  },
};

// Mock Activity Feed
export const mockRecentActivity = [
  {
    id: 1,
    type: 'user_joined',
    user: 'Sarah Chen',
    action: 'joined the workspace',
    timestamp: '2 minutes ago',
    avatar: 'SC',
  },
  {
    id: 2,
    type: 'order_placed',
    user: 'John Doe',
    action: 'placed order #4821',
    timestamp: '15 minutes ago',
    avatar: 'JD',
  },
  {
    id: 3,
    type: 'alert_triggered',
    user: 'System',
    action: 'API rate limit warning',
    timestamp: '1 hour ago',
    avatar: '!',
  },
  {
    id: 4,
    type: 'product_updated',
    user: 'Emma Wilson',
    action: 'updated Product #2847',
    timestamp: '2 hours ago',
    avatar: 'EW',
  },
  {
    id: 5,
    type: 'user_joined',
    user: 'Michael Brown',
    action: 'logged in from San Francisco',
    timestamp: '3 hours ago',
    avatar: 'MB',
  },
  {
    id: 6,
    type: 'alert_triggered',
    user: 'System',
    action: 'Neo4j query slow (>5000ms)',
    timestamp: '5 hours ago',
    avatar: '!',
  },
];

// Mock Chart Data - User Activity Trend (30 days)
export const mockUserActivityData = Array.from({ length: 30 }, (_, i) => {
  const date = new Date();
  date.setDate(date.getDate() - (29 - i));
  return {
    date: date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
    users: Math.floor(2000 + Math.random() * 1500 + i * 20),
  };
});

// Mock Traffic Sources
export const mockTrafficSources = [
  { name: 'Direct', value: 4240, percentage: 45 },
  { name: 'Organic Search', value: 2826, percentage: 30 },
  { name: 'Referral', value: 1413, percentage: 15 },
  { name: 'Social', value: 706, percentage: 7.5 },
  { name: 'Email', value: 235, percentage: 2.5 },
];

// Mock Top Products
export const mockTopProducts = [
  {
    rank: 1,
    name: 'Enterprise Dashboard',
    category: 'SaaS',
    revenue: 124500,
    sparkline: [20, 35, 45, 50, 55, 60, 70],
  },
  {
    rank: 2,
    name: 'Analytics Pro',
    category: 'Analytics',
    revenue: 98300,
    sparkline: [15, 25, 30, 38, 45, 52, 58],
  },
  {
    rank: 3,
    name: 'Data Connector',
    category: 'Integration',
    revenue: 76200,
    sparkline: [10, 18, 25, 32, 40, 48, 54],
  },
  {
    rank: 4,
    name: 'Mobile SDK',
    category: 'Development',
    revenue: 54800,
    sparkline: [8, 12, 18, 24, 30, 38, 44],
  },
  {
    rank: 5,
    name: 'API Gateway',
    category: 'Infrastructure',
    revenue: 42100,
    sparkline: [5, 10, 15, 20, 26, 32, 38],
  },
];

// Mock Notifications
export const mockNotifications = [
  {
    id: 1,
    type: 'user',
    title: 'New user registered',
    description: 'Sarah Chen joined as USER role',
    timestamp: '2m ago',
    read: false,
    icon: 'UserPlus',
    color: 'blue',
  },
  {
    id: 2,
    type: 'alert',
    title: 'Order threshold exceeded',
    description: 'Product #4821 hit 500 orders',
    timestamp: '15m ago',
    read: false,
    icon: 'AlertTriangle',
    color: 'amber',
  },
  {
    id: 3,
    type: 'warning',
    title: 'API rate limit warning',
    description: 'Endpoint /users nearing limit (80%)',
    timestamp: '1h ago',
    read: true,
    icon: 'AlertOctagon',
    color: 'red',
  },
  {
    id: 4,
    type: 'system',
    title: 'System alert',
    description: 'Neo4j query took >5000ms — performance warning',
    timestamp: '2h ago',
    read: false,
    icon: 'Activity',
    color: 'teal',
  },
  {
    id: 5,
    type: 'success',
    title: 'Weekly digest ready',
    description: 'Your analytics report for Jun 1-7 is ready',
    timestamp: 'Jun 8',
    read: true,
    icon: 'CheckCircle',
    color: 'green',
  },
];

// Mock Analytics Events
export const mockAnalyticsEvents = [
  { id: 1, event: 'page_view', page: '/dashboard', count: 15420, users: 3842, timestamp: '2026-03-28T09:30:00Z' },
  { id: 2, event: 'page_view', page: '/users', count: 8234, users: 2154, timestamp: '2026-03-28T09:35:00Z' },
  { id: 3, event: 'page_view', page: '/analytics', count: 5678, users: 1823, timestamp: '2026-03-28T09:40:00Z' },
  { id: 4, event: 'user_login', page: '/login', count: 4521, users: 4521, timestamp: '2026-03-28T09:45:00Z' },
  { id: 5, event: 'user_register', page: '/register', count: 892, users: 892, timestamp: '2026-03-28T09:50:00Z' },
  { id: 6, event: 'task_create', page: '/tasks', count: 3245, users: 1245, timestamp: '2026-03-28T09:55:00Z' },
  { id: 7, event: 'file_upload', page: '/files', count: 2134, users: 856, timestamp: '2026-03-28T10:00:00Z' },
  { id: 8, event: 'api_call', page: '/api/users', count: 12456, users: 3421, timestamp: '2026-03-28T10:05:00Z' },
];

// Mock Funnel Data
export const mockFunnelData = [
  { step: 'Landing Page', users: 10000, percentage: 100, dropoff: 0 },
  { step: 'Sign Up', users: 7500, percentage: 75, dropoff: 25 },
  { step: 'Email Verify', users: 6000, percentage: 60, dropoff: 15 },
  { step: 'Profile Complete', users: 4500, percentage: 45, dropoff: 15 },
  { step: 'First Task', users: 3000, percentage: 30, dropoff: 15 },
  { step: 'Active User', users: 2250, percentage: 22.5, dropoff: 7.5 },
];

// Mock Conversion Data (time series)
export const mockConversionData = Array.from({ length: 30 }, (_, i) => {
  const date = new Date();
  date.setDate(date.getDate() - (29 - i));
  return {
    date: date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
    signups: Math.floor(150 + Math.random() * 50),
    conversions: Math.floor(80 + Math.random() * 30),
    rate: Math.floor(45 + Math.random() * 20),
  };
});

// Mock Event Distribution (Pie Chart)
export const mockEventDistribution = [
  { name: 'Page Views', value: 42450, color: '#003D82' },
  { name: 'User Actions', value: 18234, color: '#0066CC' },
  { name: 'API Calls', value: 12456, color: '#FFA726' },
  { name: 'File Operations', value: 8542, color: '#14B8A6' },
  { name: 'Errors', value: 1234, color: '#EF4444' },
];

// Mock Real-time Events (for live updates)
export const mockRealtimeEvents = [
  { id: 1, user: 'alice.johnson', action: 'viewed dashboard', timestamp: new Date().toISOString(), type: 'page_view' },
  { id: 2, user: 'bob.smith', action: 'created task #4521', timestamp: new Date().toISOString(), type: 'task_create' },
  { id: 3, user: 'sarah.chen', action: 'uploaded file.pdf', timestamp: new Date().toISOString(), type: 'file_upload' },
  { id: 4, user: 'john.doe', action: 'logged in', timestamp: new Date().toISOString(), type: 'user_login' },
];

// Mock Neo4j Graph Data
export const mockGraphNodes = [
  { id: 'user-1', label: 'Alice Johnson', type: 'User', properties: { email: 'alice@acme.com', role: 'ADMIN' } },
  { id: 'user-2', label: 'Bob Smith', type: 'User', properties: { email: 'bob@acme.com', role: 'SUPER_ADMIN' } },
  { id: 'user-3', label: 'Sarah Chen', type: 'User', properties: { email: 'sarah@acme.com', role: 'USER' } },
  { id: 'task-1', label: 'Build Dashboard', type: 'Task', properties: { status: 'IN_PROGRESS', priority: 'HIGH' } },
  { id: 'task-2', label: 'API Integration', type: 'Task', properties: { status: 'COMPLETED', priority: 'HIGH' } },
  { id: 'task-3', label: 'Write Tests', type: 'Task', properties: { status: 'TODO', priority: 'MEDIUM' } },
  { id: 'project-1', label: 'NEXUS Platform', type: 'Project', properties: { status: 'ACTIVE', team_size: 12 } },
  { id: 'service-1', label: 'Auth Service', type: 'Service', properties: { status: 'UP', version: '1.2.0' } },
  { id: 'service-2', label: 'User Service', type: 'Service', properties: { status: 'UP', version: '1.1.5' } },
  { id: 'service-3', label: 'Task Service', type: 'Service', properties: { status: 'DEGRADED', version: '1.0.8' } },
];

export const mockGraphEdges = [
  { id: 'e1', source: 'user-1', target: 'task-1', type: 'ASSIGNED_TO', label: 'assigned' },
  { id: 'e2', source: 'user-1', target: 'task-2', type: 'COMPLETED', label: 'completed' },
  { id: 'e3', source: 'user-2', target: 'task-3', type: 'CREATED', label: 'created' },
  { id: 'e4', source: 'user-3', target: 'task-1', type: 'COLLABORATES', label: 'collaborates' },
  { id: 'e5', source: 'task-1', target: 'project-1', type: 'BELONGS_TO', label: 'belongs to' },
  { id: 'e6', source: 'task-2', target: 'project-1', type: 'BELONGS_TO', label: 'belongs to' },
  { id: 'e7', source: 'task-3', target: 'project-1', type: 'BELONGS_TO', label: 'belongs to' },
  { id: 'e8', source: 'user-1', target: 'service-1', type: 'MANAGES', label: 'manages' },
  { id: 'e9', source: 'service-1', target: 'service-2', type: 'DEPENDS_ON', label: 'depends on' },
  { id: 'e10', source: 'service-2', target: 'service-3', type: 'CALLS', label: 'calls' },
];

// Mock System Health Metrics (for Admin Monitoring)
export const mockSystemHealth = {
  cpu: { current: 45, max: 100, history: Array.from({ length: 20 }, () => Math.floor(30 + Math.random() * 40)) },
  memory: { current: 62, max: 100, history: Array.from({ length: 20 }, () => Math.floor(50 + Math.random() * 30)) },
  disk: { current: 38, max: 100, history: Array.from({ length: 20 }, () => Math.floor(30 + Math.random() * 20)) },
  network: { current: 28, max: 100, history: Array.from({ length: 20 }, () => Math.floor(15 + Math.random() * 30)) },
};

export const mockServiceMetrics = [
  { name: 'Auth Service', uptime: 99.98, avgResponseTime: 45, requestsPerMin: 1240, errors: 2 },
  { name: 'User Service', uptime: 99.95, avgResponseTime: 62, requestsPerMin: 890, errors: 5 },
  { name: 'Task Service', uptime: 98.5, avgResponseTime: 124, requestsPerMin: 450, errors: 18 },
  { name: 'File Service', uptime: 99.99, avgResponseTime: 38, requestsPerMin: 340, errors: 1 },
  { name: 'Notification Service', uptime: 99.87, avgResponseTime: 52, requestsPerMin: 780, errors: 8 },
];

export const mockErrorLogs = [
  { id: 1, level: 'ERROR', service: 'Task Service', message: 'Database connection timeout', timestamp: '2m ago', count: 3 },
  { id: 2, level: 'WARN', service: 'User Service', message: 'Slow query detected (>1000ms)', timestamp: '15m ago', count: 12 },
  { id: 3, level: 'ERROR', service: 'Auth Service', message: 'JWT token validation failed', timestamp: '1h ago', count: 2 },
  { id: 4, level: 'WARN', service: 'Notification Service', message: 'Kafka lag detected', timestamp: '2h ago', count: 5 },
  { id: 5, level: 'INFO', service: 'File Service', message: 'Storage threshold at 85%', timestamp: '3h ago', count: 1 },
];

