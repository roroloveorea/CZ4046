from ctypes import util
import numpy as np
import copy

#####Variables that can be changed, constructing new map or with diff discount factors.
BOARD_ROWS = 6
BOARD_COLS = 6
GOOD_STATES = [(1, 3)]
BAD_STATES = [(1, 1),(2, 2),(3, 3),(4, 4),(1, 5),(2, 4),(5, 2)]
START = (3, 2)
WALLS = [(0, 1),(1, 4),(4, 2),(4, 3)]
GAMMA = 0.99
EPSILON = 10**(-6)
NT_REWARDS = -0.04    #reward value for non terminal states 
###########################################
NUM_OF_ACTIONS = 4
ACTIONS = [(-1,0),(0,-1),(1,0),(0,1)]   #Up Left Down Right



all_states = [[0] * BOARD_COLS for i in range(BOARD_ROWS)]
#policy = [[0] * BOARD_COLS for i in range(BOARD_ROWS)]

for state in GOOD_STATES:
    all_states[state[0]][state[1]] = 10   

for state in BAD_STATES:
    all_states[state[0]][state[1]] = -1

all_states[4][1] = -15
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
        utility = 10
    elif (row, col) in BAD_STATES:
        utility = -1
    elif (row, col) == (4,1):
        utility = -15
    utility += 0.1 * (GAMMA * get_utility(states,row,col,(action-1)%4))
    utility += 0.1 * (GAMMA * get_utility(states,row,col,(action+1)%4))
    utility += 0.8 * (GAMMA * get_utility(states,row,col,(action)))

    return utility
    

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
                    print("+10 (U)", end=" | ")
                elif matrix[row][col] == 1:
                    print("+10 (L)", end=" | ")
                elif matrix[row][col] == 2:
                    print("+10 (D)", end=" | ")
                elif matrix[row][col] == 3:
                    print("+10 (R)", end=" | ")
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
            else:
                print(str(matrix[row][col])[:6], end=" | ")

        print()

def value_iteration(states):     #find the value of each state
    iteration = 1
    while True: 
        print("Iteration " , iteration)

        next_state = copy.deepcopy(states)   #take the previous states and clone it to modify it
        max_diff = 0
        for row in range(BOARD_ROWS):
            for col in range(BOARD_COLS):
                #if (row,col) in GOOD_STATES or (row,col) in BAD_STATES or (row,col) in WALLS:
                    #continue  # this is for terminating state

                
                utilities = []
                for action in range(NUM_OF_ACTIONS):
                    utilities.append(evaluate_utility(states, row, col, action))
                
                next_state[row][col] = max(utilities)
                max_diff = max(max_diff,abs(next_state[row][col]-states[row][col]))    
        states = next_state
        print_states(states)
        if max_diff < EPSILON * (1-GAMMA)/GAMMA:     #stop iterating once max_diff between previous iteration and current iteration < epsilon
            break
        iteration += 1
    return states

    
                



print("Displaying the Map")
print("\n")
print_states(all_states)
print("\n")


all_states = value_iteration(all_states)

best_policy = get_policy(all_states)
print("\n")
print("***************Best Policy*********************")
print("\n")
print_policy(best_policy)






