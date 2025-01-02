import { Routes } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { HomeComponent } from './components/home/home.component';
import { MenuCreateComponent } from './components/menu/menu-create/menu-create.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { MenuEditComponent } from './components/menu/menu-edit/menu-edit.component';
import { FoodComponent } from './components/food/food.component';
import { FoodCreateComponent } from './components/food/food-create/food-create.component';
import { FoodEditComponent } from './components/food/food-edit/food-edit.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'iniciar-sesion', component: LoginComponent },
  { path: 'registrarse', component: RegisterComponent },
  { path: 'menus', component: MenuComponent },
  { path: 'menu/crear', component: MenuCreateComponent },
  { path: 'menu/:id/editar', component: MenuEditComponent },
  { path: 'comidas', component: FoodComponent },
  { path: 'comidas/crear', component: FoodCreateComponent },
  { path: 'comidas/:id/editar', component: FoodEditComponent },
];
