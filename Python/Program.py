from Parser import *
import os
import copy

def main():
	variables = {}
	
	os.system('cls')
	print("Welcome to MatrixScape!\n")
	while True:
		entry = input(">> ").strip()
		
		index = entry.find("//")
		if index >= 0:
			entry = entry[:index]
			
		complete = True
		if entry.endswith(";"):
			entry = entry[:(len(entry) - 1)]
			complete = False
			
		if entry == "exit" or entry == "quit" or entry == "close":
			os.system('cls')
			break
			
		if entry == "clear":
			os.system('cls')
			print("Welcome to MatrixScape!\n")
			continue
			
		i1 = Parser.parse(entry, variables).solve(variables)
		
		if type(i1) in [Err, Num, Bool, Tuple, Range, Type]:
			print(i1, "\n")
			
			
		elif type(i1) is Mat:
			if i1.i is None:
				err("invalid matrix")
			else:
				i1.i.print()
			
		
		elif type(i1) is Declare:
			s = i1.name
			i2 = i1.i
			variables[s] = i2
			
			if complete:
				if type(i2) is Mat:
					print(s + " = ")
					i2.i.print()
				else:
					print(s + " = " + str(i2) + "\n")
			
			
		elif type(i1) is Set:
			i2 = i1.i2
			i3 = i1.i3
			i1 = i1.i1
			
			s = ""
			if type(i2) is Var:
				s = i2.name + " = \n"
				
			i2 = i2.solve(variables)
			if type(i2) is Err:
				err(i2.message)
			elif not type(i2) is Mat:
				err("must use matrix")
			else:
				
				if type(i1) in [Rows, Cols] and type(i3) is Mat:
					i4 = i1.i.solve(variables)
					if type(i4) is Err:
						err(i4.message)
					elif isinteger(i4):
					
						try:
							if type(i1) is Rows:
								i2.i.set_row(Precision.num(i4), i3.i)
							else:
								i2.i.set_column(Precision.num(i4), i3.i)
								
								
							if complete:
								print(s, end="")
								i2.i.print()
						except(MatrixError):
							err("invalid dimensions")
							
					else:
						err("must use integer")
						
						
				elif type(i1) is Range and type(i3) is Num:
					i1 = i1.solve(variables)
					if type(i1) is Err:
						err(i1.message)
						
					else:
						r1 = Precision.num(i1.i1)
						r2 = Precision.num(i1.i2)
						
						try:
							i2.i.set_value(r1, r2, Precision.num(i3.i))
							if complete:
								print(s, end="")
								i2.i.print()
						except(MatrixError):
							err("invalid dimensions")
							
				else:
					err("invalid set")
							
			
		elif type(i1) is Null:
			pass			
			
		elif type(i1) is List:
			matrices = "matrices:  "
			numbers = "numbers:   "
			booleans = "booleans:  "
			tuples = "tuples:    "
			ranges = "ranges:    "
			
			for var in variables:
				t = type(variables[var])
				
				if t is Mat:
					matrices += var + ", "
				elif t is Num:
					numbers += var + ", "
				elif t is Bool:
					booleans += var + ", "
				elif t is Tuple:
					tuples += var + ", "
				elif t is Ranges:
					ranges += var + ", "
			
			final = matrices[:(len(matrices) - 2)] + "\n" + numbers[:(len(numbers) - 2)] + "\n" + booleans[:(len(booleans) - 2)] + "\n"
			final += tuples[:(len(tuples) - 2)] + "\n" + ranges[:(len(ranges) - 2)] + "\n"
			print(final)
			
		
		else:
			err("invalid command")
			
			
def err(err_message):
	print("Error: " + err_message)
	
def isinteger(n):
	return type(n) is Num and type(Precision.num(n.i)) is int
	
	
if __name__ == "__main__":
	main()
	