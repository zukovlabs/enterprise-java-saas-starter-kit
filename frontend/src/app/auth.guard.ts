import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (!token) {
    router.navigate(['/login']);
    return false;
  }

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    if (payload.exp * 1000 < Date.now()) {
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
      router.navigate(['/login']);
      return false;
    }
  } catch {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    router.navigate(['/login']);
    return false;
  }

  return true;
};
