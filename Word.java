class Word implements Comparable
{
	private String word;
	private String backwards;
	private int length;

	// Constructor
	public Word(String word)
	{
		this.word = word;
		backwards = reverse(word);
		length = word.length();
	}

	// Necessary for sorting by length:
	public int compareTo(Object w)
	{
		if (((Word)w).getLength() > length)
			return -1;
		if (((Word)w).getLength() < length)
			return 1;
		return 0;
	}

	public boolean isPalindrome()
	{
		if (length == 0 || length == 1)
			return true;
		if (word.charAt(0) != word.charAt(length - 1))
			return false;
		return (new Word(word.substring(1, length - 1)).isPalindrome());
	}

	public String reverse(String string)
	{
		StringBuilder sb = new StringBuilder(string);
		return sb.reverse().toString();
    }

	// Assessors
	public String getWord()	{return word;}
	public String getBackwards() {return backwards;}
	public int getLength() {return length;}

}