import { Component,Renderer2,Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  // standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent{
  title = 'ShopApp-Angular';

  // code under this line is for fixing page refresh issue of dark mode
  constructor(private renderer: Renderer2, @Inject(DOCUMENT) private document: Document) {}
  ngAfterViewInit() {
    setTimeout(() => {
      this.renderer.setStyle(this.document.body, 'transition', 'all 0.3s linear');
    }, 0);
    setTimeout(() => {
      this.renderer.addClass(this.document.body, 'transition');
    }, 0);
  }
}
