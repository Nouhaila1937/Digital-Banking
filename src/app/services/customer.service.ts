import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Customer} from '../module/customer.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  constructor(private http:HttpClient) {}
  // URL de base écrite en dur
  private baseUrl = "http://localhost:8081";

  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.baseUrl+"/customers")
  }

  public searchCustomers(keyword : string):Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(this.baseUrl+"/customers/search?keyword="+keyword)
  }

  public saveCustomers(customer : Customer):Observable <Customer>{
    return this.http.post<Customer>(this.baseUrl+"/addcustomer",customer)
  }
}

