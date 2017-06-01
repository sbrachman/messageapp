import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {Message} from './message.model';

@Injectable()
export class MessagesService {

    private resourceUrl = 'api/messages';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    // create(message: Message): Observable<Message> {
    //     const copy = this.convert(message);
    //     return this.http.post(this.resourceUrl, copy).map((res: Response) => {
    //         const jsonResponse = res.json();
    //         this.convertItemFromServer(jsonResponse);
    //         return jsonResponse;
    //     });
    // }

    create(message: Message, login: string): Observable<ResponseWrapper> {
        const copy = this.convert(message);
        return this.http.post(`${this.resourceUrl}/${login}`, copy).map((res: Response) => {
            const jsonResponse = res.json();
            // this.convertItemFromServer(jsonResponse);
            return new ResponseWrapper(res.headers, jsonResponse, res.status);
        });
    }

    query(login: string): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrl}/${login}`)
            .map((res: Response) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.sentTime = this.dateUtils
            .convertDateTimeFromServer(entity.sentTime);
    }

    private convert(message: Message): Message {
        const copy: Message = Object.assign({}, message);

        copy.sentTime = this.dateUtils.toDate(message.sentTime);
        return copy;
    }

}
