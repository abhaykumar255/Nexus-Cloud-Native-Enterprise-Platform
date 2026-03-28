import React, { useState, useEffect } from 'react';
import Modal from '@/components/molecules/Modal';
import Input from '@/components/atoms/Input';
import Button from '@/components/atoms/Button';
import { User, Mail, Shield } from 'lucide-react';
import { User as UserType, UserRole, UserStatus } from '@/types';

interface UserFormModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (user: Partial<UserType>) => void;
  user?: UserType | null;
  mode: 'create' | 'edit';
}

const UserFormModal: React.FC<UserFormModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  user,
  mode,
}) => {
  const [formData, setFormData] = useState<Partial<UserType>>({
    firstName: '',
    lastName: '',
    email: '',
    role: 'USER' as UserRole,
    status: 'ACTIVE' as UserStatus,
  });

  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    if (user && mode === 'edit') {
      setFormData({
        id: user.id,
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        role: user.role,
        status: user.status,
      });
    } else {
      setFormData({
        firstName: '',
        lastName: '',
        email: '',
        role: 'USER',
        status: 'ACTIVE',
      });
    }
    setErrors({});
  }, [user, mode, isOpen]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => {
        const newErrors = { ...prev };
        delete newErrors[name];
        return newErrors;
      });
    }
  };

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.firstName?.trim()) {
      newErrors.firstName = 'First name is required';
    }
    if (!formData.lastName?.trim()) {
      newErrors.lastName = 'Last name is required';
    }
    if (!formData.email?.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i.test(formData.email)) {
      newErrors.email = 'Invalid email address';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validate()) {
      onSubmit(formData);
      onClose();
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title={mode === 'create' ? 'Create New User' : 'Edit User'}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-body font-medium text-text-primary mb-2">
              First Name <span className="text-red">*</span>
            </label>
            <Input
              name="firstName"
              value={formData.firstName || ''}
              onChange={handleChange}
              placeholder="John"
              leftIcon={<User className="w-4 h-4" />}
              error={errors.firstName}
            />
            {errors.firstName && (
              <p className="text-caption text-red mt-1">{errors.firstName}</p>
            )}
          </div>

          <div>
            <label className="block text-body font-medium text-text-primary mb-2">
              Last Name <span className="text-red">*</span>
            </label>
            <Input
              name="lastName"
              value={formData.lastName || ''}
              onChange={handleChange}
              placeholder="Doe"
              error={errors.lastName}
            />
            {errors.lastName && (
              <p className="text-caption text-red mt-1">{errors.lastName}</p>
            )}
          </div>
        </div>

        <div>
          <label className="block text-body font-medium text-text-primary mb-2">
            Email <span className="text-red">*</span>
          </label>
          <Input
            type="email"
            name="email"
            value={formData.email || ''}
            onChange={handleChange}
            placeholder="john.doe@example.com"
            leftIcon={<Mail className="w-4 h-4" />}
            error={errors.email}
            disabled={mode === 'edit'}
          />
          {errors.email && (
            <p className="text-caption text-red mt-1">{errors.email}</p>
          )}
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-body font-medium text-text-primary mb-2">
              Role <span className="text-red">*</span>
            </label>
            <select
              name="role"
              value={formData.role}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue"
            >
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
              <option value="SUPER_ADMIN">Super Admin</option>
            </select>
          </div>

          <div>
            <label className="block text-body font-medium text-text-primary mb-2">
              Status <span className="text-red">*</span>
            </label>
            <select
              name="status"
              value={formData.status}
              onChange={handleChange}
              className="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue"
            >
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="SUSPENDED">Suspended</option>
            </select>
          </div>
        </div>

        <div className="flex items-center gap-2 p-3 bg-blue-pale rounded-lg">
          <Shield className="w-5 h-5 text-blue" />
          <p className="text-caption text-text-primary">
            {mode === 'create' 
              ? 'A temporary password will be sent to the user\'s email.' 
              : 'User will be notified of profile changes via email.'}
          </p>
        </div>

        <div className="flex justify-end gap-3 pt-4 border-t border-border">
          <Button type="button" variant="outline" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" variant="primary">
            {mode === 'create' ? 'Create User' : 'Save Changes'}
          </Button>
        </div>
      </form>
    </Modal>
  );
};

export default UserFormModal;

