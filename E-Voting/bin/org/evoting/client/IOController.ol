include "console.iol"

type Ballot: void {
	.userId:int
	.password:string
	.vote*:bool
}

interface Interface {
    RequestResponse: getBallot( void )( Ballot )
}
 
outputPort Controller {
    Interfaces: Interface
}
 
embedded {
    Java: "org.evoting.client.Controller" in Controller
}
 
main
{
    getBallot@Controller()( a );
    println@Console(a.userId[0])();
    println@Console(a.password[0])()
}