import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TokenService } from '../../core/services/token.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 2rem;">
      <h1>Dashboard</h1>
      <p>Bienvenido, {{ user?.name }}</p>
      <button (click)="logout()">Cerrar Sesión</button>
    </div>
  `
})
export class DashboardComponent {
  user: any;

  constructor(
    private tokenService: TokenService,
    private router: Router
  ) {
    this.user = this.tokenService.getUser();
  }

  logout() {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }
}