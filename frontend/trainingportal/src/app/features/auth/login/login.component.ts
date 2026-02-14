import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TokenService } from '../../../core/services/token.service';
import { environment } from '../../../../environments/environment';
import Swal from 'sweetalert2';
import { PALETTE } from '../../../../_palette';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  document = '';
  password = '';

  private loginUrl = `${environment.apiUrl}/users/login`;

  constructor(
    private http: HttpClient, 
    private router: Router,
    private tokenService: TokenService
  ) {}

  login() {
    // Validación básica
    if (!this.document || !this.password) {
      Swal.fire({
        icon: 'warning',
        title: 'Campos vacíos',
        text: 'Por favor ingresa documento y contraseña',
        timer: 2000,
        showConfirmButton: false,
        background: PALETTE.white,
        color: PALETTE.textDark
      });
      return;
    }

    const params = new HttpParams()
      .set('document', this.document)
      .set('password', this.password);

    this.http.post(this.loginUrl, null, { params }).subscribe({
      next: (res: any) => {
        console.log('Login exitoso:', res);
        
        if (res.data?.token) {
          this.tokenService.setToken(res.data.token);
          this.tokenService.setUser(res.data);
          
          Swal.fire({
            icon: 'success',
            title: '¡Bienvenido!',
            text: `Hola ${res.data.name || ''}`,
            timer: 1500,
            showConfirmButton: false,
            background: PALETTE.white,
            color: PALETTE.textDark
          }).then(() => {
            this.router.navigate(['/dashboard']);
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'No se pudo iniciar sesión',
            confirmButtonColor: PALETTE.primaryDark,
            background: PALETTE.white,
            color: PALETTE.textDark
          });
        }
      },
      error: (err) => {
        console.error('Error login:', err);
        Swal.fire({
          icon: 'error',
          title: 'Credenciales incorrectas',
          text: 'Usuario o contraseña inválidos',
          confirmButtonColor: PALETTE.primaryDark,
          background: PALETTE.white,
          color: PALETTE.textDark
        });
      }
    });
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }
}