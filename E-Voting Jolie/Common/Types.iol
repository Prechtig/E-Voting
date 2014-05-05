type EncryptedBallot: void {
	.sid: string
	.userId: raw
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

type SignedElectionOptions: void {
	.electionId: int
	.electionOptions*: void {
		.id: int
		.name: string
		.partyId: int
	}
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
	.startTime: long
	.endTime: long
}

type LoginRequest: void {
	.userId: int
	.passwordHash: string
}

type LoginResponse: void {
	.sid: string
}

type Validation: void {
	.message: string
	.signature: raw
}

type ElectionOptionsList: void {
	.electionOptions*: void {
		.id: int
		.name: string
		.partyId: int
	}
	.validator: Validation
}

type ElectionStart: void {
	.startTime: long
	.endTime: long
	.validator: Validation
}