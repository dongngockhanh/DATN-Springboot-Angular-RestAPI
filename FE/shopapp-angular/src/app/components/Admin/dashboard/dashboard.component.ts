import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { faFaceLaughWink, faTag } from '@fortawesome/free-solid-svg-icons';
import {faSearch} from '@fortawesome/free-solid-svg-icons'
import {faBell} from '@fortawesome/free-solid-svg-icons'
import {faEnvelope} from '@fortawesome/free-solid-svg-icons'
import {faTachometerAlt} from '@fortawesome/free-solid-svg-icons'
import {faBookmark} from '@fortawesome/free-solid-svg-icons'
import {faReceipt} from '@fortawesome/free-solid-svg-icons'
import {faCartShopping} from '@fortawesome/free-solid-svg-icons'
import {faRocket} from '@fortawesome/free-solid-svg-icons'
import {faUser} from '@fortawesome/free-solid-svg-icons'
import {faBars} from '@fortawesome/free-solid-svg-icons'
import {faPaperPlane} from '@fortawesome/free-solid-svg-icons'
import {faGear} from '@fortawesome/free-solid-svg-icons'
import {faRightFromBracket} from '@fortawesome/free-solid-svg-icons'
// import { AuthService } from 'src/app/_service/auth.service';s
import { StorageService } from '../../../services/storage.service';
import { UserService } from '../../../services/user.service';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit{
  faceLaugh = faFaceLaughWink;
  search = faSearch
  bell = faBell;
  evelope =faEnvelope;
  tachometer = faTachometerAlt;
  bookmark = faBookmark;
  receipt = faReceipt;
  cart= faCartShopping;
  rocket = faRocket;
  userIcon = faUser;
  paperPlane = faPaperPlane;
  bars = faBars;
  gear = faGear;
  logoutIcon = faRightFromBracket;
  tag = faTag;
  user = faUser;
  statistics = faTachometerAlt;
  constructor(private storageService:StorageService,
              // private authService:AuthService,
              private userService: UserService,
              private router: Router,
              private route: ActivatedRoute
            ){}
  isRouteActive!: boolean;
  ngOnInit() {
    this.router.events.subscribe(() => {
    this.isRouteActive = this.route.firstChild ? true : false;});
  }
  logout() {
    this.userService.logout();
    this.router.navigate(['/login-page']);
  }
  // logout(){
    // this.authService.logout().subscribe({
    //   next: res =>{
    //     this.storageService.clean();
    //     this.router.navigate(['/']);
    //   }
    // })
  // }
  

  
}
