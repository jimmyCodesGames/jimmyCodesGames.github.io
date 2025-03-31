# James McAdams
# Feb. 22, 2025

# Write a program that rotates the contents of a 3x3 list as follows:
# 1 2 3       7 4 1
# 4 5 6  -->  8 5 2
# 7 8 9       9 6 3
import math

initial = [
1,2,3,4,5,6,
7,8,9,10,11,12,
13,14,15,16,17,18,
19,20,21,22,23,24,
25,26,27,28,29,30,
31,32,33,34,35,36]

final = [0]*len(initial)

rowSize = int(math.sqrt(len(initial)))
row = 0
wrap = 0

for i in range(len(initial)):
    if i%rowSize == 0 and i != 0:
        row += 1
    if i+((i+1)*(rowSize-1))-row-wrap*len(initial) >= len(initial):
        wrap += 1
    final[i+((i+1)*(rowSize-1))-row-wrap*len(initial)] = initial[i]

print("Initial Positions:")
for i in range(len(initial)):
    print(initial[i],end="\t")
    if (i+1)%rowSize == 0 and i != 0:
        print("\n", end="")

print("\nFinal Positions (Rotated 90 degrees):")
for i in range(len(final)):
    print(final[i],end="\t")
    if (i+1)%rowSize == 0 and i != 0:
        print("\n", end="")
