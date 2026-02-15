import { Routes } from '@angular/router';
import { AdminGuard } from './core/guards/admin.guard';
import { AuthGuard } from './core/guards/auth.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  // Dashboard con rutas hijas
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent),
    canActivate: [AuthGuard],
    children: [
      {
        path: 'manage-courses',
        loadComponent: () =>
          import('./features/courses/manage-courses/manage-courses.component').then(
            (m) => m.ManageCoursesComponent,
          ),
        canActivate: [AdminGuard],
      },
      {
        path: 'courses',
        loadComponent: () =>
          import('./features/courses/course-list/course-list.component').then(
            (m) => m.CourseListComponent,
          ),
      },
      {
        path: 'my-courses',
        loadComponent: () =>
          import('./features/courses/my-courses/my-courses.component').then(
            (m) => m.MyCoursesComponent,
          ),
      },
    ],
  },

  // Ruta comodín para 404
  { path: '**', redirectTo: 'login' },
];
