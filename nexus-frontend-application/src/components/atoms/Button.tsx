import React from 'react';
import { cn } from '@/utils/cn';
import { Loader2 } from 'lucide-react';

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'success' | 'danger' | 'ghost' | 'outline';
  size?: 'sm' | 'md' | 'lg';
  isLoading?: boolean;
  leftIcon?: React.ReactNode;
  rightIcon?: React.ReactNode;
  fullWidth?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      className,
      variant = 'primary',
      size = 'md',
      isLoading = false,
      leftIcon,
      rightIcon,
      fullWidth = false,
      disabled,
      children,
      ...props
    },
    ref
  ) => {
    const baseStyles = 'inline-flex items-center justify-center font-medium transition-smooth rounded-medium focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed';

    const variants = {
      primary: 'bg-blue text-white hover:bg-blue-dark focus:ring-blue',
      secondary: 'bg-navy text-white hover:bg-navy-light focus:ring-navy',
      success: 'bg-success text-white hover:opacity-90 focus:ring-success',
      danger: 'bg-danger text-white hover:opacity-90 focus:ring-danger',
      ghost: 'bg-transparent text-text-primary hover:bg-background-dark/5 focus:ring-blue',
      outline: 'bg-transparent border-2 border-border text-text-primary hover:bg-background-dark/5 focus:ring-blue',
    };

    const sizes = {
      sm: 'h-8 px-3 text-caption gap-1.5',
      md: 'h-10 px-4 text-body gap-2',
      lg: 'h-12 px-6 text-body-large gap-2',
    };

    return (
      <button
        ref={ref}
        className={cn(
          baseStyles,
          variants[variant],
          sizes[size],
          fullWidth && 'w-full',
          className
        )}
        disabled={disabled || isLoading}
        {...props}
      >
        {isLoading ? (
          <Loader2 className="animate-spin" size={size === 'sm' ? 14 : 16} />
        ) : leftIcon ? (
          <span className="flex items-center">{leftIcon}</span>
        ) : null}
        
        {children}
        
        {rightIcon && !isLoading && (
          <span className="flex items-center">{rightIcon}</span>
        )}
      </button>
    );
  }
);

Button.displayName = 'Button';

export default Button;

