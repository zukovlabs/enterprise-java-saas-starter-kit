import {Component, OnInit, ViewChild, AfterViewInit, inject, ChangeDetectionStrategy} from '@angular/core';
import { Customer } from '../../models/customer';
import { CustomerService } from '../../services/customer.service';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CustomerDialog } from './customer-dialog/customer-dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ConfirmDialog } from '../../components/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatIconModule,
    MatDialogModule,
    MatSnackBarModule],
  templateUrl: './customers.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './customers.scss'
})
export class Customers implements OnInit, AfterViewInit {

  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'email', 'company', 'status', 'actions'];
  dataSource = new MatTableDataSource<Customer>([]);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  private customerService = inject(CustomerService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

  ngOnInit(): void {
    this.loadCustomers();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  loadCustomers() {
    this.customerService.getCustomers().subscribe({
      next: (data) => {
        this.dataSource.data = data;
      },
      error: (err) => {
        console.error(err);
        this.showError('Failed to load customers');
      }
    });
  }

  openAddDialog() {
    const dialogRef = this.dialog.open(CustomerDialog);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.customerService.createCustomer(result).subscribe({
          next: () => {
            this.loadCustomers();
            this.showSuccess('Customer created successfully');
          },
          error: (err) => {
            console.error(err);
            this.showError('Failed to create customer');
          }
        });
      }
    });
  }

  deleteCustomer(id: number) {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      width: '400px',
      data: {
        title: 'Delete Customer',
        message: 'Are you sure you want to delete this customer? This action cannot be undone.',
        confirmText: 'Delete',
        confirmColor: 'warn'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.customerService.deleteCustomer(id).subscribe({
          next: () => {
            this.loadCustomers();
            this.showSuccess('Customer deleted');
          },
          error: (err) => {
            console.error(err);
            this.showError('Failed to delete customer');
          }
        });
      }
    });
  }

  private showSuccess(message: string) {
    this.snackBar.open(message, 'OK', {
      duration: 3000,
      horizontalPosition: 'right',
      verticalPosition: 'bottom',
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string) {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['error-snackbar']
    });
  }
}
