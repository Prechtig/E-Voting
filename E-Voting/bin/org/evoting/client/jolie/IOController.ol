include "console.iol"

type Ballot: void {
	.userId:int
	.password:string
	.vote*:bool
}

type Candidates: void {
	.candidate*:string
}

interface Interface {
    RequestResponse: getBallot( void )( Ballot )
    OneWay: setCandidateList( Candidates )
}
 
outputPort Controller {
    Interfaces: Interface
}
 
embedded {
    Java: "org.evoting.client.Controller" in Controller
}
 
main
{
	a[0] = "Strygg";
	a[1] = "Ezalor";
	a[2] = "Naix";
		
	setCandidateList@Controller( a );
    getBallot@Controller()( a );
    println@Console(a.userId[0])();
    println@Console(a.password[0])()
}