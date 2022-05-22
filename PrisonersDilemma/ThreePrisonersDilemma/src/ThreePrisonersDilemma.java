public class ThreePrisonersDilemma {
	
	/* 
	 This Java program models the two-player Prisoner's Dilemma game.
	 We use the integer "0" to represent cooperation, and "1" to represent 
	 defection. 
	 
	 Recall that in the 2-players dilemma, U(DC) > U(CC) > U(DD) > U(CD), where
	 we give the payoff for the first player in the list. We want the three-player game 
	 to resemble the 2-player game whenever one player's response is fixed, and we
	 also want symmetry, so U(CCD) = U(CDC) etc. This gives the unique ordering
	 
	 U(DCC) > U(CCC) > U(DDC) > U(CDC) > U(DDD) > U(CDD)
	 
	 The payoffs for player 1 are given by the following matrix: */
	
	static int[][][] payoff = {  
		{{6,3},  //payoffs when first and second players cooperate 
		 {3,0}}, //payoffs when first player coops, second defects
		{{8,5},  //payoffs when first player defects, second coops
	     {5,2}}};//payoffs when first and second players defect
	
	/* 
	 So payoff[i][j][k] represents the payoff to player 1 when the first
	 player's action is i, the second player's action is j, and the
	 third player's action is k.
	 
	 In this simulation, triples of players will play each other repeatedly in a
	 'match'. A match consists of about 100 rounds, and your score from that match
	 is the average of the payoffs from each round of that match. For each round, your
	 strategy is given a list of the previous plays (so you can remember what your 
	 opponent did) and must compute the next action.  */
	
	
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
	
	/* Here are four simple strategies: */
	
	class NicePlayer extends Player {
		//NicePlayer always cooperates
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			return 0; 
		}
	}
	
	class NastyPlayer extends Player {
		//NastyPlayer always defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			return 1; 
		}
	}
	
	class RandomPlayer extends Player {
		//RandomPlayer randomly picks his action each time
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			if (Math.random() < 0.5)
				return 0;  //cooperates half the time
			else
				return 1;  //defects half the time
		}
	}
	
	class TolerantPlayer extends Player {
		//TolerantPlayer looks at his opponents' histories, and only defects
		//if at least half of the other players' actions have been defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			int opponentCoop = 0;
			int opponentDefect = 0;
			for (int i=0; i<n; i++) {
				if (oppHistory1[i] == 0)
					opponentCoop = opponentCoop + 1;
				else
					opponentDefect = opponentDefect + 1;
			}
			for (int i=0; i<n; i++) {
				if (oppHistory2[i] == 0)
					opponentCoop = opponentCoop + 1;
				else
					opponentDefect = opponentDefect + 1;
			}
			if (opponentDefect > opponentCoop)
				return 1;
			else
				return 0;
		}
	}
	class RX2 extends Player {
		//TolerantPlayer looks at his opponents' histories, and only defects
		//if at least half of the other players' actions have been defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {		
			int opponentCoop1 = 0;
			int opponentDefect1 = 0;
			int opponentCoop2 = 0;
			int opponentDefect2 = 0;
			int myCoop = 0;
			int myDefect = 0;
			int percentageofDefect1 = 100;    //percentage of defects done by opponent 1
			int percentageofDefect2 = 100;    //percentage of defects done by opponent 2
			int percentageofCoop1 = 0;        //percentage of copperate done by opponent 1
			int percentageofCoop2 = 0;	      //percentage of cooperate done by opponent 2
			int percentageofmyDefect = 100;   //percentage of defects done by my player
			int percentageofmyCoop = 0;       //percentage of cooperate done by my player
			int percentageofDefectM1 = 0;     //percentage of defects done by my player and opponent 1
			int percentageofCoopM1 = 0;       //percentage of cooperate done by my player and opponent 1
			int percentageofDefectM2 = 0;     //percentage of defects done by my player and opponent 2
			int percentageofCoopM2 = 0;       //percentage of cooperate done by my player and opponent 2

			//Calculate the percentages as mentioned above using history of my player, opponent 1 and opponent 2
			for (int i=0; i<n; i++) {
				if (oppHistory1[i] == 0)
					opponentCoop1 = opponentCoop1 + 1;
				else
					opponentDefect1 = opponentDefect1 + 1;
			}
			if(n>0){
			percentageofDefect1 = opponentDefect1/n * 100;
			percentageofCoop1 = opponentCoop1/n * 100;
			}
			for (int i=0; i<n; i++) {
				if (oppHistory2[i] == 0)
					opponentCoop2 = opponentCoop2 + 1;
				else
					opponentDefect2 = opponentDefect2 + 1;
			}
			if(n>0){
			percentageofDefect2 = opponentDefect2/n * 100;
			percentageofCoop2 = opponentCoop2/n * 100;
			}
			//my coop and percentage
			for (int i=0; i<n; i++) {
				if (myHistory[i] == 0)
					myCoop = myCoop + 1;
				else
					myDefect = myDefect+ 1;
			}
			if(n>0){
				percentageofmyDefect = myDefect/n * 100;
				percentageofmyCoop = myCoop/n * 100;
				}
			if(n>0){
					percentageofDefectM1 = ((myDefect+opponentDefect1)/(2*n)) * 100;
					percentageofCoopM1 = ((myCoop+opponentCoop1)/(2*n)) * 100;
					percentageofDefectM2 = ((myDefect+opponentDefect2)/(2*n)) * 100;
					percentageofCoopM2 = ((myCoop+opponentCoop2)/(2*n)) * 100;
					}

			//end of calculations	
			if (n < 1){
				return 0;
			}
				if (oppHistory1[n-1] == oppHistory2[n-1]){
					
					return oppHistory1[n-1];
				}
				else{
					//if opponent1 of them is a nasty player and the other is NICE, my player will cooperate
					if(percentageofCoop2==100 && percentageofCoop1==0)   
					return 0;
	//if opponent2 of them is a nasty player and the other is NICE, my player will cooperate
				if(percentageofCoop1==100 && percentageofCoop2==0)   
					return 0;
	// if both players are nice, my player will exploit that and defect
				if(percentageofCoop1==0 && percentageofCoop2==0)
					return 1;
	// if one of the players happen to be a tolerantplayer, or both are, my player will defect whenever the total percentage
	// of cooperation of my player and the other player is above 50%. 
				if(percentageofCoopM1>51 || percentageofCoopM2>51)
					return 1;
	// if both opponents cooperate alot(up to 85%), then my player will always defect to maximize points
				if(percentageofCoop2>85 && percentageofCoop1 > 85)  
					return 1;
				if(percentageofCoop1 > 85 && percentageofCoop2 > 85) 
					return 1;
	// if myplayer+opponent1/2 do not add up to at least 51%
	//but both opponent cooperate more than 66% but less than 85%, my player will cooperate, 
	//since they might be tolerant player
				if (percentageofCoop1 >66 && percentageofCoop2 > 66)
					return 0;
				if (percentageofCoop2 >66 && percentageofCoop1 > 66)
					return 0;
	
	// if both my opponents are nasty players(defect more than 40%), my player will always defect
				if (percentageofDefect1 > 35 && percentageofDefect2 > 35)
					return 1;
				else
				//my player will always defect for other scenarios not mentioned above
					return 0;
			// 		if (Math.random() < 0.999)
			// 	return 0;  //cooperates half the time
			// else
			// 	return 1;  //defects half the time
				}			
		}
	}
	class RX4 extends Player {
		//TolerantPlayer looks at his opponents' histories, and only defects
		//if at least half of the other players' actions have been defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {

			if (n==0)
				return 0;

			if(oppHistory1[n-1] == oppHistory2[n-1])
				return oppHistory1[n-1];
			// if(oppHistory1[n-1] == 1 || oppHistory2[n-1] == 1)
			// 	return 1;
			else
					return myHistory[n-1];


		}

	}

	class RX3 extends Player {
		//TolerantPlayer looks at his opponents' histories, and only defects
		//if at least half of the other players' actions have been defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {		
			int opponentCoop1 = 0;
			int opponentDefect1 = 0;
			int opponentCoop2 = 0;
			int opponentDefect2 = 0;
			int myCoop = 0;
			int myDefect = 0;
			int percentageofDefect1 = 100;    //percentage of defects done by opponent 1
			int percentageofDefect2 = 100;    //percentage of defects done by opponent 2
			int percentageofCoop1 = 0;        //percentage of copperate done by opponent 1
			int percentageofCoop2 = 0;	      //percentage of cooperate done by opponent 2
			int percentageofmyDefect = 100;   //percentage of defects done by my player
			int percentageofmyCoop = 0;       //percentage of cooperate done by my player
			int percentageofDefectM1 = 0;     //percentage of defects done by my player and opponent 1
			int percentageofCoopM1 = 0;       //percentage of cooperate done by my player and opponent 1
			int percentageofDefectM2 = 0;     //percentage of defects done by my player and opponent 2
			int percentageofCoopM2 = 0;       //percentage of cooperate done by my player and opponent 2

			//Calculate the percentages as mentioned above using history of my player, opponent 1 and opponent 2
			for (int i=0; i<n; i++) {
				if (oppHistory1[i] == 0)
					opponentCoop1 = opponentCoop1 + 1;
				else
					opponentDefect1 = opponentDefect1 + 1;
			}
			if(n>0){
			percentageofDefect1 = opponentDefect1/n * 100;
			percentageofCoop1 = opponentCoop1/n * 100;
			}
			for (int i=0; i<n; i++) {
				if (oppHistory2[i] == 0)
					opponentCoop2 = opponentCoop2 + 1;
				else
					opponentDefect2 = opponentDefect2 + 1;
			}
			if(n>0){
			percentageofDefect2 = opponentDefect2/n * 100;
			percentageofCoop2 = opponentCoop2/n * 100;
			}
			//my coop and percentage
			for (int i=0; i<n; i++) {
				if (myHistory[i] == 0)
					myCoop = myCoop + 1;
				else
					myDefect = myDefect+ 1;
			}
			if(n>0){
				percentageofmyDefect = myDefect/n * 100;
				percentageofmyCoop = myCoop/n * 100;
				}
			if(n>0){
					percentageofDefectM1 = ((myDefect+opponentDefect1)/(2*n)) * 100;
					percentageofCoopM1 = ((myCoop+opponentCoop1)/(2*n)) * 100;
					percentageofDefectM2 = ((myDefect+opponentDefect2)/(2*n)) * 100;
					percentageofCoopM2 = ((myCoop+opponentCoop2)/(2*n)) * 100;
					}
//--------------------------------End of calculations--------------------------------
			if (n < 1){    //first round my player will cooperate
				return 0;
			}
     //first 10 rounds my player will return whatever my opponent returns if both opp are suspected to be t4t
				if (oppHistory1[n-1] == oppHistory2[n-1]){
					
					return oppHistory1[n-1];
				}


					//if opponent1 of them is a nasty player and the other is NICE, my player will cooperate
					if(percentageofCoop2==100 && percentageofCoop1==0)   
					return 1;
	//if opponent2 of them is a nasty player and the other is NICE, my player will cooperate
				if(percentageofCoop1==100 && percentageofCoop2==0)   
					return 1;

				else
					return 1;

				
				
				

		}
	}


	class RX extends Player {
		//TolerantPlayer looks at his opponents' histories, and only defects
		//if at least half of the other players' actions have been defects
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			int opponentCoop1 = 0;
			int opponentDefect1 = 0;
			int opponentCoop2 = 0;
			int opponentDefect2 = 0;
			int myCoop = 0;
			int myDefect = 0;
			int percentageofDefect1 = 100;    //percentage of defects done by opponent 1
			int percentageofDefect2 = 100;    //percentage of defects done by opponent 2
			int percentageofCoop1 = 0;        //percentage of copperate done by opponent 1
			int percentageofCoop2 = 0;	      //percentage of cooperate done by opponent 2
			int percentageofmyDefect = 100;   //percentage of defects done by my player
			int percentageofmyCoop = 0;       //percentage of cooperate done by my player
			int percentageofDefectM1 = 0;     //percentage of defects done by my player and opponent 1
			int percentageofCoopM1 = 0;       //percentage of cooperate done by my player and opponent 1
			int percentageofDefectM2 = 0;     //percentage of defects done by my player and opponent 2
			int percentageofCoopM2 = 0;       //percentage of cooperate done by my player and opponent 2

			//Calculate the percentages as mentioned above using history of my player, opponent 1 and opponent 2
			for (int i=0; i<n; i++) {
				if (oppHistory1[i] == 0)
					opponentCoop1 = opponentCoop1 + 1;
				else
					opponentDefect1 = opponentDefect1 + 1;
			}
			if(n>0){
			percentageofDefect1 = opponentDefect1/n * 100;
			percentageofCoop1 = opponentCoop1/n * 100;
			}
			for (int i=0; i<n; i++) {
				if (oppHistory2[i] == 0)
					opponentCoop2 = opponentCoop2 + 1;
				else
					opponentDefect2 = opponentDefect2 + 1;
			}
			if(n>0){
			percentageofDefect2 = opponentDefect2/n * 100;
			percentageofCoop2 = opponentCoop2/n * 100;
			}
			//my coop and percentage
			for (int i=0; i<n; i++) {
				if (myHistory[i] == 0)
					myCoop = myCoop + 1;
				else
					myDefect = myDefect+ 1;
			}
			if(n>0){
				percentageofmyDefect = myDefect/n * 100;
				percentageofmyCoop = myCoop/n * 100;
				}
			if(n>0){
					percentageofDefectM1 = ((myDefect+opponentDefect1)/(2*n)) * 100;
					percentageofCoopM1 = ((myCoop+opponentCoop1)/(2*n)) * 100;
					percentageofDefectM2 = ((myDefect+opponentDefect2)/(2*n)) * 100;
					percentageofCoopM2 = ((myCoop+opponentCoop2)/(2*n)) * 100;
					}

			//end of calculations

			if(n < 1)
			     return 0;     //always cooperate on the first game. because T4tP and tolerantP are revengeful players


//if opponent1 of them is a nasty player and the other is NICE, my player will cooperate
		    if(percentageofCoop2==100 && percentageofCoop1==0)   
				return 0;
//if opponent2 of them is a nasty player and the other is NICE, my player will cooperate
			if(percentageofCoop1==100 && percentageofCoop2==0)   
				return 0;
// if both players are nice, my player will exploit that and defect
			if(percentageofCoop1==0 && percentageofCoop2==0)
			    return 1;
// if one of the players happen to be a tolerantplayer, or both are, my player will defect whenever the total percentage
// of cooperation of my player and the other player is above 50%. 
			if(percentageofCoopM1>51 || percentageofCoopM2>51)
				return 1;
// if both opponents cooperate alot(up to 85%), then my player will always defect to maximize points
			if(percentageofCoop2>85 && percentageofCoop1 > 85)  
				return 1;
			if(percentageofCoop1 > 85 && percentageofCoop2 > 85) 
				return 1;
// if myplayer+opponent1/2 do not add up to at least 51%
//but both opponent cooperate more than 66% but less than 85%, my player will cooperate, 
//since they might be tolerant player
			if (percentageofCoop1 >66 && percentageofCoop2 > 66)
				return 0;
			if (percentageofCoop2 >66 && percentageofCoop1 > 66)
				return 0;

// if both my opponents are nasty players(defect more than 40%), my player will always defect
			if (percentageofDefect1 > 90 && percentageofDefect2 > 90)
			    return 1;
			else
			//my player will always defect for other scenarios not mentioned above
			    return 1;
			
		}
	}
	
	class FreakyPlayer extends Player {
		//FreakyPlayer determines, at the start of the match, 
		//either to always be nice or always be nasty. 
		//Note that this class has a non-trivial constructor.
		int action;
		FreakyPlayer() {
			if (Math.random() < 0.5)
				action = 0;  //cooperates half the time
			else
				action = 1;  //defects half the time
		}
		
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			return action;
		}	
	}

	class T4TPlayer extends Player {
		//Picks a random opponent at each play, 
		//and uses the 'tit-for-tat' strategy against them 
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			if (n==0) return 0; //cooperate by default
			if (Math.random() < 0.5)
				return oppHistory1[n-1];
			else
				return oppHistory2[n-1];
		}	
	}

	class Shaun extends Player {
		//Picks a random opponent at each play, 
		//and uses the 'tit-for-tat' strategy against them 
		int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
			if (n==0) return 0; //cooperate by default
			if (oppHistory1[n-1]==oppHistory2[n-1])
				return oppHistory1[n-1];
			else
				return myHistory[n-1];
		}	
	}

	
	/* In our tournament, each pair of strategies will play one match against each other. 
	 This procedure simulates a single match and returns the scores. */
	float[] scoresOfMatch(Player A, Player B, Player C, int rounds) {
		int[] HistoryA = new int[0], HistoryB = new int[0], HistoryC = new int[0];
		float ScoreA = 0, ScoreB = 0, ScoreC = 0;
		
		for (int i=0; i<rounds; i++) {
			int PlayA = A.selectAction(i, HistoryA, HistoryB, HistoryC);
			int PlayB = B.selectAction(i, HistoryB, HistoryC, HistoryA);
			int PlayC = C.selectAction(i, HistoryC, HistoryA, HistoryB);
			ScoreA = ScoreA + payoff[PlayA][PlayB][PlayC];
			ScoreB = ScoreB + payoff[PlayB][PlayC][PlayA];
			ScoreC = ScoreC + payoff[PlayC][PlayA][PlayB];
			HistoryA = extendIntArray(HistoryA, PlayA);
			HistoryB = extendIntArray(HistoryB, PlayB);
			HistoryC = extendIntArray(HistoryC, PlayC);
		}
		float[] result = {ScoreA/rounds, ScoreB/rounds, ScoreC/rounds};
		return result;
	}
	
//	This is a helper function needed by scoresOfMatch.
	int[] extendIntArray(int[] arr, int next) {
		int[] result = new int[arr.length+1];
		for (int i=0; i<arr.length; i++) {
			result[i] = arr[i];
		}
		result[result.length-1] = next;
		return result;
	}
	
	/* The procedure makePlayer is used to reset each of the Players 
	 (strategies) in between matches. When you add your own strategy,
	 you will need to add a new entry to makePlayer, and change numPlayers.*/
	int numPlayers = 90;



	Player makePlayer(int which){
		// if(which == 0) {
		// 	// return your player
		// 	return new RX2();
		// }
		// if(which == 1) {
		// 	// return your player
		// 	return new Shaun();
		// }
		if(which == 0) {
			// return your player
			return new RX4();
		}

		else if(which <=30){
			return new T4TPlayer();
		}
		else if (which <= 60){
			// return 30 of this player
			return new TolerantPlayer();
		}
		else {
			// return remaining player
			return new NastyPlayer();
		}
	}

	//  int numPlayers = 10;
	//  Player makePlayer(int which) {
	// 	 switch (which) {
	// 	 case 0: return new TolerantPlayer();
	// 	 case 1: return new TolerantPlayer();
	// 	 case 2: return new NastyPlayer();
	// 	 case 3: return new NastyPlayer();
	// 	 case 4: return new NicePlayer();
	// 	 case 5: return new NicePlayer();
	// 	 case 6: return new RX();
	// 	 case 7: return new RX();
	// 	 case 8: return new T4TPlayer();
	// 	 case 9: return new T4TPlayer();

	// 	 }
	// 	 throw new RuntimeException("Bad argument passed to makePlayer");
	//  }
	/* Finally, the remaining code actually runs the tournament. */
	
	public static void main (String[] args) {
		ThreePrisonersDilemma instance = new ThreePrisonersDilemma();
		instance.runTournament();
	}
	
	boolean verbose = false; // set verbose = false if you get too much text output
	
	void runTournament() {
		float[] totalScore = new float[numPlayers];

		// This loop plays each triple of players against each other.
		// Note that we include duplicates: two copies of your strategy will play once
		// against each other strategy, and three copies of your strategy will play once.

		for (int i=0; i<numPlayers; i++) for (int j=i; j<numPlayers; j++) for (int k=j; k<numPlayers; k++) {

			Player A = makePlayer(i); // Create a fresh copy of each player
			Player B = makePlayer(j);
			Player C = makePlayer(k);
			//int rounds = 1000;
			int rounds = 90 + (int)Math.rint(20 * Math.random()); // Between 90 and 110 rounds
			float[] matchResults = scoresOfMatch(A, B, C, rounds); // Run match
			totalScore[i] = totalScore[i] + matchResults[0];
			totalScore[j] = totalScore[j] + matchResults[1];
			totalScore[k] = totalScore[k] + matchResults[2];
			if (verbose){
				System.out.println("Out of " + rounds +" times");
				System.out.println(A.name() + " scored " + matchResults[0] +
						" points, " + B.name() + " scored " + matchResults[1] + 
						" points, and " + C.name() + " scored " + matchResults[2] + " points.");
			}
		}
		int[] sortedOrder = new int[numPlayers];
		// This loop sorts the players by their score.
		for (int i=0; i<numPlayers; i++) {
			int j=i-1;
			for (; j>=0; j--) {
				if (totalScore[i] > totalScore[sortedOrder[j]]) 
					sortedOrder[j+1] = sortedOrder[j];
				else break;
			}
			sortedOrder[j+1] = i;
		}
		
		// Finally, print out the sorted results.
		if (verbose) System.out.println();
		System.out.println("Tournament Results");
		for (int i=0; i<numPlayers; i++) 
			System.out.println(makePlayer(sortedOrder[i]).name() + ": " 
				+ totalScore[sortedOrder[i]] + " points.");
		
	} // end of runTournament()
	
} // end of class PrisonersDilemma
