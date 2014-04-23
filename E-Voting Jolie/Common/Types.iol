type EncryptedBallot: void {
    .userId: raw
    .passwordHash: raw
    .timestamp: raw
    .vote*: raw
}

type EncryptedBallotList: void {
	.numberOfElectionOptions: int
	.votes*: void {
		.vote*: void {
			.electionOptionId: int
			.encryptedVote: raw	
		}
		
	}
}

type EncryptedElectionOptions: void {
	.timestamp: raw
	.electionOptions: raw
}

type PublicKeys: void {
	.elgamalPublicKey: void {
		.y: string
		.parameters: void {
			.p: string
			.g: string
			.l: int
		}
	}
	.rsaPublicKey: raw
}

type ElectionStatus: void {
	.running: bool
	.endTime: long
}

type Confirmation: void {
	.confirmed: bool
}

type ElectionStart: void{
	.endTime: long
}