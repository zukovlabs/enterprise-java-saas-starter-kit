import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from './services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Do not attempt refresh if the refresh call itself failed or the request is for auth endpoints
      if (error.status === 401 && !req.url.includes('/auth/')) {
        const authService = inject(AuthService);
        return authService.refreshTokens().pipe(
          switchMap((newToken: string) => {
            const retried = req.clone({
              setHeaders: { Authorization: `Bearer ${newToken}` }
            });
            return next(retried);
          }),
          catchError(() => throwError(() => error))
        );
      }
      return throwError(() => error);
    })
  );
};
