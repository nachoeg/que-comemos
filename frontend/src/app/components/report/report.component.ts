import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Chart, { ChartType } from 'chart.js/auto'; // Importa Chart.js
import { ReportService } from '../../services/report-service/report-service';

@Component({
  selector: 'app-report',
  imports: [],
  templateUrl: './report.component.html',
  styleUrl: './report.component.css'
})
export class ReportComponent implements OnInit{

  periodoSeleccionado: string = 'diario'; // Valor por defecto
  menuChart: any;
  comidaChart: any;
  montoChart: any;

  constructor(private reportService: ReportService) { }

  ngOnInit(): void {
    this.cargarReportes();
  }

  cargarReportes(): void {
    this.cargarReporteMenus();
    this.cargarReporteComidas();
    this.cargarReporteMontos();
  }

  cargarReporteMenus(): void {
    this.reportService.getReporteMenusPorPeriodo(this.periodoSeleccionado).subscribe(reporteMenu => {
      this.renderChart(reporteMenu, 'menuChart', 'Cantidad de Menús Vendidos', this.menuChart, 'bar');
    });
  }

  cargarReporteComidas(): void {
    this.reportService.getReporteComidasPorPeriodo(this.periodoSeleccionado).subscribe(reporteComida => {
      this.renderChart(reporteComida, 'comidaChart', 'Cantidad de Comidas Vendidas', this.comidaChart, 'bar');
    });
  }

  cargarReporteMontos(): void {
    this.reportService.getReporteMontosPorPeriodo(this.periodoSeleccionado).subscribe(reporteMonto => {
      this.renderChart(reporteMonto, 'montoChart', 'Ingresos', this.montoChart, 'bar');
    });
  }

  renderChart(reporte: { [key: string]: number }, canvasId: string, chartLabel: string, chartInstance: any, chartTypeString: string): void {
    const labels = Object.keys(reporte);
    const data = Object.values(reporte);

    if (chartInstance) {
      chartInstance.destroy();
    }

    // Convierte el string a ChartType
    const chartType: ChartType = chartTypeString as ChartType;

    chartInstance = new Chart(canvasId, {
      type: chartType,
      data: {
        labels: labels,
        datasets: [{
          label: chartLabel,
          data: data,
          backgroundColor: 'rgba(54, 162, 235, 0.2)',
          borderColor: 'rgba(54, 162, 235, 1)',
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });

    if (canvasId === 'menuChart') {
      this.menuChart = chartInstance;
    } else if (canvasId === 'comidaChart') {
      this.comidaChart = chartInstance;
    } else if (canvasId === 'montoChart') {
      this.montoChart = chartInstance;
    }
  }

  cambiarPeriodo(periodo: string): void {
    this.periodoSeleccionado = periodo;
    this.cargarReportes();
  }
}