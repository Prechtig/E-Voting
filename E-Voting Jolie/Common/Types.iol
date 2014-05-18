type EncryptedBallot: void {
	.sid: string
	.userId: raw
	.vote*: raw
	.signature: raw
}

type SignedBallotList: void {
	.votes*: void {
		.index: int
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
		.name: string
		.id: int
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
	.sid: string
}

type LoginRequest: void {
	.userId: raw
	.passwordHash: raw
	.sid: string
}

type LoginResponse: void {
	.success: bool
	.validator: Validation
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

type SessionRequest: void {
	.sid: string
}