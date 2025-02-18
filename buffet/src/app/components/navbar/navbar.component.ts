import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { AlertService } from '../../services/alert-service/alert.service';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule, MatIconModule],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit, OnDestroy {
  isLoggedIn: boolean = false;
  usuarioLogeado: any = null;
  private destroy$ = new Subject<void>();

  constructor(
    private loginServicio: LoginServicio,
    private alertService: AlertService
  ) {}

  ngOnInit() {
    this.loginServicio.isUserLoggedIn$
      .pipe(takeUntil(this.destroy$))
      .subscribe((isLoggedIn) => {
        this.isLoggedIn = isLoggedIn;
      });

    this.loginServicio.userLogged$
      .pipe(takeUntil(this.destroy$))
      .subscribe((user) => {
        this.usuarioLogeado = user;
      });
  }

  ngOnDestroy() {
    this.destroy$.next();
    this.destroy$.complete();
  }

  logout() {
    this.loginServicio.logout();
    this.alertService.showAlert('Sesión cerrada correctamente', 'success');
  }
}
