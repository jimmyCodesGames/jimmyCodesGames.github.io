/*
* MINESWEEPER
* Made by James McAdams
* 10/23/25
*/

#include <stdlib.h>
#include <stdio.h>
#include <time.h>
#include <string.h>

#define BOARDSIZE 9 // Formatting compatible for up to 99 x 99 board. Ideal board: 9 x 9
#define NUMBOMBS (BOARDSIZE * BOARDSIZE / 8) // Spawns bombs proportional to BOARDSIZE. Ideal for 9 x 9 (10 bombs)
#define ALLTILESREVEALED (BOARDSIZE * BOARDSIZE - NUMBOMBS) // Constant to keep track of number of tiles not containing a bomb
#define MAXINPUT 255 // Maximum number of character inputs. Exceeding this will cause unintended behavior or crash.
#define HIDDENBOMB 9
#define REVEALEDBOMB 10
#define NOTREVEALED -1
#define ZERO 0 // Special case for Minesweeper. If numAdjacent bombs = 0, reveal adjacent

// Struct holding 2D array of the visible characters of the board to be printed to screen
typedef struct VisibleBoard {
    char ** board;
} VisibleBoard;

// Struct holding 2D array of the values that influence the VisibleBoard display
// Keeps track of the number of tiles revealed to enable win detection
typedef struct HiddenBoard {
    int ** board;
    int tilesRevealed;
} HiddenBoard;

// Node for singly linked list to temporarily store randomly generated bomb coordinates
typedef struct CoordsNode{
    int ROW;
    int COL;
    struct CoordsNode * next;
} CoordsNode;

// Function Prototypes
void initializeVisibleBoard(VisibleBoard ** b);
void initializeHiddenBoard(HiddenBoard ** b);
void initializeCoordsNode(CoordsNode ** c, int row, int col);
void freeVisibleBoard(VisibleBoard * b);
void freeHiddenBoard(HiddenBoard * b);
void addBombs(HiddenBoard * b);
int pushCoords(CoordsNode *coords, CoordsNode *c);
int updateVisibleBoard(VisibleBoard * v, HiddenBoard * h);
void pickTile(HiddenBoard * b, int row, int col);
void revealAdjacent(HiddenBoard * b, int r, int c);
void revealAllBombs(HiddenBoard * b);
int numAdjacentBombs(HiddenBoard * b, int r, int c);
void clearBoard(HiddenBoard * b);
void resetGame(VisibleBoard * v, HiddenBoard * h);
void printBoard(VisibleBoard * v);
int promptRestart(HiddenBoard * h, VisibleBoard * v, int lost);
int promptCoords(VisibleBoard * v, int * row, int * col);

// Allocates space for VisibleBoard struct and 2D board array
void initializeVisibleBoard(VisibleBoard ** b) {
    (*b) = malloc(sizeof(VisibleBoard));
    (*b)->board = malloc(BOARDSIZE * sizeof(char *));
    for (int i = 0; i < BOARDSIZE; i++) {
        (*b)->board[i] = malloc(BOARDSIZE * sizeof(char));
    }
}

// Allocates space for HiddenBoard struct and 2D board array
// Sets tilesRevealed to 0
// Initiliazes each element of board to -1
// Adds NUMBOMBS randomly placed bombs
void initializeHiddenBoard(HiddenBoard ** b) {
    (*b) = malloc(sizeof(HiddenBoard));
    (*b)->board = malloc(BOARDSIZE * sizeof(int *));
    for (int i = 0; i < BOARDSIZE; i++) {
        (*b)->board[i] = malloc(BOARDSIZE * sizeof(int));
    }
    (*b)->tilesRevealed = 0;
    for (int row = 0; row < BOARDSIZE; row++) {
        for (int col = 0; col < BOARDSIZE; col++) {
           (*b)->board[row][col] = -1;
        }
    }
    addBombs(*b);
}

// Allocates space for CoordsNode and assigns values to ROW and COL
void initializeCoordsNode(CoordsNode ** c, int row, int col) {
    (*c) = malloc(sizeof(CoordsNode));
    (*c)->next = NULL;
    (*c)->ROW = row;
    (*c)->COL = col;
}

// Frees all memory associated with VisibleBoard
void freeVisibleBoard(VisibleBoard * b) {
    for (int i = 0; i < BOARDSIZE; i++)
        free(b->board[i]);
    free(b->board);
    free(b);
}

// Frees all memory associated with HiddenBoard
void freeHiddenBoard(HiddenBoard * b) {
    for (int i = 0; i < BOARDSIZE; i++)
        free(b->board[i]);
    free(b->board);
    free(b);
}

// Creates a singly linked list of CoordsNode containing ROW and COL values
// Continues to randomly generate values and traverse linked list to prevent duplicates
// Once NUMBOMBS bomb coordinates are created, each bomb location on HiddenBoard is set to HIDDENBOMB
// Frees all CoordsNodes
void addBombs(HiddenBoard * b) {
    int numBombsAdded = 0;
    CoordsNode * head =  NULL;
    initializeCoordsNode(&head, -1, -1);
    while (numBombsAdded < NUMBOMBS) {
        int row = rand()%BOARDSIZE;
        int col = rand()%BOARDSIZE;
        CoordsNode * newCoords = NULL;
        initializeCoordsNode(&newCoords, row, col);
        if (pushCoords(head, newCoords)) numBombsAdded++;
    }
    CoordsNode * walker = head->next;
    free(head);
    while(walker) {
        b->board[walker->ROW][walker->COL] = HIDDENBOMB;
        CoordsNode * temp = walker->next;
        free(walker);
        walker = temp;
    }
}

// Attempt to add new CoordsNord. Free new CoordsNode and return 0 if coord already exists. Otherwise, add and return 1;
int pushCoords(CoordsNode *coords, CoordsNode *c) {
    CoordsNode * walker = coords;
    while (walker) {
        if (walker->ROW == c->ROW && walker->COL == c->COL) {
            free(c);
            return 0;
        } 
        if (!walker->next)
            break;
        walker = walker->next;
    }
    walker->next = c;
    return 1;
}

// Updates VisibleBoard 2d char array to reflect HiddenBoard
int updateVisibleBoard(VisibleBoard * v, HiddenBoard * h) {
    int lost = 0;
    for (int row = 0; row < BOARDSIZE; row++) {
        for (int col = 0; col < BOARDSIZE; col++) {
           switch(h->board[row][col]) {
                case NOTREVEALED:
                case HIDDENBOMB:
                    v->board[row][col] = ' ';
                    break;
                case REVEALEDBOMB:
                    v->board[row][col] = 'x';
                    lost = 1;
                    break;
                default:
                    v->board[row][col] = '0' + h->board[row][col];
           }
        }
    }
    return lost;
}

// Checks HiddenBoard[row][col]. Update board. If value is 0 after revealing, call revealAdjacent.
void pickTile(HiddenBoard * b, int row, int col) {

    // Base Case to stop out of bounds or infinite recursion
    if (!(row >= 0 && row < BOARDSIZE && col >= 0 && col < BOARDSIZE) || b->board[row][col] == ZERO)
        return;

    switch (b->board[row][col]) {
        case HIDDENBOMB: 
            revealAllBombs(b); 
            break;
        case NOTREVEALED: 
            b->board[row][col] = numAdjacentBombs(b, row, col);
            b->tilesRevealed++;
            break;
        default:
            break;
    }

    // If tile revealed had value of ZERO, start recursive flood fill to reveal adjacent tiles
    if (b->board[row][col] == ZERO) 
        revealAdjacent(b, row, col);
}

// Works as a flood fill with pickTile() to recursively fill board so long as a 0 is revealed
void revealAdjacent(HiddenBoard * b, int r, int c) {
    for(int i = -1; i < 2; i++) {
        for(int j = -1; j<2; j++) {
            if(!(i == 0 && j == 0))
                pickTile(b, r + i, c + j);
        }
    }
}

// Changes HiddenBoard values to reveal the bomb locations in VisibleBoard.
void revealAllBombs(HiddenBoard * b) {
    for (int row = 0; row < BOARDSIZE; row++) {
        for (int col = 0; col < BOARDSIZE; col++) {
            if (b->board[row][col] == HIDDENBOMB) 
                b->board[row][col] = REVEALEDBOMB;
        }
    }
}

// Returns number of adjacent elements with the value of HIDDENBOMB
int numAdjacentBombs(HiddenBoard * b, int r, int c) {
    int numBombs = 0;
    for(int i = -1; i < 2; i++) 
        for(int j = -1; j<2; j++)
            if (r+i >= 0 && r+i < BOARDSIZE && c+j >= 0 && c+j < BOARDSIZE && !(i == 0 && j == 0)) 
                if(b->board[r+i][c+j] == HIDDENBOMB)
                    numBombs++;
    return numBombs;
}

// Reset HiddenBoard values to NOTREVEALED
void clearBoard(HiddenBoard * b) {
    for (int row = 0; row < BOARDSIZE; row++) {
        for (int col = 0; col < BOARDSIZE; col++) {
            b->board[row][col] = NOTREVEALED;
        }
    }
}

// Resets HiddenBoard and VisibleBoard for new game
void resetGame(VisibleBoard * v, HiddenBoard * h) {
    clearBoard(h);
    addBombs(h);
    updateVisibleBoard(v, h);
    h->tilesRevealed = 0;
    printBoard(v);
}

// Prints formatted board with values from VisibleBoard
void printBoard(VisibleBoard * v) {
    printf("MINESWEEPER\n");
    printf("-----------\n");
    printf("   ||");
    for (int i = 0; i < BOARDSIZE; i++) 
        printf("%2d |", i+1);
    printf("\n---||");
    for (int i = 0; i < BOARDSIZE-1; i++) 
            printf("====");
    printf("===|\n");
    for (int row = 0; row < BOARDSIZE; row++) {
        printf("%2d ||", row+1);
        for (int col = 0; col < BOARDSIZE; col++) 
            printf(" %c |", v->board[row][col]);
        printf("\n");
        if (row < BOARDSIZE-1) {
            printf("---||");
            for (int i = 0; i < BOARDSIZE-1; i++) 
                    printf("---+");
            printf("---|\n");
        }
        else { 
            printf("-----");
            for (int i = 0; i < BOARDSIZE-1; i++) 
                    printf("----");
            printf("---|\n");
        }
    }
}

// Player Lost. Continues to prompt player to start new game until player enters valid input.
// If '0' is entered, return 1 to quit game. Otherwise reset game and return 0
int promptRestart(HiddenBoard * h, VisibleBoard * v, int lost) {

    revealAllBombs(h);
    updateVisibleBoard(v, h);
    printBoard(v);
    printf("YOU %s!!!\n\n", lost ? "LOST" : "WON");
    printf("Would you like to start a new game?\n");

    char in[MAXINPUT];
    while (1) {
        printf("Type 'yes' to start or type '0' to quit\n");
        fgets(in, sizeof(in), stdin);

        if (strcmp(in, "0\n") == 0)
            return 1;

        if (strcmp(in, "yes\n") != 0) {
            printf("INVALID ENTRY\n\n");
            printBoard(v);    
        } 
        else
            break;
    }
    resetGame(v, h);
    return 0;
}

// Prompts player to pick a tile. Will continue prompting until valid input is made
// If '0' is entered, return 1 to quit game. Otherwise set value of x and y and return 0
int promptCoords(VisibleBoard * v, int * row, int * col) {

    char in[MAXINPUT];
    int x, y;
    while (1) {
        printf("type '0' to quit\n");
        printf("PICK A TILE: Enter tile as x y\n");
        if(!fgets(in, sizeof(in), stdin)) {
            printf("INVALID ENTRY\n\n");
            printBoard(v); 
        }
        char overflow[MAXINPUT];
        int numInputs = sscanf(in," %d %d %s", &x, &y, overflow);
        if (numInputs != 2 || x <= 0 || x > BOARDSIZE || y <= 0 || y > BOARDSIZE) {
            if (numInputs == 1 && x == 0) 
                return 1;
            printf("INVALID ENTRY\n\n");
            printBoard(v); 
        } 
        else 
            break;
    }

    *col = x - 1;
    *row = y - 1;
    return 0;
}



// Loops through a constant game of Minesweeper
int main() {
    srand(time(NULL)); // Ensure the game differs every time

    HiddenBoard * valsBoard;
    initializeHiddenBoard(&valsBoard);
    VisibleBoard * display;
    initializeVisibleBoard(&display);
    updateVisibleBoard(display, valsBoard);

    printf("Welcome to Minesweeper!\n\n");
    printf("There are exactly %d bombs.\n", NUMBOMBS);
    printf("Try your best not to hit any.\n");
    printf("Good luck!\n\n");

    printBoard(display);

    int quit = 0;
    while(1) {
        int col, row;
        quit = promptCoords(display, &row, &col);
        if(quit)
            break;
        pickTile(valsBoard, row, col);
        int lost = updateVisibleBoard(display, valsBoard);
        if (lost || valsBoard->tilesRevealed == ALLTILESREVEALED) {
            quit = promptRestart(valsBoard, display, lost);
            if(quit)
                break;
        }
        else 
            printBoard(display);  
    }

    freeVisibleBoard(display);
    freeHiddenBoard(valsBoard);
    return 0;
}

