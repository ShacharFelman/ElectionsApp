package listeners;

import model.Citizen;

public interface ElectionModelListenable {
	public void updateElectorHadVoted(Citizen c);

	public void updateElectionStarted();

	public void updateElectionEnded();
}
