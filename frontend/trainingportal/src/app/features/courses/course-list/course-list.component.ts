import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import Swal from 'sweetalert2';
import { PALETTE } from '../../../../_palette';
import { environment } from '../../../../environments/environment';
import { EnrollmentService } from '../../../core/services/enrollment.service';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './course-list.component.html',
  styleUrls: ['./course-list.component.scss'],
})
export class CourseListComponent implements OnInit {
  coursesByModule: any = {};
  myCourses: any[] = [];
  loading = true;
  private apiUrl = `${environment.apiUrl}/courses`;

  constructor(
    private http: HttpClient,
    private enrollmentService: EnrollmentService,
  ) {}

  ngOnInit(): void {
    this.loadCourses();
    this.loadMyCourses();
  }

  getModuleKeys(): string[] {
    return Object.keys(this.coursesByModule);
  }

  loadCourses(): void {
    this.http.get(`${this.apiUrl}/list`).subscribe({
      next: (res: any) => {
        const allCoursesByModule = res.data || {};

        // Filtrar solo cursos activos
        this.coursesByModule = {};

        Object.keys(allCoursesByModule).forEach((module) => {
          const activeCourses = allCoursesByModule[module].filter((c: any) => c.active === true);
          if (activeCourses.length > 0) {
            this.coursesByModule[module] = activeCourses;
          }
        });

        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando cursos:', err);
        this.loading = false;
      },
    });
  }

  loadMyCourses(): void {
    this.enrollmentService.getMyCourses().subscribe({
      next: (res: any) => {
        this.myCourses = res.data || [];
      },
      error: (err) => console.error('Error cargando mis cursos:', err),
    });
  }

  isEnrolled(courseId: number): any {
    return this.myCourses.find((c) => c.courseId === courseId);
  }

  enroll(course: any): void {
    Swal.fire({
      title: '¿Inscribirse?',
      text: `¿Quieres inscribirte en "${course.title}"?`,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: PALETTE.primaryDark,
      cancelButtonColor: PALETTE.error,
      confirmButtonText: 'Sí, inscribirme',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.enrollmentService.enroll(course.id).subscribe({
          next: (res) => {
            Swal.fire({
              icon: 'success',
              title: '¡Inscrito!',
              text: 'Te has inscrito correctamente',
              timer: 1500,
              showConfirmButton: false,
            });
            this.loadMyCourses();
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: err.error?.message || 'No se pudo inscribir',
              confirmButtonColor: PALETTE.primaryDark,
            });
          },
        });
      }
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'Inscrito':
        return PALETTE.secondaryDark;
      case 'Iniciado':
        return PALETTE.secondary;
      case 'En progreso':
        return PALETTE.primary;
      case 'Completado':
        return PALETTE.primaryDark;
      default:
        return PALETTE.textLight;
    }
  }
}
