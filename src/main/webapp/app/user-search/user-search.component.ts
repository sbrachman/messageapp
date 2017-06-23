import { Component, OnDestroy } from '@angular/core';
import {User} from '../shared/user/user.model';
import {UserView} from './user-view.model';
import {ITEMS_PER_PAGE} from '../shared/constants/pagination.constants';
import {PaginationConfig} from '../blocks/config/uib-pagination.config';
import {Principal} from '../shared/auth/principal.service';
import {UserService} from '../shared/user/user.service';
import {PaginationUtil, AlertService, ParseLinks, EventManager} from 'ng-jhipster';
import {ActivatedRoute, Router} from '@angular/router';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';

@Component({
  selector: 'jhi-user-search',
  templateUrl: './user-search.component.html',
  styles: []
})
export class UserSearchComponent implements OnDestroy {

  users: UserView[];

  error: any;
  success: any;
  routeData: any;
  links: any;
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  currentSearch: string;

  constructor(
    private userService: UserService,
    private parseLinks: ParseLinks,
    private alertService: AlertService,
    private principal: Principal,
    private eventManager: EventManager,
    private paginationUtil: PaginationUtil,
    private paginationConfig: PaginationConfig,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
  }

  ngOnDestroy() {
    this.routeData.unsubscribe();
  }

  search() {
    if (this.currentSearch) {
      this.userService.search({
        page: this.page - 1,
        size: this.itemsPerPage,
        // sort: this.sort(),
        query: this.currentSearch}).subscribe(
        (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
        (res: ResponseWrapper) => this.onError(res.json)
      );
    }
  }

  trackIdentity(index, item: User) {
    return item.id;
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  transition() {
    this.router.navigate(['/search'], {
      queryParams: {
        page: this.page,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
  }

  private onSuccess(data, headers) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    this.users = data;
  }

  private onError(error) {
    this.alertService.error(error.error, error.message, null);
  }

}
