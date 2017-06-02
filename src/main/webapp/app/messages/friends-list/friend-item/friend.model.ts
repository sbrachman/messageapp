export class Friend {

    public login: string;
    public messagetime: any;
    public lastMessage: string;
    public delivered: boolean;

    constructor(login: string, messagetime: any, lastMessage: string, delivered: boolean) {
        this.login = login;
        this.messagetime = messagetime;
        this.lastMessage = lastMessage;
        this.delivered = delivered;
    }
}
