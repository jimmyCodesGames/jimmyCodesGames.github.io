# James McAdams
# Feb. 22, 2025

# Given the maximum weight a single truck can hold
# and a list of integers representing the weights of
# loads that will be loaded onto trucks, write a program
# that organizes the loads into individual trucks in a
# way that minimizes the number of trucks required but
# does not exceed the maximum weight per truck.

import math

maxWeight = int(input("What is the maximum weight per truck?\n"))

weights = []
weightIn = input("Enter each weight value seperated by a space:\n")
weightIn += " "
newValue = []
total = 0
heaviest = 0
lightest = maxWeight+1
truckNum = 0
currentLoad = 0
tooHeavy = maxWeight+1

for i in range(len(weightIn)):
    if weightIn[i] != " ":
        newValue += weightIn[i]
    else:
        total += int("".join(str(i) for i in newValue))
        weights.append("".join(str(i) for i in newValue))
        newValue = []

print("Theoretical Min:", math.ceil(total/maxWeight))

while len(weights) > 0:
    truckNum += 1
    print("Load for truck number:", truckNum)
    tooHeavy = maxWeight+1
    currentLoad = 0
    while True:
        heaviest = 0
        lightest = maxWeight+1
        for item in weights:
            if int(item) > heaviest and int(item) < tooHeavy:
                heaviest = int(item)
            if int(item) < lightest:
                lightest = int(item)
        if currentLoad+heaviest <= maxWeight:
            if str(heaviest) in weights:
                currentLoad += heaviest
                weights.remove(str(heaviest))
                print(str(heaviest))
        else:
            tooHeavy = heaviest
        if len(weights) > 0:
            if currentLoad+lightest > maxWeight:
                break
    
    
