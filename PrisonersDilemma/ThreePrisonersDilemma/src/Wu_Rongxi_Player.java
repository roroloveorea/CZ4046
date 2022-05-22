public class Wu_Rongxi_Player {
	abstract class Player {
		// This procedure takes in the number of rounds elapsed so far (n), and 
		// the previous plays in the match, and returns the appropriate action.
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			throw new RuntimeException("You need to override the selectAction method.");
		}
		
		// Used to extract the name of this player class.
		final String name() {
			String result = getClass().getName();
			return result.substring(result.indexOf('$')+1);
		}
	}
    class RX4 extends Player {


		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {

			if (n==0)
				return 0;   //cooperate in initial round

			if(oppHistory1[n-1] == oppHistory2[n-1])
				return oppHistory1[n-1];   //follow whatever my opponents do in the previous round if they did the same as each other

			else
					return myHistory[n-1];   //else return my own last move


		}

	}
}

