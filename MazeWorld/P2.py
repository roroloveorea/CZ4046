from ctypes import util
from json.encoder import INFINITY
import numpy as np
import copy
import random

#####Variables that can be changed, constructing new map or with diff discount factors.
BOARD_ROWS = 6
BOARD_COLS = 6
GOOD_STATES = [(0, 0),(0, 2),(0, 5),(1, 3),(2, 4),(3, 4)]
BAD_STATES = [(1, 1),(2, 2),(3, 3),(4, 4),(1, 5)]
START = (3, 2)
WALLS = [(0, 1),(1, 4),(4, 1),(4, 2),(4, 3)]
GAMMA = 0.99
EPSILON = 10**(-6)
NT_REWARDS = -0.04    #reward value for non terminal states 
###########################################
NUM_OF_ACTIONS = 4
ACTIONS = [(-1,0),(0,-1),(1,0),(0,1)]   #Up Left Down Right



all_states = [[0] * BOARD_COLS for i in range(BOARD_ROWS)]
#policy = [[0] * BOARD_COLS for i in range(BOARD_ROWS)]

for state in GOOD_STATES:
    all_states[state[0]][state[1]] = 1   

for state in BAD_STATES:
    all_states[state[0]][state[1]] = -1

policy = [[random.randint(0,3) for j in range(BOARD_COLS)] for j in range(BOARD_ROWS)]  #initialise all the policies randomly 






def get_policy(states):   #find the best move the AI can take at that state
    policy = [[None] * BOARD_COLS for i in range(BOARD_ROWS)]
    for row in range(BOARD_ROWS):
        for col in range(BOARD_COLS):
            # if (row,col) in GOOD_STATES or (row,col) in BAD_STATES or (row,col) in WALLS:
            #     continue   #uncomment for terminating 
            best_action = -99999
            best_utility = -99999
            for action in range(NUM_OF_ACTIONS):
                utility = evaluate_utility(states, row, col, action)
                if utility > best_utility: 
                    best_utility = utility
                    best_action = action
            policy[row][col] = best_action

    return policy
    
def print_policy(matrix):   ##function to print the policy
    for row in range(BOARD_ROWS):
        for col in range(BOARD_COLS):
            if (row, col) in WALLS:
                print(" WALL ", end=" | ")
            elif (row, col) in GOOD_STATES:
                if matrix[row][col] == 0:
                    print("+1 (U)", end=" | ")
                elif matrix[row][col] == 1:
                    print("+1 (L)", end=" | ")
                elif matrix[row][col] == 2:
                    print("+1 (D)", end=" | ")
                elif matrix[row][col] == 3:
                    print("+1 (R)", end=" | ")
            elif (row, col) in BAD_STATES:
                if matrix[row][col] == 0:
                    print("-1 (U)", end=" | ")
                elif matrix[row][col] == 1:
                    print("-1 (L)", end=" | ")
                elif matrix[row][col] == 2:
                    print("-1 (D)", end=" | ")
                elif matrix[row][col] == 3:
                    print("-1 (R)", end=" | ")
            else:
                if matrix[row][col] == 0:
                    print("  Up  ", end=" | ")
                elif matrix[row][col] == 1:
                    print(" Left ", end=" | ")
                elif matrix[row][col] == 2:
                    print(" Down ", end=" | ")
                elif matrix[row][col] == 3:
                    print(" Right", end=" | ")
                #print(matrix[row][col],3)), end="| ")

        print()           
    
def print_states(matrix):
    for row in range(BOARD_ROWS):
        for col in range(BOARD_COLS):
            if (row, col) in WALLS:
                print("WALL", end=" | ")
            elif(row,col) in GOOD_STATES:
                print(" 1  ", end=" | ")
            elif(row,col) in BAD_STATES:
                print(" -1 ", end=" | ")
            else:
                print(str(matrix[row][col])[:6], end=" | ")

        print()

def get_utility(states, row, col, action):      #to get the utility of that action in that state
    temp_row = row + ACTIONS[action][0]
    temp_col = col + ACTIONS[action][1]
    if temp_row < 0 or temp_row >= BOARD_ROWS or temp_col < 0 or temp_col >= BOARD_COLS \
        or (temp_row, temp_col) in WALLS:
        return states[row][col]
    else:
        return states[temp_row][temp_col]


def evaluate_utility(states, row, col, action):    #get best utility of that state
    utility = NT_REWARDS
    #non terminating states: even when at good states and bad states the IA continues to move
    if (row, col) in GOOD_STATES:
        utility = 1
    elif (row, col) in BAD_STATES:
        utility = -1
    utility += 0.1 * (GAMMA * get_utility(states,row,col,(action-1)%4))
    utility += 0.1 * (GAMMA * get_utility(states,row,col,(action+1)%4))
    utility += 0.8 * (GAMMA * get_utility(states,row,col,(action)))

    return utility

def policy_making(policy, states):   #finding a utility given the current policy and state
    max_diff = 0
    while True:
        next_state = copy.deepcopy(states)    #clone the states to next_state
        max_diff = 0
        for row in range(BOARD_ROWS):
            for col in range(BOARD_COLS):
                next_state[row][col] = evaluate_utility(states, row, col, policy[row][col])
                max_diff = max(max_diff, abs(next_state[row][col] - states[row][col]))  #find max diff between current 
                                                                                        #state and previous state
        states = next_state
        #print("change")
        if max_diff < EPSILON * (1 - GAMMA)/GAMMA:   
            break
    return states

def policy_iteration(policy, state):      #find a better policy or original policy remains based on utility value
    iteration = 1
    while True:
        state = policy_making(policy, state)        #return the state with corresponding utility
        modified = 0     #policy not being modified yet; modified = 0;

        for row in range(BOARD_ROWS):
            for col in range(BOARD_COLS):
                best_action = None
                best_utility = -float("inf")
                for action in range(NUM_OF_ACTIONS):
                    ut = evaluate_utility(state, row, col, action)   #find the best utility and corresponding action
                    if ut > best_utility:
                        best_action = action     
                        best_utility = ut      
                    
                if best_utility > evaluate_utility(state , row, col, policy[row][col]):   #update utility and policy
                    policy[row][col] = best_action
                    modified = 1   #policy is being modified

        print("Iteration ", iteration)
        print_policy(policy)
        #print(state[1][2])
        


        if modified == 0:   #final policy will be generated when changes is not needed anymore
            break
        iteration += 1

    return policy, state
    
                




print("\n")

policy, all_states = policy_iteration(policy, all_states)
# all_states = value_iteration(all_states)

best_policy = get_policy(all_states)
print("\n")
print("Displaying the Map")
print("\n")
print_states(all_states)
print("\n")
print("***************Best Policy*********************")
print("\n")
print_policy(policy)
#print(policy)






