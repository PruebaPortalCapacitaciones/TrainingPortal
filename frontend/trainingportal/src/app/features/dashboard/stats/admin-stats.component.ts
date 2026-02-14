import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PALETTE } from '../../../../_palette';
import { ReportingService } from '../../../core/services/reporting.service';

@Component({
  selector: 'app-admin-stats',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './admin-stats.component.html',
  styleUrls: ['./admin-stats.component.scss'],
})
export class AdminStatsComponent implements OnInit {
  stats: any = {};
  loading = true;

  // Mapeo de keys a nombres legibles
  statusLabels: { [key: string]: string } = {
    inscritos: 'Inscritos',
    iniciados: 'Iniciados',
    enProgreso: 'En progreso',
    completados: 'Completados',
  };

  constructor(private reportingService: ReportingService) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.reportingService.getAdminStats().subscribe({
      next: (res) => {
        this.stats = res.data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando estadísticas:', err);
        this.loading = false;
      },
    });
  }

  getStatusColor(status: string): string {
    switch (status) {
      case 'inscritos':
        return PALETTE.secondaryDark;
      case 'iniciados':
        return PALETTE.secondary;
      case 'enProgreso':
        return PALETTE.primary;
      case 'completados':
        return PALETTE.primaryDark;
      default:
        return PALETTE.textLight;
    }
  }

  // Para mostrar nombres legibles
  getStatusLabel(key: string): string {
    return this.statusLabels[key] || key;
  }

  // Getter para convertir el objeto en array tipado
  get statusEntries() {
    const entries = this.stats.enrollmentsByStatus || {};
    return Object.keys(entries).map((key) => ({
      key: key,
      value: entries[key],
    }));
  }
}
