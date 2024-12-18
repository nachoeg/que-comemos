import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email = '';  // Initialize with an empty string
  password = '';  // Initialize with an empty string

  constructor() {}

  login() {
    console.log(this.email);
    console.log(this.password);
  }

}
