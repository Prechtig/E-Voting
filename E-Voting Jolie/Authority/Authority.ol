include "console.iol"
include "IAuthorityController.iol"
include "../Common/IAuthorityCommunication.iol"

outputPort IPP {
	Location: "socket://localhost:8000"
    Protocol: sodep
    Interfaces: IAuthorityController, IAuthorityCommunication
}

main
{
    getUserInput@IPP( )( )
}