include "console.iol"

interface Interface {
    RequestResponse: getBallot( void )( int )
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
    println@Console(a)()
}