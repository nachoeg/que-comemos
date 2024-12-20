import { Component, Input } from '@angular/core';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card'; 

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [MatSidenavModule, MatListModule, MatIconModule, RouterModule, CommonModule, MatCardModule ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {

  @Input({required: true}) displaySidebar!: boolean;

  constructor(private router: Router) {
    this.router.events.subscribe(() => {
      this.showInitialMessage = this.router.url === '/';
    });
  }

  showInitialMessage = true;

  navigateToMenu() {
    this.router.navigate(['/menu']);
    this.displaySidebar = false; // Close the sidebar
  }

  navigateToHome() {
    this.router.navigate(['/home']);
    this.displaySidebar = false; // Close the sidebar
  }

}
