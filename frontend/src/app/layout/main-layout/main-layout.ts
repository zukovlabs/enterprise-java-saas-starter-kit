import {Component, inject, ChangeDetectionStrategy} from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet, RouterLink, Router } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { AuthService } from '../../services/auth.service';
import { ConfirmDialog } from '../../components/confirm-dialog/confirm-dialog';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    RouterLink,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatMenuModule,
    MatTooltipModule
  ],
  templateUrl: './main-layout.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './main-layout.scss'
})
export class MainLayoutComponent {
  private authService = inject(AuthService);
  private router = inject(Router);
  private dialog = inject(MatDialog);

  logout(): void {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '400px',
      data: {
        title: 'Logout',
        message: 'Are you sure you want to log out?',
        confirmText: 'Logout',
        confirmColor: 'primary'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.authService.logout();
        this.router.navigate(['/login']);
      }
    });
  }
}
