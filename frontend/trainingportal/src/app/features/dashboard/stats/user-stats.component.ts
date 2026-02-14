import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import Chart from 'chart.js/auto';
import { PALETTE } from '../../../../_palette';

@Component({
  selector: 'app-user-stats',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './user-stats.component.html',
  styleUrls: ['./user-stats.component.scss'],
})
export class UserStatsComponent implements OnInit, AfterViewInit {
  @Input() courses: any[] = [];
  @ViewChild('progressChart') progressChart!: ElementRef;

  stats = {
    total: 0,
    inscritos: 0,
    iniciados: 0,
    enProgreso: 0,
    completados: 0,
  };

  chart: any;

  ngOnInit(): void {
    this.calculateStats();
  }

  ngAfterViewInit(): void {
    this.createChart();
  }

  calculateStats(): void {
    this.stats.total = this.courses.length;
    this.stats.inscritos = this.courses.filter((c) => c.status === 'Inscrito').length;
    this.stats.iniciados = this.courses.filter((c) => c.status === 'Iniciado').length;
    this.stats.enProgreso = this.courses.filter((c) => c.status === 'En progreso').length;
    this.stats.completados = this.courses.filter((c) => c.status === 'Completado').length;
  }

  createChart(): void {
    const ctx = this.progressChart.nativeElement.getContext('2d');

    this.chart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['Inscritos', 'Iniciados', 'En progreso', 'Completados'],
        datasets: [
          {
            data: [
              this.stats.inscritos,
              this.stats.iniciados,
              this.stats.enProgreso,
              this.stats.completados,
            ],
            backgroundColor: [
              PALETTE.secondaryLight,
              PALETTE.primaryLight,
              PALETTE.primary,
              PALETTE.primaryDark,
            ],
            borderWidth: 0,
            hoverOffset: 8,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: {
          padding: {
            top: 20,
            bottom: 20,
            left: 100,
          },
        },
        plugins: {
          legend: {
            position: 'right',
            align: 'center',
            labels: {
              color: PALETTE.textDark,
              font: {
                size: 13,
                family: "'Segoe UI', Roboto, sans-serif",
              },
              padding: 15,
              usePointStyle: true,
              pointStyle: 'circle',
              boxWidth: 8,
              boxHeight: 8,
            },
            onClick: (e: any) => {
              e.stopPropagation();
            },
          },
          tooltip: {
            backgroundColor: PALETTE.white,
            titleColor: PALETTE.textDark,
            bodyColor: PALETTE.textSoft,
            borderColor: PALETTE.secondaryLight,
            borderWidth: 1,
            padding: 12,
            cornerRadius: 8,
            boxPadding: 6,
            callbacks: {
              label: (context: any) => {
                const label = context.label || '';
                const value = context.raw || 0;
                const total = context.dataset.data.reduce((a: number, b: number) => a + b, 0);
                const percentage = total > 0 ? Math.round((value / total) * 100) : 0;
                return `${label}: ${value} cursos (${percentage}%)`;
              },
            },
          },
        },
        cutout: '65%',
        animation: {
          animateRotate: true,
          animateScale: true,
          duration: 1000,
          easing: 'easeInOutQuart',
        },
      },
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
