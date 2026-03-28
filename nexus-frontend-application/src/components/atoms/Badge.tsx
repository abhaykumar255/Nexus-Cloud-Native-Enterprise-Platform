import React from 'react';
import { cn } from '@/utils/cn';

export interface BadgeProps extends React.HTMLAttributes<HTMLSpanElement> {
  variant?: 'default' | 'success' | 'warning' | 'danger' | 'info';
  size?: 'sm' | 'md';
}

const Badge: React.FC<BadgeProps> = ({
  className,
  variant = 'default',
  size = 'md',
  children,
  ...props
}) => {
  const variants = {
    default: 'bg-background text-text-primary border border-border',
    success: 'bg-success-light text-success border border-success/20',
    warning: 'bg-amber-light text-amber border border-amber/20',
    danger: 'bg-danger-light text-danger border border-danger/20',
    info: 'bg-blue-pale text-blue border border-blue/20',
  };

  const sizes = {
    sm: 'px-2 py-0.5 text-caption',
    md: 'px-2.5 py-1 text-caption',
  };

  return (
    <span
      className={cn(
        'inline-flex items-center justify-center font-medium rounded-small whitespace-nowrap',
        variants[variant],
        sizes[size],
        className
      )}
      {...props}
    >
      {children}
    </span>
  );
};

Badge.displayName = 'Badge';

export default Badge;

