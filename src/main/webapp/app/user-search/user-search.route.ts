import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { PaginationUtil } from 'ng-jhipster';
import {Principal} from '../shared/auth/principal.service';
import {UserSearchComponent} from './user-search.component';
import {UserRouteAccessService} from '../shared/auth/user-route-access-service';

@Injectable()
export class UserSearchResolve implements CanActivate {

  constructor(private principal: Principal) { }

  canActivate() {
    return this.principal.identity().then((account) => this.principal.hasAnyAuthority(['ROLE_USER']));
  }
}

@Injectable()
export class UserSearchResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
    const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'login,asc';
    return {
      page: this.paginationUtil.parsePage(page),
      predicate: this.paginationUtil.parsePredicate(sort),
      ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const userSearchRoute: Routes = [
  {
    path: 'search',
    component: UserSearchComponent,
    resolve: {
      'pagingParams': UserSearchResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Find Users'
    },
    canActivate: [UserRouteAccessService],
  }
];
