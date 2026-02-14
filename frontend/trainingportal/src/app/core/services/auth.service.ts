import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private tokenService: TokenService) {}

  /* Método para obtener el rol del usuario actual. */
  getUserRole(): 'ADMIN' | 'USER' | null {
    const user = this.tokenService.getUser();
    return user?.role || null;
  }

  /* Método para verificar si el usuario es administrador. */
  isAdmin(): boolean {
    return this.getUserRole() === 'ADMIN';
  }

  /* Método para verificar si el usuario es un usuario estandar. */
  isUser(): boolean {
    return this.getUserRole() === 'USER';
  }

  /* Método para verificar si el usuario está autenticado. */
  isAuthenticated(): boolean {
    return this.tokenService.isAuthenticated();
  }

  /* Método para obtener la información del usuario actual. */
  getUser(): any {
    return this.tokenService.getUser();
  }

  /* Método para obtener el token de autenticación. */
  getToken(): string | null {
    return this.tokenService.getToken();
  }
}
