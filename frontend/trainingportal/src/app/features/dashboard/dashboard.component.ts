import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { EnrollmentService } from '../../core/services/enrollment.service';
import { TokenService } from '../../core/services/token.service';
import { AdminStatsComponent } from './stats/admin-stats.component';
import { UserStatsComponent } from './stats/user-stats.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatMenuModule,
    UserStatsComponent,
    AdminStatsComponent,
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
  user: any;
  isAdmin = false;
  userCourses: any[] = [];
  userCoursesLoaded = false;

  constructor(
    private tokenService: TokenService,
    private authService: AuthService,
    private enrollmentService: EnrollmentService,
    private router: Router,
  ) {
    this.user = this.tokenService.getUser();
    this.isAdmin = this.authService.isAdmin();
    this.loadUserCourses();
  }

  loadUserCourses() {
    this.enrollmentService.getMyCourses().subscribe({
      next: (res) => {
        this.userCourses = res.data || [];
        this.userCoursesLoaded = true;
      },
      error: (err) => {
        console.error('Error cargando cursos del usuario:', err);
        this.userCoursesLoaded = true;
      },
    });
  }

  logout() {
    this.tokenService.removeToken();
    this.router.navigate(['/login']);
  }

  showWelcome(): boolean {
    return this.router.url === '/dashboard';
  }

  goToDashboard() {
    this.router.navigate(['/dashboard']);
  }
}
