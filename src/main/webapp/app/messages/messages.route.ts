
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';
import {MessagesComponent} from './messages.component';
import {UserRouteAccessService} from '../shared/auth/user-route-access-service';
import {ConversationComponent} from './conversation/conversation.component';

export const messagesRoute: Routes = [
  {
    path: 'messages',
    component: MessagesComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'Messages'
    },
    canActivate: [UserRouteAccessService],
    children: [
      {path: ':login', component: ConversationComponent}
    ]
  },
];
