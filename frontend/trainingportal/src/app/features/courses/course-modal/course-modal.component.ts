import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-course-modal',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './course-modal.component.html',
  styleUrls: ['./course-modal.component.scss']
})
export class CourseModalComponent {
  courseData: any = {};
  modules: string[] = [];
  private apiUrl = `${environment.apiUrl}/courses`;

  constructor(
    public dialogRef: MatDialogRef<CourseModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient
  ) {
    // Si es edición, copiar los datos
    if (data.isEdit && data.course) {
      this.courseData = { ...data.course };
    } else {
      // Valores por defecto para nuevo curso
      this.courseData = {
        active: true,
        level: 'Básico'
      };
    }

    // Cargar módulos del backend
    this.loadModules();
  }

  loadModules() {
    this.http.get(`${this.apiUrl}/modules`).subscribe({
      next: (res: any) => {
        this.modules = res.data || [];
      },
      error: (err) => {
        console.error('Error cargando módulos:', err);
        // Fallback por si falla el backend
        this.modules = ['Fullstack', 'APIs e Integraciones', 'Cloud', 'Data Engineer'];
      }
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.data.isEdit) {
      // Para editar, solo enviamos los campos que pueden cambiar
      const updateData = {
        title: this.courseData.title,
        description: this.courseData.description,
        module: this.courseData.module,
        durationHours: this.courseData.durationHours,
        level: this.courseData.level
      };
      this.dialogRef.close({ action: 'update', data: updateData });
    } else {
      this.dialogRef.close({ action: 'create', data: this.courseData });
    }
  }
}