import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';
import Swal from 'sweetalert2';
import { PALETTE } from '../../../../_palette';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  document = '';
  password = '';
  name = '';
  email = '';

  private registerUrl = `${environment.apiUrl}/users/register`;

  constructor(private http: HttpClient, private router: Router) {}

  register() {
    // Validación básica
    if (!this.document || !this.password || !this.name || !this.email) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        text: 'Por favor completa todos los campos',
        timer: 2000,
        showConfirmButton: false,
        background: PALETTE.white,
        color: PALETTE.textDark
      });
      return;
    }

    // Validación simple de email
    if (!this.email.includes('@')) {
      Swal.fire({
        icon: 'warning',
        title: 'Email inválido',
        text: 'Por favor ingresa un email válido',
        confirmButtonColor: PALETTE.primaryDark,
        background: PALETTE.white,
        color: PALETTE.textDark
      });
      return;
    }

    const body = {
      document: this.document,
      password: this.password,
      name: this.name,
      email: this.email
    };

    this.http.post(this.registerUrl, body).subscribe({
      next: (res: any) => {
        console.log('Usuario registrado:', res);
        
        Swal.fire({
          icon: 'success',
          title: '¡Registro exitoso!',
          text: 'Tu cuenta ha sido creada',
          timer: 2000,
          showConfirmButton: false,
          background: PALETTE.white,
          color: PALETTE.textDark
        }).then(() => {
          this.router.navigate(['/login']);
        });
      },
      error: (err) => {
        console.error('Error al registrar:', err);
        
        let errorMessage = 'Error en el registro, verifica los datos';
        
        // Si el backend envía mensaje específico
        if (err.error?.message) {
          errorMessage = err.error.message;
        }
        
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: errorMessage,
          confirmButtonColor: PALETTE.primaryDark,
          background: PALETTE.white,
          color: PALETTE.textDark
        });
      }
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}