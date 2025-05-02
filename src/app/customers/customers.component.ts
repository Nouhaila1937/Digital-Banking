import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {AsyncPipe, CommonModule, NgForOf, NgIf} from "@angular/common";
import {CustomerService} from '../services/customer.service';
import {catchError, Observable, throwError} from 'rxjs';
import {Customer} from '../module/customer.model';
@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    HttpClientModule,
    AsyncPipe,
    CommonModule
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customers: Observable<Customer[]> | undefined;
  errorMessage!: string;
  constructor(private customerService : CustomerService) { }

  ngOnInit():void{
     this.customers=this.customerService.getCustomers();
  }

}
