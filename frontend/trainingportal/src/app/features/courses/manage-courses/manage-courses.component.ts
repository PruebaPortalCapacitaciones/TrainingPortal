import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';  // <-- IMPORTAR
import { environment } from '../../../../environments/environment';
import Swal from 'sweetalert2';
import { PALETTE } from '../../../../_palette';
import { CourseModalComponent } from '../course-modal/course-modal.component';  // <-- IMPORTAR

@Component({
  selector: 'app-manage-courses',
  standalone: true,
  imports: [
    CommonModule, 
    MatIconModule, 
    MatTooltipModule,
    MatDialogModule  // <-- AGREGAR
  ],
  templateUrl: './manage-courses.component.html',
  styleUrls: ['./manage-courses.component.scss']
})
export class ManageCoursesComponent implements OnInit {
  courses: any[] = [];
  loading = false;
  private apiUrl = `${environment.apiUrl}/courses`;

  constructor(
    private http: HttpClient,
    private dialog: MatDialog  // <-- INYECTAR
  ) {}

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses(): void {
    this.loading = true;
    this.http.get(`${this.apiUrl}/list`).subscribe({
      next: (res: any) => {
        const grouped = res.data || {};
        this.courses = Object.values(grouped).flat();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando cursos:', err);
        this.loading = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los cursos',
          confirmButtonColor: PALETTE.primaryDark
        });
      }
    });
  }

  toggleStatus(course: any): void {
    const action = course.active ? 'desactivar' : 'activar';
    
    Swal.fire({
      title: `¿${action} curso?`,
      text: `El curso "${course.title}" será ${action}do`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: PALETTE.primaryDark,
      cancelButtonColor: PALETTE.error,
      confirmButtonText: 'Sí, continuar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.http.delete(`${this.apiUrl}/${course.id}`).subscribe({
          next: (res: any) => {
            Swal.fire({
              icon: 'success',
              title: '¡Actualizado!',
              text: res.message || 'Estado cambiado correctamente',
              timer: 1500,
              showConfirmButton: false
            });
            this.loadCourses();
          },
          error: (err) => {
            console.error('Error cambiando estado:', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo cambiar el estado',
              confirmButtonColor: PALETTE.primaryDark
            });
          }
        });
      }
    });
  }

  editCourse(course: any): void {
    const dialogRef = this.dialog.open(CourseModalComponent, {
      width: '600px',
      data: {
        title: 'Editar Curso',
        isEdit: true,
        course: course
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result?.action === 'update') {
        this.http.put(`${this.apiUrl}/${course.id}`, result.data).subscribe({
          next: (res: any) => {
            Swal.fire({
              icon: 'success',
              title: '¡Actualizado!',
              text: res.message || 'Curso actualizado correctamente',
              timer: 1500,
              showConfirmButton: false
            });
            this.loadCourses();
          },
          error: (err) => {
            console.error('Error actualizando curso:', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo actualizar el curso',
              confirmButtonColor: PALETTE.primaryDark
            });
          }
        });
      }
    });
  }

  createCourse(): void {
    const dialogRef = this.dialog.open(CourseModalComponent, {
      width: '600px',
      data: {
        title: 'Nuevo Curso',
        isEdit: false
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result?.action === 'create') {
        this.http.post(this.apiUrl, result.data).subscribe({
          next: (res: any) => {
            Swal.fire({
              icon: 'success',
              title: '¡Creado!',
              text: res.message || 'Curso creado correctamente',
              timer: 1500,
              showConfirmButton: false
            });
            this.loadCourses();
          },
          error: (err) => {
            console.error('Error creando curso:', err);
            Swal.fire({
              icon: 'error',
              title: 'Error',
              text: 'No se pudo crear el curso',
              confirmButtonColor: PALETTE.primaryDark
            });
          }
        });
      }
    });
  }
}