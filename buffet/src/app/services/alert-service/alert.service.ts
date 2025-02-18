import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export interface Alert {
  message: string;
  type: 'success' | 'warning' | 'danger' | 'primary' | 'info';
}

@Injectable({
  providedIn: 'root',
})
export class AlertService {
  private alertSubject = new Subject<Alert[]>();
  private alerts: Alert[] = [];
  alertState$ = this.alertSubject.asObservable();

  showAlert(
    message: string,
    type: 'success' | 'warning' | 'danger' | 'primary' | 'info'
  ) {
    const alert: Alert = { message, type };
    this.alerts.push(alert);
    this.alertSubject.next(this.alerts);

    setTimeout(() => {
      this.removeAlert(alert);
    }, 5000);
  }

  removeAlert(alert: Alert) {
    this.alerts = this.alerts.filter((a) => a !== alert);
    this.alertSubject.next(this.alerts);
  }
}
