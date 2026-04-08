import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Dashboard } from './pages/dashboard/dashboard';
import { Customers } from './pages/customers/customers';
import { Settings } from './pages/settings/settings';
import { Landing } from './pages/landing/landing';
import { MainLayoutComponent } from './layout/main-layout/main-layout';
import { authGuard } from './auth.guard';
import { Register } from './pages/register/register';

export const routes: Routes = [
  {
    path: '',
    component: Landing
  },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', component: Dashboard },
      { path: 'customers', component: Customers },
      { path: 'settings', component: Settings }
    ]
  }
];