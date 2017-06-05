export class Friend {

    public login: string;
    public messageTime: any;
    public lastMessage: string;
    public lastMessageSenderId: number;
    public messageDelivered: boolean;

    constructor(login: string, messageTime: any, lastMessage: string, lastMessageSenderId: number, messageDelivered: boolean) {
        this.login = login;
        this.messageTime = messageTime;
        this.lastMessage = lastMessage;
        this.lastMessageSenderId = lastMessageSenderId;
        this.messageDelivered = messageDelivered;
    }
}
