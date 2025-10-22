import { AfterViewInit, Component, Input, ViewChild } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink],
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
}
