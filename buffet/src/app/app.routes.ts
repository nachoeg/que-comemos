import { Routes } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { HomeComponent } from './components/home/home.component';


export const routes: Routes = [
    { path: 'menu', component: MenuComponent },
    { path: 'home', component: HomeComponent }
];
