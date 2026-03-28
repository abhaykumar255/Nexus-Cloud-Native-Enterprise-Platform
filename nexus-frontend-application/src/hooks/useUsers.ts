import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { userService, CreateUserRequest, UpdateUserRequest } from '@/services/userService';
import { PaginationParams } from '@/types';
import toast from 'react-hot-toast';

export const useUsers = (params?: PaginationParams) => {
  const queryClient = useQueryClient();

  // Fetch users
  const { data, isLoading, error, refetch } = useQuery({
    queryKey: ['users', params],
    queryFn: () => userService.getUsers(params),
  });

  // Create user mutation
  const createMutation = useMutation({
    mutationFn: (data: CreateUserRequest) => userService.createUser(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
      toast.success('User created successfully');
    },
    onError: (error: any) => {
      toast.error(error.message || 'Failed to create user');
    },
  });

  // Update user mutation
  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: UpdateUserRequest }) =>
      userService.updateUser(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
      toast.success('User updated successfully');
    },
    onError: (error: any) => {
      toast.error(error.message || 'Failed to update user');
    },
  });

  // Delete user mutation
  const deleteMutation = useMutation({
    mutationFn: (id: string) => userService.deleteUser(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['users'] });
      toast.success('User deleted successfully');
    },
    onError: (error: any) => {
      toast.error(error.message || 'Failed to delete user');
    },
  });

  return {
    users: data?.data.content || [],
    totalPages: data?.data.totalPages || 0,
    totalElements: data?.data.totalElements || 0,
    isLoading,
    error,
    refetch,
    createUser: createMutation.mutate,
    updateUser: updateMutation.mutate,
    deleteUser: deleteMutation.mutate,
    isCreating: createMutation.isPending,
    isUpdating: updateMutation.isPending,
    isDeleting: deleteMutation.isPending,
  };
};

export const useUser = (id: string) => {
  return useQuery({
    queryKey: ['user', id],
    queryFn: () => userService.getUserById(id),
    enabled: !!id,
  });
};

