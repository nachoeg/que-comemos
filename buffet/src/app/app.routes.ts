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
import { UsuariosComponent } from './components/usuarios/usuarios.component';
import { UsuarioDetalleComponent } from './components/usuarios/usuario-detalle/usuario-detalle.component';
import { UsuarioEditarComponent } from './components/usuarios/usuario-editar/usuario-editar.component';
import { AuthGuard } from './services/authGuard/auth-guard';
import { ErrorComponent } from './components/error/error.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'iniciar-sesion', component: LoginComponent },
  { path: 'registrarse', component: RegisterComponent },
  { path: 'menus', component: MenuComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
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
  { path: 'comidas', component: FoodComponent, canActivate: [AuthGuard], data: { roles: ['USER', 'ADMIN'] } },
  { path: 'comidas/crear', component: FoodCreateComponent },
  { path: 'comidas/:id/editar', component: FoodEditComponent },
  { path: 'usuarios', component: UsuariosComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'usuarios/:id', component: UsuarioDetalleComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'usuarios/editar/:id', component: UsuarioEditarComponent, canActivate: [AuthGuard], data: { roles: ['ADMIN'] } },
  { path: 'error', component: ErrorComponent },
  { path: 'forbidden', component: ForbiddenComponent }
];
