import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  accessToken!: any ;
  isAuthenticated: boolean = false;
  roles: any;
  username: any;
  password:any;

  constructor(private http: HttpClient ,private router:Router) {
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
    this.isAuthenticated = true;
    this.accessToken = data["access-token"];
    let decodeJwt:any=jwtDecode(this.accessToken);
    this.roles = decodeJwt.scope;
    this.username = decodeJwt.sub;

  }

  logout() {
    this.isAuthenticated = false;
    this.accessToken = undefined;
    this.username = undefined;
    this.roles = undefined;
  }

  loadJwtTokenFromLocalStorage() {
    let token = window.localStorage.getItem("jwt-token");
    if (token) {
      this.loadProfile({"access_token": token})
      this.router.navigateByUrl("/admin/customers");
    }else{
      this.router.navigateByUrl("/login");
    }
  }

}
