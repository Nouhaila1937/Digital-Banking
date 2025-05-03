import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  accessToken!: string ;
  isAuthenticated: boolean = false;
  roles: any;
  username: any;
  password:any;

  constructor(private http: HttpClient) {
  }

  public login(username: string, pass: string) {
    let options = {
      headers: new HttpHeaders().set("Content-Type", "application/x-www-form-urlencoded")
    };
    let params = new HttpParams()
      .set("username", username).set("password", pass);
    return this.http.post("http://localhost:8081/auth/login", params, options);
  }

  loadProfile(data: any) {
    this.accessToken = data['access-token'];
  }
}
