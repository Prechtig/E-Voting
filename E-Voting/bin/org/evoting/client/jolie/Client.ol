include "console.iol"
include "IClient.iol"

outputPort Controller {
    Interfaces: Interface
}

embedded {
    Java: "org.evoting.client.Controller" in Controller
}

main
{
	a = "Ezalor";
	println@Console( "requesting" ) ();
	request.ZipCode = "10007";
	LocalTimeByZipCode@LocalTimeSoap( request )( response );
	println@Console( response.LocalTimeByZipCodeResult ) ();
	setCandidateList@Controller( a );
    getBallot@Controller()( b )
    
}