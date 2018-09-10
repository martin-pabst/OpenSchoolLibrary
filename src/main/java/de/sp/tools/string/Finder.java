package de.sp.tools.string;

public class Finder {

	private String text;

	private int pos;

	private int markBegin;

	private int markEnd;

	private int lastWordLength = 0;

	public Finder(String text) {
		super();
		this.text = text;
		pos = 0;

	}

	public boolean seek(String word){
		return text.indexOf(word, pos) >= 0;
	}

	public boolean find(String word) {

		int i = text.indexOf(word, pos);

		if (i < 0) {
			return false;
		}

		lastWordLength = word.length();
		pos = i;

		return true;
	}

	public boolean findBackward(String word) {

		int i = text.lastIndexOf(word, pos);

		if (i < 0) {
			return false;
		}

		lastWordLength = word.length();
		pos = i;

		return true;
	}

    public Finder jumpTo(String word) {
        find(word);
        return this;
    }

	public Finder jumpToBackward(String word) {
		findBackward(word);
		return this;
	}

	public Finder skipFoundWord() {

		pos += lastWordLength;
		lastWordLength = 0;
		return this;

	}

	public Finder markBegin() {
		markBegin = pos;
		return this;
	}

	public Finder markEnd() {
		markEnd = pos;
		return this;
	}

	public String getMarkedText() {
		return text.substring(markBegin, markEnd);
	}

	public void cropToSelection() {
		setText(getMarkedText());
	}

	public String findLinkURL(String endOfText) throws WordNotFoundException {
		jumpTo(endOfText + "</a>");
		jumpToBackward("href='");

		skipFoundWord();
		markBegin();
		jumpTo("\'");
		markEnd();

		return getMarkedText();

	}

	public void setText(String text) {
		this.text = text;
		pos = 0;
		markBegin = 0;
		markEnd = 0;
		lastWordLength = 0;

	}

	public Finder jumpToEnd() {
		pos = text.length();
		return this;
	}

	public Finder jumpToBegin() {
		pos = 0;
		return this;
	}

	/**
	 * Holt den text-content des XMLElements element
	 * 
	 * @param element
	 * @return
	 */
	public String getXMLText(String element) {


		jumpTo("<" + element);
        skipFoundWord();
        jumpTo(">").skipFoundWord().markBegin();

		int depth = 1;

		while(depth > 0){

		    int posElementstart = text.indexOf("<" + element, pos);
		    int posElementEnd = text.indexOf("</" + element, pos);

		    if(posElementstart > 0 && posElementstart < posElementEnd){
		        depth++;
		        pos = posElementstart + ("" + element).length();
            } else if(posElementEnd > 0){
		        depth--;
		        if(depth == 0){
		            pos = posElementEnd;
		            markEnd();
                }
		        pos = posElementEnd + ("</" + element + ">").length();
            } else {
                return "";
            }
        }

		String text = getMarkedText();

		text = text.replaceAll("&nbsp;", " ");
		text = text.trim();

		return text;

	}

	public int getLength() {
		return text.length();
	}

	public Finder replaceAll(String text, String replacement) {

		text.replace(text, replacement);
		return this;

	}

	@Override
	public String toString() {
		return text;
	}

	public Finder skipNext(String s) {

		jumpTo(s);
		skipFoundWord();

		return this;

	}

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean textEnded() {
        return pos >= text.length();
	}

    public Finder jumpToOrToEnd(String word) {

    if(!find(word)) {
        lastWordLength = 0;
        pos = text.length();
    }
        return this;

    }

	public String getText() {
		return text;
	}
}
