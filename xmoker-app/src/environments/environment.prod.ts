const host = typeof window !== 'undefined' ? window.location.origin : '';

export const environment = {
  production: true,
  apiUrl: `${host}/api`
};
