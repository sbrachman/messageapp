import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import {JhipmessageappSharedModule} from '../shared/shared.module';
import {JhipmessageappAdminModule} from '../admin/admin.module';
import {FriendsListComponent} from './friends-list/friends-list.component';
import {MessagesComponent} from './messages.component';
import {MdChipsModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FriendItemComponent} from './friends-list/friend-item/friend-item.component';
import {MessagesService} from './messages.service';
import {FriendService} from './friends-list/friend.service';
import {messagesRoute} from './messages.route';
import {ConversationComponent} from './conversation/conversation.component';





@NgModule({
  imports: [
    JhipmessageappSharedModule,
    JhipmessageappAdminModule,
    RouterModule.forRoot(messagesRoute, { useHash: true }),
    BrowserAnimationsModule,
    MdChipsModule
  ],
  declarations: [
    MessagesComponent,
    ConversationComponent,
    FriendsListComponent,
    FriendItemComponent
  ],
  providers: [
    MessagesService,
    FriendService
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class MessagesModule {}
