import { Component, ChangeDetectionStrategy, Inject, Optional } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { Customer, CUSTOMER_STATUSES } from '../../../models/customer';

export interface CustomerDialogData {
  customer?: Customer;
}

@Component({
  selector: 'app-customer-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './customer-dialog.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './customer-dialog.scss'
})
export class CustomerDialog {
  form: FormGroup;
  readonly statuses = CUSTOMER_STATUSES;
  readonly isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<CustomerDialog>,
    @Optional() @Inject(MAT_DIALOG_DATA) data: CustomerDialogData | null
  ) {
    const customer = data?.customer;
    this.isEditMode = !!customer;

    this.form = this.fb.group({
      firstName: [customer?.firstName ?? '', [Validators.required, Validators.maxLength(100)]],
      lastName: [customer?.lastName ?? '', [Validators.required, Validators.maxLength(100)]],
      email: [customer?.email ?? '', [Validators.required, Validators.email, Validators.maxLength(255)]],
      company: [customer?.company ?? '', Validators.maxLength(255)],
      status: [customer?.status ?? 'Active', Validators.required]
    });
  }

  save() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  close() {
    this.dialogRef.close();
  }
}
