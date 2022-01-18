import java.util.*;
import java.io.*;


public class ChessSudoku
{
	/* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
	 * a standard Sudoku puzzle, SIZE is 3 and N is 9. 
	 */
	public int SIZE, N;

	/* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
	 * not yet been revealed are stored as 0. 
	 */
	public int grid[][];

	/* Booleans indicating whether of not one or more of the chess rules should be 
	 * applied to this Sudoku. 
	 */
	public boolean knightRule;
	public boolean kingRule;
	public boolean queenRule;

	
	// Field that stores the same Sudoku puzzle solved in all possible ways
	public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();


	/* If true is provided as input, the method should find finds ALL 
	 * possible solutions and store them in the field named solutions. */
	public void solve(boolean allSolutions) {
		if (! allSolutions) {
			this.solveOnce();
		} else {
			this.solveAll();
			for (ChessSudoku solution : this.solutions) {
				for (int i = 0; i < this.N; i++) {
					for (int j = 0; j < this.N; j++) {
						this.grid[i][j] = solution.grid[i][j];
					}
				}
				break;
			}
		}

	}

	private boolean solveOnce() {
		// loop through each entry of the grid
		for (int row = 0; row < this.N; row++) {
			for (int column = 0; column < this.N; column++) {
				// check if entry is empty (==0)
				if (this.grid[row][column] == 0) {
					// try each number
					for (int x = 1; x <= this.N; x++) {
						// if no rule is broken, fill that entry with that number
						if (checkRules(x, row, column)) {
							this.grid[row][column] = x;

							// check if we can continue filling the empty entries without breaking rules
							if (this.solveOnce()) {
								return true;
							} else {
								// this entry is not leading to a solution -> undo entry (backtrack)
								this.grid[row][column] = 0;
							}
						}
					}
					// no number fits without breaking rules
					// sudoku is unsolvable
					return false;
				}
			}
		}
		// sudoku is completed --> solved
		return true;
	}

	private boolean solveAll() {
		// loop through each entry of the grid
		for (int row = 0; row < this.N; row++) {
			for (int column = 0; column < this.N; column++) {
				// check if entry is empty (==0)
				if (this.grid[row][column] == 0) {
					// try each number
					for (int x = 1; x <= this.N; x++) {
						// if no rule is broken, fill that entry with that number
						if (checkRules(x, row, column)) {
							this.grid[row][column] = x;

							// check if we can continue filling the empty entries without breaking rules
							if (this.solveAll()) {
								// add to solutions
								ChessSudoku newSolution = new ChessSudoku(this.SIZE);
								for (int i = 0; i < this.N; i++) {
									for (int j =0; j <this.N; j++) {
										newSolution.grid[i][j] = this.grid[i][j];
									}
								}
								this.solutions.add(newSolution);
							}

							// test another number, undo entry (backtrack)
							this.grid[row][column] = 0;

						}
					}
					// no number fits without breaking rules
					// sudoku is unsolvable
					return false;
				}
			}
		}
		// sudoku is completed --> solved
		return true;
	}

	private boolean checkRow(int x, int row) {
		// loop through the numbers in the same row
		for (int i = 0; i < this.N; i++) {
			// return false if that number is already in the row
			if (this.grid[row][i] == x) {
				return false;
			}
		}
		// return true if that number is not in that row
		return true;
	}

	private boolean checkColumn(int x, int column) {
		// loop through the numbers in the same column
		for (int i = 0; i < this.N; i++) {
			// return false if that number is already in the column
			if (this.grid[i][column] == x) {
				return false;
			}
		}
		// return true if that number is not in that column
		return true;
	}

	private boolean checkBox(int x, int row, int column) {
		// calculate the bounds of the box
		int rowBound = row - row % this.SIZE;
		int colBound = column - column % this.SIZE;

		// loop through the numbers in that box
		for (int i = rowBound; i < rowBound + this.SIZE; i++) {
			for (int j = colBound; j < colBound + this.SIZE; j++) {
				// return false if that number is already in the box
				if (this.grid[i][j] == x) {
					return false;
				}
			}
		}
		// return true if that number is not in that box
		return true;
	}

	private boolean checkKnightRule(int x, int row, int column) {
		int ycoor = row -1;
		int xcoor = column -2;

		// check the 8 possible positions
		for (int place = 1; place <= 8; place ++) {
			if (0 <= ycoor && ycoor < this.N && 0 <= xcoor && xcoor < this.N) {
				if (this.grid[ycoor][xcoor] == x) {
					return false;
				}
			}
			// adjust variables for next position
			switch (place) {
				case 1:
					ycoor += 2;
					break;
				case 2:
					ycoor += 1;
					xcoor += 1;
					break;
				case 3:
					xcoor += 2;
					break;
				case 4:
					ycoor -= 1;
					xcoor += 1;
					break;
				case 5:
					ycoor -= 2;
					break;
				case 6:
					ycoor -= 1;
					xcoor -= 1;
					break;
				case 7:
					xcoor -= 2;
					break;

			}
		}

		// return true if that number is not a knight's move away
		return true;
	}

	private boolean checkKingRule(int x, int row, int column) {
		int ycoor = row + 1;
		int xcoor = column - 1;

		// check the 4 diagonals
		for (int place = 1; place <= 4; place++) {
			if (0 <= ycoor && ycoor < this.N && 0 <= xcoor && xcoor < this.N) {
				if (this.grid[ycoor][xcoor] == x) {
					return false;
				}
			}
			switch (place) {
				case 1:
					xcoor += 2;
					break;
				case 2:
					ycoor -= 2;
					break;
				case 3:
					xcoor -= 2;
					break;
			}
		}

		// return true if that number is not a king's move away
		return true;
	}

	private boolean checkQueenRule(int x, int row, int column) {
		// if the number is not a 9, ignore
		if (x != this.N) {
			return true;
		}
		// check the diagonals
		int i,j;
		// check the \ diagonal
		if (row >= column) {
			i = row - column;
			j = 0;
		} else {
			i = 0;
			j = column - row;
		}
		for ( ; i < this.N && j < this.N; i++, j++) {
			if (this.grid[i][j] == x) {
				return false;
			}
		}
		// check the / diagonal
		if (this.N - 1 - row < column ) {
			i = this.N -1;
			j = column - (this.N-1-row);
		} else {
			i = row + column;
			j = 0;
		}
		for ( ; i >= 0 && j < this.N; i--, j++) {
			if (this.grid[i][j] == x) {
				return false;
			}
		}

		// return true if that number is not a queen's move away
		return true;
	}

	private boolean checkRules(int x, int row, int column) {
		// basic rules
		boolean respectsRules = checkRow(x, row) && checkColumn(x, column) && checkBox(x, row, column);
		// additional rules
		if (this.knightRule) {
			respectsRules = respectsRules && checkKnightRule(x, row, column);
		}
		if (this.kingRule) {
			respectsRules = respectsRules && checkKingRule(x, row, column);
		}
		if (this.queenRule && x == this.N) {
			respectsRules = respectsRules && checkQueenRule(x, row, column);
		}
		return respectsRules;
	}


	/* Default constructor.  This will initialize all positions to the default 0
	 * value.  Use the read() function to load the Sudoku puzzle from a file or
	 * the standard input. */
	public ChessSudoku( int size ) {
		SIZE = size;
		N = size*size;

		grid = new int[N][N];
		for( int i = 0; i < N; i++ ) 
			for( int j = 0; j < N; j++ ) 
				grid[i][j] = 0;
	}


	/* readInteger is a helper function for the reading of the input file.  It reads
	 * words until it finds one that represents an integer. For convenience, it will also
	 * recognize the string "x" as equivalent to "0". */
	static int readInteger( InputStream in ) throws Exception {
		int result = 0;
		boolean success = false;

		while( !success ) {
			String word = readWord( in );

			try {
				result = Integer.parseInt( word );
				success = true;
			} catch( Exception e ) {
				// Convert 'x' words into 0's
				if( word.compareTo("x") == 0 ) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}

		return result;
	}


	/* readWord is a helper function that reads a word separated by white space. */
	static String readWord( InputStream in ) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while( whiteSpace.indexOf(currentChar) > -1 ) {
			currentChar = in.read();
		}

		// Read all characters until you reach white space
		while( whiteSpace.indexOf(currentChar) == -1 ) {
			result.append( (char) currentChar );
			currentChar = in.read();
		}
		return result.toString();
	}


	/* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
	 * grid is filled in one row at at time, from left to right.  All non-valid
	 * characters are ignored by this function and may be used in the Sudoku file
	 * to increase its legibility. */
	public void read( InputStream in ) throws Exception {
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				grid[i][j] = readInteger( in );
			}
		}
	}


	/* Helper function for the printing of Sudoku puzzle.  This function will print
	 * out text, preceded by enough ' ' characters to make sure that the printint out
	 * takes at least width characters.  */
	void printFixedWidth( String text, int width ) {
		for( int i = 0; i < width - text.length(); i++ )
			System.out.print( " " );
		System.out.print( text );
	}


	/* The print() function outputs the Sudoku grid to the standard output, using
	 * a bit of extra formatting to make the result clearly readable. */
	public void print() {
		// Compute the number of digits necessary to print out each number in the Sudoku puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

		// Create a dashed line to separate the boxes 
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for( int lineInit = 0; lineInit < lineLength; lineInit++ )
			line.append('-');

		// Go through the grid, printing out its values separated by spaces
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				printFixedWidth( String.valueOf( grid[i][j] ), digits );
				// Print the vertical lines between boxes 
				if( (j < N-1) && ((j+1) % SIZE == 0) )
					System.out.print( " |" );
				System.out.print( " " );
			}
			System.out.println();

			// Print the horizontal line between boxes
			if( (i < N-1) && ((i+1) % SIZE == 0) )
				System.out.println( line.toString() );
		}
	}


	/* The main function reads in a Sudoku puzzle from the standard input, 
	 * unless a file name is provided as a run-time argument, in which case the
	 * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
	 * outputs the completed puzzle to the standard output. */
	public static void main( String args[] ) throws Exception {
		String start = "C:\\Users\\annie\\Desktop\\McGill\\WINTER 2021\\COMP250- Intro to Computer Science\\Lecture Codes\\";
		String toTest = "test.txt";
		InputStream in = new FileInputStream(toTest);

		// The first number in all Sudoku files must represent the size of the puzzle.  See
		// the example files for the file format.
		int puzzleSize = readInteger( in );
		if( puzzleSize > 100 || puzzleSize < 1 ) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}

		ChessSudoku s = new ChessSudoku( puzzleSize );
		
		// Modify these to add rules to your sudoku
		s.knightRule = false;
		s.kingRule = false;
		s.queenRule = false;
		
		// read the rest of the Sudoku puzzle
		s.read( in );

		System.out.println("Before the solve:");
		s.print();
		System.out.println();

		// Solve the puzzle by finding one solution.
		s.solve(true);

		// Print out the (hopefully completed!) puzzle
		System.out.println("After the solve:");
		//s.print();

		System.out.println(s.solutions.size() + " solutions");
		for (ChessSudoku solution : s.solutions) {
			solution.print();
			System.out.println();
		}
		System.out.println(s.solutions.size() + " solutions");

	}
}
