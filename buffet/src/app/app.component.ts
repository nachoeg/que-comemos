import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './components/navbar/navbar.component';
import { LoginServicio } from './services/login-servicio/login-servicio';
import { AuthGuard } from './services/authGuard/auth-guard';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent],
  providers: [LoginServicio, AuthGuard],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = '¿Qué comemos?';
}
