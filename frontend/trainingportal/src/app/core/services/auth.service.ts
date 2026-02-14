import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private tokenService: TokenService) { }

  getUserRole(): 'ADMIN' | 'USER' | null {
    const user = this.tokenService.getUser();
    return user?.role || null;
  }

  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }


  isUser(): boolean {
    return this.getUserRole() === 'USER';
  }

  isAuthenticated(): boolean {
    return this.tokenService.isAuthenticated();
  }

  getUser(): any {
    return this.tokenService.getUser();
  }

  getToken(): string | null {
    return this.tokenService.getToken();
  }
}