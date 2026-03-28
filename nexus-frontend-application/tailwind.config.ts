import type { Config } from 'tailwindcss';

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        // NEXUS Design System Colors
        navy: {
          DEFAULT: '#0F2A4A',
          light: '#1A3C5E',
          dark: '#0A1628',
        },
        blue: {
          DEFAULT: '#1B5FA8',
          light: '#2066B4',
          dark: '#154F95',
          pale: '#EBF4FF',
        },
        amber: {
          DEFAULT: '#E8A020',
          light: '#FFF8EC',
        },
        teal: {
          DEFAULT: '#0D7377',
          light: '#E6F7F7',
        },
        success: {
          DEFAULT: '#1A6B3C',
          light: '#EDFAF1',
        },
        danger: {
          DEFAULT: '#C0392B',
          light: '#FFF0F0',
        },
        surface: {
          DEFAULT: '#FFFFFF',
          dark: '#1E2533',
        },
        background: {
          DEFAULT: '#F4F6F9',
          dark: '#151B27',
        },
        border: {
          DEFAULT: '#C8D0DA',
          dark: '#2D3748',
        },
        text: {
          primary: '#1A1F2E',
          secondary: '#4A5568',
          muted: '#718096',
          'primary-dark': '#E8EAF0',
          'secondary-dark': '#9FAAB8',
          'muted-dark': '#6B7A92',
        },
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
      fontSize: {
        'heading-1': ['32px', { lineHeight: '1.2', fontWeight: '700' }],
        'heading-2': ['24px', { lineHeight: '1.3', fontWeight: '600' }],
        'heading-3': ['18px', { lineHeight: '1.4', fontWeight: '600' }],
        'body-large': ['15px', { lineHeight: '1.6', fontWeight: '400' }],
        'body': ['14px', { lineHeight: '1.5', fontWeight: '400' }],
        'caption': ['12px', { lineHeight: '1.4', fontWeight: '400' }],
        'code': ['13px', { lineHeight: '1.5', fontWeight: '400' }],
      },
      spacing: {
        'xs': '4px',
        'sm': '8px',
        'md': '16px',
        'lg': '24px',
        'xl': '32px',
        '2xl': '48px',
        '3xl': '64px',
      },
      borderRadius: {
        'small': '6px',
        'medium': '10px',
        'large': '14px',
      },
      boxShadow: {
        'card': '0 1px 4px rgba(0,0,0,0.07), 0 4px 12px rgba(0,0,0,0.05)',
        'modal': '0 8px 32px rgba(0,0,0,0.18), 0 2px 8px rgba(0,0,0,0.10)',
        'dropdown': '0 4px 16px rgba(0,0,0,0.12)',
      },
    },
  },
  plugins: [],
} satisfies Config;

