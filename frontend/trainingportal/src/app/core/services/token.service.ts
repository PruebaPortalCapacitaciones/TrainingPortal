import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  private readonly TOKEN_KEY = 'token';
  private readonly USER_KEY = 'user';

  /* Método para almacenar el token de autenticación. */
  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /* Método para obtener el token de autenticación. */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /* Método para eliminar el token de autenticación. */
  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
  }

  /* Método para almacenar la información del usuario. */
  setUser(user: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
  }

  /* Método para obtener la información del usuario. */
  getUser(): any {
    const user = localStorage.getItem(this.USER_KEY);
    return user ? JSON.parse(user) : null;
  }

  /* Método para verificar si el usuario está autenticado. */
  isAuthenticated(): boolean {
    return !!this.getToken();
  }
}
