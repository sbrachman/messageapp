export class Friend {
    public login: string;
    public messagetime: any;
    public message: string;

    constructor(login: string, messagetime: any, message: string) {
        this.login = login;
        this.messagetime = messagetime;
        this.message = message;
    }
}
