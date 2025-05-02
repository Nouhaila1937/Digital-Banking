import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {NgForOf, NgIf} from "@angular/common";
@Component({
  selector: 'app-customers',
  imports: [
    NgIf,
    NgForOf,
    HttpClientModule
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit{
  customers: any;

  constructor(private http:HttpClient) {}
  ngOnInit():void{
      this.http.get("http://localhost:8081/customers").subscribe(data=>{ //une fois la réponse arrive par défaut il va stocker les données dans un object ts qui est data
        this.customers=data;

      },error => {
        console.log(error)
      })
  }

}
