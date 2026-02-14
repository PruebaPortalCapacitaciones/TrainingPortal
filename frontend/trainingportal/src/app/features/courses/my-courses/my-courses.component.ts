import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import Swal from 'sweetalert2';
import { PALETTE } from '../../../../_palette';
import { environment } from '../../../../environments/environment';
import { EnrollmentService } from '../../../core/services/enrollment.service';
import { TokenService } from '../../../core/services/token.service';

@Component({
  selector: 'app-my-courses',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, MatTooltipModule],
  templateUrl: './my-courses.component.html',
  styleUrls: ['./my-courses.component.scss'],
})
export class MyCoursesComponent implements OnInit {
  courses: any[] = [];
  loading = true;

  constructor(
    private enrollmentService: EnrollmentService,
    private tokenService: TokenService,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.loadMyCourses();
  }

  loadMyCourses(): void {
    this.loading = true;
    this.enrollmentService.getMyCourses().subscribe({
      next: (res: any) => {
        this.courses = res.data || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando cursos:', err);
        this.loading = false;
      },
    });
  }

  downloadCertificate(courseId: number) {
    const url = `${environment.apiUrl}/certificates/download/${courseId}`;

    // El interceptor agregará el token automáticamente
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (blob) => {
        // Crear URL del blob y descargar
        const blobUrl = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = blobUrl;
        link.download = `certificado-${courseId}.pdf`;
        link.click();

        // Limpiar
        window.URL.revokeObjectURL(blobUrl);
      },
      error: (err) => {
        console.error('Error descargando certificado:', err);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo descargar el certificado',
          confirmButtonColor: PALETTE.primaryDark,
        });
      },
    });
  }

  advanceCourse(course: any): void {
    this.enrollmentService.advance(course.courseId).subscribe({
      next: (res: any) => {
        const newProgress = res.data.progress;
        const wasCompleted = newProgress === 100;

        if (wasCompleted) {
          Swal.fire({
            icon: 'success',
            title: '¡Curso completado!',
            text: `Felicidades, has completado "${course.title}"`,
            timer: 3000,
            showConfirmButton: true,
            confirmButtonColor: PALETTE.primaryDark,
          });
        } else {
          Swal.fire({
            icon: 'info',
            title: 'Progreso actualizado',
            text: `Ahora llevas ${newProgress}% del curso`,
            timer: 1500,
            showConfirmButton: false,
          });
        }

        this.loadMyCourses();
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: err.error?.message || 'No se pudo actualizar el progreso',
          confirmButtonColor: PALETTE.primaryDark,
        });
      },
    });
  }

  removeCourse(course: any): void {
    Swal.fire({
      title: '¿Eliminar curso?',
      text: `¿Quieres eliminar "${course.title}" de tus cursos?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: PALETTE.primaryDark,
      cancelButtonColor: PALETTE.error,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
    }).then((result) => {
      if (result.isConfirmed) {
        this.enrollmentService.remove(course.courseId).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: 'Eliminado',
              text: 'Curso eliminado correctamente',
              timer: 1500,
              showConfirmButton: false,
            });
            this.loadMyCourses();
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: err.error?.message || 'No se pudo eliminar el curso',
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
        return PALETTE.mintDark;
      case 'Iniciado':
        return PALETTE.mint;
      case 'En progreso':
        return PALETTE.primary;
      case 'Completado':
        return PALETTE.primaryDark;
      default:
        return PALETTE.textLight;
    }
  }

  isCourseActive(course: any): boolean {
    return course.active === true;
  }
}
