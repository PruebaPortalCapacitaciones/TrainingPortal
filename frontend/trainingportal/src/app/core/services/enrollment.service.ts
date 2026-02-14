import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EnrollmentService {
  private apiUrl = `${environment.apiUrl}/enrollments`;

  constructor(private http: HttpClient) {}

  /* Método para obtener los cursos inscritos del usuario actual. */
  getMyCourses(): Observable<any> {
    return this.http.get(`${this.apiUrl}/my-courses`);
  }

  /* Método para inscribir a un curso. */
  enroll(courseId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/enroll?courseId=${courseId}`, null);
  }

  /* Método para eliminar la inscripción de un curso. */
  remove(courseId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/remove?courseId=${courseId}`);
  }

  /* Método para avanzar el progreso de un curso */
  advance(courseId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/advance?courseId=${courseId}`, null);
  }
}
