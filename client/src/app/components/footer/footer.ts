import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-footer',
  imports: [],
  templateUrl: './footer.html',
  styleUrl: './footer.scss',
})
export class Footer {
  currentYear = new Date().getFullYear();
  @Input()
  firstStudentEmail!: string;
  @Input()
  secondStudentEmail!: string;
  @Input()
  firstStudentName!: string;
  @Input()
  secondStudentName!: string;
}
