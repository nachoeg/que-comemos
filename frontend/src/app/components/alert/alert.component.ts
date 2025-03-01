import { Component, OnInit } from '@angular/core';
import {
  AlertService,
  Alert,
} from '../../services/alert-service/alert.service';
import { CommonModule } from '@angular/common';
import {
  trigger,
  state,
  style,
  transition,
  animate,
} from '@angular/animations';

@Component({
  selector: 'app-alert',
  imports: [CommonModule],
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css'],
  animations: [
    trigger('alertAnimation', [
      state('void', style({ opacity: 0, transform: 'translateX(20px)' })),
      transition(':enter, :leave', [animate('0.3s ease-in-out')]),
    ]),
  ],
})
export class AlertComponent implements OnInit {
  alerts: Alert[] = [];

  constructor(private alertService: AlertService) {}

  ngOnInit() {
    this.alertService.alertState$.subscribe((alerts) => {
      this.alerts = alerts;
    });
  }

  removeAlert(alert: Alert) {
    this.alertService.removeAlert(alert);
  }
}
