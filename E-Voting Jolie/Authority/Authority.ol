include "console.iol"
include "IAuthority.iol"

outputPort Controller {
    Interfaces: IAuthorityController
}

embedded {
    Java: "org.evoting.authority.ConsoleIO" in Controller
}

main
{
    getUserInput@Controller( ) ( );
}