class Match
{
	private int index;
	private int begin;
	private int end;

	public Match(int index, int begin, int end)
	{
		this.index = index;
		this.begin = begin;
		this.end = end;
	}

	public int getIndex() {return index;}
	public int getBegin() {return begin;} //Index of first spot
	public int getEnd() {return end;} //After index of last spot

}