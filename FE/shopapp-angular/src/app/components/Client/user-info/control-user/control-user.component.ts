import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-control-user',
  templateUrl: './control-user.component.html',
  styleUrl: './control-user.component.scss'
})
export class ControlUserComponent {
  constructor(private router:Router) {}

}
