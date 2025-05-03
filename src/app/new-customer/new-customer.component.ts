import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Customer} from '../module/customer.model';
import {CustomerService} from '../services/customer.service';
import {HttpClientModule} from '@angular/common/http';
import {AsyncPipe, CommonModule} from '@angular/common';

@Component({
  selector: 'app-new-customer',
  imports: [
    HttpClientModule,
    CommonModule,
    ReactiveFormsModule,
  ],
  templateUrl: './new-customer.component.html',
  styleUrl: './new-customer.component.css'
})
export class NewCustomerComponent implements OnInit{
  newCustomerFormGroup!:FormGroup ;
  constructor(private fb: FormBuilder,private customerService:CustomerService) {}

  ngOnInit() {
    this.newCustomerFormGroup=this.fb.group({
      name:this.fb.control(null,[Validators.required,Validators.minLength(3)]),
      email:this.fb.control(null,[Validators.email,Validators.required])
    });
  }
  handleSaveCustomer() {
  let customer:Customer=this.newCustomerFormGroup.value;
  this.customerService.saveCustomers(customer).subscribe({
    next:data=>{
      alert("Customer added successfully");
    },
    error:err=>{
      console.log(err);
    }
  })
  }
}
