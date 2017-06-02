import {Component, Input, OnInit} from '@angular/core';
import {Friend} from './friend.model';

@Component({
  selector: 'jhi-friend-item',
  templateUrl: './friend-item.component.html',
  styleUrls: ['./friend.component.css']
})
export class FriendItemComponent implements OnInit {

  @Input()
  friend: Friend;

  @Input()
  login: string;

  ngOnInit() {
  }

}
