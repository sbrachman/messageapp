import { Component, OnInit } from '@angular/core';
import {Friend} from './friend-item/friend.model';
import {FriendService} from './friend.service';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';
import {AlertService} from 'ng-jhipster';

@Component({
  selector: 'app-friends-list',
  templateUrl: './friends-list.component.html',
  styles: [],

})
export class FriendsListComponent implements OnInit {
   friends: Friend[];

  constructor(private friendService: FriendService,
              private alertService: AlertService) { }

  ngOnInit() {
    this.loadAll();
  }



  loadAll() {
      this.friendService.query().subscribe(
          (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
          (res: ResponseWrapper) => this.onError(res.json)
      );
  }



  private onSuccess(data, headers) {
      // this.links = this.parseLinks.parse(headers.get('link'));
      // this.totalItems = headers.get('X-Total-Count');
      this.friends = [];
      for (let i = 0; i < data.length; i++) {
          this.friends.push(data[i]);
      }
  }

  private onError(error) {
      this.alertService.error(error.message, null, null);
  }

}
