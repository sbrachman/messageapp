import {Component, Input, OnInit} from '@angular/core';
import {Friend} from './friend.model';

@Component({
  selector: 'app-friend-item',
  templateUrl: './friend-item.component.html',
  styles: []
})
export class FriendItemComponent implements OnInit {

  @Input()
  friend: Friend;

  @Input()
  login: string;

  ngOnInit() {
  }



}
