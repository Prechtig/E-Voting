type EncryptedBallot: void {
	.userId: raw
    .passwordHash: raw
    .electionId: raw
    .vote*: raw
    .signature: raw
}

type EncryptedBallotList: void {
	.votes*: void {
		.vote*: void {
			.electionOptionId: int
			.encryptedVote: raw	
		}
	}
	.signature: raw
}

type EncryptedElectionOptions: void {
	.electionId: int
	.electionOptions: {
		.id: int
		.name: string
		.partyId: int
	}
	.endTime: long
	.signature: raw
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
	.endTimeHash: raw
}