export class Message {

  public messageText: string;
  public senderLogin: string;
  public receiverLogin: string;
  public sentTime: any;
  public delivered: boolean;

    constructor(messageText: string, senderLogin: string, receiverLogin: string, sentTime: any, delivered: boolean) {
        this.messageText = messageText;
        this.senderLogin = senderLogin;
        this.receiverLogin = receiverLogin;
        this.sentTime = sentTime;
        this.delivered = delivered;
    }
}
