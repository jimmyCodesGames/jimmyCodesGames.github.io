# James McAdams
# Connect Four
# March 18, 2025

# Update board based on which player is playing and selected space
def update_board(col, turn, board):
    row = 0
    while board[row+1][col] == 0 and row < 5:
        row += 1 
    board[row][col] = turn
    return board

# Verify that selected location is valid and is not already in use
def check_valid(col, board):
    if col > -1 and col < 7 and board[0][col] == 0:
        return True
    return False

# Given selected space, check if win condition is met by adding up adjacent spaces that match turn number
# If matching spaces to the left/ right, up/ down, or either diagonal direction >= 3, win is True
def check_win(col, turn, board):
    row = 0
    while board[row+1][col] == 0 and row < 5:
        row += 1
    left = 0
    right = 0
    up = 0
    down = 0
    rd_up = 0
    rd_down = 0
    ld_up = 0
    ld_down = 0
    while col-left-1 > -1 and board[row][col-left-1] == turn:
        left += 1
    while col+right+1 < 7 and board[row][col+right+1] == turn:
        right += 1
    while row-up-1 > -1 and board[row-up-1][col] == turn:
        up += 1
    while row+down+1 < 6 and board[row+down+1][col] == turn:
        down += 1
    while row-rd_up-1 > -1 and col+rd_up+1 < 7 and board[row-rd_up-1][col+rd_up+1] == turn:
        rd_up += 1
    while row-ld_up-1 > -1 and col-ld_up-1 > -1 and board[row-ld_up-1][col-ld_up-1] == turn:
        ld_up += 1
    while row+rd_down+1 < 6 and col+rd_down+1 < 7 and board[row+rd_down+1][col+rd_down+1] == turn:
        rd_down += 1
    while row+ld_down+1 < 6 and col-ld_down-1 > -1 and board[row+ld_down+1][col-ld_down-1] == turn:
        ld_down += 1
    if left+right >= 3 or up+down >= 3 or rd_up+ld_down >= 3 or ld_up+rd_down >= 3:
        return True
    return False

# Check if board is full. If every space is occupied, board is full (Tie game = True)
def check_tie(board):
    for col in range(7):
        if board[0][col] == 0:
            return False
    return True

# Display the game board     
def display(board):
    print("|1|2|3|4|5|6|7|")
    for row in range(6):
        for col in range(7):
            if(col == 0):
                print("|", end="")
            if board[row][col] == 0:
                print(" ", end="")
            elif board[row][col] == 1:
                print("X", end="")
            else:
                print("O", end="")
            print("|", end="")
        print()
    print("---------------")

def main():
    
    while True:

        turn = 0

        playing = False

        # Game board
        # 0 = empty space. 1 = player 1 space (X). 2 = player 2 space (O)
        board = [[0,0,0,0,0,0,0],
                 [0,0,0,0,0,0,0],
                 [0,0,0,0,0,0,0],
                 [0,0,0,0,0,0,0],
                 [0,0,0,0,0,0,0],
                 [0,0,0,0,0,0,0],
                 [0,0,0,0,0,0,0]]

        # Intro. Will loop until player input = "yes"
        print("Welcome to Connect Four!\n")
        try:
            start = input("would you like to start a new game? \nType 'yes' to start\n")
            start = start.lower()
            print()
            if start == "yes":
                playing = True
        except:
            pass

        # Loops gameplay until a player wins or players tie
        while playing == True:

            display(board)

            # Alternates player turn. turn%2 = 0, Player 1's turn, else Player 2's turn
            if turn%2 == 0:
                print("Player 1 (X): It is your turn")
                try:
                    # Player selectes column
                    col = int(input("Select a column (1-7):\n"))-1
                    
                    # If valid entry: check if player won, update board, increase turn by 1
                    if check_valid(col, board) == True:
                        if check_win(col, 1, board) == True:
                            update_board(col, 1, board)
                            display(board)
                            print("Player 1 wins!\n")
                            playing = False
                            break
                        update_board(col, 1, board)
                        turn += 1
                        print()
                    else:
                        print("Invalid selection\n")
                except:
                    print("Invalid selection\n")
                    pass
            else:
                print("Player 2 (O): It is your turn")
                try:
                    # Player selectes column
                    col = int(input("Select a column (1-7):\n"))-1

                    # If valid entry: check if player won, update board, increase turn by 1
                    if check_valid(col, board) == True:
                        if check_win(col, 2, board) == True:
                            update_board(col, 2, board)
                            display(board)
                            print("Player 2 wins!\n")
                            playing = False
                            break
                        update_board(col, 2, board)
                        turn += 1
                        print()
                    else:
                        print("Invalid selection\n")
                except:
                    print("Invalid selection\n")
                    pass

            # Check if game is tied
            if check_tie(board) == True:
                display(board)
                print("Tie Game!\n")
                playing = False
main()
