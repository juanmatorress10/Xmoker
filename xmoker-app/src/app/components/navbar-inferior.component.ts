import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-navbar-inferior',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './navbar-inferior.component.html',
  styleUrls: ['./navbar-inferior.component.css']
})
export class NavbarInferiorComponent {}
