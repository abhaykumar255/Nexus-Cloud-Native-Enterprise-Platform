import React, { useState, useEffect } from 'react';
import Card from '@/components/atoms/Card';
import Badge from '@/components/atoms/Badge';
import Button from '@/components/atoms/Button';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import {
  Server, Cpu, HardDrive, Activity, AlertCircle, CheckCircle,
  RefreshCw, Download, Database
} from 'lucide-react';
import { mockSystemHealth, mockServiceMetrics, mockErrorLogs } from '@/mocks/mockData';

const AdminMonitoringPage: React.FC = () => {
  const [systemHealth, setSystemHealth] = useState(mockSystemHealth);
  const [autoRefresh, setAutoRefresh] = useState(true);

  // Simulate real-time updates
  useEffect(() => {
    if (!autoRefresh) return;

    const interval = setInterval(() => {
      setSystemHealth(prev => ({
        cpu: {
          ...prev.cpu,
          current: Math.min(Math.max(prev.cpu.current + (Math.random() - 0.5) * 10, 20), 80),
          history: [...prev.cpu.history.slice(1), Math.floor(30 + Math.random() * 40)]
        },
        memory: {
          ...prev.memory,
          current: Math.min(Math.max(prev.memory.current + (Math.random() - 0.5) * 10, 40), 85),
          history: [...prev.memory.history.slice(1), Math.floor(50 + Math.random() * 30)]
        },
        disk: {
          ...prev.disk,
          current: Math.min(Math.max(prev.disk.current + (Math.random() - 0.5) * 5, 30), 50),
          history: [...prev.disk.history.slice(1), Math.floor(30 + Math.random() * 20)]
        },
        network: {
          ...prev.network,
          current: Math.min(Math.max(prev.network.current + (Math.random() - 0.5) * 15, 10), 60),
          history: [...prev.network.history.slice(1), Math.floor(15 + Math.random() * 30)]
        },
      }));
    }, 3000);

    return () => clearInterval(interval);
  }, [autoRefresh]);

  const getHealthStatus = (value: number) => {
    if (value < 50) return { status: 'healthy', color: 'green', badge: 'success' };
    if (value < 75) return { status: 'warning', color: 'amber', badge: 'warning' };
    return { status: 'critical', color: 'red', badge: 'danger' };
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-heading-1 text-text-primary font-bold">System Monitoring</h1>
          <p className="text-body text-text-muted mt-1">
            Monitor system health, services, and performance metrics
          </p>
        </div>
        <div className="flex gap-3">
          <Button 
            variant={autoRefresh ? 'primary' : 'outline'} 
            size="sm"
            onClick={() => setAutoRefresh(!autoRefresh)}
          >
            <RefreshCw className={`w-4 h-4 mr-2 ${autoRefresh ? 'animate-spin' : ''}`} />
            {autoRefresh ? 'Auto-refresh ON' : 'Auto-refresh OFF'}
          </Button>
          <Button variant="outline" size="sm">
            <Download className="w-4 h-4 mr-2" />
            Export Report
          </Button>
        </div>
      </div>

      {/* System Health Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        {[
          { key: 'cpu', label: 'CPU Usage', icon: Cpu, data: systemHealth.cpu },
          { key: 'memory', label: 'Memory', icon: Database, data: systemHealth.memory },
          { key: 'disk', label: 'Disk Usage', icon: HardDrive, data: systemHealth.disk },
          { key: 'network', label: 'Network', icon: Activity, data: systemHealth.network },
        ].map(({ key, label, icon: Icon, data }) => {
          const health = getHealthStatus(data.current);
          return (
            <Card key={key} className="p-4">
              <div className="flex items-center justify-between mb-2">
                <Icon className={`w-6 h-6 text-${health.color}`} />
                <Badge variant={health.badge as any} size="sm">{health.status}</Badge>
              </div>
              <p className="text-caption text-text-muted mb-1">{label}</p>
              <p className="text-heading-2 font-bold text-text-primary">
                {data.current.toFixed(1)}%
              </p>
              <div className="mt-3">
                <div className="h-1.5 bg-gray-200 rounded-full overflow-hidden">
                  <div 
                    className={`h-full bg-${health.color} transition-all duration-500`}
                    style={{ width: `${data.current}%` }}
                  />
                </div>
              </div>
            </Card>
          );
        })}
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* CPU History */}
        <Card className="p-6">
          <h3 className="text-heading-3 font-semibold mb-4">CPU Usage History</h3>
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={systemHealth.cpu.history.map((value, idx) => ({ time: idx, value }))}>
                <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
                <XAxis dataKey="time" tick={{ fill: '#6B7280', fontSize: 12 }} />
                <YAxis tick={{ fill: '#6B7280', fontSize: 12 }} domain={[0, 100]} />
                <Tooltip />
                <Line type="monotone" dataKey="value" stroke="#0066CC" strokeWidth={2} dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>

        {/* Memory History */}
        <Card className="p-6">
          <h3 className="text-heading-3 font-semibold mb-4">Memory Usage History</h3>
          <div className="h-64">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={systemHealth.memory.history.map((value, idx) => ({ time: idx, value }))}>
                <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
                <XAxis dataKey="time" tick={{ fill: '#6B7280', fontSize: 12 }} />
                <YAxis tick={{ fill: '#6B7280', fontSize: 12 }} domain={[0, 100]} />
                <Tooltip />
                <Line type="monotone" dataKey="value" stroke="#14B8A6" strokeWidth={2} dot={false} />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </Card>
      </div>

      {/* Service Metrics */}
      <Card className="p-6">
        <h3 className="text-heading-3 font-semibold mb-4">Service Health</h3>
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-border">
                <th className="text-left py-3 px-4 text-caption font-semibold text-text-muted">SERVICE</th>
                <th className="text-left py-3 px-4 text-caption font-semibold text-text-muted">UPTIME</th>
                <th className="text-left py-3 px-4 text-caption font-semibold text-text-muted">AVG RESPONSE</th>
                <th className="text-left py-3 px-4 text-caption font-semibold text-text-muted">REQ/MIN</th>
                <th className="text-left py-3 px-4 text-caption font-semibold text-text-muted">ERRORS</th>
                <th className="text-left py-3 px-4 text-caption font-semibold text-text-muted">STATUS</th>
              </tr>
            </thead>
            <tbody>
              {mockServiceMetrics.map((service, idx) => (
                <tr key={idx} className="border-b border-border last:border-0 hover:bg-gray-50">
                  <td className="py-3 px-4">
                    <div className="flex items-center gap-2">
                      <Server className="w-4 h-4 text-text-muted" />
                      <span className="text-body font-medium text-text-primary">{service.name}</span>
                    </div>
                  </td>
                  <td className="py-3 px-4">
                    <span className="text-body text-text-primary">{service.uptime}%</span>
                  </td>
                  <td className="py-3 px-4">
                    <span className="text-body text-text-primary">{service.avgResponseTime}ms</span>
                  </td>
                  <td className="py-3 px-4">
                    <span className="text-body text-text-primary">{service.requestsPerMin}</span>
                  </td>
                  <td className="py-3 px-4">
                    <Badge variant={service.errors > 10 ? 'danger' : service.errors > 5 ? 'warning' : 'success'} size="sm">
                      {service.errors}
                    </Badge>
                  </td>
                  <td className="py-3 px-4">
                    <Badge variant={service.uptime >= 99.5 ? 'success' : service.uptime >= 98 ? 'warning' : 'danger'}>
                      {service.uptime >= 99.5 ? <CheckCircle className="w-3 h-3 mr-1" /> : <AlertCircle className="w-3 h-3 mr-1" />}
                      {service.uptime >= 99.5 ? 'Healthy' : service.uptime >= 98 ? 'Degraded' : 'Down'}
                    </Badge>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </Card>

      {/* Error Logs */}
      <Card className="p-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-heading-3 font-semibold">Recent Error Logs</h3>
          <Button variant="outline" size="sm">View All</Button>
        </div>
        <div className="space-y-3">
          {mockErrorLogs.map((log) => (
            <div
              key={log.id}
              className="flex items-start gap-4 p-4 border border-border rounded-lg hover:bg-gray-50"
            >
              <div className={`w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0 ${
                log.level === 'ERROR' ? 'bg-red-pale text-red' :
                log.level === 'WARN' ? 'bg-amber-pale text-amber' :
                'bg-blue-pale text-blue'
              }`}>
                <AlertCircle className="w-5 h-5" />
              </div>
              <div className="flex-1">
                <div className="flex items-center gap-2 mb-1">
                  <Badge variant={log.level === 'ERROR' ? 'danger' : log.level === 'WARN' ? 'warning' : 'info'} size="sm">
                    {log.level}
                  </Badge>
                  <span className="text-body font-medium text-text-primary">{log.service}</span>
                </div>
                <p className="text-body text-text-primary mb-1">{log.message}</p>
                <div className="flex items-center gap-3 text-caption text-text-muted">
                  <span>{log.timestamp}</span>
                  <span>•</span>
                  <span>Count: {log.count}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      </Card>
    </div>
  );
};

export default AdminMonitoringPage;

