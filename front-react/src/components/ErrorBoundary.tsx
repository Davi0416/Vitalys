import { Component, type ReactNode, type ErrorInfo } from 'react';

interface Props { children: ReactNode; }
interface State { hasError: boolean; message: string; }

export default class ErrorBoundary extends Component<Props, State> {
  state: State = { hasError: false, message: '' };

  static getDerivedStateFromError(error: Error): State {
    return { hasError: true, message: error.message };
  }

  componentDidCatch(error: Error, info: ErrorInfo) {
    console.error('[ErrorBoundary]', error, info.componentStack);
  }

  render() {
    if (this.state.hasError) {
      return (
        <div style={{
          display: 'flex', flexDirection: 'column', alignItems: 'center',
          justifyContent: 'center', minHeight: '60vh', gap: '12px',
          color: 'var(--text-secondary)', textAlign: 'center', padding: '24px',
        }}>
          <svg width="40" height="40" viewBox="0 0 24 24" fill="none"
            stroke="var(--danger)" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="12" r="10"/>
            <line x1="12" y1="8" x2="12" y2="12"/>
            <line x1="12" y1="16" x2="12.01" y2="16"/>
          </svg>
          <p style={{ fontWeight: 600, fontSize: '16px', color: 'var(--text-primary)' }}>
            Algo deu errado
          </p>
          <p style={{ fontSize: '13px', maxWidth: '380px' }}>{this.state.message}</p>
          <button
            onClick={() => window.location.reload()}
            style={{
              marginTop: '8px', padding: '8px 20px', borderRadius: '8px',
              background: 'var(--brand-grad)', color: '#fff', border: 'none',
              cursor: 'pointer', fontFamily: 'inherit', fontWeight: 600, fontSize: '13px',
            }}
          >
            Recarregar página
          </button>
        </div>
      );
    }
    return this.props.children;
  }
}
