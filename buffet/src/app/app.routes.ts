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
import { SugerenciasCrearComponent } from './components/sugerencias/sugerencias-crear/sugerencias-crear.component';
import { SugerenciasDetalleComponent } from './components/sugerencias/sugerencias-detalle/sugerencias-detalle.component';
import { SugerenciasComponent } from './components/sugerencias/sugerencias.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { PerfilEditarComponent } from './components/perfil/perfil-editar/perfil-editar.component';
import { AuthGuard } from './services/authGuard/auth-guard';
import { ErrorComponent } from './components/error/error.component';
import { ForbiddenComponent } from './components/forbidden/forbidden.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { ShoppingCartComponent } from './components/purchase-order/shopping-cart/shopping-cart.component';
import { PurchaseOrderComponent } from './components/purchase-order/purchase-order.component';
import { MenuOrdersComponent } from './components/menu/menu-orders/menu-orders.component';
import { ReportComponent } from './components/report/report.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'iniciar-sesion', component: LoginComponent },
  { path: 'registrarse', component: RegisterComponent },
  {
    path: 'menus',
    component: MenuComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'USER', 'MANAGER'] },
  },
  {
    path: 'menus/crear',
    component: MenuCreateComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'menus/:id',
    component: MenuItemComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'USER', 'MANAGER'] },
  },
  {
    path: 'menus/:id/editar',
    component: MenuEditComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'menus/:menuId/crear-estructura',
    component: EstructuraCreateComponent,
  },
  {
    path: 'menus/:id/:estructuraId/agregar-comida',
    component: EstructuraAddFoodComponent,
  },
  {
    path: 'comidas',
    component: FoodComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER', 'ADMIN', 'MANAGER'] },
  },
  { path: 'comidas/crear', component: FoodCreateComponent },
  { path: 'comidas/:id/editar', component: FoodEditComponent },
  {
    path: 'usuarios',
    component: UsuariosComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'usuarios/:id',
    component: UsuarioDetalleComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'usuarios/editar/:id',
    component: UsuarioEditarComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'sugerencias',
    component: SugerenciasComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'MANAGER'] },
  },
  {
    path: 'sugerencias/enviar',
    component: SugerenciasCrearComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
  },
  {
    path: 'sugerencias/:id',
    component: SugerenciasDetalleComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'MANAGER'] },
  },
  {
    path: 'perfil',
    component: PerfilComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'MANAGER', 'USER'] },
  },
  {
    path: 'perfil/editar/:id',
    component: PerfilEditarComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN', 'MANAGER', 'USER'] },
  },
  {
    path: 'usuarios',
    component: UsuariosComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'usuarios/:id',
    component: UsuarioDetalleComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'usuarios/editar/:id',
    component: UsuarioEditarComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  {
    path: 'carrito',
    component: ShoppingCartComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
  },
  {
    path: 'pedidos',
    component: PurchaseOrderComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER'] },
  },
  {
    path: 'pedidos/manager',
    component: MenuOrdersComponent,
    canActivate: [AuthGuard],
    data: { roles: ['MANAGER'] },
  },
  {
    path: 'reportes',
    component: ReportComponent,
    canActivate: [AuthGuard],
    data: { roles: ['ADMIN'] },
  },
  { path: 'error', component: ErrorComponent },
  { path: 'forbidden', component: ForbiddenComponent },
  { path: '**', component: PageNotFoundComponent },
];
