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
import { MenuItemComponent } from './components/menu/menu-item/menu-item.component';
import { EstructuraAddFoodComponent } from './components/menu/estructura-add-food/estructura-add-food.component';
import { EstructuraCreateComponent } from './components/menu/estructura-create/estructura-create.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'iniciar-sesion', component: LoginComponent },
  { path: 'registrarse', component: RegisterComponent },
  { path: 'menus', component: MenuComponent },
  { path: 'menus/crear', component: MenuCreateComponent },
  { path: 'menus/:id', component: MenuItemComponent },
  { path: 'menus/:id/editar', component: MenuEditComponent },
  {
    path: 'menus/:menuId/crear-estructura',
    component: EstructuraCreateComponent,
  },
  {
    path: 'menus/:id/:estructuraId/agregar-comida',
    component: EstructuraAddFoodComponent,
  },
  { path: 'comidas', component: FoodComponent },
  { path: 'comidas/crear', component: FoodCreateComponent },
  { path: 'comidas/:id/editar', component: FoodEditComponent },
];
