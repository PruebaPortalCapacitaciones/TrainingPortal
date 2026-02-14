import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import Swal from 'sweetalert2';
import { PALETTE } from '../../../../_palette';
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
    MatButtonModule,
  ],
  templateUrl: './course-modal.component.html',
  styleUrls: ['./course-modal.component.scss'],
})
export class CourseModalComponent implements OnInit {
  courseData: any = {};
  modules: string[] = [];
  levels: string[] = [];
  loadingModules = true;
  loadingLevels = true;
  private apiUrl = `${environment.apiUrl}/courses`;

  constructor(
    public dialogRef: MatDialogRef<CourseModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private http: HttpClient,
  ) {
    if (data.isEdit && data.course) {
      this.courseData = { ...data.course };
    } else {
      this.courseData = {
        active: true,
      };
    }
  }

  ngOnInit(): void {
    this.loadModules();
    this.loadLevels();
  }

  loadModules() {
    this.loadingModules = true;
    this.http.get(`${this.apiUrl}/modules`).subscribe({
      next: (res: any) => {
        this.modules = res.data || [];
        this.loadingModules = false;
      },
      error: (err) => {
        console.error('Error cargando módulos:', err);
        this.loadingModules = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los módulos',
          confirmButtonColor: PALETTE.primaryDark,
        });
      },
    });
  }

  loadLevels() {
    this.loadingLevels = true;
    this.http.get(`${this.apiUrl}/levels`).subscribe({
      next: (res: any) => {
        this.levels = res.data || [];
        this.loadingLevels = false;
      },
      error: (err) => {
        console.error('Error cargando niveles:', err);
        this.loadingLevels = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los niveles',
          confirmButtonColor: PALETTE.primaryDark,
        });
      },
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.data.isEdit) {
      const updateData = {
        title: this.courseData.title,
        description: this.courseData.description,
        module: this.courseData.module,
        durationHours: this.courseData.durationHours,
        level: this.courseData.level,
      };
      this.dialogRef.close({ action: 'update', data: updateData });
    } else {
      this.dialogRef.close({ action: 'create', data: this.courseData });
    }
  }
}
