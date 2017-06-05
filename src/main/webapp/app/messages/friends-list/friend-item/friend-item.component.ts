import {Component, Input, OnInit} from '@angular/core';
import {Friend} from './friend.model';
import {Principal} from '../../../shared/auth/principal.service';

@Component({
  selector: 'jhi-friend-item',
  templateUrl: './friend-item.component.html',
  styleUrls: ['./friend.component.css']
})
export class FriendItemComponent implements OnInit {

  @Input()
  friend: Friend;
  accountId: number;
  boldStyle: boolean;

  constructor(private principal: Principal) {}

  ngOnInit() {
    this.principal.identity().then((account) => {
      this.accountId = account.id;
      this.setBoldMessageText();
    });
  }

  setDelivered() {
    this.friend.messageDelivered = true;
    this.setBoldMessageText();
  }

  setBoldMessageText() {
    if ((!this.friend.messageDelivered) && (this.friend.lastMessageSenderId !== this.accountId)) {
      this.boldStyle = true;
    } else {
      this.boldStyle = false;
    }
  }

}
