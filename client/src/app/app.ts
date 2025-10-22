import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './components/navbar/navbar';
import { ExpenseManager } from './components/expense-manager/expense-manager';
import { Footer } from './components/footer/footer';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Navbar, Footer, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('client');

  firstStudentName = 'Bhoj G.C';
  firstStudentEmail = 'ghartich@sheridancollege.ca';
  secondStudentEmail = 'aijugeor@sheridancollege.ca';
  secondStudentName = 'Alen Aiju George';
}
