import java.io.*;
import java.util.*;

public class Dictionary
{
	private int numberOfWords;
	private Word[] wordArray;
	private int[] nextWordWithArray;

	public static void main(String args[])
	{
	   	Dictionary dictionary = new Dictionary("WORD.LST");

	   	// Since the length of the palindrome is of concern,
	   	// shortest words are searched first.
	   	dictionary.sortWordsByLength();

	   	List<Word> palindrome = new LinkedList<Word>();

		// Looks for palindromes with each letter in them:
		int temp;
		int lettersSoFar = 0;
		boolean[] alphabet = initializeAlphabet();
		for (int i = 0; i < dictionary.numberOfWords() && lettersSoFar < 26; i++)
		{
			if (dictionary.wordArray[i].isPalindrome())
			{
				for (int j = 0; j < dictionary.wordArray[i].getWord().length(); j++)
				{
					temp = letterToNumber(dictionary.wordArray[i].getWord().charAt(j));
					if (!alphabet[temp])
					{
						alphabet[temp] = true;
						lettersSoFar++;
					}
				}
			}
		}

		// Searches for multiple-word palindromes for which
		// single-word ones cannot be found:
		for (int i = 0; i < 26; i++)
		{
			if (!alphabet[i])
			{
				boolean isThereMatch = false;
				String testPhrase = "";
				String wordWithLetter = dictionary.nextWordWith(numberToLetter(i));
				while(wordWithLetter.compareTo("$") != 0 && !isThereMatch)
				{
					// This loop starts with -1, because each word is first checked without a
					// second word appended to it. (The wordAt method returns an empty string
					// if the requested index is out of bounds.)
					//
					// Note that the loop only tests the first 100 words in the dictionary (by length.)
					// In practice, palindromes are found in individual words for each of the
					// sought-after letters, so the program would work even if the number was set to 0.
					// (It would also word if the word-appending functionality had been left out of the
					// program entirely.)
					for (int wordTwo = -1; wordTwo < 100 && !isThereMatch; wordTwo++)
					{
						// The phrase is tested with and without its last letter:
						for(int tries = 0; tries < 2 && !isThereMatch; tries++)
						{
							testPhrase = (wordWithLetter + dictionary.wordAt(wordTwo).getWord());
							if (tries != 0)
								testPhrase = testPhrase.substring(0, testPhrase.length() - 1);
							System.out.println(testPhrase);
							List<Match> matchedWords = dictionary.wordsBackwardsInWordSequence(testPhrase);

							WordSequence wordSequence = new WordSequence(testPhrase);
							isThereMatch = wordSequence.testEveryPossibility(matchedWords);

							if (isThereMatch)
							{
								String palindromeSequence = "";
								int k = wordSequence.matchingArray.length - 1;
								while (k >= 0)
								{
									palindromeSequence = palindromeSequence + " " + dictionary.wordAt(wordSequence.matchingArray[k]).getWord();
									k = k - dictionary.wordAt(wordSequence.matchingArray[k]).getLength();
								}
								if (dictionary.wordAt(wordTwo).getLength() > 0)
									palindrome.add(new Word(wordWithLetter + " " + dictionary.wordAt(wordTwo).getWord() + " " + palindromeSequence));
								else
									palindrome.add(new Word(wordWithLetter + palindromeSequence));
								System.out.println("Match Found");
							}
						}
					}
					if (!isThereMatch)
						wordWithLetter = dictionary.nextWordWith(numberToLetter(i));
				}
			}
		}

		// These multiple-word palindromes that we have found are scanned
		// to see which letters they contain:
		lettersSoFar = 0;
		boolean[] alphabet2 = initializeAlphabet();
		for (Word finalWord : palindrome)
		{
			for (int j = 0; j < finalWord.getLength(); j++)
			{
				if (finalWord.getWord().charAt(j) != ' ')
				{
					temp = letterToNumber(finalWord.getWord().charAt(j));
					if (!alphabet2[temp])
					{
						alphabet2[temp] = true;
						lettersSoFar++;
					}
				}
			}
		}

		// Single word palindromes are added to the list until all letters are present:
		boolean keepWord;
		for (int i = 0; i < dictionary.numberOfWords() && lettersSoFar < 26; i++)
		{
			if (dictionary.wordArray[i].isPalindrome())
			{
				keepWord = false;
				for (int j = 0; j < dictionary.wordArray[i].getWord().length(); j++)
				{
					temp = letterToNumber(dictionary.wordArray[i].getWord().charAt(j));
					if (!alphabet2[temp])
					{
						alphabet2[temp] = true;
						keepWord = true;
						lettersSoFar++;
					}
				}
				if (keepWord)
					palindrome.add(dictionary.wordArray[i]);
			}
		}

		// The palindrome is printed out:
		System.out.println("Palindromic Pangram:");

		ListIterator<Word> it = palindrome.listIterator(0);

		while (it.hasNext())
			System.out.print(it.next().getWord() + " ");
		it.previous();
		while(it.hasPrevious())
			System.out.print(it.previous().getWord() + " ");
	}

	// Constructor
	public Dictionary(String fileName)
	{
		try {
			BufferedReader bf = new BufferedReader(
				 new FileReader(fileName));
			String word = null;

			int lines = 0;
			while ((word = bf.readLine()) != null)
				lines++;
			wordArray = new Word[lines];
			bf.close();

			BufferedReader bf2 = new BufferedReader(
				 new FileReader(fileName));

			for (int i = 0;(word = bf2.readLine()) != null; i++)
				wordArray[i] = new Word(word);

		} catch (Exception e) {System.err.println("Unable to read from " + fileName);}

		numberOfWords = wordArray.length;

		// Initializes the array used in the nextWordWith method:
		nextWordWithArray = new int[26];
		for (int k = 0; k < 26; k++)
			nextWordWithArray[k] = 0;
    }

    // Returns the next word in the dictionary that contains a certain letter.
    // Returns $ when the words have been exhausted, then repeats.
	public String nextWordWith(char letter)
	{
		for (int i = nextWordWithArray[letterToNumber(letter)]; i < numberOfWords(); i++)
		{
			for (int j = 0; j < wordArray[i].getLength(); j++)
			{
				if (wordArray[i].getWord().charAt(j) == letter)
				{
					nextWordWithArray[letterToNumber(letter)] = i + 1;
					return (wordArray[i].getWord());
				}
			}
		}
		nextWordWithArray[letterToNumber(letter)] = 0;
		return "$";
	}

    public void sortWordsByLength()
	{
		Arrays.sort(wordArray);
	}

    public int numberOfWords()
    {
		return numberOfWords;
	}

	public Word wordAt(int index)
	{
		if (index >= numberOfWords || index < 0)
			return (new Word(""));
		return wordArray[index];
	}

	// Returns 0-25 for a-z
	public static int letterToNumber(char letter)
	{
		return ((int)letter - 97);
	}

	// Returns a-z for 0-25
	public static char numberToLetter(int number)
	{
		return ((char)(number + 97));
	}

	// This array is used to keep track of the letters
	// that are present in a sequence of words.
	private static boolean[] initializeAlphabet()
	{
		boolean[] alphabet = new boolean[26];
		for (int k = 0; k < 26; k++)
			alphabet[k] = false;
		return alphabet;
	}

	// Uses the Knuth-Morris-Pratt algorithm to find letter patterns in Strings:
	// T = text, P = pattern
	public static List<Integer> KMPMatcher(char[] T, char[] P)
	{
		List<Integer> shifts = new ArrayList<Integer>();
		int n = T.length;
		int m = P.length;
		int[] pi = computePi(P);
		int q = 0;
		for (int i = 1; i <= n; i++)
		{
			while (q > 0 && P[q] != T[i-1])
			{
				q = pi[q-1];
			}
			if (P[q] == T[i-1])
			{
				q = q + 1;
			}
			if (q == m)
			{
				shifts.add(i - m);
				q = pi[q-1];
			}
		}
		return shifts;
	}

	//Used in KMPMatcher:
	public static int[] computePi(char[] P)
	{
		int m = P.length;
		int[] pi = new int[m];
		pi[0] = 0;
		int k = 0;
		for (int q = 2; q <= m; q++)
		{
			while (k > 0 && P[k] != P[q-1])
			{
				k = pi[k-1];
			}
			if (P[k] == P[q-1])
			{
				k = k + 1;
			}
			pi[q-1] = k;
		}
		return pi;
	}

	// Returns a list of the positions words can be found backwards in a String:
	public List<Match> wordsBackwardsInWordSequence(String testPhrase)
	{
		List<Match> backwardsMatches = new ArrayList<Match>();
		for (int i = 0; i < numberOfWords(); i++)
		{
			if (wordArray[i].getBackwards().length() < 5)
			{
				List<Integer> shifts = KMPMatcher(testPhrase.toCharArray(), wordArray[i].getBackwards().toCharArray());
				for (int shift : shifts)
				{
					backwardsMatches.add(new Match(i, shift, (shift + wordArray[i].getBackwards().length())));
				}
			}
		}
		return backwardsMatches;
	}
}