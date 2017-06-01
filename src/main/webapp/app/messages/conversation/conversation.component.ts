import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {MessagesService} from '../messages.service';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';
import {Message} from '../message.model';
import {AlertService} from 'ng-jhipster';





@Component({
  selector: 'app-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css']
})
export class ConversationComponent implements OnInit {

  typedText: string;

  messages: Message[];

  login: string;

  @ViewChild('conversationWindow')
  private conversationWindow: ElementRef;

  constructor(private messagesService: MessagesService,
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
        }
      );
  }


  loadAll() {
    this.messagesService.query(this.login).subscribe(
      (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
      (res: ResponseWrapper) => this.onError(res.json)
    );
  }

  onMessageSend() {
    this.messagesService.create({messageText: this.typedText, senderLogin: '',
      receiverLogin: '', sentTime: null}, this.login).subscribe(
        (res: ResponseWrapper) => {
          this.messages.push(res.json);
        },
        (res: ResponseWrapper) => console.log(res.headers)
    );
    this.typedText = '';
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
