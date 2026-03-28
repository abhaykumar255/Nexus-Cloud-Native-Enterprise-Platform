import React, { useState } from 'react';
import { useUsers } from '@/hooks/useUsers';
import { useAuthStore } from '@/store/authStore';
import { permissions } from '@/utils/permissions';
import Card from '@/components/atoms/Card';
import Button from '@/components/atoms/Button';
import Badge from '@/components/atoms/Badge';
import Spinner from '@/components/atoms/Spinner';
import { Plus, Search, Edit, Trash2 } from 'lucide-react';
import { formatDate } from '@/utils/format';
import { User, UserStatus } from '@/types';
import UserFormModal from './UserFormModal';
import toast from 'react-hot-toast';

const UsersPage: React.FC = () => {
  const [page, setPage] = useState(0);
  const { users, totalPages, isLoading } = useUsers({ page, size: 10 });
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState<User | null>(null);
  const [modalMode, setModalMode] = useState<'create' | 'edit'>('create');
  const [searchQuery, setSearchQuery] = useState('');

  // Get current user and their permissions
  const { user: currentUser } = useAuthStore();
  const canCreate = currentUser ? permissions.canCreateUser(currentUser.role) : false;
  const canEdit = currentUser ? permissions.canEditUser(currentUser.role) : false;
  const canDelete = currentUser ? permissions.canDeleteUser(currentUser.role) : false;

  const getStatusVariant = (status: UserStatus) => {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'INACTIVE':
        return 'default';
      case 'SUSPENDED':
        return 'danger';
      default:
        return 'default';
    }
  };

  const handleCreateUser = () => {
    setSelectedUser(null);
    setModalMode('create');
    setIsModalOpen(true);
  };

  const handleEditUser = (user: User) => {
    setSelectedUser(user);
    setModalMode('edit');
    setIsModalOpen(true);
  };

  const handleDeleteUser = (user: User) => {
    if (confirm(`Are you sure you want to delete ${user.firstName} ${user.lastName}?`)) {
      toast.success(`User ${user.email} deleted successfully`);
      // In real implementation, call delete API
    }
  };

  const handleFormSubmit = (userData: Partial<User>) => {
    if (modalMode === 'create') {
      toast.success(`User ${userData.email} created successfully`);
      // In real implementation, call create API
    } else {
      toast.success(`User ${userData.email} updated successfully`);
      // In real implementation, call update API
    }
  };

  // Filter users based on search query
  const filteredUsers = users.filter(user => {
    if (!searchQuery) return true;
    const query = searchQuery.toLowerCase();
    return (
      user.firstName.toLowerCase().includes(query) ||
      user.lastName.toLowerCase().includes(query) ||
      user.email.toLowerCase().includes(query) ||
      user.username.toLowerCase().includes(query) ||
      user.department?.toLowerCase().includes(query) ||
      user.jobTitle?.toLowerCase().includes(query)
    );
  });

  if (isLoading && users.length === 0) {
    return (
      <div className="flex items-center justify-center h-96">
        <Spinner size="lg" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-heading-1 text-text-primary font-bold mb-2">Users</h1>
          <p className="text-body text-text-secondary">
            Manage your platform users
          </p>
        </div>
        {canCreate && (
          <Button variant="primary" leftIcon={<Plus size={18} />} onClick={handleCreateUser}>
            Add User
          </Button>
        )}
      </div>

      {/* Search */}
      <div className="flex gap-4">
        <div className="flex-1 relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-text-muted" size={18} />
          <input
            type="text"
            placeholder="Search users..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full h-10 pl-10 pr-4 bg-surface border border-border rounded-medium text-body focus:outline-none focus:ring-2 focus:ring-blue"
          />
        </div>
      </div>

      {/* Table */}
      <Card padding="none">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-background border-b border-border">
              <tr>
                <th className="px-6 py-3 text-left text-caption font-semibold text-text-secondary uppercase tracking-wider">
                  User
                </th>
                <th className="px-6 py-3 text-left text-caption font-semibold text-text-secondary uppercase tracking-wider">
                  Email
                </th>
                <th className="px-6 py-3 text-left text-caption font-semibold text-text-secondary uppercase tracking-wider">
                  Role
                </th>
                <th className="px-6 py-3 text-left text-caption font-semibold text-text-secondary uppercase tracking-wider">
                  Status
                </th>
                <th className="px-6 py-3 text-left text-caption font-semibold text-text-secondary uppercase tracking-wider">
                  Created
                </th>
                <th className="px-6 py-3 text-right text-caption font-semibold text-text-secondary uppercase tracking-wider">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {filteredUsers.map((user) => (
                <tr key={user.id} className="hover:bg-background transition-smooth">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 rounded-full bg-blue-pale text-blue flex items-center justify-center text-body font-semibold">
                        {user.firstName[0]}{user.lastName[0]}
                      </div>
                      <div>
                        <p className="text-body font-medium text-text-primary">
                          {user.firstName} {user.lastName}
                        </p>
                        <p className="text-caption text-text-muted">@{user.username}</p>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="text-body text-text-primary">{user.email}</span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Badge variant="info" size="sm">{user.role}</Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <Badge variant={getStatusVariant(user.status)} size="sm">
                      {user.status}
                    </Badge>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className="text-body text-text-muted">
                      {formatDate(user.createdAt)}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-right">
                    <div className="flex items-center justify-end gap-2">
                      {canEdit && (
                        <button
                          onClick={() => handleEditUser(user)}
                          className="p-2 hover:bg-blue-50 rounded-lg transition-colors"
                          title="Edit user"
                        >
                          <Edit size={16} className="text-blue" />
                        </button>
                      )}
                      {canDelete && (
                        <button
                          onClick={() => handleDeleteUser(user)}
                          className="p-2 hover:bg-red-50 rounded-lg transition-colors"
                          title="Delete user"
                        >
                          <Trash2 size={16} className="text-red" />
                        </button>
                      )}
                      {!canEdit && !canDelete && (
                        <span className="text-caption text-text-muted">View Only</span>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="px-6 py-4 border-t border-border flex items-center justify-between">
            <p className="text-caption text-text-muted">
              Page {page + 1} of {totalPages}
            </p>
            <div className="flex gap-2">
              <Button
                variant="outline"
                size="sm"
                onClick={() => setPage(page - 1)}
                disabled={page === 0}
              >
                Previous
              </Button>
              <Button
                variant="outline"
                size="sm"
                onClick={() => setPage(page + 1)}
                disabled={page >= totalPages - 1}
              >
                Next
              </Button>
            </div>
          </div>
        )}
      </Card>

      {/* User Form Modal */}
      <UserFormModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleFormSubmit}
        user={selectedUser}
        mode={modalMode}
      />
    </div>
  );
};

export default UsersPage;

