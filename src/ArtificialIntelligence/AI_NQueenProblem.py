board = [[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]
N = 4

def safeState(board,row,col):
    for i in range(N):
        if (board[row][i]==1 or board[i][col]):
            return False
    for i in range(N):
        for j in range(N):
            if( (i+j==row+col) or (i-j==row-col) ):
                if board[i][j]==1:
                    return False
    return True
    
def solution(board,row):
    if(row>=N):
        return True 
    else:
        for col in range(N): 
            if(safeState(board,row,col)): 
                print("placed in {0},{1} //FORWARD EDGE".format(row+1,col+1))
                board[row][col]=1
                if(solution(board,row+1)):
                    return True
                print("Failed to place in {0},{1} //BACKTRACK:Level down".format(row+1,col+1))
                board[row][col]=0  
            else:
                print("Failed to place in {0},{1} //BACKTRACK: move to adj".format(row+1,col+1))
    return False

def displayBoard(): 
    for i in range(N): 
        for j in range(N): 
            print (str(board[i][j]),end=" ")
        print() 

if(solution(board,0)):
    print("Solution:")
    displayBoard()
else:
    print("No solution possible")


