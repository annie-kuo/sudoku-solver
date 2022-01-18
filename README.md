# sudoku-solver
This program solves sudokus using a brute-force approach. Puzzles can be modify to include additional rules (see below) and to be of any square size.

## Additional Rules
Three rules can be added on top of the classical sudoku rules:
- The Knight Rule: a digit must not appear a chess knight’s move away from itself (either two squares vertically and one horizontally, or two squares horizontally and one square vertically).
- The King Rule: a digit must not be a King’s move away from itself (i.e. on top of classical rules, a digit must not be a single diagonal away from itself).
- The Queen Rule: the largest number in the puzzle (every 9s in a 3x3 puzzle, every 16s in a 4x4 puzzle, ...) acts like a chess Queen and must not be in the same row/-
column/3x3 box or diagonal of itself.
