import { Component } from '@angular/core';
import { DarkModeService } from 'angular-dark-mode';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  // dark mode
  darkMode$ = this.darkModeService.darkMode$;

  constructor(private darkModeService: DarkModeService) {}

  onToggle(): void {
    this.darkModeService.toggle();
  }
}
