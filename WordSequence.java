import java.util.List;
import java.util.ArrayList;

// The method testEveryPossibility in this class is used to check if a sequence of
// words can be lined up in such a way that they match up with another sequence of words.
class WordSequence
{
	public int[] currentArray;
	public int[] matchingArray;
	private String testPhrase;

	public WordSequence(String testPhrase)
	{
		this.testPhrase = testPhrase;
	}

	// If the method is called without a matcheArray, one is generated:
	public boolean testEveryPossibility(List<Match> toDo)
	{
		int[] matchArray = new int[testPhrase.length()];
		for (int k = 0; k < testPhrase.length(); k++)
			matchArray[k] = -1;
		return testEveryPossibility(matchArray, toDo);
	}

	// This method takes a array of matched words and a list of matches and recursively
	// checks to see if the matches in the list can be added to matchArray filling it up.
	//
	// The method returns whether or not a match has been found. If one has been
	// found, it saves in matchingArray an array the length of the word sequence with
	// each position containing the index of the word that would need to be placed
	// in that position for the words to match up.
	public boolean testEveryPossibility(int[] matchArray, List<Match> toDo)
	{
		currentArray = (int[])matchArray.clone();

		boolean keepGoing = false;
		for (int j = 0; j < currentArray.length && !keepGoing; j++)
			if (currentArray[j] == -1)
				keepGoing = true;

		// Updates matchArray now that a match has been found:
		if (!keepGoing)
		{
			matchingArray = (int[])currentArray.clone();
			return true;
		}

		for (Match nextMatch : toDo)
		{
			boolean stop = false;

			// Checks to see if there is an open spot for nextMatch in the current array of matches:
			for (int i = nextMatch.getBegin(); i < nextMatch.getEnd() && !stop; i++)
				if (currentArray[i] != -1)
					stop = true;

			// If the spot is clear, this new match is added to currentArray:
			if (!stop)
			{
				for (int i = nextMatch.getBegin(); i < nextMatch.getEnd(); i++)
					currentArray[i] = nextMatch.getIndex();
				List<Match> willDo = new ArrayList<Match>(toDo);
				willDo.remove(nextMatch);

				// The remaining matches are tested recursivly:
				if (testEveryPossibility(currentArray, willDo))
					return true;

				// If the function returns false, the match is removed from currentArray:
				else
					for (int i = nextMatch.getBegin(); i < nextMatch.getEnd(); i++)
						currentArray[i] = -1;
			}
		}
		return false;
	}
}


