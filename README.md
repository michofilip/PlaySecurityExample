# PlaySecurityExample
## Example of authentication and authorization with Play Framework


### No restriction
  

    def public(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
        ...
    }

### Logged user only

    def priv(): Action[AnyContent] = authenticated { implicit userReq: UserRequest[AnyContent] =>
        ...
    }

### Logged user with additional permissions

- #### Single role required


    def adminOneRole(): Action[AnyContent] = authenticated.withRole("ADMIN") { implicit userReq: UserRequest[AnyContent] =>
        ...
    }

- #### One of many roles required


    def adminAnyRoles(): Action[AnyContent] = authenticated.withAnyRoles("ADMIN", "DEV") { implicit userReq: UserRequest[AnyContent] =>
        ...
    }

- #### All roles required


    def adminAllRoles(): Action[AnyContent] = authenticated.withAllRoles("ADMIN", "DEV") { implicit userReq: UserRequest[AnyContent] =>
        ...
    }
