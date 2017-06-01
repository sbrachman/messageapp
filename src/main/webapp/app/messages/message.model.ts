export class Message {

  public messageText: string;
  public senderLogin: string;
  public receiverLogin: string;
  public sentTime: any;


  constructor(messageText: string, senderLogin: string, receiverLogin: string, sentTime: any) {
    this.messageText = messageText;
    this.senderLogin = senderLogin;
    this.receiverLogin = receiverLogin;
    this.sentTime = sentTime;
  }
}
