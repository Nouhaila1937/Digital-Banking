import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {AsyncPipe, CommonModule, NgForOf, NgIf} from "@angular/common";
import {CustomerService} from '../services/customer.service';
import {catchError, Observable, throwError} from 'rxjs';
import {Customer} from '../module/customer.model';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {Router} from '@angular/router';
@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    HttpClientModule,
    AsyncPipe,
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customers: Observable<Customer[]> | undefined;
  errorMessage!: string;
  searchFormGroup : FormGroup | undefined;
  constructor(private customerService : CustomerService, private fb : FormBuilder, private router : Router) { }

  ngOnInit():void{
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control("")
    });
    this.handleSearchCustomers();
  }
  handleSearchCustomers() {
    let kw=this.searchFormGroup?.value.keyword;
    this.customers=this.customerService.searchCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage=err.message;
        return throwError(err);
      })
    );
  }

  handleDeleteCustomer(c: Customer) {
    let confirmation=confirm("Tu es sur de vouloir supprimmer ce customer?")
    if(!confirmation) return;
    this.customerService.deleteCustomer(c.id).subscribe({
      next:(res)=>{
        this.handleSearchCustomers();
      },
      error:err=>{
        console.log(err)
      }
    })
  }
}
