import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { TokenService } from '../../core/services/token.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, MatIconModule, MatMenuModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  user: any;
  isAdmin = false;

  constructor(
    private tokenService: TokenService,
    private authService: AuthService,
    private router: Router
  ) {
    this.user = this.tokenService.getUser();
    this.isAdmin = this.authService.isAdmin();
  }

  logout() {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }

  showWelcome(): boolean {
    return this.router.url === '/dashboard';
  }

  // NUEVO: Volver al dashboard principal
  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }
}