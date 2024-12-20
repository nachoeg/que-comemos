import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from "./components/navbar/navbar.component";
import { SidebarComponent } from "./components/sidebar/sidebar.component";
import { LoginServicio } from './services/login-servicio/login-servicio';


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, SidebarComponent],
  providers: [LoginServicio],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'buffet';

  displaySideBar = true;

  onToggleEvent(){
    this.displaySideBar = !this.displaySideBar;
  }

}
