import { api } from './api';
import { Service, PaginatedResponse, PaginationParams } from '@/types';

export interface CreateServiceRequest {
  name: string;
  description: string;
  type: string;
  endpoint: string;
  healthCheckUrl: string;
}

export const serviceService = {
  getServices: (params?: PaginationParams) =>
    api.get<PaginatedResponse<Service>>('/services', params),

  getServiceById: (id: string) =>
    api.get<Service>(`/services/${id}`),

  createService: (data: CreateServiceRequest) =>
    api.post<Service>('/services', data),

  updateService: (id: string, data: Partial<CreateServiceRequest>) =>
    api.put<Service>(`/services/${id}`, data),

  deleteService: (id: string) =>
    api.delete(`/services/${id}`),

  healthCheck: (id: string) =>
    api.get<{ status: string }>(`/services/${id}/health`),
};

