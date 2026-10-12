export const CUSTOMER_STATUSES = ['New', 'Lead', 'Active', 'Inactive'] as const;

export type CustomerStatus = typeof CUSTOMER_STATUSES[number];

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  company?: string;
  status?: CustomerStatus;
}

export type CustomerPayload = Omit<Customer, 'id'>;
