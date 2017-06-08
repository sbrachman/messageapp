export class UserView {
  public login: string;
  public firstName?: string;
  public lastName?: string;
  public createdDate: any;

  constructor(login: string, firstName: string, lastName: string, createdDate: any) {
    this.login = login;
    this.firstName = firstName;
    this.lastName = lastName;
    this.createdDate = createdDate;
  }
}
