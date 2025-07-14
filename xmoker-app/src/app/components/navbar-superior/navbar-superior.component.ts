import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserAvatarComponent } from '../user-avatar/user-avatar.component';

@Component({
  selector: 'app-navbar-superior',
  standalone: true,
  imports: [CommonModule, UserAvatarComponent],
  templateUrl: './navbar-superior.component.html',
  styleUrls: ['./navbar-superior.component.css'],
})
export class NavbarSuperiorComponent {}
