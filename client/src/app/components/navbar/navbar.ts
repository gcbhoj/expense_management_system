import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import * as bootstrap from 'bootstrap';
import { AdminAuthService } from '../../services/admin-auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {
  @Input()
  firstStudentEmail!: string;
  @Input()
  secondStudentEmail!: string;
  @Input()
  firstStudentName!: string;
  @Input()
  secondStudentName!: string;

  constructor(
    public adminAuthService: AdminAuthService,
    private router: Router
  ) {}

  logout() {
    this.adminAuthService.logout();
    this.router.navigate(['/home']);
  }
}
