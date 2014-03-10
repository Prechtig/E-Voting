include "BulletinBoardInterface.iol"
include "console.iol"

inputPort BulletinBoardService {
    Location: "socket://localhost:8000/"
    Protocol: sodep
    Interfaces: BulletinBoardInterface
}

main
{
	getCandidates(  )( result ) {
		result[0] = "Mark";
		result[1] = "Mikkel";
		result[2] = "Andreas"
		
	};
	vote(  )( registered ) {
		registered = false
	}
}