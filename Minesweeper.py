# James McAdams
# Minesweeper
# March 14, 2025

import random

# Display game board. board[row][col] will indicate either blank or revealed spaces.
def display(board):
    print("MINESWEEPER")
    print("-----------")
    for row in range(10):
        for col in range(10):
            print("",board[row][col],"",end="")
            if col != 0:
                print("|",end="")
            else:
                print("||",end="")
        print()
        if row != 9:
            if row != 0:
                print("---||---+---+---+---+---+---+---+---+---|")
            else:
                print("========================================|")
        else:
            print("----------------------------------------|")

# Update displayed board 2D list to match background 2D list
def update_board(background,board):
    for row in range(10):
        for col in range(10):
            if background[row][col] != -1 and background[row][col] != 10:
                board[row][col] = str(background[row][col])
            if background[row][col] == 9:
                board[row][col] = "X"
    return board

# Determine value of selected space based on # of adjacent bombs
def check_val(col,row, background):
    val = 0
    if background[row][col] != 10:
        if background[row-1][col] == 10:
            val += 1
        if row != 9 and background[row+1][col] == 10:
            val += 1
        if background[row-1][col-1] == 10:
            val += 1
        if col != 9 and row != 9 and background[row+1][col+1] == 10:
            val += 1
        if background[row][col-1] == 10:
            val += 1
        if col != 9 and background[row][col+1] == 10:
            val += 1
        if col != 9 and background[row-1][col+1] == 10:
            val += 1
        if row != 9 and background[row+1][col-1] == 10:
            val += 1
        background[row][col] = val
    else:
        background[row][col] = 9

    return background

# Check if selected space is = 9. If so, space was a bomb. Return True if bomb, False if not. 
def check_lost(background):
    for row in range(10):
        for col in range(10):
            if background[row][col] == 9:
                return True
    return False

# Check if any spaces = -1. If none exist, player won (Return True). else, return False.
def check_win(background):
    win = True
    for row in range(10):
        for col in range(10):
            if row > 0 and col > 0 and background[row][col] == -1:
                win = False
    return win

# If selected space = 0, reveal all adjacent spaces. Iterate 10 times to reveal entire board of adjacent zeros/ spaces.
def check_zeros(background):
    for i in range(10):
        for row in range(10):
            for col in range(10):
                if background[row][col] == 0:
                    if row > 1 and background[row-1][col] == -1:
                        background = check_val(col,row-1,background)
                    if row > 1 and col > 1 and background[row-1][col-1] == -1:
                        background = check_val(col-1,row-1,background)
                    if col != 9 and row != 9 and background[row+1][col+1] == -1:
                        background = check_val(col+1,row+1,background)
                    if col > 1 and background[row][col-1] == -1:
                        background = check_val(col-1,row,background)
                    if col != 9 and background[row][col+1] == -1:
                        background = check_val(col+1,row,background)
                    if row != 9 and background[row+1][col] == -1:
                        background = check_val(col,row+1,background)
                    if row > 1 and col != 9 and background[row-1][col+1] == -1:
                        background = check_val(col+1,row-1,background)
                    if col > 1 and row != 9 and background[row+1][col-1] == -1:
                        background = check_val(col-1,row+1,background)
    return background

# Convert all hidden bombs (background[row][col] = 10) to revelaed bombs (background[row][col] = 9)
def reveal(background):
    for row in range(10):
            for col in range(10):
                if background[row][col] == 10:
                    background[row][col] = 9
    return background

def main():
    while True:

        lost = False
        win = False

        # Board that gets displayed for the player
        board = [[" ","1","2","3","4","5","6","7","8","9"],
             ["1"," "," "," "," "," "," "," "," "," "],
             ["2"," "," "," "," "," "," "," "," "," "],
             ["3"," "," "," "," "," "," "," "," "," "],
             ["4"," "," "," "," "," "," "," "," "," "],
             ["5"," "," "," "," "," "," "," "," "," "],
             ["6"," "," "," "," "," "," "," "," "," "],
             ["7"," "," "," "," "," "," "," "," "," "],
             ["8"," "," "," "," "," "," "," "," "," "],
             ["9"," "," "," "," "," "," "," "," "," "]]

        # Board that stores values of spaces to be displayed when changed
        # -1 = blank | 10 = hidden bomb | 9 = revealed bomb | all other values indicate # of adjacent bombs
        background = [[-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1],
             [-1,-1,-1,-1,-1,-1,-1,-1,-1,-1]]

        # Loops until 10 unique bomb locations are selected and assigned
        bombs = 0
        locations = []
        while bombs < 10:
            row = random.randint(1,9)
            col = random.randint(1,9)
            if (row,col) not in locations:
                background[row][col] = 10
                locations.append((row,col))
                bombs+=1
                
        # Intro and display blank board
        print("Welcome to Minesweeper!\n")
        print("There are exactly 10 bombs.")
        print("Try your best not to hit any.")
        print("Good luck!\n")

        display(board)
        
        # Loop gameplay until game is won or lost
        while lost == False and win == False:
            # Input selected space
            pos = input("Choose a tile. (\"xy\")\n")
            print()
            try:
                # If valid input: assign value to space, reveal adjacent zeros, check win/lose, update displayed board, display updated board
                if int(pos[0]) > 0 and int(pos[1]) > 0 and int(pos) < 100:
                    background = check_val(int(pos[0]),int(pos[1]),background)
                    background = check_zeros(background)
                    if check_lost(background) == True:
                        lost = True
                        reveal(background)
                    elif check_win(background) == True:
                        win = True
                        reveal(background)     
                    board = update_board(background,board)
                    display(board)
            except:
                pass
            
        # Display if player won or lost
        if lost == True:
            print("YOU LOSE\n")
        else:
            print("YOU WON!!!\n")

        # Prompt player to start new game. Loop until player types "yes"
        while True:
            newGame = input("Would you like to play a new game?\n(Type 'yes' for new game)\n")
            newGame = newGame.lower()
            try:
                if newGame == "yes":  
                    print("\nNew game generated\n")
                    break
            except:
                pass
            
main()
