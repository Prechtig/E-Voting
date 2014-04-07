type EncryptedBallot: void {
    .userId: raw
    .passwordHash: raw
    .timestamp: raw
    .vote*: raw
}

type EncryptedBallotList: void {
	.numberOfCandidates: int
	.votes*: void {
		.vote*: void {
			.candidateId: int
			.encryptedVote: raw	
		}
		
	}
}

type EncryptedCandidateList: void {
	.timestamp: raw
	.candidates: raw
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