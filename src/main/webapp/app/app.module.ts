import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { JhipmessageappSharedModule, UserRouteAccessService } from './shared';
import { JhipmessageappHomeModule } from './home/home.module';
import { JhipmessageappAdminModule } from './admin/admin.module';
import { JhipmessageappAccountModule } from './account/account.module';
import { JhipmessageappEntityModule } from './entities/entity.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent,
} from './layouts';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MessagesModule} from './messages/messages.module';
import {MdChipsModule} from '@angular/material';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        JhipmessageappSharedModule,
        JhipmessageappHomeModule,
        JhipmessageappAdminModule,
        JhipmessageappAccountModule,
        JhipmessageappEntityModule,
        MessagesModule,
        BrowserAnimationsModule,
        MdChipsModule,
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        // MessagesComponent,
        // DialogComponent,
        // FriendsListComponent,
        // FriendItemComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService,
        // FriendService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class JhipmessageappAppModule {}
