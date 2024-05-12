import { Component,Renderer2,Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { NavigationCancel, NavigationEnd, NavigationError, NavigationStart, Router,Event } from '@angular/router';

@Component({
  // standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent{
  title = 'ShopApp-Angular';
  isDashboardRoute :boolean = false;
  loadingData: boolean = false;

  // code under this line is for fixing page refresh issue of dark mode
  constructor(private router: Router,
              private renderer: Renderer2, 
              @Inject(DOCUMENT) private document: Document) {
                this.router.events.subscribe((event: Event) => {
                  if (event instanceof NavigationStart) {
                    this.loadingData = false;
                  }
                  if (event instanceof NavigationEnd) {
                    this.isDashboardRoute = event.urlAfterRedirects.startsWith('/admin');
                    // setTimeout(() => {
                      this.loadingData = true;
                    // }, 300);
                  }
                });
  }
  ngAfterViewInit() {
    setTimeout(() => {
      this.renderer.setStyle(this.document.body, 'transition', 'all 0.3s linear');
    }, 0);
    setTimeout(() => {
      this.renderer.addClass(this.document.body, 'transition');
    }, 0);
  }
}
