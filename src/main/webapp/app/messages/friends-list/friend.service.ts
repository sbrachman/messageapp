import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';
import {Friend} from './friend-item/friend.model';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';
import {createRequestOption} from '../../shared/model/request-util';

@Injectable()
export class FriendService {

    private resourceUrl = 'api/messages';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(friend: Friend) {
        friend.messagetime = this.dateUtils
            .convertDateTimeFromServer(friend.messagetime);
    }

}
