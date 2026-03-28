import React from 'react';
import { cn } from '@/utils/cn';
import { Loader2 } from 'lucide-react';

export interface SpinnerProps {
  size?: 'sm' | 'md' | 'lg';
  className?: string;
}

const Spinner: React.FC<SpinnerProps> = ({ size = 'md', className }) => {
  const sizes = {
    sm: 16,
    md: 24,
    lg: 32,
  };

  return (
    <Loader2
      className={cn('animate-spin text-blue', className)}
      size={sizes[size]}
    />
  );
};

Spinner.displayName = 'Spinner';

export default Spinner;

