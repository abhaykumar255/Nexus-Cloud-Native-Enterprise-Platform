import { format, formatDistance, formatRelative } from 'date-fns';

/**
 * Format date to readable string
 */
export const formatDate = (date: string | Date, formatStr = 'MMM dd, yyyy'): string => {
  return format(new Date(date), formatStr);
};

/**
 * Format date to relative time (e.g., "2 hours ago")
 */
export const formatRelativeTime = (date: string | Date): string => {
  return formatDistance(new Date(date), new Date(), { addSuffix: true });
};

/**
 * Format date to relative description (e.g., "yesterday at 3:20 PM")
 */
export const formatRelativeDate = (date: string | Date): string => {
  return formatRelative(new Date(date), new Date());
};

/**
 * Format number with commas
 */
export const formatNumber = (num: number): string => {
  return new Intl.NumberFormat('en-US').format(num);
};

/**
 * Format number as percentage
 */
export const formatPercentage = (num: number, decimals = 1): string => {
  return `${num.toFixed(decimals)}%`;
};

/**
 * Format bytes to human-readable size
 */
export const formatBytes = (bytes: number, decimals = 2): string => {
  if (bytes === 0) return '0 Bytes';

  const k = 1024;
  const dm = decimals < 0 ? 0 : decimals;
  const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];

  const i = Math.floor(Math.log(bytes) / Math.log(k));

  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(dm))} ${sizes[i]}`;
};

/**
 * Truncate string with ellipsis
 */
export const truncate = (str: string, length: number): string => {
  if (str.length <= length) return str;
  return `${str.slice(0, length)}...`;
};

