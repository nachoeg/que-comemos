import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { LoginServicio } from '../../services/login-servicio/login-servicio';
import { AlertService } from '../../services/alert-service/alert.service';
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../../services/usuarios-service/usuarios-service';
import { Subject, takeUntil } from 'rxjs';
import { CartService } from '../../services/cart-service/cart-service';
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
  cantidadItems: number = 0;

  constructor(private router: Router,
    public loginServicio: LoginServicio,
  private cartService: CartService,
  private alertService: AlertService) { }
  
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

      this.cartService.cantidadItems$.pipe(takeUntil(this.destroy$)).subscribe(cantidad => {
        this.cantidadItems = cantidad;
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
