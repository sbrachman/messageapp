import {Component, ElementRef, OnInit, ViewChild, OnDestroy} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {ConversationService} from './conversation.service';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';
import {Message} from '../message.model';
import {AlertService} from 'ng-jhipster';

@Component({
  selector: 'jhi-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit, OnDestroy {

  typedText: string;

  messages: Message[];

  login: string;

  interval: any;

  @ViewChild('conversationWindow')
  private conversationWindow: ElementRef;

  constructor(private conversationService: ConversationService,
              private route: ActivatedRoute,
              private router: Router,
              private alertService: AlertService) { }

  ngOnInit() {
    this.route.params
      .subscribe(
        (params: Params) => {
          this.login = params['login'];
          this.messages = [];
          this.loadAll();
          this.enablePullingMessages();
        }
      );
  }

  private enablePullingMessages() {
    clearInterval(this.interval);
    this.interval = setInterval(() => this.loadNew(), 500);
  }

  ngOnDestroy() {
    clearInterval(this.interval);
  }

  private loadAll() {
    this.conversationService.query(this.login).subscribe(
      (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
      (res: ResponseWrapper) => this.onError(res.json)
    );
  }

  private loadNew() {
    this.conversationService.queryNew(this.login).subscribe(
      (res: ResponseWrapper) => {
        if (res) {
          if (res.status === 200) {
            this.onSuccess(res.json, res.headers);
          }
        }
      }
    );
  }

  onMessageSend() {
    if (this.typedText.length > 1) {
      this.conversationService.create({messageText: this.typedText, senderLogin: '',
        receiverLogin: '', sentTime: null, delivered: false}, this.login)
          .subscribe((res: ResponseWrapper) => {
                this.messages.push(res.json);
            },
            (res: ResponseWrapper) => console.log(res.headers));
      this.typedText = '';
    }
  }

  private onSuccess(data, headers) {
      for (let i = 0; i < data.length; i++) {
        this.messages.push(data[i]);
      }
  }

  private onError(error) {
    this.alertService.error(error.message, null, null);
  }

}
