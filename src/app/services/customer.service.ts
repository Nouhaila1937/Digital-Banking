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
}
