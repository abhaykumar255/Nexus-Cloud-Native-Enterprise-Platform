import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { useAuth } from '@/hooks/useAuth';
import { LoginRequest } from '@/types';
import Button from '@/components/atoms/Button';
import Input from '@/components/atoms/Input';
import Card from '@/components/atoms/Card';
import { LogIn } from 'lucide-react';

const loginSchema = z.object({
  username: z.string().min(1, 'Username is required'),
  password: z.string().min(1, 'Password is required'),
});

type LoginFormData = z.infer<typeof loginSchema>;

const LoginPage: React.FC = () => {
  const navigate = useNavigate();
  const { login, isLoading, isAuthenticated } = useAuth();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
  });

  const onSubmit = (data: LoginFormData) => {
    login(data as LoginRequest);
  };

  React.useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard');
    }
  }, [isAuthenticated, navigate]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-background p-4">
      <Card className="w-full max-w-md" padding="lg">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-heading-1 text-navy font-bold mb-2">NEXUS</h1>
          <p className="text-body text-text-secondary">
            Sign in to your account
          </p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input
            label="Username"
            {...register('username')}
            error={errors.username?.message}
            placeholder="Enter your username"
            fullWidth
          />

          <Input
            label="Password"
            type="password"
            {...register('password')}
            error={errors.password?.message}
            placeholder="Enter your password"
            fullWidth
          />

          <Button
            type="submit"
            variant="primary"
            fullWidth
            isLoading={isLoading}
            leftIcon={<LogIn size={18} />}
          >
            Sign In
          </Button>
        </form>

        {/* Mock Credentials Info */}
        <div className="mt-6 p-4 bg-blue/10 border border-blue/20 rounded-lg">
          <p className="text-caption font-semibold text-blue mb-2">
            🧪 Demo Mode - Use these credentials:
          </p>
          <div className="space-y-1 text-caption text-text-secondary">
            <p><strong>Admin:</strong> alice@acme.com / password123</p>
            <p><strong>Super Admin:</strong> bob@acme.com / password123</p>
            <p><strong>User:</strong> sarah@acme.com / password123</p>
          </div>
        </div>

        {/* Footer */}
        <div className="mt-6 text-center">
          <p className="text-caption text-text-muted">
            Don't have an account?{' '}
            <Link
              to="/register"
              className="text-blue hover:underline font-medium"
            >
              Sign up
            </Link>
          </p>
        </div>
      </Card>
    </div>
  );
};

export default LoginPage;

