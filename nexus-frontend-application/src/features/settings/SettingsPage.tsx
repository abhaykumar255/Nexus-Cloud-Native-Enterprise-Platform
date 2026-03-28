import React, { useState } from 'react';
import Card from '@/components/atoms/Card';
import Button from '@/components/atoms/Button';
import Input from '@/components/atoms/Input';
import Badge from '@/components/atoms/Badge';
import { User, Shield, Bell, Palette, Globe, Lock, Mail, Phone, Camera, Trash2 } from 'lucide-react';
import { useAuthStore } from '@/store/authStore';

const SettingsPage: React.FC = () => {
  const { user } = useAuthStore();
  const [activeTab, setActiveTab] = useState<'profile' | 'security' | 'notifications' | 'preferences'>('profile');
  const [formData, setFormData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    phone: '',
    bio: '',
    currentPassword: '',
    newPassword: '',
    confirmPassword: '',
  });

  const tabs = [
    { id: 'profile', label: 'Profile', icon: User },
    { id: 'security', label: 'Security', icon: Shield },
    { id: 'notifications', label: 'Notifications', icon: Bell },
    { id: 'preferences', label: 'Preferences', icon: Palette },
  ];

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSave = () => {
    console.log('Saving settings:', formData);
    // In real implementation, call API
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-heading-1 text-text-primary font-bold">Settings</h1>
        <p className="text-body text-text-muted mt-1">
          Manage your account settings and preferences
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-4 gap-6">
        {/* Sidebar Tabs */}
        <Card className="p-4 h-fit">
          <nav className="space-y-1">
            {tabs.map((tab) => {
              const Icon = tab.icon;
              return (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id as any)}
                  className={`w-full flex items-center gap-3 px-4 py-3 rounded-lg text-left transition-colors ${
                    activeTab === tab.id
                      ? 'bg-blue text-white font-semibold'
                      : 'text-text-primary hover:bg-gray-100'
                  }`}
                >
                  <Icon className="w-5 h-5" />
                  <span className="text-body">{tab.label}</span>
                </button>
              );
            })}
          </nav>
        </Card>

        {/* Content Area */}
        <div className="lg:col-span-3">
          {activeTab === 'profile' && (
            <Card className="p-6">
              <h2 className="text-heading-2 font-bold text-text-primary mb-6">Profile Information</h2>
              
              {/* Avatar */}
              <div className="flex items-center gap-6 mb-6 pb-6 border-b border-border">
                <div className="w-24 h-24 rounded-full bg-blue text-white flex items-center justify-center text-heading-1 font-bold">
                  {user?.firstName?.charAt(0)}{user?.lastName?.charAt(0)}
                </div>
                <div>
                  <Button variant="outline" size="sm">
                    <Camera className="w-4 h-4 mr-2" />
                    Change Photo
                  </Button>
                  <p className="text-caption text-text-muted mt-2">JPG, PNG. Max 2MB</p>
                </div>
              </div>

              {/* Form */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
                <div>
                  <label className="block text-body font-medium text-text-primary mb-2">
                    First Name
                  </label>
                  <Input
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleInputChange}
                    placeholder="John"
                  />
                </div>
                <div>
                  <label className="block text-body font-medium text-text-primary mb-2">
                    Last Name
                  </label>
                  <Input
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleInputChange}
                    placeholder="Doe"
                  />
                </div>
                <div>
                  <label className="block text-body font-medium text-text-primary mb-2">
                    Email
                  </label>
                  <Input
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleInputChange}
                    placeholder="john.doe@example.com"
                    leftIcon={<Mail className="w-4 h-4" />}
                  />
                </div>
                <div>
                  <label className="block text-body font-medium text-text-primary mb-2">
                    Phone
                  </label>
                  <Input
                    type="tel"
                    name="phone"
                    value={formData.phone}
                    onChange={handleInputChange}
                    placeholder="+1 (555) 000-0000"
                    leftIcon={<Phone className="w-4 h-4" />}
                  />
                </div>
                <div className="md:col-span-2">
                  <label className="block text-body font-medium text-text-primary mb-2">
                    Bio
                  </label>
                  <textarea
                    name="bio"
                    value={formData.bio}
                    onChange={handleInputChange}
                    rows={4}
                    placeholder="Tell us about yourself..."
                    className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue"
                  />
                </div>
              </div>

              <div className="flex justify-end gap-3">
                <Button variant="outline">Cancel</Button>
                <Button variant="primary" onClick={handleSave}>Save Changes</Button>
              </div>
            </Card>
          )}

          {activeTab === 'security' && (
            <Card className="p-6">
              <h2 className="text-heading-2 font-bold text-text-primary mb-6">Security Settings</h2>

              {/* Change Password */}
              <div className="mb-8 pb-8 border-b border-border">
                <h3 className="text-body font-semibold text-text-primary mb-4">Change Password</h3>
                <div className="space-y-4 max-w-md">
                  <div>
                    <label className="block text-body font-medium text-text-primary mb-2">
                      Current Password
                    </label>
                    <Input
                      type="password"
                      name="currentPassword"
                      value={formData.currentPassword}
                      onChange={handleInputChange}
                      placeholder="••••••••"
                      leftIcon={<Lock className="w-4 h-4" />}
                    />
                  </div>
                  <div>
                    <label className="block text-body font-medium text-text-primary mb-2">
                      New Password
                    </label>
                    <Input
                      type="password"
                      name="newPassword"
                      value={formData.newPassword}
                      onChange={handleInputChange}
                      placeholder="••••••••"
                      leftIcon={<Lock className="w-4 h-4" />}
                    />
                  </div>
                  <div>
                    <label className="block text-body font-medium text-text-primary mb-2">
                      Confirm New Password
                    </label>
                    <Input
                      type="password"
                      name="confirmPassword"
                      value={formData.confirmPassword}
                      onChange={handleInputChange}
                      placeholder="••••••••"
                      leftIcon={<Lock className="w-4 h-4" />}
                    />
                  </div>
                  <Button variant="primary">Update Password</Button>
                </div>
              </div>

              {/* Two-Factor Authentication */}
              <div className="mb-8 pb-8 border-b border-border">
                <h3 className="text-body font-semibold text-text-primary mb-2">Two-Factor Authentication</h3>
                <p className="text-caption text-text-muted mb-4">
                  Add an extra layer of security to your account
                </p>
                <div className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                  <div className="flex items-center gap-3">
                    <Shield className="w-5 h-5 text-green" />
                    <div>
                      <p className="text-body font-medium text-text-primary">2FA Status</p>
                      <p className="text-caption text-text-muted">Currently disabled</p>
                    </div>
                  </div>
                  <Button variant="outline" size="sm">Enable 2FA</Button>
                </div>
              </div>

              {/* Active Sessions */}
              <div>
                <h3 className="text-body font-semibold text-text-primary mb-4">Active Sessions</h3>
                <div className="space-y-3">
                  <div className="flex items-center justify-between p-4 border border-border rounded-lg">
                    <div>
                      <p className="text-body font-medium text-text-primary">MacBook Pro • Chrome</p>
                      <p className="text-caption text-text-muted">San Francisco, CA • Current session</p>
                    </div>
                    <Badge variant="success">Active</Badge>
                  </div>
                  <div className="flex items-center justify-between p-4 border border-border rounded-lg">
                    <div>
                      <p className="text-body font-medium text-text-primary">iPhone 14 • Safari</p>
                      <p className="text-caption text-text-muted">San Francisco, CA • 2 hours ago</p>
                    </div>
                    <Button variant="outline" size="sm">Revoke</Button>
                  </div>
                </div>
              </div>
            </Card>
          )}

          {activeTab === 'notifications' && (
            <Card className="p-6">
              <h2 className="text-heading-2 font-bold text-text-primary mb-6">Notification Preferences</h2>

              <div className="space-y-6">
                <div className="pb-6 border-b border-border">
                  <h3 className="text-body font-semibold text-text-primary mb-4">Email Notifications</h3>
                  <div className="space-y-3">
                    {[
                      { label: 'Task assignments', description: 'When you are assigned a new task' },
                      { label: 'Task updates', description: 'When a task you\'re involved in is updated' },
                      { label: 'Comments', description: 'When someone comments on your tasks' },
                      { label: 'Weekly digest', description: 'A weekly summary of your activity' },
                    ].map((item, idx) => (
                      <label key={idx} className="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg cursor-pointer">
                        <div>
                          <p className="text-body font-medium text-text-primary">{item.label}</p>
                          <p className="text-caption text-text-muted">{item.description}</p>
                        </div>
                        <input type="checkbox" className="w-5 h-5 text-blue rounded" defaultChecked />
                      </label>
                    ))}
                  </div>
                </div>

                <div>
                  <h3 className="text-body font-semibold text-text-primary mb-4">Push Notifications</h3>
                  <div className="space-y-3">
                    {[
                      { label: 'Browser notifications', description: 'Show desktop notifications in your browser' },
                      { label: 'Mobile push', description: 'Send push notifications to your mobile device' },
                      { label: 'Critical alerts only', description: 'Only receive notifications for critical alerts' },
                    ].map((item, idx) => (
                      <label key={idx} className="flex items-center justify-between p-3 hover:bg-gray-50 rounded-lg cursor-pointer">
                        <div>
                          <p className="text-body font-medium text-text-primary">{item.label}</p>
                          <p className="text-caption text-text-muted">{item.description}</p>
                        </div>
                        <input type="checkbox" className="w-5 h-5 text-blue rounded" defaultChecked={idx === 0} />
                      </label>
                    ))}
                  </div>
                </div>
              </div>
            </Card>
          )}

          {activeTab === 'preferences' && (
            <Card className="p-6">
              <h2 className="text-heading-2 font-bold text-text-primary mb-6">App Preferences</h2>

              <div className="space-y-6">
                <div className="pb-6 border-b border-border">
                  <h3 className="text-body font-semibold text-text-primary mb-4">Appearance</h3>
                  <div className="grid grid-cols-3 gap-3">
                    {['Light', 'Dark', 'System'].map((theme) => (
                      <button
                        key={theme}
                        className={`p-4 border-2 rounded-lg text-center transition-colors ${
                          theme === 'Light' ? 'border-blue bg-blue-pale' : 'border-border hover:border-blue-200'
                        }`}
                      >
                        <Palette className="w-6 h-6 mx-auto mb-2 text-text-primary" />
                        <p className="text-body font-medium text-text-primary">{theme}</p>
                      </button>
                    ))}
                  </div>
                </div>

                <div className="pb-6 border-b border-border">
                  <h3 className="text-body font-semibold text-text-primary mb-4">Language & Region</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div>
                      <label className="block text-body font-medium text-text-primary mb-2">
                        Language
                      </label>
                      <select className="w-full px-4 py-2 border border-border rounded-lg">
                        <option>English (US)</option>
                        <option>Spanish</option>
                        <option>French</option>
                        <option>German</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-body font-medium text-text-primary mb-2">
                        Timezone
                      </label>
                      <select className="w-full px-4 py-2 border border-border rounded-lg">
                        <option>Pacific Time (PT)</option>
                        <option>Eastern Time (ET)</option>
                        <option>Central European Time (CET)</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="text-body font-semibold text-text-primary mb-4">Data & Privacy</h3>
                  <div className="space-y-3">
                    <Button variant="outline" className="w-full justify-start">
                      <Globe className="w-4 h-4 mr-2" />
                      Download your data
                    </Button>
                    <Button variant="outline" className="w-full justify-start text-red border-red hover:bg-red-50">
                      <Trash2 className="w-4 h-4 mr-2" />
                      Delete account
                    </Button>
                  </div>
                </div>
              </div>
            </Card>
          )}
        </div>
      </div>
    </div>
  );
};

export default SettingsPage;


