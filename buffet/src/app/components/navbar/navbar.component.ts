import { Component, EventEmitter, Output } from '@angular/core';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatToolbarModule} from '@angular/material/toolbar';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  @Output() emiteToggleSidenav = new EventEmitter<void>();

  emitToggleEvent(){
    this.emiteToggleSidenav.emit();
  }

  constructor(private router: Router) {}

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

}
