import { Component } from '@angular/core';
import { DropdownModule } from 'primeng/dropdown';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrl: './statistics.component.scss'
})
export class StatisticsComponent {
  today!:Date;
  constructor() {
    this.today = new Date();
  }
  options = [
  {name: 'thống kê theo ngày'},
  {name: 'thống kê theo tuần'},
  {name: 'thống kê theo tháng'},
  {name: 'thống kê theo năm'}
  ]
  selectedOption:any;
  ngOnInit() {
    this.selectedOption = this.options[0];
    console.log(this.selectedOption);
  }
  test() {
    console.log(this.selectedOption);
  }
}
