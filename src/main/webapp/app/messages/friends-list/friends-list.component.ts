import { Component, OnInit, OnDestroy } from '@angular/core';
import {Friend} from './friend-item/friend.model';
import {FriendService} from './friend.service';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';
import {AlertService} from 'ng-jhipster';

@Component({
  selector: 'jhi-friends-list',
  templateUrl: './friends-list.component.html',
  styles: [],

})
export class FriendsListComponent implements OnInit, OnDestroy {
   friends: Friend[];
   interval: any;

  constructor(private friendService: FriendService,
              private alertService: AlertService) { }

  ngOnInit() {
    this.loadAll();
    this.enablePullingFriends();
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

  loadAll() {
      this.friendService.query().subscribe(
          (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
          (res: ResponseWrapper) => this.onError(res.json)
      );
  }

  enablePullingFriends() {
    clearInterval(this.interval);
    this.interval = setInterval(() => this.loadAll(), 5000);
  }

  private onSuccess(data, headers) {
    this.friends = data;
  }

  private onError(error) {
      this.alertService.error(error.message, null, null);
  }

}
